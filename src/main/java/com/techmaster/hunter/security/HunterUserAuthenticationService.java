package com.techmaster.hunter.security;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.techmaster.hunter.cache.HunterCacheUtil;
import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.HunterUserDao;
import com.techmaster.hunter.exception.HunterRunTimeException;
import com.techmaster.hunter.obj.beans.HunterUser;
import com.techmaster.hunter.obj.beans.UserLoginBean;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterSessionFactory;
import com.techmaster.hunter.util.HunterUtility;
import com.techmaster.hunter.xml.XMLService;
import com.techmaster.hunter.xml.XMLServiceImpl;
import com.techmaster.hunter.xml.XMLTree;


public class HunterUserAuthenticationService {
	
	@Autowired private HunterJDBCExecutor hunterJDBCExecutor;
	@Autowired private HunterUserDao hunterUserDao;
	
	public static final String ROLES_KEY = "ROLES_KEY";
	public static final int MAX_USER_LOGIN_COUNT = 5;
	private Logger logger = Logger.getLogger(getClass());
	
	public static final String NOT_RECOGNIZED_CREDENTIALS = "Credentials not recognized. Please try again or conduct your administrator";
	public static final String USER_PASSWORD_MISMATCH = "Wrong user name or password. Please try again.";
	
   /*
		AccountStatusException, 
		ActiveDirectoryAuthenticationException, 
		AuthenticationCancelledException, 
		AuthenticationCredentialsNotFoundException, 
		AuthenticationServiceException, 
		BadCredentialsException, 
		InsufficientAuthenticationException, 
		NonceExpiredException, 
		PreAuthenticatedCredentialsNotFoundException, 
		ProviderNotFoundException, 
		RememberMeAuthenticationException, 
		SessionAuthenticationException, 
		UsernameNotFoundException
    */
    
	public HunterJDBCExecutor getHunterJDBCExecutor() {
		return hunterJDBCExecutor;
	}
	public void setHunterJDBCExecutor(HunterJDBCExecutor hunterJDBCExecutor) {
		this.hunterJDBCExecutor = hunterJDBCExecutor;
	}
	
	public Map<String, Object> getUserNamePasswordAndRole(String userName){
		
		String query = hunterJDBCExecutor.getQueryForSqlId("getUserRoleDetails");
		List<Object> values = new ArrayList<Object>();
		values.add(userName);
		values.add(userName);
		
		List<Map<String, Object>>  listMap = hunterJDBCExecutor.executeQueryRowMap(query, values);
		Map<String, Object> results = new HashMap<>();
		
		if(listMap != null && !listMap.isEmpty()){
			for(Map<String, Object> map : listMap){
				String password = map.get("PSSWRD") == null ? null : map.get("PSSWRD") + "";
				String userName_ = map.get("USR_NAM") == null ? null : map.get("USR_NAM") + "";
				if(password != null && userName_ != null){
					results.put("PSSWRD", password);
					results.put("USR_NAM", userName_);
					break;
				}else if(userName_ == null && password != null){
					results.put("PSSWRD", password);
				}
			}
		}else{
			return results;
		}
		
		List<String> roles = new ArrayList<>();
		
		for(Map<String, Object> map : listMap){
			String role = map.get("RL_NAM") != null ? map.get("RL_NAM").toString() : null;
			if(role != null){
				roles.add(role);
			}
		}
		
		results.put(ROLES_KEY, roles);
		
		return results;
	}
	
	public Map<String, Object> authenthicate(String userName, String password){
		
		Map<String, Object> params = getUserNamePasswordAndRole(userName);
		
		//wrong user name.
		if((params == null || params.isEmpty()) || params.get("USR_NAM") == null){
			params.put("ERROR", NOT_RECOGNIZED_CREDENTIALS);
			return params;
		}
		
		Object refPassword =  params.get("PSSWRD");
		
		UserLoginBean userLoginBean = null;
		
		if(refPassword == null || !refPassword.toString().equals(password)){
			int newCount = getIncrementedFailureLoginCount(userName);
			userLoginBean = getUserLoginBeanByUserName(userName);
			updateLoginDataXMLForLogin(userLoginBean, HunterUtility.formatDate(new Date(), null), isLocked(newCount) ? "Blocked" : HunterConstants.STATUS_FAILED, "173.63.174.127", userName, password, Integer.toString(newCount)); 
			putParamsForBlockedUnBlocked(params,newCount); 
		}else{
			String status = HunterConstants.STATUS_FAILED;
			int newCount = getIncrementedFailureLoginCount(userName);
			if(!isLocked(newCount)){ 
				logger.debug("Login successful, resetting failed login count"); 
				params.put("BLOCKED", false);
				resetFailedLoginCounts(userName); 
			}else{
				status = "Blocked";
				logger.debug("Account is blocked and cannot reset failed login counts."); 
				putParamsForBlockedUnBlocked(params,newCount);
			}
			userLoginBean = getUserLoginBeanByUserName(userName);
			updateLoginDataXMLForLogin(userLoginBean, HunterUtility.formatDate(new Date(), null), status, "173.63.174.127", userName, password, Integer.toString(newCount));
			
		}
		
		return params;
	}
	
