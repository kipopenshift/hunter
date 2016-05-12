package com.techmaster.hunter.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.HunterUserDao;
import com.techmaster.hunter.obj.beans.HunterUser;
import com.techmaster.hunter.obj.beans.UserLoginBean;


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
		
		if(refPassword == null || !refPassword.toString().equals(password)){
			int newCount = getIncrementedFailureLoginCount(userName);
			putParamsForBlockedUnBlocked(params,newCount); 
		}else{
			int newCount = getIncrementedFailureLoginCount(userName);
			if(!isLocked(newCount)){ 
				logger.debug("Login successful, resetting failed login count"); 
				params.put("BLOCKED", false);
				resetFailedLoginCounts(userName); 
			}else{
				logger.debug("Account is blocked and cannot reset failed login counts."); 
				putParamsForBlockedUnBlocked(params,newCount); 
			}
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
		if(userLoginDetails != null && !userLoginDetails.isEmpty()){
			int currCnt = Integer.parseInt(userLoginDetails.get("FLD_LGN_CNT").toString());
			logger.debug("Current failed login count : " + currCnt); 
			if(isLocked(currCnt)){ 
				logger.debug("User has had more than five failed login. Account being blocked.count : " + currCnt); 
				String blockQuery = "UPDATE USR_LGN_BN bn SET bn.FLD_LGN_CNT = ?, bn.BLCKD = ? WHERE bn.USR_ID = (select hu.USR_ID from HNTR_USR hu where hu.USR_NAM = ?)";
				currCnt++;
				List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{currCnt,"Y", userName});
				hunterJDBCExecutor.executeUpdate(blockQuery, values);
				return currCnt;
			}else{
				logger.debug("Increasing failed login count for the user : " + userName); 
				String incrementQuery = "UPDATE USR_LGN_BN bn SET bn.FLD_LGN_CNT = ? WHERE bn.USR_ID = (select hu.USR_ID from HNTR_USR hu where hu.USR_NAM = ?)";
				currCnt++;
				List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{currCnt,userName});
				hunterJDBCExecutor.executeUpdate(incrementQuery, values);
				return currCnt;
			}
		}else{
			logger.debug("Did not find user login for userName : " + userName);  
			UserLoginBean userLoginBean = createUserLoginForUser(userName);
			int currCnt= userLoginBean.getFaildedLoginCount();
			currCnt++;
			userLoginBean.setFaildedLoginCount(currCnt);
			hunterUserDao.updateUserLoginBean(userLoginBean); 
			return currCnt;
		}
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
		String rQuery = "UPDATE USR_LGN_BN bn SET bn.FLD_LGN_CNT = ?, bn.BLCKD = ? WHERE bn.USR_ID = (select hu.USR_ID from HNTR_USR hu where hu.USR_NAM = ?)";
		List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{0,"N", userName});
		logger.debug("Executing query : " + rQuery);
		hunterJDBCExecutor.executeUpdate(rQuery, values);
		logger.debug("Successfully reset login failed attempts for the user!"); 
	}
	

}
