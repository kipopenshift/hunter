package com.techmaster.hunter.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.UserRoleDao;
import com.techmaster.hunter.obj.beans.UserRole;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterSessionFactory;
import com.techmaster.hunter.util.HunterUtility;

public class UserRoleDaoImpl implements UserRoleDao{
	
	Logger logger = Logger.getLogger(getClass());
	@Autowired private HunterJDBCExecutor hunterJDBCExecutor;
	
	@Override
	public void insertUserRole(UserRole userRole) {
		logger.debug("Saving user role : " + userRole);
		HunterHibernateHelper.saveEntity(userRole); 
		logger.debug("Don saving user role!"); 
	}

	@Override
	public UserRole getUserRoleById(Long roleId) {
		logger.debug("Getting user role by id : " + roleId);
		UserRole userRole = HunterHibernateHelper.getEntityById(roleId, UserRole.class);
		logger.debug("Retrieved user role : " + userRole);  
		return userRole;
	}

	@Override
	public List<UserRole> getAllUserRole() {
		logger.debug("Fetching all user roles...");
		List<UserRole> userRoles = HunterHibernateHelper.getAllEntities(UserRole.class);
		logger.debug("Retrieved all : " + HunterUtility.stringifyList(userRoles));  
		return userRoles;
	}

	@Override
	public String addRoleToUser(Long userId, Long userRoleId) {
		logger.debug("Adding role of id : " + userRoleId + " to user with user id  : "  + userId);
		List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{userId, userRoleId});
		String queryCheck = hunterJDBCExecutor.getQueryForSqlId("getAddRoleValidationData");
		Map<Integer, List<Object>> counts = hunterJDBCExecutor.executeQueryRowList(queryCheck, values);
		if(!counts.isEmpty()){
			List<Object> rowList = counts.get(1);
			String name = rowList.get(0)+"";
			String message = "User already has this role ( " + name + " )"; logger.debug(message); 
			return message;
		}
		String query = "INSERT INTO HNTR_USR_RLS(USR_ID,ROLE_ID) VALUES (?,?)";
		hunterJDBCExecutor.executeUpdate(query, values); 
		logger.debug("Finished adding role to user!"); 
		return null;
	}

	@Override
	public String removeRoleToUser(Long userId, Long userRoleId) {
		logger.debug("Removing role ( " + userRoleId + " ) from user ( " + userId + " )");
		List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{userId, userRoleId});
		String updateQuery = "DELETE FROM HNTR_USR_RLS where USR_ID = ? and ROLE_ID = ?";
		hunterJDBCExecutor.executeUpdate(updateQuery, values);
		logger.debug("Finished removing role from user!!"); 
		return null;
	}

	@Override
	public List<UserRole> getUserRolesForUserId(Long userId) {
		logger.debug("Fetching all roles for user : " + userId); 
		List<UserRole> userRoles = new ArrayList<UserRole>();
		String roleIdQuery = hunterJDBCExecutor.getQueryForSqlId("getUserRoleIdForUserId");
		List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{userId});
		Map<Integer, List<Object>> rowMapList = hunterJDBCExecutor.executeQueryRowList(roleIdQuery, values);
		String roleIdsStr = null;
		if(rowMapList != null && !rowMapList.isEmpty()){
			List<Object> roleIdList = rowMapList.get(1);
			roleIdsStr = roleIdList.get(0)+"";
		}else{
			logger.debug("No roles are found for the user : " + userId); 
			return userRoles;
		}
		String rolesQuery = "FROM UserRole u WHERE u.roleId IN (" + roleIdsStr + ")";
		Session session = null;
		try {
			logger.debug("Quering for roles..."); 
			session = HunterSessionFactory.getSessionFactory().openSession();
			Query query = session.createQuery(rolesQuery);
			List<?> list = query.list();
			for(Object obj : list){
				UserRole userRole = (UserRole)obj;
				userRoles.add(userRole);
			}
			logger.debug("Returned "); 
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session);
		}
		logger.debug("Finished fetching rolew for the user : " + userRoles); 
		return userRoles;
	}

}