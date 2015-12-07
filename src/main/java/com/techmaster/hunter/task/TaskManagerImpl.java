package com.techmaster.hunter.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.constants.UIMessageConstants;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.HunterMessageReceiverDao;
import com.techmaster.hunter.dao.types.ReceiverRegionDao;
import com.techmaster.hunter.exception.HunterRunTimeException;
import com.techmaster.hunter.gateway.beans.CMClientBean;
import com.techmaster.hunter.gateway.beans.GatewayClient;
import com.techmaster.hunter.gateway.beans.OzekiClient;
import com.techmaster.hunter.gateway.beans.SafaricomClient;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.EmailMessage;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.HunterClient;
import com.techmaster.hunter.obj.beans.HunterMessageReceiver;
import com.techmaster.hunter.obj.beans.Message;
import com.techmaster.hunter.obj.beans.ReceiverGroup;
import com.techmaster.hunter.obj.beans.ReceiverRegion;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskMessageReceiver;
import com.techmaster.hunter.obj.beans.TextMessage;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterLogFactory;
import com.techmaster.hunter.util.HunterUtility;
import com.techmaster.hunter.util.UIMessageHandler;

public class TaskManagerImpl implements TaskManager{
	
	private static UIMessageHandler uiMessageHandler = UIMessageHandler.getInstance();
	private static final Logger logger = Logger.getLogger(TaskManagerImpl.class);
	
	@Autowired private ReceiverRegionDao receiverRegionDao;
	@Autowired private HunterMessageReceiverDao hunterMessageReceiverDao;
	@Autowired private HunterJDBCExecutor hunterJDBCExecutor;
	
	@Override
	public List<HunterMessageReceiver> getHntrMsgRcvrsFrmRgn(String countryName, String regionLevel, String regionLevelName, String contactType, boolean activeOnly) {
		
		String query = hunterJDBCExecutor.getQueryForSqlId("getHntrMsgRcvrsFrmRgn");
		String active = activeOnly ? HunterUtility.singleQuote("Y") : HunterUtility.singleQuote("true") + "," + HunterUtility.singleQuote("false");
		
		Map<String, Object> values = new HashMap<>();
		values.put(":receiverRegionLevel", HunterUtility.singleQuote(regionLevel));
		values.put(":receiverRegionLevelName", HunterUtility.singleQuote(regionLevelName));
		values.put(":receiverType", HunterUtility.singleQuote(contactType));
		values.put(":active", active); 
		
		List<HunterMessageReceiver> hunterMessageReceivers = HunterHibernateHelper.replaceQueryAndExecuteForList(HunterMessageReceiver.class, query, values);
		logger.debug("Finished fetting Hunter Message Reiceivers! Size( " + hunterMessageReceivers.size() + " )" );  
		
		return hunterMessageReceivers;
	}

	@Override
	public List<TaskMessageReceiver> genTaskMsgRcvrsFrmRgn(String countryName,String regionLevel, String regionLevelName, String contactType, boolean activeOnly, Long taskId) {
		
		List<TaskMessageReceiver> tskMsgRcvrs = new ArrayList<>();
		List<HunterMessageReceiver> hntrMsgRcvrs = getHntrMsgRcvrsFrmRgn(countryName, regionLevel, regionLevelName, contactType, activeOnly);
		
		if(hntrMsgRcvrs != null && hntrMsgRcvrs.size() > 0){
			for(HunterMessageReceiver hntrMsgRcvr : hntrMsgRcvrs){
				TaskMessageReceiver tskMsgRcvr = createTskMsgRcvrFrmHntrMsgRcvr(hntrMsgRcvr, taskId, false);
				tskMsgRcvrs.add(tskMsgRcvr);
			}
		}else{
			logger.warn("No hunter message receivers for the given region. Country ( " + countryName + " ) regionLevel( " + regionLevel + " ) regionLevelName ( " + regionLevelName + " )");
		}
		
		logger.debug("Finished creating task message receivers from hunter message receivers! Size( " + tskMsgRcvrs.size() + " )");  
		return tskMsgRcvrs;
	}


