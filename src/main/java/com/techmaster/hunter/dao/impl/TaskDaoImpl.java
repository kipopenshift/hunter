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
import org.springframework.beans.factory.annotation.Autowired;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.json.ReceiverGroupJson;
import com.techmaster.hunter.obj.beans.HunterClient;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.region.RegionService;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterSessionFactory;
import com.techmaster.hunter.util.HunterUtility;

public class TaskDaoImpl implements TaskDao{
	
	private static final Logger logger = Logger.getLogger(TaskDaoImpl.class);
	@Autowired HunterJDBCExecutor hunterJDBCExecutor;
	@Autowired RegionService regionService;

	
	@Override
	public void insertTask(Task task) {
		logger.debug("Inserting the task...");
		Long nextId = getNextTaskId();
		logger.debug("Assigned next task id : " + nextId); 
		task.setTaskId(nextId); 
		HunterHibernateHelper.saveEntity(task);
		logger.debug("Finished inserting task"); 
	}

	@Override
	public void update(Task task) {
		logger.debug("Upading task...");
		HunterHibernateHelper.updateEntity(task);
		logger.debug("Finished updating task!"); 
	}

	@Override
	public void insertTasks(List<Task> tasks) {
		logger.debug("Inserting list of tasks...");
		HunterHibernateHelper.saveEntities(tasks);
		logger.debug("Successfully finished inserting list of tasks."); 
	}

	@Override
	public Task getTaskById(Long taskId) {
		logger.debug("Loading task for given Id ( " + taskId + " )" );
		Task task = HunterHibernateHelper.getEntityById(taskId, Task.class);
		logger.debug("Successfully loaded task by Id!"); 
		return task;
	}

	@Override
	public List<Task> getTasksForClientUserName(String userName) {
		logger.debug("fetching tasks for client user  name = " + userName);
		String query = "SELECT u.userId FROM HunterClient h WHERE h.userName = '" + userName + "'";
		logger.debug("Created query >> " + query);
		List<HunterClient> clients = HunterHibernateHelper.executeQueryForObjList(HunterClient.class, query);
		HunterClient client = null;
		if(clients != null && clients.size() > 0){
			client = clients.get(0);
		}else{
			logger.warn("No client found for the user name passed in. Returning empty Task ArrayList!!"); 
			return new ArrayList<Task>();
		}
		Long clientId = client.getClientId();
		logger.debug("Otained user name for client Id >> " + clientId);
		query = "FROM Task t WHERE t.clientId = '" + clientId + "'";
		logger.debug("Created query >> " + query);
		List<Task> tasks = HunterHibernateHelper.executeQueryForObjList(Task.class, query);
		logger.debug("Successfully obtained tasks list for client user name. Size ( " + tasks == null ? 0 : tasks.size() + " )");
		return tasks;
	}

