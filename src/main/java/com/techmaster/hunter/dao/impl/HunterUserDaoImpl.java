package com.techmaster.hunter.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import com.techmaster.hunter.dao.types.HunterAddressDao;
import com.techmaster.hunter.dao.types.HunterCreditCardDao;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.HunterUserDao;
import com.techmaster.hunter.json.HunterUserJson;
import com.techmaster.hunter.obj.beans.HunterAddress;
import com.techmaster.hunter.obj.beans.HunterCreditCard;
import com.techmaster.hunter.obj.beans.HunterUser;
import com.techmaster.hunter.obj.beans.UserLoginBean;
import com.techmaster.hunter.obj.converters.HunterUserConverter;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterSessionFactory;
import com.techmaster.hunter.util.HunterUtility;

public class HunterUserDaoImpl implements HunterUserDao{

	private static HunterCreditCardDao hunterCreditCardDao = new HunterCreditCardDaoImpl();
	private static HunterAddressDao hunterAddressDao = new HunterAddressDaoImpl();
	private Logger logger = Logger.getLogger(getClass());
	
	private static Long maxAddressId;
	private static Long maxCreditCardId;
	
	private static HunterJDBCExecutor hunterJDBCExecutor = HunterDaoFactory.getInstance().getDaoObject(HunterJDBCExecutor.class);
	
	static{
		maxAddressId = hunterAddressDao.getNextAddressId() - 1;
		maxCreditCardId = hunterCreditCardDao.getNextCreditCardId() - 1;
	}
	
	public void refreshAllMaxIds(){
		maxAddressId = hunterAddressDao.getNextAddressId() - 1;
		maxCreditCardId = hunterCreditCardDao.getNextCreditCardId() - 1;
	}
	
