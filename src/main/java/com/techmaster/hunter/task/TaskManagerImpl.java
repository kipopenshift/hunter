package com.techmaster.hunter.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.techmaster.hunter.cache.HunterCacheUtil;
import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.constants.UIMessageConstants;
import com.techmaster.hunter.dao.impl.HunterDaoFactory;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.HunterMessageReceiverDao;
import com.techmaster.hunter.dao.types.MessageDao;
import com.techmaster.hunter.dao.types.ReceiverRegionDao;
import com.techmaster.hunter.dao.types.ServiceProviderDao;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.enums.HunterUserRolesEnums;
import com.techmaster.hunter.exception.HunterRunTimeException;
import com.techmaster.hunter.gateway.beans.CMClient;
import com.techmaster.hunter.gateway.beans.CMClientService;
import com.techmaster.hunter.gateway.beans.GateWayClientHelper;
import com.techmaster.hunter.gateway.beans.GateWayClientService;
import com.techmaster.hunter.gateway.beans.GatewayClient;
import com.techmaster.hunter.gateway.beans.HunterEmailClient;
import com.techmaster.hunter.gateway.beans.HunterEmailClientService;
import com.techmaster.hunter.gateway.beans.OzekiClient;
import com.techmaster.hunter.gateway.beans.OzekiClientService;
import com.techmaster.hunter.json.ReceiverGroupJson;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.EmailMessage;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.HunterJacksonMapper;
import com.techmaster.hunter.obj.beans.HunterMessageReceiver;
import com.techmaster.hunter.obj.beans.Message;
import com.techmaster.hunter.obj.beans.ReceiverRegion;
import com.techmaster.hunter.obj.beans.ServiceProvider;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskHistory;
import com.techmaster.hunter.obj.beans.TaskMessageReceiver;
import com.techmaster.hunter.obj.beans.TextMessage;
import com.techmaster.hunter.region.RegionService;
import com.techmaster.hunter.util.HunterHibernateHelper;
import com.techmaster.hunter.util.HunterUtility;

public class TaskManagerImpl implements TaskManager{
	
	private static final Logger logger = Logger.getLogger(TaskManagerImpl.class);
	
	@Autowired private ReceiverRegionDao receiverRegionDao;
	@Autowired private HunterMessageReceiverDao hunterMessageReceiverDao;
	@Autowired private HunterJDBCExecutor hunterJDBCExecutor;
	@Autowired private ServiceProviderDao serviceProviderDao;
	@Autowired private RegionService regionService;
	@Autowired private MessageDao messageDao;
	@Autowired private HunterJacksonMapper hunterJacksonMapper;
	@Autowired private TaskDao taskDao;
	@Autowired private HunterDaoFactory hunterDaoFactory;
	
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

	private List<String> validateGroupsAndRegions(Task task){
		String groupQuery = hunterJDBCExecutor.getQueryForSqlId("getTotalTaskGroupsReceivers");
		int groupCount = 0;
		List<Object> values = new ArrayList<>();
		values.add(task.getTaskId());
		Map<Integer, List<Object>> rowListsMap = hunterJDBCExecutor.executeQueryRowList(groupQuery, values);
		if(rowListsMap != null && !rowListsMap.isEmpty()){
			List<Object> rowList = rowListsMap.get(1);
			if(rowList != null && !rowList.isEmpty()){
				String obj = HunterUtility.getStringOrNullOfObj(rowList.get(0));
				if(obj != null)
					groupCount = Integer.parseInt(obj); 
				logger.debug("Total receiver for task group : " + groupCount);
			}
		}
		List<String> errorList = new ArrayList<>();
		//Does is have region or group configured?
		boolean noRegionConfigured = task.getTaskRegions().isEmpty();
		boolean noGroupConfigured = task.getTaskGroups().isEmpty();
		
		if(noRegionConfigured && noGroupConfigured){
			errorList.add("No group or region is configured!");
		}
		
		Object [] receiversNumberObj = regionService.getTrueHntrMsgRcvrCntFrTaskRgns(task.getTaskId());
		int regionCount = Integer.parseInt(receiversNumberObj[0]+"");
		
		if((regionCount+groupCount) < 1){
			errorList.add("No receivers found for tsk groups and regions!");
		}
		
		return errorList;
		
	}

