package com.techmaster.hunter.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.obj.beans.HunterClient;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.region.RegionService;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterLogFactory;
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
		HunterLogFactory.getLog(getClass()).debug("Obtained next hunter user id >> " + nextId); 
		return nextId;
		
	}
	
	public static void main(String[] args) {
		
		List<Task> tasks = new TaskDaoImpl().getTasksForClientUserName("hlangat");
		System.out.println(HunterUtility.stringifyList(tasks)); 
		
	}

	@Override
	public List<Task> getTaskForClientId(Long clientId) {
		
		SessionFactory sessionFactory = HunterSessionFactory.getSessionFactory();
		Session session = null;
		List<Task> clientTasks = new ArrayList<>();
		
		
		try {
			session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Task.class).add(Restrictions.eq("clientId", clientId));
			List<?> list = criteria.list();
			for(Object obj : list){
				Task  task = (Task)obj;
				clientTasks.add(task);
			}
			HunterLogFactory.getLog(getClass()).debug("Successfully obtained clients for clientId(" + clientId + ") " + HunterUtility.stringifyList(clientTasks)); 
			HunterHibernateHelper.closeSession(session); 
		} catch (HibernateException e) {
			e.printStackTrace();
		}finally{
			HunterHibernateHelper.closeSession(session); 	
		}
		return clientTasks;
	}

	@Override
	public void deleteTask(Task task) {
		logger.debug("Deleting task >> " + task);
		regionService.removeAllRegionsForTask(task.getTaskId());
		HunterHibernateHelper.deleteEntity(task); 
		logger.debug("Finished deleting task!");
	}


}