	@Override
	public void insertHunterUser(HunterUser user) {
		

		HunterMessageDaoHelper.refreshMapAndCurrentIds();
		refreshAllMaxIds();
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		// set up parent keys for hunter user.
		
		if(user.getUserId() == null || user.getUserId().equals(new Long(0))){  
			
			Long nextId = getNextUserId();
			logger.debug("Set next id for the user >> " + nextId);
			user.setUserId(nextId);
			
			logger.debug("Setting id for the addresses");
			Set<HunterAddress> addresses = user.getAddresses();
			if(addresses != null && !addresses.isEmpty()){
				for(HunterAddress address : addresses){
					address.setUserId(user.getUserId());
					maxAddressId++;
					address.setId(maxAddressId);
				}
			}
			
			logger.debug("Setting credit card and user id for the creditCards");
			Set<HunterCreditCard> creditCards = user.getCreditCards();
			if(creditCards != null && !creditCards.isEmpty()){
				for(HunterCreditCard card : creditCards){
					card.setUserId(user.getUserId()); 
					maxCreditCardId++;
					card.setCardId(maxCreditCardId); 
				}
			}
			
		}
		
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.save(user);
			trans.commit();
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		} finally{
			HunterHibernateHelper.closeSession(session); 
		}
		
		
	}

	@Override
	public void insertHunterUsers(List<HunterUser> hunterUsers) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			for(HunterUser user : hunterUsers){
				session.save(user);
			}
			trans.commit();
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		} finally{
			HunterHibernateHelper.closeSession(session); 
		}
	}

	@Override
	public void deleteHunterUserById(Long userId) {
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			HunterUser user = (HunterUser)session.get(HunterUser.class, userId);
			if(user != null){
				session.delete(user);
				trans.commit();
			}
			logger.debug("Successfull deleted user >> " + user.toString());
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
	}

	@Override
	public void deleteHunterUser(HunterUser user) {
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.delete(user);
			trans.commit();
			logger.debug("Successfull deleted user >> " + user.toString());
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
		
	}

	@Override
	public HunterUser getHunterUserById(Long id) {
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		HunterUser user = null;
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			user = (HunterUser)session.get(HunterUser.class, id);
			trans.commit();
			logger.debug("Successfull deleted user >> " + user.toString());
			HunterHibernateHelper.closeSession(session);
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
		
		return user;
	}

	@Override
	public List<HunterUser> getAllUsers() {
		logger.debug("Loading all users...");
		List<HunterUser> users = HunterHibernateHelper.getAllEntities(HunterUser.class);
		logger.debug("Finished loading all users! Size ( " + users.size() + " )");
		return users;
	}

	@Override
	public void updateUser(HunterUser update) {
		
		if(update == null || update.getUserId() == null) return;
		Session session = null;
		Transaction trans = null;
		
		try {
			SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			session.update(update);
			trans.commit();
			HunterHibernateHelper.closeSession(session); 
			logger.info("Successfully updated user >> " + update);
			
		} catch (HibernateException e) {
			e.printStackTrace();
			HunterHibernateHelper.rollBack(trans); 
		}finally{
			HunterHibernateHelper.closeSession(session); 
		}
		
	}
	
	public HunterUser getUserByUserName(String userName){
		logger.debug("Fetching user of user name (" + userName + ")");  
		String query = "FROM HunterUser h WHERE h.userName = '" + userName + "'";
		List<HunterUser> hunterUsers = HunterHibernateHelper.executeQueryForObjList(HunterUser.class, query);
		logger.debug("Successfully obtained user. Size of list : " + hunterUsers.size());
		// this assumes that userName is a unique field.
		return hunterUsers.isEmpty() ? null : hunterUsers.get(0);
		
	}

	@Override
	public Long getNextUserId() {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		Long nextId = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(HunterUser.class).setProjection(Projections.max("userId"));
			Long maxId = (Long)criteria.uniqueResult();
			
			nextId = maxId == null ? 1 : (maxId + 1);
			
			HunterHibernateHelper.closeSession(session); 
			
		} catch (HibernateException e) {
			HunterHibernateHelper.rollBack(trans); 
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session); 	
		}
		logger.debug("Obtained next hunter user id >> " + nextId); 
		return nextId;
	
	
	}

	@Override
	public Map<String, List<String>> getFullNamesForUserNames(List<String> userNames) {
		logger.debug("Fetching full names...");
		String query = "SELECT h.FRST_NAM, h.LST_NAM, h.USR_NAM FROM HNTR_USR h WHERE h.USR_NAM in (" + HunterUtility.getSingleQuotedCommaDelimitedForList(userNames) + ")";
		logger.debug("Executing queqy : " + query);
		Map<Integer, List<Object>> rowMapList = hunterJDBCExecutor.executeQueryRowList(query, null);
		Map<String, List<String>>  output = new HashMap<String, List<String>>();
		if(rowMapList == null || rowMapList.isEmpty()){
			logger.warn("No data found for userNames : " + HunterUtility.stringifyList(userNames)); 
			logger.debug("Returning empty map.."); 
			return output;
		}else{
			for(Map.Entry<Integer, List<Object>> entry : rowMapList.entrySet()){
				List<Object> rowList = entry.getValue();
				String firstName = rowList.get(0)+"";
				String lastName = rowList.get(1)+"";
				String userName = rowList.get(2)+"";
				List<String> outputList = new ArrayList<>();
				outputList.add(firstName);
				outputList.add(lastName);
				outputList.add(userName); 
				output.put(userName, outputList);
			}
		}
		logger.debug("Done fetching full names. Size ( " + output.size() + " )"); 
		return output;
	}

	@Override
	public List<HunterUserJson> getAllUserJson() {
		logger.debug("Fetching all user jsons..."); 
		List<HunterUser> hunterUsers = getAllUsers(); 
		List<HunterUserJson> hunterUserJsons = HunterUserConverter.getInstance().createJsonFromUsers(hunterUsers);
		logger.debug("Finished fetching user jsons. Size ( " + hunterUserJsons.size() + " )");  
		return hunterUserJsons;
	}

	@Override
	public String validateAndDeleteById(Long userId) {
		StringBuilder taskErrors = null; 
		HunterJDBCExecutor hunterJDBCExecutor = HunterDaoFactory.getInstance().getDaoObject(HunterJDBCExecutor.class);
		if(hunterJDBCExecutor == null){
			logger.debug("HunterJDBCExecutor is null!!"); 
		}
		String query = hunterJDBCExecutor.getQueryForSqlId("validateHunterUserDelete");
		List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{userId});
		Map<Integer, List<Object>> rowListMap = hunterJDBCExecutor.executeQueryRowList(query, values);
		int index = 0;
		if(rowListMap != null && !rowListMap.isEmpty()){
			for(Map.Entry<Integer, List<Object>> entry : rowListMap.entrySet()){
				List<Object> rowList = entry.getValue();
				String taskIdStr = rowList.get(0).toString();
				if(taskErrors == null){
					taskErrors = new StringBuilder().append("User cannot be deleted. The user has the following tasks " + "( "); 
				}
				if(index < rowListMap.size() - 1){
					taskErrors.append(taskIdStr + ",");
				}else{
					taskErrors.append(taskIdStr);
				}
				index++;
			}
			if(taskErrors != null && taskErrors.toString().contains("(")){
				taskErrors.append(" )");
			}
		}else{
			logger.debug("User passed delete validations. Deleting the user..."); 
			deleteHunterUserById(userId); 
		}
		
		if( taskErrors != null ){
			logger.debug("Validation finished : " + taskErrors.toString()); 
			return taskErrors.toString();
		}
		return null;
	}

	@Override
	public List<UserLoginBean> getUserLoginBeanByUserName(String userName) {
		String query = "SELECT h.userLoginBean FROM HunterUser h WHERE h.userName = '" + userName + "'";
		logger.debug("Executing query : " + query); 
		List<UserLoginBean> userLoginBeans = HunterHibernateHelper.executeQueryForObjList(UserLoginBean.class, query);
		logger.debug("Obtained user login bean : " + HunterUtility.stringifyList(userLoginBeans)); 
		return userLoginBeans;
	}

	@Override
	public void updateUserLoginBean(UserLoginBean userLoginBean) {
		logger.debug("Updating user login bean : " + userLoginBean); 
		HunterHibernateHelper.updateEntity(userLoginBean);
		logger.debug("Successfully updated user login bean!"); 
	}

	@Override
	public List<HunterUserJson> getAllUsersWhoAreClients() {
		
		logger.debug("Loading users who are clients..."); 
		
		List<HunterUserJson> hunterUserJsons = new ArrayList<>();
		HunterJDBCExecutor hunterJDBCExecutor = HunterDaoFactory.getInstance().getDaoObject(HunterJDBCExecutor.class);
		String  query = hunterJDBCExecutor.getQueryForSqlId("getAllClientsDetails");
		List<Map<String, Object>> rowMapList = hunterJDBCExecutor.executeQueryRowMap(query, null);
		
		if( HunterUtility.isCollectionNotEmpty( rowMapList ) ){
			for(Map<String,Object> rowMap : rowMapList){
				HunterUserJson userJson = new HunterUserJson();
		        userJson.setActive(HunterUtility.getBooleanForYN(rowMap.get("ACTIV")+""));
				userJson.setBlocked(HunterUtility.getBooleanForYN(rowMap.get("ACTIV")+"")); 
				userJson.setCreatedBy(HunterUtility.getStringOrNullOfObj( rowMap.get("CRTD_BY") ));
				userJson.setCretDate(HunterUtility.getStringOrNullOfObj( rowMap.get("CRET_DATE") ));
				userJson.setEmail(HunterUtility.getStringOrNullOfObj( rowMap.get("EMAIL") ));;
				userJson.setFirstName(HunterUtility.getStringOrNullOfObj( rowMap.get("FRST_NAM") ));
				userJson.setLastName(HunterUtility.getStringOrNullOfObj( rowMap.get("LST_NAM") ));
				userJson.setLastUpdate(HunterUtility.getStringOrNullOfObj( rowMap.get("LST_UPDATE") ));
				userJson.setLastUpdatedBy(HunterUtility.getStringOrNullOfObj( rowMap.get("LST_UPDTD_BY") ));
				userJson.setMiddleName(HunterUtility.getStringOrNullOfObj( rowMap.get("MDDL_NAM") ));
				userJson.setPhoneNumber(HunterUtility.getStringOrNullOfObj( rowMap.get("PHN_NUMBR") ));
				userJson.setUserId( HunterUtility.getLongFromObject( rowMap.get("USR_ID") ) ); 
				userJson.setUserName(HunterUtility.getStringOrNullOfObj( rowMap.get("USR_NAM") )); 
				userJson.setUserType(HunterUtility.getStringOrNullOfObj( rowMap.get("USR_TYP") ));
				hunterUserJsons.add(userJson); 
			}
		}
		
		logger.debug("Finished loading users who are clients..."); 
		return hunterUserJsons;
		 
	}
	
	

	
}