	@Override
	public List<String> validateTask(Task task) {
		
		logger.debug("Starting task validation process"); 
		List<String> errors = new ArrayList<String>();
		if(task.getDesiredReceiverCount() < 1 ){
			//Task must have more than one receiver
			errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_TASK_006)); 
		}
		String taskLifeStatus = task.getTaskLifeStatus();
		if(!taskLifeStatus.equals(HunterConstants.STATUS_APPROVED)){
			//Task is not in approved status!
			errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_TASK_001));
		}
		String query = "SELECT t.TSK_DEL_STS FROM task t WHERE t.TSK_ID = '" +  task.getTaskId() + "'";
		Map<Integer, List<Object>> rowListMap = hunterJDBCExecutor.executeQueryRowList(query, null);
		List<Object> rowList = rowListMap.get(1);
		String delStatus = rowList != null && !rowList.isEmpty() ? rowList.get(0).toString() : null;
		
		if(delStatus != null && (delStatus.equals(HunterConstants.STATUS_PARTIAL) || delStatus.equals(HunterConstants.STATUS_PROCESSED) || delStatus.equals(HunterConstants.STATUS_FAILED)) ){
			errors.add("Task has been processed already!"); 
		}
		
		List<String> regionGroupErrors = validateGroupsAndRegions(task);
		if(!regionGroupErrors.isEmpty()){
			logger.debug("Groups and regions validation failed : " + HunterUtility.stringifyList(regionGroupErrors)); 
			errors.addAll(regionGroupErrors);
		}
		
		
		if(task.getTaskMessage() == null){
			//Task has no message configured!
			errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_TASK_003));
		}else{
			Message message = messageDao.getMessageById(task.getTaskId());
			String lifeStatus = message.getMsgLifeStatus();
			boolean isApproved = lifeStatus.equals(HunterConstants.STATUS_APPROVED);
			boolean isCompleted = lifeStatus.equals(HunterConstants.STATUS_PROCESSED);
			if(!isApproved && !isCompleted){
				//Task Message is not in approved status!
				errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_MSG_001)); 
			}else if(isCompleted && !isApproved){ 
				//Task message is already processed!
				errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_MSG_003));
			}
			if(message.getMsgText() == null || message.getMsgText().trim().equals("")){ 
				//Task Message has no Text!
				errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_MSG_002));
			}
			ServiceProvider serviceProvider  = message.getProvider();
			if(serviceProvider == null && !(task.getTaskMessage() instanceof EmailMessage)){
				errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_MSG_006)); 
			}
		}
		
		// make sure that the money is enough!!
		List<String> errors_ = validateTaskFinance(task);
		errors.addAll(errors_);
		
		Map<String,String> statuses = taskDao.getTaskStatuses(task.getTaskId());
		
		
		if(statuses == null || statuses.isEmpty() || HunterConstants.STATUS_PENDING.equals(statuses.get(HunterConstants.STATUS_TYPE_DELIVERY))){  
			//"Task has been submitted and is pending processing"
			errors.add(HunterCacheUtil.getInstance().getUIMsgDescForMsgId(UIMessageConstants.MSG_TASK_012)); 
		}
		
		
		if(!errors.isEmpty()){
			logger.debug("Completed task validation. Task is not valid >> " + HunterUtility.stringifyList(errors)); 
		}else{
			logger.debug("Completed task validation. Task is valid!");
		}
		return errors;
	}
	
	@Override
	public boolean userHasRole(String roleName, String userName) {
		String query = hunterJDBCExecutor.getQueryForSqlId("getUserRoleDetails");
		List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{userName,userName});
		Map<Integer, List<Object>> rowMapList = hunterJDBCExecutor.executeQueryRowList(query, values);
		boolean hasApproveRole = false;
		if(rowMapList != null && !rowMapList.isEmpty()){
			for(Map.Entry<Integer, List<Object>> entry : rowMapList.entrySet()){
				List<Object> role = entry.getValue();
				String inRoleName = role.get(2).toString(); 
				hasApproveRole = roleName.equals(inRoleName); 
				if(hasApproveRole)
					break;
			}
		}
		return hasApproveRole;
	}

	@Override
	public List<String> validateStatusChange(Long taskId, String status, String userName) {
		
		logger.debug("Starting task status change validation process. Task Id = " + taskId + ". To status = " + status); 
		
		Task task = taskDao.getTaskById(taskId);
		Message message = task.getTaskMessage();
		Set<ReceiverGroupJson> taskGroups = task.getTaskGroups();
		List<String> errors = new ArrayList<>();
		
		String stsQuery = "SELECT TSK_LF_STS FROM TASK WHERE TSK_ID = ?";
		List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{taskId});
		Map<Integer, List<Object>> stsRowListMap = hunterJDBCExecutor.executeQueryRowList(stsQuery, values);
		String currentStatus = stsRowListMap == null || stsRowListMap.isEmpty() ? null : stsRowListMap.get(1).get(0).toString();
		values.clear();
		/*
		 * User must have approver role to approve or unapprove a task.
		 * */
		if(status.equals(HunterConstants.STATUS_APPROVED) || (status.equals(HunterConstants.STATUS_DRAFT) && currentStatus.equals(HunterConstants.STATUS_APPROVED))){ 
			if(!userHasRole(HunterUserRolesEnums.ROLE_TASK_APPROVER.getName(), userName)){
				errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_TASK_009));
				return errors;
			}
		}
		
		/*
		 * Moving it to draft needs nothing much else. 
		 */
		
		if(HunterConstants.STATUS_DRAFT.equals(status)){
			logger.debug("Moving task to draft. No more validations required."); 
			return errors;
		}
		
		
		/*
		 * for review and draft status, just ensure that the message is there 
		 * and that either groups or regions is not empty.
		 */
		if(status != null && !status.equals(HunterConstants.STATUS_APPROVED)){
			if(message == null){
				errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_TASK_003 ));
			}
			if(taskGroups.isEmpty() && task.getTaskRegions().isEmpty()){
				//Task has no region and no groups configured!
				errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_TASK_008 ));
			}
			String errorMsg = errors.isEmpty() ? "Not errors found!!" : "Errors found : \n" + HunterUtility.stringifyList(errors);
			logger.debug(errorMsg);
			return errors;
		}
		
		if(message != null){
			//Task message must be in approved status
			String msgStatus = message.getMsgLifeStatus();
			if( !msgStatus.equals(HunterConstants.STATUS_APPROVED )){
				errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_MSG_005 ));
			}
		}else{
			//Task has no message configured!
			errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_MSG_003 )); 
		}
		
		int groupCount = 0;
		
		if(taskGroups != null && !taskGroups.isEmpty()){
			for(ReceiverGroupJson group : taskGroups){
				int count = group.getReceiverCount();
				groupCount += count;
			}
		}
		
		Object[] countData = regionService.getTrueHntrMsgRcvrCntFrTaskRgns(taskId);
		int regionCount = countData != null ? Integer.parseInt(countData[0]+"") : 0; 
		int totalCount = groupCount + regionCount;
		
		if(totalCount == 0){
			//Task regions and groups configured have no receivers
			errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_TASK_007 ));
		}
		
		String errorMsg = errors.isEmpty() ? "Not errors found!!" : "Errors found !! : \n" + HunterUtility.stringifyList(errors);
		logger.debug(errorMsg);
		
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
	public Map<String, Object> getGateWayClientExecuteMap(Task task) {
		Map<String, Object> executeClientMap = null;
		String type = task.getTskMsgType();
		if(type.equals(HunterConstants.MESSAGE_TYPE_EMAIL)){
			executeClientMap = new HashMap<>();
			executeClientMap.put(GatewayClient.TASK_BEAN, task);
		}
		return executeClientMap;
	}

	@Override
	public Map<String, Object> processTask(Task task, AuditInfo auditInfo) {
		
		Map<String, Object> results = new HashMap<String, Object>();
		
		//user must have task processor privileges
		if(!userHasRole(HunterUserRolesEnums.ROLE_TASK_PROCESSOR.getName(), auditInfo.getLastUpdatedBy())){ 
			List<String> processErrors = validateTask(task);
			String message = HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_TASK_010 );
			logger.debug(message); 
			processErrors.add(message);
			results.put(GatewayClient.TASK_VALIDATION_ERRORS, processErrors);
			results.put(GatewayClient.TASK_VALIDATION_STATUS, HunterConstants.STATUS_FAILED);
			return results;
		}
		
		List<String> errors = validateTask(task);
		boolean isLocked = GateWayClientHelper.getInstance().isTaskLocked(task);
		
		if(!isLocked){
			logger.debug("Task is not locked. Locking the task now..."); 
			task.setTaskDeliveryStatus(HunterConstants.STATUS_PENDING);
			GateWayClientHelper.getInstance().lockTask(task.getTaskId(), HunterConstants.STATUS_PENDING);
		}else{
			logger.debug("Task is locked. Doing nothing and retuning..."); 
			errors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_TASK_012)); 
		}
		
		if(errors.size() == 0){
			logger.debug("No processing validation errors found. Sending task process notification email..."); 
			task.setProcessedOn(new Date());
			task.setProcessedBy(auditInfo.getLastUpdatedBy()); // this is set immediately.
			if(task.getGateWayClient().equals(HunterConstants.CLIENT_CM)){
				GateWayClientService client = new CMClientService();
				Map<String, Object> executeParams = new HashMap<>();
				executeParams.put(GateWayClientService.PARAM_AUDIT_INFO, auditInfo);
				executeParams.put(GateWayClientService.TASK_BEAN, task);
				client.execute(executeParams);
			}else if(task.getGateWayClient().equals(HunterConstants.CLIENT_OZEKI)){
				GateWayClientService client = new OzekiClientService();
				Map<String, Object> executeParams = new HashMap<>();
				executeParams.put(GateWayClientService.PARAM_AUDIT_INFO, auditInfo);
				executeParams.put(GateWayClientService.TASK_BEAN, task);
				client.execute(executeParams);
			}else if(task.getGateWayClient().equals(HunterConstants.CLIENT_HUNTER_EMAIL)){ 
				HunterEmailClientService emailClientService = new HunterEmailClientService();
				Map<String, Object> executeParams = new HashMap<>();
				executeParams.put(GateWayClientService.PARAM_AUDIT_INFO, auditInfo);
				executeParams.put(GateWayClientService.TASK_BEAN, task);
				emailClientService.execute(executeParams);
			}else{
				GatewayClient gatewayClient = getClientForTask(task);
				Map<String, Object> executeParams = getGateWayClientExecuteMap(task);
				gatewayClient.execute(executeParams);
			}
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
		
		//append '_(#id#)' for identification
		taskName = taskName + "_" + task.getTaskId();
		
		String query = hunterJDBCExecutor.getQueryForSqlId("getClientDetailsForTaskOwner"); 
		List<Object> values = new ArrayList<>();
		values.add(newOwner);
		
		Map<Integer, List<Object>> results = hunterJDBCExecutor.executeQueryRowList(query, values);
		
		if(results.isEmpty()){
			throw new IllegalArgumentException("User name provided has not client associated. userName : " + newOwner);
		}
		
		Long clientId = HunterUtility.getLongFromObject(results.get(1).get(2));
		logger.debug("Obtained client id : " + clientId); 
		
		Long nextTaskId = HunterHibernateHelper.getMaxEntityIdAsNumber(Task.class, Long.class, "taskId");
		nextTaskId = nextTaskId == null ? 1 : (nextTaskId+1);
		
		Task copy = new Task();
		copy.setTaskId(nextTaskId); 
		copy.setClientId(clientId);
		copy.setDescription(taskDescription);
		copy.setGateWayClient(task.getGateWayClient());
		copy.setRecurrentTask(task.isRecurrentTask());
		copy.setTaskApproved(false); // make this un-approved since it's just a copy.
		copy.setTaskApprover(null); 
		copy.setTaskBudget(task.getTaskBudget()); // make the budget zero first.
		copy.setTaskCost(task.getTaskCost()); 
		copy.setTaskDateline(null);
		copy.setTaskDeliveryStatus(HunterConstants.STATUS_CONCEPTUAL);
		copy.setTaskLifeStatus(HunterConstants.STATUS_DRAFT);
		copy.setTaskName(taskName);
		copy.setTaskObjective(task.getTaskObjective());
		copy.setTaskType(task.getTaskType());
		copy.setTskAgrmntLoc(task.getTskAgrmntLoc());
		copy.setDesiredReceiverCount(task.getDesiredReceiverCount());
		copy.setAvailableReceiverCount(task.getAvailableReceiverCount());
		copy.setConfirmedReceiverCount(task.getConfirmedReceiverCount()); 
		copy.setTskMsgType(task.getTskMsgType()); 
		copy.setProcessedBy(task.getProcessedBy());
		copy.setProcessedOn(task.getProcessedOn());
		copy.setGateWayClient(task.getGateWayClient()); 
		
		copy.setCreatedBy(auditInfo.getCreatedBy());
		copy.setCretDate(new Date());
		copy.setUpdatedBy(auditInfo.getLastUpdatedBy()); 
		copy.setLastUpdate(new Date());
		
		Message message = task.getTaskMessage();
		
		if (message instanceof TextMessage){
			
			TextMessage textMessage = (TextMessage)message;
			TextMessage copyTextMessage = cloneTextMessage(textMessage);
			
			copyTextMessage.setCreatedBy(auditInfo.getCreatedBy());
			copyTextMessage.setLastUpdate(auditInfo.getLastUpdate());
			copyTextMessage.setLastUpdatedBy(auditInfo.getLastUpdatedBy());
			copyTextMessage.setCretDate(auditInfo.getCretDate()); 
			
			logger.debug("Text message copied : " + copyTextMessage); 
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
		
		//it is a one to one relationship.
		copy.getTaskMessage().setMsgId(copy.getTaskId()); 
		
		logger.debug("Copying task receiver groups..."); 
		Set<ReceiverGroupJson> receiverGroups = task.getTaskGroups();
		copy.setTaskGroups(receiverGroups);
		logger.debug("Finished copying task receiver groups!");
		
		logger.debug("Copying task regions..."); 
		Set<ReceiverRegion> receiverRegions = task.getTaskRegions();
		copy.setTaskRegions(receiverRegions); 
		logger.debug("Finished copying task receiver regions!");

		return copy;
	}

	@Override
	public GatewayClient getClientForTask(Task task) {

		GatewayClient gatewayClient = null;
		String client = task.getGateWayClient();
		
		if(client != null && client.equals(HunterConstants.CLIENT_CM)){
			gatewayClient = new CMClient(task);
		}else if(client != null && client.equals(HunterConstants.CLIENT_OZEKI)){
			gatewayClient = new OzekiClient(task);
		}else if(client != null && client.equals(HunterConstants.CLIENT_HUNTER_EMAIL)){
			gatewayClient = HunterEmailClient.getInstance();
		}else{
			logger.warn("Gateway client configured does not exist or it's not implemented yet!"); 
			throw new IllegalArgumentException("No service for the requested parameter : " + client);
		}
		
		return gatewayClient;
	}
	
	private String getCloneMsgLifeStatus(String currSts){
		if(!HunterUtility.notNullNotEmpty(currSts)) 
			return HunterConstants.STATUS_DRAFT; 
		if(HunterConstants.STATUS_PROCESSED.equals(currSts)) 
			return HunterConstants.STATUS_APPROVED;
		return currSts;
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
		copyTextMessage.setMsgLifeStatus(getCloneMsgLifeStatus(textMessage.getMsgLifeStatus()));
		copyTextMessage.setMsgSendDate(textMessage.getMsgSendDate());
		
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
		copyEmailMessage.setEmailTemplateName(emailMessage.getEmailTemplateName()); 
		copyEmailMessage.setPriority(emailMessage.getPriority());
		copyEmailMessage.setCssObject(emailMessage.getCssObject());
		copyEmailMessage.setReplyTo(emailMessage.getReplyTo());
		copyEmailMessage.setAttchmtntFileType(emailMessage.getAttchmtntFileType()); 
		copyEmailMessage.setAttchmntBnId(emailMessage.getAttchmntBnId()); 
		copyEmailMessage.setHasAttachment(emailMessage.isHasAttachment());
		copyEmailMessage.setMultiPart(emailMessage.isMultiPart()); 
		copyEmailMessage.setMsgLifeStatus(getCloneMsgLifeStatus(emailMessage.getMsgLifeStatus()));
		copyEmailMessage.setMsgSendDate(emailMessage.getMsgSendDate());
		
		copyEmailMessage.setCcList(emailMessage.getCcList());
		copyEmailMessage.seteBody(emailMessage.geteBody());
		copyEmailMessage.setToList(emailMessage.getToList()); 
		copyEmailMessage.seteFooter(emailMessage.geteFooter());
		copyEmailMessage.seteFrom(emailMessage.geteFrom()); 
		copyEmailMessage.seteSubject(emailMessage.geteSubject());
		
		return copyEmailMessage;
		
	}

	private static void setDefaultMessageFields(Message message){
		message.setActualReceivers(0);
		message.setConfirmedReceivers(0);
		message.setCreatedBy(null);
		message.setCretDate(new Date());
		message.setDesiredReceivers(0);
		message.setLastUpdate(new Date());
		message.setMsgDeliveryStatus(HunterConstants.STATUS_CONCEPTUAL);
		message.setMsgLifeStatus(HunterConstants.STATUS_DRAFT);
		message.setMsgText(null); 
	}

	@Override
	public Message getTaskDefaultMessage(Long taskId, String type) {
		Message message = null;
		if(type.equals(HunterConstants.MESSAGE_TYPE_TEXT)){
			message = new TextMessage();
			setDefaultMessageFields(message);
			return (TextMessage)message;
		}
		return message;
	}

	@Override
	public TextMessage convertTextMessage(String json) {
		
		logger.debug("Converting json to TextMessage : " + json); 
		
		if(json == null)
			return null;
		
		TextMessage message = new TextMessage();
		JSONObject msgJson = new JSONObject(json);
		
		logger.debug("Creating textMessage for json >> " + msgJson); 
		
		long msgId = msgJson.getLong("msgId");
		String msgSendDate_ = msgJson.has("msgSendDate") ? msgJson.get("msgSendDate") != null ? msgJson.get("msgSendDate").toString() : null : null;
		
		if(msgSendDate_ != null){
			msgSendDate_ = msgSendDate_.replaceAll("T", " ");
			msgSendDate_ = msgSendDate_.replaceAll("Z", "");
		}
		
		Date msgSendDate = HunterUtility.parseDate(msgSendDate_, HunterConstants.HUNTER_DATE_FORMAT_MIN);
		String msgTaskType = msgJson.has("msgTaskType") ? msgJson.get("msgTaskType") != null ? msgJson.get("msgTaskType").toString() : null : null;
		
		int desiredReceivers = msgJson.has("desiredReceivers") ? msgJson.getInt("desiredReceivers") : 0;
		int actualReceivers = msgJson.has("actualReceivers") ? msgJson.getInt("actualReceivers") : 0;
		int confirmedReceivers = msgJson.has("confirmedReceivers") ? msgJson.getInt("confirmedReceivers") : 0;
		
		String msgDeliveryStatus = msgJson.has("msgDeliveryStatus") ? msgJson.get("msgDeliveryStatus") != null ? msgJson.get("msgDeliveryStatus").toString() : null : null;
		String msgLifeStatus = msgJson.has("msgLifeStatus") ? msgJson.get("msgLifeStatus") != null ? msgJson.get("msgLifeStatus").toString() : null : null;
		String msgText = msgJson.has("msgText") ? msgJson.get("msgText") != null ? msgJson.get("msgText").toString() : null : null;
		
		String msgOwner = msgJson.has("msgOwner") ? msgJson.get("msgOwner") != null ? msgJson.get("msgOwner").toString() : null : null;
		String text = msgJson.has("text") ? msgJson.get("text") != null ? msgJson.get("text").toString() : null : null;
		String disclaimer = msgJson.has("disclaimer") ? msgJson.get("disclaimer") != null ? msgJson.get("disclaimer").toString() : null : null;
		String fromPhone = msgJson.has("fromPhone") ? msgJson.get("fromPhone") != null ? msgJson.get("fromPhone").toString() : null : null;
		String toPhone = msgJson.has("toPhone") ? msgJson.get("toPhone") != null ? msgJson.get("toPhone").toString() : null : null;
		int pageWordCount = msgJson.has("pageWordCount") ? msgJson.getInt("pageWordCount") : 0;
		
		String cretDate_ = msgJson.has("cretDate") ? msgJson.get("cretDate") != null ? msgJson.get("cretDate").toString() : null : null;
		Date cretDate = HunterUtility.parseDate(cretDate_, HunterConstants.HUNTER_DATE_FORMAT_MIN);
		String lastUpdate_ = msgJson.has("lastUpdate") ? msgJson.get("lastUpdate") != null ? msgJson.get("lastUpdate").toString() : null : null;
		Date lastUpdate = HunterUtility.parseDate(lastUpdate_, HunterConstants.HUNTER_DATE_FORMAT_MIN);
		String createdBy = msgJson.has("createdBy") ? msgJson.get("createdBy") != null ? msgJson.get("createdBy").toString() : null : null;
		String lastUpdatedBy = msgJson.has("lastUpdatedBy") ? msgJson.get("lastUpdatedBy") != null ? msgJson.get("lastUpdatedBy").toString() : null : null;
		
		message.setMsgId(msgId);
		message.setMsgDeliveryStatus(msgDeliveryStatus);
		message.setMsgLifeStatus(msgLifeStatus);
		message.setMsgSendDate(msgSendDate);
		message.setMsgTaskType(msgTaskType);
		message.setMsgText(msgText);
		message.setDesiredReceivers(desiredReceivers);
		message.setActualReceivers(actualReceivers);
		message.setConfirmedReceivers(confirmedReceivers);
		message.setMsgOwner(msgOwner);
		
		message.setCretDate(cretDate);
		message.setLastUpdate(lastUpdate);
		message.setCreatedBy(createdBy);
		message.setLastUpdatedBy(lastUpdatedBy);
		
		message.setText(text);
		message.setDisclaimer(disclaimer); 
		message.setFromPhone(fromPhone);
		message.setToPhone(toPhone); 
		message.setPageWordCount(pageWordCount); 
		
		String providerStr = HunterUtility.getStringOrNullFromJSONObj(msgJson, "provider"); 
		ServiceProvider pvdr = null;
		
		try {
			if(providerStr != null && !HunterUtility.isNumeric(providerStr)){
				logger.debug("Provider is json. Reading value... " + providerStr);
				JSONObject providerJson = new JSONObject(providerStr);
				providerJson = HunterUtility.selectivelyCopyJSONObject(providerJson, new String[]{"handler","hibernateLazyInitializer"});
				pvdr = hunterJacksonMapper.readValue(providerJson.toString(), ServiceProvider.class);
			}else{
				logger.debug("Provider value is numerice : " + providerStr); 
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Long providerId = null; 
		ServiceProvider serviceProvider = null;
		
		if(pvdr == null){
			providerId = HunterUtility.getLongFromObject(providerStr);
			logger.debug("Successfully obtained provider id : " + providerId);
			serviceProvider = serviceProviderDao.getServiceProviderById(providerId);
			message.setProvider(serviceProvider); 
		}else{
			message.setProvider(pvdr); 
		}
		
		logger.debug("Successfully created textMessage >> " + message); 
		
		
		return message;
	}

	@Override
	public String addGroupToTask(Long groupId, Long taskId) {
		
		String checkQuery = hunterJDBCExecutor.getQueryForSqlId("checkExistentForTaskAndReceiverGroup");
		List<Object> values = new ArrayList<>();
		values.add(taskId);
		values.add(groupId);
		values.add(taskId);
		values.add(groupId);
		
		Map<Integer, List<Object>> rowMapList = hunterJDBCExecutor.executeQueryRowList(checkQuery, values);
		List<Object> counts = rowMapList.get(1);
		
		int taskCount = Integer.parseInt(counts.get(0)+""); 
		int groupCount = Integer.parseInt(counts.get(1)+"");
		int alreadyCount = Integer.parseInt(counts.get(2)+"");
		
		if(alreadyCount >= 1){
			logger.debug("Task group is already added to the task! Returning..."); 
			return "Receiver group is already added to task!";
		}else if(taskCount == 0 && groupCount == 0){
			logger.debug("No task and no group found for ( task id : " + taskId + ", group id : " + groupId + " )"); 
			return "No task and no group found for ( task id : " + taskId + ", group id : " + groupId + " )";
		}else if(taskCount == 0 && groupCount != 0){
			logger.debug("No task found for ( task id : " + taskId + " )");
			return "No task found for ( task id : " + taskId + " )";
		}else if(taskCount != 0 && groupCount == 0){
			logger.debug("No group found for ( group id : " + groupId + " )");
			return "No group found for ( group id : " + groupId + " )";
		}
		
		String insertQuery = "INSERT INTO TSK_GRPS (TSK_ID,GRP_ID) VALUES(?, ?) ";
		values.clear();
		values.add(taskId);
		values.add(groupId);
		
		logger.debug("Executing query to insert group to task : " + insertQuery); 
		
		try {
			hunterJDBCExecutor.executeUpdate(insertQuery, values);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		return null;
	}

	@Override
	public void removeGroupFromTask(Long groupId, Long taskId) {
		String delQuery = "DELETE FROM TSK_GRPS WHERE TSK_ID = ? AND GRP_ID = ?";
		logger.debug("Executing query to remove group from task : " + delQuery); 
		List<Object> values = Arrays.asList(new Object[]{taskId,groupId});
		hunterJDBCExecutor.executeUpdate(delQuery, values); 
		logger.debug("Finished executing query to remove group from task"); 
	}

	@Override
	public int getTotalTaskGroupsReceivers(Long taskId) {
		logger.debug("Fetching groups receiver count for task id : " + taskId); 
		String query = hunterJDBCExecutor.getQueryForSqlId("getTotalTaskGroupsReceivers");
		List<Object> values = new ArrayList<>();
		values.add(taskId);
		Map<Integer, List<Object>> groups = hunterJDBCExecutor.executeQueryRowList(query, values);
		List<Object> rowList = groups != null && !groups.isEmpty() ? groups.get(1) : new ArrayList<>();
		int count = Integer.parseInt((rowList.isEmpty() ? 0 : rowList.get(0)).toString());
		logger.debug("Receiver count obtained : " + count); 
		return count;
	}

	@Override
	public List<String> validateTaskFinance(Task task) {
		
		String msgType = task.getTskMsgType();
		List<String> errors = new ArrayList<>();
		
		if(msgType != null && msgType.equals(HunterConstants.MESSAGE_TYPE_EMAIL)){
			logger.debug("Sending emails does not take money. Returning."); 
			return errors;
		}
		Message message = task.getTaskMessage();
		if(message == null){
			// we are not validating message here.
			logger.debug("Task does not have message configured!!"); 
			return errors;
		}
		ServiceProvider serviceProvider = task.getTaskMessage().getProvider();
		float budget = task.getTaskBudget();
		if(budget <= 0){
			errors.add("Task cannot have negative budget!");
		}
		float perMsg = serviceProvider.getCstPrTxtMsg();
		Object[] countData = regionService.getTrueHntrMsgRcvrCntFrTaskRgns(task.getTaskId());
		int regionCount = (Integer)countData[0];
		int groupCount = getTotalTaskGroupsReceivers(task.getTaskId());
		int totalCount = regionCount + groupCount;
		
		logger.debug("Total receivers for the task : " + totalCount);
		logger.debug("Cost per message : " + perMsg);
		
		int totalCost = 0;
		for(int i=0; i<totalCount;i++){
			totalCost += perMsg;
		}
		logger.debug("Total cost : " + totalCost);
		if(budget < totalCost){
			//Task budget insufficient for # receivers configured.
			errors.add("Task budget insufficient for " + totalCount + " receivers configured!");
		}
		
		return errors;
	}

	@Override
	public List<Object> validateMessageDelete(Long msgId) {
		List<Object> errors = new ArrayList<>();
		String query = hunterJDBCExecutor.getQueryForSqlId("getValidateDeleteTaskMessage");
		Map<Integer, List<Object>> errorsListMap = hunterJDBCExecutor.executeQueryRowList(query, hunterJDBCExecutor.getValuesList(new Object[]{msgId}));
		errors = errorsListMap.get(1);
		return errors;
	}

	@Override
	public int getTaskGroupTotalNumber(Long taskId) {
		String query = hunterJDBCExecutor.getQueryForSqlId("getTotalTaskGroupsForTaskId");
		logger.debug("Executing query : " + query); 
		List<Object> values = hunterJDBCExecutor.getValuesList(new Object[]{taskId});
		Map<Integer, List<Object>>  rowMapLists = hunterJDBCExecutor.executeQueryRowList(query, values);
		List<Object> result = rowMapLists != null && !rowMapLists.isEmpty() ? rowMapLists.get(1) : new ArrayList<>();
		int totalGroups = result.isEmpty() ? 0 : Integer.parseInt(result.get(0)+""); 
		logger.debug("Total groups obtained for task : " + totalGroups); 
		return totalGroups;
	}

	@Override
	public TaskHistory getNewTaskHistoryForEventName(Long taskId,String evenName,String eventUser) {
		TaskHistory taskHistory = new TaskHistory();
		taskHistory.setEvenName(evenName);
		taskHistory.setEventDate(new Date());
		taskHistory.setEventUser(eventUser);
		taskHistory.setTaskId(taskId); 
		return taskHistory;
	}

	@Override
	public void setTaskHistoryStatusAndMessage(TaskHistory taskHistory,String eventStatus, String message) {
		taskHistory.setEventStatus(eventStatus);
		taskHistory.setEventMessage(message);
	}

	@Override
	public String deleteTask(Long taskId) {
		return null;
	}

	@Override
	public TextMessage getDefaultTextMessage(Task task, AuditInfo auditInfo) {
		
		TextMessage textMessage = new TextMessage();
		textMessage.setCreatedBy(auditInfo.getCreatedBy());
		textMessage.setCretDate(new Date());
		textMessage.setLastUpdate(new Date());
		textMessage.setMsgDeliveryStatus(HunterConstants.STATUS_CONCEPTUAL);
		textMessage.setMsgId(task.getTaskId());
		textMessage.setMsgLifeStatus(HunterConstants.STATUS_DRAFT);
		textMessage.setMsgTaskType(task.getTaskType());
		
		textMessage.setPageable(false);
		textMessage.setActualReceivers(0);
		textMessage.setConfirmedReceivers(0); 
		textMessage.setDesiredReceivers(0); 
		textMessage.setMsgOwner(null); 
		textMessage.setMsgSendDate(new Date());
		textMessage.setPageWordCount(0); 
		textMessage.setProvider(null); 
		textMessage.setToPhone(null); 
		
		return textMessage;
	}


	

	


}