	public void putParamsForBlockedUnBlocked(Map<String, Object> params, int newCount){
		params.put("ERROR", USER_PASSWORD_MISMATCH);
		params.put("BLOCKED", isLocked(newCount) + "" );  
		params.put("COUNT", newCount);
		params.put("REM_COUNT", isLocked(newCount) ? "0" : (MAX_USER_LOGIN_COUNT-newCount)+""); 
	}
	
	public Map<String, Object> getUserLoginDetails(String userName){
		String query = hunterJDBCExecutor.getQueryForSqlId("getUserLoginBeanDetails");
		logger.debug("Executing query : " + query);
		List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{userName});
		List<Map<String, Object>> mapList = hunterJDBCExecutor.executeQueryRowMap(query, values);
		if(mapList == null || mapList.isEmpty()){
			logger.debug("Found no login bean details for user name : " + userName); 
			return new HashMap<String, Object>();
		}else {
			return mapList.get(0);
		}
	}
	
	public int getIncrementedFailureLoginCount(String userName){
		Map<String, Object> userLoginDetails = getUserLoginDetails(userName);
		int currCnt = 0;
		if(userLoginDetails != null && !userLoginDetails.isEmpty()){
			currCnt = Integer.parseInt(userLoginDetails.get("FLD_LGN_CNT").toString());
			logger.debug("Current failed login count : " + currCnt); 
			if(isLocked(currCnt)){ 
				logger.debug("User has had more than five failed login. Account being blocked.count : " + currCnt); 
				String blockQuery = "UPDATE USR_LGN_BN bn SET bn.FLD_LGN_CNT = ?, bn.BLCKD = ? WHERE bn.USR_ID = (select hu.USR_ID from HNTR_USR hu where hu.USR_NAM = ?)";
				currCnt++;
				List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{currCnt,"Y", userName});
				hunterJDBCExecutor.executeUpdate(blockQuery, values);
			}else{
				logger.debug("Increasing failed login count for the user : " + userName); 
				String incrementQuery = "UPDATE USR_LGN_BN bn SET bn.FLD_LGN_CNT = ? WHERE bn.USR_ID = (select hu.USR_ID from HNTR_USR hu where hu.USR_NAM = ?)";
				currCnt++;
				List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{currCnt,userName});
				hunterJDBCExecutor.executeUpdate(incrementQuery, values);
			}
		}else{
			logger.debug("Did not find user login for userName : " + userName);  
			UserLoginBean userLoginBean = createUserLoginForUser(userName);
			currCnt= userLoginBean.getFaildedLoginCount();
			currCnt++;
			userLoginBean.setFaildedLoginCount(currCnt);
			hunterUserDao.updateUserLoginBean(userLoginBean); 
		}
		return currCnt;
	}
	
	public UserLoginBean getUserLoginBeanByUserName(String userName){
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Query query = null;
		List<?> userLoginBeans = null;
		
		
		try {
			session = sessionFactory.openSession();
			String hql = "FROM UserLoginBean u WHERE u.userId = ( SELECT h.userId FROM HunterUser h WHERE h.userName = :userName )";      
			query = session.createQuery(hql);
			query.setParameter("userName", userName);
			userLoginBeans = query.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session); 
		}
        
		UserLoginBean userLoginBean = HunterUtility.isCollectionNullOrEmpty( userLoginBeans ) ? null : (UserLoginBean) userLoginBeans.get(0);
		return userLoginBean;
	}
	
	public UserLoginBean createUserLoginForUser(String userName){
		logger.debug("Creating user login bean for the user..."); 
		HunterUser hunterUser = hunterUserDao.getUserByUserName(userName);
		UserLoginBean userLoginBean = new UserLoginBean();
		userLoginBean.setBlocked(false);
		userLoginBean.setLastLoginTime(new Date()); 
		userLoginBean.setFaildedLoginCount(0); 
		userLoginBean.setUserId(hunterUser.getUserId()); 
		hunterUser.setUserLoginBean(userLoginBean);
		hunterUserDao.updateUser(hunterUser); 
		logger.debug("Successfully created user login bean for the user!!"); 
		return userLoginBean;
	}
	
	public boolean isLocked(int loginCount){
		return loginCount > MAX_USER_LOGIN_COUNT;
	}
	
	public void resetFailedLoginCounts(String userName){
		String rQuery = "UPDATE USR_LGN_BN bn SET bn.FLD_LGN_CNT = ?, bn.BLCKD = ?, bn.LST_LGN_TM = sysdate  WHERE bn.USR_ID = (select hu.USR_ID from HNTR_USR hu where hu.USR_NAM = ?)";
		List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{0,"N", userName});
		logger.debug("Executing query : " + rQuery);
		hunterJDBCExecutor.executeUpdate(rQuery, values);
		logger.debug("Successfully reset login failed attempts for the user!"); 
	}
	
	public int getNextLoginNum(XMLService existingLoginData){
		NodeList nodeList = existingLoginData.getNodeListForPathUsingJavax("//login");
		int loginNum = 1;
		for(int i=0; i<nodeList.getLength();i++){
			Node node = nodeList.item(i);
			if( node.getNodeName().equalsIgnoreCase("login") ){
				int currLoginNum = Integer.valueOf( node.getAttributes().getNamedItem("loginNum").getTextContent() );  
				if( currLoginNum > loginNum ){
					loginNum = currLoginNum;
				}
			}
		}
		return loginNum + 1;
	}
	
	public void updateLoginDataXMLForLogin(UserLoginBean userLoginBean, String loginDate,String status,String ipAddress,String userName, String loginPassword, String failedNo){
		
		if(userName == null){
			logger.debug("User login information does not exist for the user name : " + userName ); 
			return;
		}
			
 		
		XMLService loginDataXML = HunterCacheUtil.getInstance().getXMLService(HunterConstants.LOGIN_DATA_SEE_XML);
		
		String mainPath = "loginData/logins/login/";
		loginDataXML.getNodeListForPathUsingJavax( mainPath + "date" ).item(0).setTextContent(loginDate); 
		loginDataXML.getNodeListForPathUsingJavax( mainPath + "status" ).item(0).setTextContent(status); 
		loginDataXML.getNodeListForPathUsingJavax( mainPath + "ip" ).item(0).setTextContent(ipAddress); 
		loginDataXML.getNodeListForPathUsingJavax( mainPath + "userName" ).item(0).setTextContent(userName); 
		loginDataXML.getNodeListForPathUsingJavax( mainPath + "password" ).item(0).setTextContent(loginPassword); 
		loginDataXML.getNodeListForPathUsingJavax( mainPath + "currFailedCount" ).item(0).setTextContent(failedNo); 
		
		Blob orinalDocBlob = userLoginBean.getLoginData();
		String loginData = HunterUtility.getBlobStr( orinalDocBlob );
		
		if( loginData == null ){
			logger.debug("Setting login data for the first time..."); 
			userLoginBean.setLoginData( HunterUtility.getStringBlob( loginDataXML.toString() ) ); 
			HunterHibernateHelper.updateEntity( userLoginBean );
			return;
		}
		
		
		try {
			
			XMLService existingDocument = new XMLServiceImpl( new XMLTree( loginData , true));
			Node node = loginDataXML.getFirstNodeUsingAjaxByName("login");
			((org.w3c.dom.Element )(node)).setAttribute("loginNum", Integer.toString( getNextLoginNum(existingDocument) ));   
			Node node2 = existingDocument.getXmlTree().getDoc().importNode(node, true);
			existingDocument.getFirstNodeUsingAjaxByName("logins").appendChild(node2);
			userLoginBean.setLoginData( HunterUtility.getStringBlob( existingDocument.toString() ) ); 
			HunterHibernateHelper.updateEntity( userLoginBean );
			
		} catch (HunterRunTimeException | ParserConfigurationException e) {
			e.printStackTrace();
			userLoginBean.setLoginData( orinalDocBlob ); 
			HunterHibernateHelper.updateEntity( userLoginBean );
			return;
		}
		
	}
	
}
