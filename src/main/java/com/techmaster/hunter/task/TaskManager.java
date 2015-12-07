package com.techmaster.hunter.task;

import java.util.List;
import java.util.Map;

import com.techmaster.hunter.gateway.beans.GatewayClient;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.EmailMessage;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.HunterClient;
import com.techmaster.hunter.obj.beans.HunterMessageReceiver;
import com.techmaster.hunter.obj.beans.Message;
import com.techmaster.hunter.obj.beans.ReceiverGroup;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskMessageReceiver;
import com.techmaster.hunter.obj.beans.TextMessage;


public interface TaskManager {
	
	public abstract List<HunterMessageReceiver> getHntrMsgRcvrsFrmRgn(String countryName, String regionLevel, String regionLevelName, String contactType, boolean  activeOnly);
	public abstract List<TaskMessageReceiver> genTaskMsgRcvrsFrmRgn(String countryName,String regionLevel, String regionLevelName, String contactType, boolean activeOnly, Long taskId);
	public abstract List<TaskMessageReceiver> getHunterReceiversForReceiverGroup(ReceiverGroup group, String contactType);
	public abstract List<TaskMessageReceiver> getPreviousReceiversForClient(HunterClient client);
	public abstract List<String> validateTask(Task task, String status);
	public abstract TaskMessageReceiver createTskMsgRcvrFrmHntrMsgRcvr(HunterMessageReceiver hntrMsgRcvr, Long taskId, boolean random);
	public abstract List<String> validateTask(Task task);
	public abstract Message getTaskMessage(Task task);
	public abstract Message getTaskIdMessage(Long taskId);
	public abstract GatewayClient getClientForTask(Task task);
	public abstract Map<String, Object>  processTask(Task task);
	public abstract List<GateWayMessage> getUnSuccessfulMessagesForTask(Task task);
	public abstract Task cloneTask(Task task, String newOwner,String taskName, String taskDescription, AuditInfo auditInfo);
	public abstract TextMessage cloneTextMessage(TextMessage textMessage);
	public abstract EmailMessage cloneEmailMessage(EmailMessage emailMessage);
	public abstract ReceiverGroup createReceiverGroup(String name, String desc,String userName, List<HunterMessageReceiver> hntMsgRcvrs, AuditInfo auditInfo);
	
	
}

