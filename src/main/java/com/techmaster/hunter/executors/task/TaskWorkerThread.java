package com.techmaster.hunter.executors.task;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskMessageReceiver;

public class TaskWorkerThread implements Runnable {
	
	private Logger logger = Logger.getLogger(TaskWorkerThread.class);
	
	private String workerId;
	private String command;
	
	public static Task task = null;
	public static String clientUserName;
	public static String activeAction;
	
	public Object synchronizer = new Object();
	
	public static final Queue<GateWayMessage> gateWayMessageQueue = new LinkedList<GateWayMessage>();
	public static Queue<TaskMessageReceiver> taskMessageReceivers = new LinkedList<TaskMessageReceiver>();  
	
	public TaskWorkerThread(String workerId, String command) {
		super();
		this.workerId = workerId;
		this.command = command;
	}

	@Override
	public void run() {
		 logger.debug("Beginning to run >> " + getThreadWithCommandName());
		 logger.debug("Thread worker assigned command >> " + this.command); 
 		 processCommand();
	}
	
	public String getThreadWithCommandName(){
		String tName = Thread.currentThread().getName();
		String allName = tName + "_" + this.workerId;
		return allName;
	}
	
	private void processCommand(){
		
		if(command.equals(TaskThreadPool.TSK_ACTN_GNRT_GTWY_MSGS)){
			
				while(!TaskThreadPool.receiversEmptied){
					TaskMessageReceiver taskMessageReceiver = null;
					synchronized (synchronizer) {
						taskMessageReceiver = taskMessageReceivers.poll();
						if(taskMessageReceivers.isEmpty()){
							TaskThreadPool.receiversEmptied = true;
							logger.debug("Task receivers is now empty... Setting empty flag!!"); 
						}
					}
					logger.debug("Creating gateway message..."); 
					GateWayMessage gateWayMessage = new GateWayMessage();
					gateWayMessage.setClntRspCode(null);
					gateWayMessage.setClntRspText(null);
					gateWayMessage.setErrorText(null);
					gateWayMessage.setMsgId(null);
					gateWayMessage.setRequestBody(null);
					gateWayMessage.setSendDate(null);
					gateWayMessage.setSubsRspnsCode(null);
					gateWayMessage.setSubsRspnsText(null);
					
					gateWayMessage.setContact(taskMessageReceiver.getReceiverContact()); 
					gateWayMessage.setCreatedBy(HunterConstants.HUNTER_ADMIN_USER_NAME);
					gateWayMessage.setCreatedOn(new Date());
					gateWayMessage.setDuration(0L);
					gateWayMessage.setgClient(task.getGateWayClient());
					gateWayMessage.setMessageType(task.getTaskType());
					gateWayMessage.setStatus(HunterConstants.STATUS_DRAFT);
					gateWayMessage.setTaskId(task.getTaskId());
					gateWayMessage.setText(task.getTaskMessage().getMsgText()); 
					
					gateWayMessageQueue.add(gateWayMessage);
					logger.debug(getThreadWithCommandName() + " created gateWayMessage >> " + gateWayMessage);
					
			}
			
		}
	}
	
	
	
	

}