	@Override
	public Long getNextTaskId() {
		
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		Transaction trans = null;
		
		Long nextId = null;
		
		try {
			session = sessionFactory.openSession();
			trans = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Task.class).setProjection(Projections.max("taskId"));
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
	
	public static void main(String[] args) {
		
		List<Task> tasks = new TaskDaoImpl().getTasksForClientUserName("hlangat");
		System.out.println(HunterUtility.stringifyList(tasks)); 
		
	}

	@Override
	public List<Task> getTaskForClientId(Long clientId) {
		logger.debug("Getting tasks for client : " + clientId); 
		String query = "FROM Task t where t.clientId = '" + clientId + "'";
		List<Task> clientTasks = HunterHibernateHelper.executeQueryForObjList(Task.class, query);
		logger.debug("Finished fetching client tasks!!"); 
		return clientTasks;
	}

	@Override
	public void deleteTask(Task task) {
		logger.debug("Deleting task >> " + task);
		regionService.removeAllRegionsForTask(task.getTaskId());
		HunterHibernateHelper.deleteEntity(task); 
		logger.debug("Finished deleting task!");
	}

	@Override
	public String getTaskMsgType(Long taskId) {
		
		String tskMsgType = null;
		
		String query = "SELECT t.TSK_MSG_TYP FROM task t WHERE t.TSK_ID = ?";
		List<Object> values = new ArrayList<>();
		values.add(taskId);
		Map<Integer, List<Object>> rowMapList = hunterJDBCExecutor.executeQueryRowList(query, values);
		
		if(rowMapList.size() >= 1){
			List<Object> row = rowMapList.get(1);
			tskMsgType = row.get(0) + "";
			logger.debug("Task Message Type obtained for taskId ( " + taskId + " ) : " + tskMsgType); 
		}
		
		return tskMsgType;
	}

	@Override
	public String getUserNameForTaskOwnerId(Long taskId) {
		String query = hunterJDBCExecutor.getQueryForSqlId("getUserNameAndIdForTaskId");
		logger.debug("Using query : " + query); 
		List<Object> values = new ArrayList<>();
		values.add(taskId);
		Map<Integer, List<Object>>  rowMapList = hunterJDBCExecutor.executeQueryRowList(query, values);
		if(rowMapList.size() > 1){
			logger.warn("Warning!!!! Returning more than one row! Using the first row."); 
		}
		List<Object> rowList = rowMapList.get(1);
		logger.debug("Obtained the list >> " + HunterUtility.stringifyList(rowList));
		String userName = null;
		if(!rowList.isEmpty()){
			userName = rowList.get(0).toString();
			logger.debug("Obtained owner owner :  " + userName); 
		}
		return userName;
	}

	@Override
	public Set<ReceiverGroupJson> getTaskReceiverGroups(Long taskId) {
		logger.debug("Fetching receiver groups for task id : " + taskId); 
		Task task = getTaskById(taskId);
		Set<ReceiverGroupJson> groups = task.getTaskGroups();
		logger.debug("Done fetching receiver groups for task id : " + taskId + "! Size ( " + groups.size() + " )"); 
		return groups;
	}

	@Override
	public void updateTaskStatus(Long taskId, String toStatus, String updateBy) {
		logger.debug("Updating status of taskId ( " + taskId + " ) toStatus ( " + toStatus + " )...");
		String updateQuery = "UPDATE task SET TSK_LF_STS = ?,LST_UPDTD = sysdate,UPDTD_BY = ? WHERE tsk_id = ?";
		String approvedUpdateQuery = "UPDATE task SET TSK_LF_STS = ?,LST_UPDTD = sysdate,UPDTD_BY = ?, TSK_APPRVD = to_char('Y'), TSK_APPRVR  = ? WHERE tsk_id = ?";
		updateQuery = toStatus.equals(HunterConstants.STATUS_APPROVED) ? approvedUpdateQuery : updateQuery;
		List<Object> values = new ArrayList<>();
		values.add(toStatus);
		values.add(updateBy);
		if(toStatus.equals(HunterConstants.STATUS_APPROVED)){
			values.add(updateBy);
		}
		values.add(taskId);
		hunterJDBCExecutor.executeUpdate(updateQuery, values);
		logger.debug("Successfully finished updating task status!!"); 
		
	}
	

	@Override
	public void updateTaskDelStatus(Long taskId, String toStatus, String updateBy) {
		logger.debug("Updating status of taskId ( " + taskId + " ) toStatus ( " + toStatus + " )...");
		String query = "UPDATE task SET TSK_DEL_STS = ?,LST_UPDTD = sysdate,UPDTD_BY = ?, TSK_APPRVD = to_char('Y'), TSK_APPRVR  = ? WHERE tsk_id = ?";
		List<Object> values = new ArrayList<>();
		values.add(toStatus);
		values.add(updateBy);
		values.add(updateBy);
		values.add(taskId);
		hunterJDBCExecutor.executeUpdate(query, values);
		logger.debug("Successfully finished updating task status!!"); 
		
	}

	@Override
	public String getCmmSprtdTskNamsFrUsrNam(String userName) {
		logger.debug("Fetching task names for user name : " + userName); 
		String query = hunterJDBCExecutor.getQueryForSqlId("getTaskNamesForClientUserName");
		List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{userName});
		Map<Integer, List<Object>> rowMapList = hunterJDBCExecutor.executeQueryRowList(query, values);
		List<Object> rowList = rowMapList == null ? new ArrayList<>() : rowMapList.get(1); 
		String taskNames = rowList.isEmpty() ? null : rowList.get(0).toString();
		logger.debug("Task names : " + taskNames); 
		return taskNames;
	}

	@Override
	public Map<String, String> getTaskStatuses(Long taskId) {
		logger.debug("Fetching statuses of taskId : " + taskId); 
		Map<String, String> statuses = new HashMap<String,String>();
		String query = "select t.TSK_DEL_STS, t.TSK_LF_STS from task t where t.TSK_ID = ?";
		List<Object> values = new ArrayList<>();
		values.add(taskId);
		Map<Integer, List<Object>> rowMapList = hunterJDBCExecutor.executeQueryRowList(query, values);
		if(rowMapList != null && !rowMapList.isEmpty()){
			List<Object> stsList = rowMapList.get(1);
			statuses.put(HunterConstants.STATUS_TYPE_DELIVERY, HunterUtility.getNullOrStrimgOfObj(stsList.get(0))); 
			statuses.put(HunterConstants.STATUS_TYPE_LIFE, HunterUtility.getNullOrStrimgOfObj(stsList.get(1)));
		}
		return statuses;
	}
	
	


}