	@Override
	public TaskMessageReceiver createTskMsgRcvrFrmHntrMsgRcvr(HunterMessageReceiver hntrMsgRcvr, Long taskId, boolean random) {
		TaskMessageReceiver tskMsgRcvr = new TaskMessageReceiver();
		tskMsgRcvr.setTaskId(taskId);
		tskMsgRcvr.setBlocked(hntrMsgRcvr.isBlocked());
		tskMsgRcvr.setRandom(random); 
		tskMsgRcvr.setReceiverContact(hntrMsgRcvr.getReceiverContact());
		tskMsgRcvr.setReceiverId(hntrMsgRcvr.getReceiverId()); 
		tskMsgRcvr.setReceiverRegionLevel(hntrMsgRcvr.getReceiverRegionLevel());
		tskMsgRcvr.setReceiverType(hntrMsgRcvr.getReceiverType());
		return tskMsgRcvr;
	}

	@Override
	public List<TaskMessageReceiver> getHunterReceiversForReceiverGroup(ReceiverGroup group, String contactType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<TaskMessageReceiver> getPreviousReceiversForClient(HunterClient client) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> validateTask(Task task) {
		
		logger.debug("Starting task validation process"); 
		List<String> errors = new ArrayList<String>();
		Set<ReceiverRegion> regions = task.getTaskRegions();
		
		
		if(task.getDesiredReceiverCount() < 1 ){
			errors.add(uiMessageHandler.getMsgTxtForMsgId(UIMessageConstants.MSG_TASK_006));
		}
		
		if(!task.getTaskLifeStatus().equals(HunterConstants.STATUS_APPROVED)){
			errors.add(uiMessageHandler.getMsgTxtForMsgId(UIMessageConstants.MSG_TASK_001));
		}
		
		if(regions == null || regions.isEmpty()){
			errors.add(uiMessageHandler.getMsgTxtForMsgId(UIMessageConstants.MSG_TASK_002)); 
		}
		
		
		if(task.getTaskMessage() == null){
			errors.add(uiMessageHandler.getMsgTxtForMsgId(UIMessageConstants.MSG_TASK_003));
		}else{
			
			Message message = task.getTaskMessage();
			String lifeStatus = message.getMsgLifeStatus();
			
			if(!lifeStatus.equals(HunterConstants.STATUS_APPROVED)){
				errors.add(uiMessageHandler.getMsgTxtForMsgId(UIMessageConstants.MSG_MSG_001)); 
			}else if(lifeStatus.equals(HunterConstants.STATUS_COMPLETED)){
				errors.add(uiMessageHandler.getMsgTxtForMsgId(UIMessageConstants.MSG_TASK_005)); 
			}
			
			if(message.getMsgText() == null){
				errors.add(uiMessageHandler.getMsgTxtForMsgId(UIMessageConstants.MSG_MSG_002));
			}
			
		}
		if(!errors.isEmpty()){
			logger.debug("Completed task validation. Task is not valid >> " + HunterUtility.stringifyList(errors)); 
		}else{
			logger.debug("Completed task validation. Task is valid!");
		}
		return errors;
	}
	

	@Override
	public List<String> validateTask(Task task, String status) {
		List<String> errors = new ArrayList<String>();
		if(status.equals(HunterConstants.STATUS_APPROVED)){
			errors = validateTask(task);
		}
		return errors;
	}

	@Override
	public Message getTaskMessage(Task task) {
		Message message = getTaskIdMessage(task.getTaskId());
		return message;
	}

	@Override
	public Message getTaskIdMessage(Long taskId) {
		Message message = HunterHibernateHelper.getEntityById(taskId, Message.class);
		return message;
	}

	@Override
	public Map<String, Object> processTask(Task task) {
		Map<String, Object> results = new HashMap<String, Object>();
		List<String> errors = validateTask(task);
		if(errors.size() == 0){
			GatewayClient gatewayClient = getClientForTask(task);
			gatewayClient.execute(null);
		}else{
			results.put(GatewayClient.TASK_VALIDATION_ERRORS, errors);
			results.put(GatewayClient.TASK_VALIDATION_STATUS, HunterConstants.STATUS_FAILED);
		}
		return results;
		
	}

	@Override
	public List<GateWayMessage> getUnSuccessfulMessagesForTask(Task task) {
		String query = "FROM GateWayMessage g WHERE g.taskId = '" + task.getTaskId() + 
						"' AND g.status != '" + HunterConstants.STATUS_PROCESSED + "'" +
						" AND g.status != '" + HunterConstants.STATUS_SUCCESS + "'";
		logger.debug("Executing query : " + query); 
		List<GateWayMessage> gateWayMessages = HunterHibernateHelper.executeQueryForObjList(GateWayMessage.class, query);
		return gateWayMessages;
	}

	@Override
	public Task cloneTask(Task task, String newOwner,String taskName, String taskDescription, AuditInfo auditInfo) {
		
		logger.debug("Starting task cloning process..."); 
		
		String query = hunterJDBCExecutor.getQueryForSqlId("getUserIdUserNameClientIdForUserName"); 
		List<Object> values = new ArrayList<>();
		values.add(newOwner);
		
		Map<Integer, List<Object>> results = hunterJDBCExecutor.executeQueryRowList(query, values);
		
		if(results.isEmpty()){
			throw new IllegalArgumentException("User name provided has not client associated. userName : " + newOwner);
		}
		
		Long clientId = HunterUtility.getLongFromObject(results.get(1).get(2));
		logger.debug("Obtained client id : " + clientId); 
		
		Task copy = new Task();
		copy.setClientId(clientId);
		copy.setDescription(taskDescription);
		copy.setGateWayClient(task.getGateWayClient());
		copy.setRecurrentTask(task.isRecurrentTask());
		copy.setTaskApproved(false); // make this un-approved since it's just a copy.
		copy.setTaskApprover(null); 
		copy.setTaskBudget(0); // make the budget zero first.
		copy.setTaskCost(task.getTaskCost()); 
		copy.setTaskDateline(null);
		copy.setTaskDeliveryStatus(HunterConstants.STATUS_CONCEPTUAL);
		copy.setTaskLifeStatus(HunterConstants.STATUS_DRAFT);
		copy.setTaskName(taskName);
		copy.setTaskObjective(task.getTaskObjective());
		copy.setTaskRegion(task.getTaskRegion());
		copy.setTaskType(task.getTaskType());
		copy.setTskAgrmntLoc(task.getTskAgrmntLoc());
		copy.setDesiredReceiverCount(task.getDesiredReceiverCount());
		copy.setAvailableReceiverCount(task.getAvailableReceiverCount());
		copy.setConfirmedReceiverCount(task.getConfirmedReceiverCount()); 
		
		copy.setCreatedBy(auditInfo.getCreatedBy());
		copy.setCretDate(new Date());
		copy.setUpdatedBy(auditInfo.getLastUpdatedBy()); 
		copy.setLastUpdate(new Date());
		
		copy.setTaskRegion(task.getTaskRegion()); // region should be one to one.
		
		Message message = task.getTaskMessage();
		
		if (message instanceof TextMessage){
			
			TextMessage textMessage = (TextMessage)message;
			TextMessage copyTextMessage = cloneTextMessage(textMessage);
			
			copyTextMessage.setCreatedBy(auditInfo.getCreatedBy());
			copyTextMessage.setLastUpdate(auditInfo.getLastUpdate());
			copyTextMessage.setLastUpdatedBy(auditInfo.getLastUpdatedBy());
			copyTextMessage.setCretDate(auditInfo.getCretDate()); 
			
			copy.setTaskMessage(copyTextMessage);
			
		} else if (message instanceof EmailMessage){
			
			EmailMessage emailMessage = (EmailMessage)message;
			EmailMessage copyEmailMessage = cloneEmailMessage(emailMessage);
			
			copyEmailMessage.setCreatedBy(auditInfo.getCreatedBy());
			copyEmailMessage.setLastUpdate(auditInfo.getLastUpdate());
			copyEmailMessage.setLastUpdatedBy(auditInfo.getLastUpdatedBy());
			copyEmailMessage.setCretDate(auditInfo.getCretDate()); 
			
			copy.setTaskMessage(copyEmailMessage);
			
		} else {
			
			throw new HunterRunTimeException("Please complete implementation before cloning this Message type!!");
			
		}
		
		return copy;
	}

	@Override
	public GatewayClient getClientForTask(Task task) {

		GatewayClient gatewayClient = null;
		String client = task.getGateWayClient();
		
		if(client != null && client.equals(HunterConstants.CLIENT_CM)){
			gatewayClient = new CMClientBean(task);
		}else if(client != null && client.equals(HunterConstants.CLIENT_OZEKI)){
			gatewayClient = new OzekiClient(task);
		}else if(client != null && client.equals(HunterConstants.CLIENT_SAFARICOM)){
			gatewayClient = new SafaricomClient(task);
		}
		
		return gatewayClient;
	}

	@Override
	public TextMessage cloneTextMessage(TextMessage textMessage) {
		
		TextMessage copyTextMessage = new TextMessage();
		
		copyTextMessage.setActualReceivers(textMessage.getActualReceivers());
		copyTextMessage.setConfirmedReceivers(textMessage.getConfirmedReceivers());
		copyTextMessage.setCreatedBy(textMessage.getCreatedBy());
		copyTextMessage.setCretDate(textMessage.getCretDate());
		copyTextMessage.setDesiredReceivers(textMessage.getDesiredReceivers());
		copyTextMessage.setDisclaimer(textMessage.getDisclaimer());
		copyTextMessage.setFromPhone(textMessage.getFromPhone());
		copyTextMessage.setLastUpdate(textMessage.getLastUpdate());
		copyTextMessage.setLastUpdatedBy(textMessage.getLastUpdatedBy());
		copyTextMessage.setMsgDeliveryStatus(HunterConstants.STATUS_CONCEPTUAL);
		copyTextMessage.setMsgOwner(textMessage.getMsgOwner());
		copyTextMessage.setMsgSendDate(null);
		copyTextMessage.setMsgTaskType(textMessage.getMsgTaskType());
		copyTextMessage.setMsgText(textMessage.getMsgText());
		copyTextMessage.setPageable(textMessage.isPageable());
		copyTextMessage.setPageWordCount(textMessage.getPageWordCount());
		copyTextMessage.setProvider(textMessage.getProvider());
		copyTextMessage.setText(textMessage.getText());
		copyTextMessage.setToPhone(textMessage.getToPhone()); 
		
		return copyTextMessage;
	}

	@Override
	public EmailMessage cloneEmailMessage(EmailMessage emailMessage) {
		
		EmailMessage copyEmailMessage = new EmailMessage();
		
		copyEmailMessage.setActualReceivers(emailMessage.getActualReceivers());
		copyEmailMessage.setConfirmedReceivers(emailMessage.getConfirmedReceivers());
		copyEmailMessage.setCreatedBy(emailMessage.getCreatedBy());
		copyEmailMessage.setCretDate(emailMessage.getCretDate());
		copyEmailMessage.setDesiredReceivers(emailMessage.getDesiredReceivers());
		copyEmailMessage.setLastUpdate(emailMessage.getLastUpdate());
		copyEmailMessage.setLastUpdatedBy(emailMessage.getLastUpdatedBy());
		copyEmailMessage.setMsgDeliveryStatus(HunterConstants.STATUS_CONCEPTUAL);
		copyEmailMessage.setMsgOwner(emailMessage.getMsgOwner());
		copyEmailMessage.setMsgSendDate(null);
		copyEmailMessage.setMsgTaskType(emailMessage.getMsgTaskType());
		copyEmailMessage.setMsgText(emailMessage.getMsgText());
		copyEmailMessage.setProvider(emailMessage.getProvider());
		
		copyEmailMessage.setCcList(emailMessage.getCcList());
		copyEmailMessage.seteBody(emailMessage.geteBody());
		copyEmailMessage.setToList(emailMessage.getToList()); 
		copyEmailMessage.seteFooter(emailMessage.geteFooter());
		copyEmailMessage.seteFrom(emailMessage.geteFrom()); 
		copyEmailMessage.seteSubject(emailMessage.geteSubject());
		
		return copyEmailMessage;
		
	}

	@Override
	public ReceiverGroup createReceiverGroup(String name, String desc, String userName, List<HunterMessageReceiver> hntMsgRcvrs, AuditInfo auditInfo) {
		
		Set<HunterMessageReceiver> hunterMessageReceivers = new HashSet<>();
		hunterMessageReceivers.addAll(hntMsgRcvrs);
		
		ReceiverGroup receiverGroup = new ReceiverGroup();
		receiverGroup.setGroupName(name);
		receiverGroup.setAuditInfo(auditInfo);
		receiverGroup.setHunterMessageReceivers(hunterMessageReceivers);
		receiverGroup.setOwnerUserName(userName); 
		
		return receiverGroup;
	}
	
	

	


}
