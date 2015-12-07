package com.techmaster.hunter.executors.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.techmaster.hunter.dao.impl.HunterClientDaoImpl;
import com.techmaster.hunter.dao.impl.TaskDaoImpl;
import com.techmaster.hunter.dao.types.HunterClientDao;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.Task;

public class TaskThreadPool {
	
	private static TaskThreadPool taskThreadPool;
	private static final Logger logger = Logger.getLogger(TaskThreadPool.class);
	private static TaskDao taskDao = new TaskDaoImpl();
	private static HunterClientDao clientDao = new HunterClientDaoImpl();
	
	public static volatile boolean receiversEmptied = false;
	
	public static String TSK_ACTN_GNRT_GTWY_MSGS = "TSK_ACTN_GNRT_GTWY_MSGS";
	
	
	private TaskThreadPool(){super();}; // private constructor.
	
	static{
		if(taskThreadPool == null){
			synchronized (TaskThreadPool.class) {
				taskThreadPool = new TaskThreadPool();
			}
		}
	}
	
	public static TaskThreadPool getInstance(){
		return taskThreadPool;
	}
	
	public static void populateTaskThreadWorkerTaskReceivers(){
		Task task = TaskWorkerThread.task;
		if(task != null){
			if(!task.getTaskReceivers().isEmpty()){
				TaskWorkerThread.taskMessageReceivers.addAll(task.getTaskReceivers());
				logger.debug("Size of receivers after population >> " + TaskWorkerThread.taskMessageReceivers.size()); 
				logger.debug("Successfully populated task receivers for TaskWorkerThread"); 
			}else{
				logger.warn("Task getTaskReceivers is empty and will not populate TaskThreadWorkerTaskReceivers!!");
			}
		}else{
			logger.warn("Task is null and will not populate TaskThreadWorkerTaskReceivers!!"); 
		}
	}
	
	public static String populateClientUserName(){
		String userName = clientDao.getHunterClientById(TaskWorkerThread.task.getClientId()).getUser().getUserName();
		logger.debug("Successfully loaded client user name >> " + userName); 
		TaskWorkerThread.clientUserName = userName;
		return userName;
	}
	
	public void resetTaskThreadWorkersForNewTaskCommand(){
		logger.debug("TaskWorkerThread for command..."); 
		TaskWorkerThread.gateWayMessageQueue.clear();
		TaskWorkerThread.taskMessageReceivers.clear();
		TaskWorkerThread.task = null;
		TaskWorkerThread.clientUserName = null;
	}
	
	
	public String getWorkThreadTaskName(String name){
		return "TASK_"+ name + "_WORKER"; 
	}
	
	public void execute(int howMany, String action, Long beanKey){
		
		logger.info(howMany + " worker threads requested.");
		logger.info(action + " action requested  >> " + action);
		
		logger.debug("Loading task from database..."); 
		Task task = taskDao.getTaskById(beanKey);
		logger.debug("Successfully loaded task from database >> " + task.toString());
		
		logger.debug("Clearing way in TaskWorkerThread in preparation for new Task Command."); 
		resetTaskThreadWorkersForNewTaskCommand();
		
		logger.debug("Preparing TaskWorkerThread for task command..."); 
		TaskWorkerThread.task = task;
		populateClientUserName();
		populateTaskThreadWorkerTaskReceivers();
		TaskWorkerThread.activeAction = action;
		logger.debug("Finshed preparing TaskWorkerThread for task command!!"); 
		
		
		ExecutorService executor = Executors.newFixedThreadPool(howMany);

		 for (int i = 0; i < 15; i++) {
			 String workerId = getWorkThreadTaskName(i+"");
			 Runnable worker = new TaskWorkerThread(workerId, action);
			 executor.execute(worker);
		 }
		 
		 while (!executor.isTerminated()) {
			 if(receiversEmptied){
				 executor.shutdown();
				 if(!TaskWorkerThread.gateWayMessageQueue.isEmpty()){
					 for(GateWayMessage message : TaskWorkerThread.gateWayMessageQueue){
						logger.debug(message);  
					 }
				 }
			 }
		 }
		 
		 
		 logger.debug("All task thread workers in the pool have been executed!"); 
	}
	
}
