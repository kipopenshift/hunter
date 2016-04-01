package com.techmaster.hunter.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.techmaster.hunter.dao.types.TaskHistoryDao;
import com.techmaster.hunter.obj.beans.TaskHistory;
import com.techmaster.hunter.util.HunterHibernateHelper;

public class TaskHistoryDaoImpl implements TaskHistoryDao{
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void insertTaskHistory(TaskHistory taskHistory) {
		logger.debug("Inserting task history...");
		HunterHibernateHelper.saveEntity(taskHistory); 
		logger.debug("Finished inserting task history!");
	}

	@Override
	public void insertTaskHistories(List<TaskHistory> taskHistory) {
		logger.debug("inserting task histories... size( " + (taskHistory == null ? 0 : taskHistory.size()) + " )");
		HunterHibernateHelper.saveEntities(taskHistory); 
		logger.debug("Done saving task histories!");
	}

	@Override
	public List<TaskHistory> getTaskHistoriesByTaskId(Long taskId) {
		logger.debug("Fetching task history for task id ( " + taskId + " )");
		String query = "FROM TaskHistory t WHERE t.taskId = " + taskId + " ORDER BY t.eventDate DESC";
		List<TaskHistory> taskHistories = HunterHibernateHelper.executeQueryForObjList(TaskHistory.class, query);
		logger.debug("Successfully obtained task histories. size ( " + (taskHistories == null ? 0 : taskHistories.size()) + " )"); 
		return taskHistories;
	}

	@Override
	public void deleteTaskHistoriesByTaskId(Long taskId) {
		logger.debug("Deleting task history of task id : " + taskId);
		String readyQuery = "delete TaskHistory where taskId = " + taskId;
		HunterHibernateHelper.executeVoidTransctnlReadyQuery(readyQuery); 
		logger.debug("Successfully deleted task");
	}

}
