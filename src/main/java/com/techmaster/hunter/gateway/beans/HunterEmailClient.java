package com.techmaster.hunter.gateway.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.techmaster.hunter.cache.HunterCacheUtil;
import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.constants.UIMessageConstants;
import com.techmaster.hunter.dao.types.GateWayMessageDao;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.json.ReceiverGroupJson;
import com.techmaster.hunter.obj.beans.EmailMessage;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskMessageReceiver;
import com.techmaster.hunter.util.HunterUtility;

public class HunterEmailClient extends AbstractHunterEmailClient{
	
	private static HunterEmailClient instance;
	private static TaskDao taskDao;
	private static Logger logger = Logger.getLogger(HunterEmailClient.class);
	private static HunterJDBCExecutor hunterJDBCExecutor;
	private static GateWayMessageDao gateWayMessageDao;
	
	private HunterEmailClient(){};
	
	static{
		if(instance == null){
			synchronized (HunterEmailClient.class) {
				instance = new HunterEmailClient();
			}
		}
	}
	
	// this method is called by spring for static injection!
	public static void wireHunterEmailManager(TaskDao taskDao,HunterJDBCExecutor hunterJDBCExecutor, GateWayMessageDao gateWayMessageDao){
		logger.debug("Loading static beans for hunter email client!");
		HunterEmailClient.taskDao = taskDao;
		HunterEmailClient.hunterJDBCExecutor = hunterJDBCExecutor;
		HunterEmailClient.gateWayMessageDao = gateWayMessageDao;
		logger.debug("Successfully loaded static beans for hunter email client!"); 
	}
	
	public static HunterEmailClient getInstance(){
		return instance;
	}

	@Override
	public Map<String, Object> execute(Map<String, Object> params) {
		
		Map<String, Object> results = new HashMap<String, Object>();
		List<String> taskProcessErrors = new ArrayList<>();
		String taskProcessStatus = null;
		
		logger.debug("Starting to process email task.");
		Task task = (Task)params.get(TASK_BEAN);
		EmailMessage emailMessage = null;
		
		if(task.getTaskMessage() instanceof EmailMessage){
			emailMessage = (EmailMessage)task.getTaskMessage();
		}else{
			String message = HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_MSG_004); 
			logger.debug(message); 
			taskProcessErrors.add(message);
			addErrorsAndStatusToResultMap(results, taskProcessErrors, taskProcessStatus); 
			return results;
		}
		
		Set<ReceiverGroupJson> taskGroups = task.getTaskGroups();
		StringBuilder quotedStringBuilder = new StringBuilder();
		List<GateWayMessage> gateWayMessages = new ArrayList<>(); 
		String[] receivers = null;
		
		if(!taskGroups.isEmpty()){
			for(ReceiverGroupJson groupJson : taskGroups){
				if(groupJson.getReceiverType().equalsIgnoreCase(HunterConstants.MESSAGE_TYPE_EMAIL)){
					quotedStringBuilder.append(groupJson.getGroupId()).append(",");
				}
			}
			String quoted = quotedStringBuilder.toString();
			if(quoted.endsWith(",")){
				quoted = quoted.substring(0, quoted.length() - 1);
			}
			String query = hunterJDBCExecutor.getQueryForSqlId("getGroupReceiverContactForReceiverIds");
			query = query.replaceAll(":GRP_ID", quoted);
			Map<Integer, List<Object>> rowMapList = hunterJDBCExecutor.executeQueryRowList(query,null);
			Map<Long, String> rcvrCntctMap = new HashMap<>();
			
			if(rowMapList != null && !rowMapList.isEmpty()){
				for(Map.Entry<Integer, List<Object>> entry : rowMapList.entrySet()){
					List<Object> rowList_ = entry.getValue();
					Long rcvrId = rowList_.get(0) == null ? 0L : HunterUtility.getLongFromObject(rowList_.get(0)); 
					String contact = rowList_.get(1)+"";
					receivers = HunterUtility.initArrayAndInsert(receivers, contact);
					rcvrCntctMap.put(rcvrId, contact);
				}
			}
			logger.debug("Obtained receiver contacts for task groups for task Id  : " + task.getTaskId() + " , " + task.getTaskObjective()); 
			
			for(Map.Entry<Long, String> entry : rcvrCntctMap.entrySet() ){
				String contct = entry.getValue();
				Long msgId = entry.getKey();
				GateWayMessage gateWayMessage = new GateWayMessage();
				gateWayMessage.setClientTagKey(task.getTaskId() + "_" + msgId); 
				gateWayMessage.setContact(contct);
				gateWayMessage.setText(HunterUtility.getStringBlob(emailMessage.getMsgText()));  
				gateWayMessage.setMsgId(emailMessage.getMsgId());
				setOtherGateWayMsgValues(gateWayMessage, task);
				gateWayMessages.add(gateWayMessage);
			}
			
			
		}else{
			logger.debug("Email task does not have groups configured"); 
		}
		
		Set<TaskMessageReceiver> taskRegionReceivers = getTskMsgRcvrsFrTskId(task.getTaskId());
		
		if(!taskRegionReceivers.isEmpty()){
			for(TaskMessageReceiver receiver : taskRegionReceivers){
				if(receiver.isActive() && !receiver.isBlocked() && receiver.getReceiverType().equals(HunterConstants.MESSAGE_TYPE_EMAIL)){
					receivers = HunterUtility.initArrayAndInsert(receivers, receiver.getReceiverContact());
					GateWayMessage gateWayMessage = new GateWayMessage();
					gateWayMessage.setClientTagKey(task.getTaskId() + "_" + emailMessage.getMsgId()); 
					gateWayMessage.setContact(receiver.getReceiverContact());
					gateWayMessage.setText(HunterUtility.getStringBlob(emailMessage.getMsgText()));  
					gateWayMessage.setMsgId(emailMessage.getMsgId());
					setOtherGateWayMsgValues(gateWayMessage, task);
					gateWayMessages.add(gateWayMessage);
				}
			}
		}else{
			logger.debug("No region receivers for task id : " + task.getTaskId() + " found!!");  
		}
		
		
		if(!taskProcessErrors.isEmpty()){
			taskProcessStatus = HunterConstants.STATUS_FAILED;
			task.setTaskLifeStatus(HunterConstants.STATUS_FAILED);
			task.setTaskDeliveryStatus(HunterConstants.STATUS_FAILED);
			taskProcessStatus = HunterConstants.STATUS_FAILED;
			addErrorsAndStatusToResultMap(results, taskProcessErrors, taskProcessStatus);
			return results;
		}
		
		
		try {
			if(receivers != null && receivers.length > 0){
				gateWayMessageDao.insertMessages(gateWayMessages);
				Map<String,Object>  sendEmailResults = new HunterEmailClientSenderBean(HunterConstants.CONFIG_HUNTER_DEFAULT_EMAIL_CONFIG_NAME, task, receivers).sendEmail();
				if(!sendEmailResults.isEmpty()){
					@SuppressWarnings("unchecked")
					List<String> errors = (List<String>)sendEmailResults.get(TASK_PROCESS_ERRORS);
					if(errors!= null && !errors.isEmpty()){
						logger.debug("Errors from sending email : " + HunterUtility.stringifyList(errors));  
					}
					taskProcessErrors.addAll(errors);
					task.setTaskDeliveryStatus(HunterConstants.STATUS_PARTIAL);
					task.setTaskLifeStatus(HunterConstants.STATUS_PROCESSED);
					taskProcessStatus = HunterConstants.STATUS_FAILED;
					addErrorsAndStatusToResultMap(results, taskProcessErrors, taskProcessStatus);
				}else{
					task.setTaskDeliveryStatus(HunterConstants.STATUS_PROCESSED);
					task.setTaskLifeStatus(HunterConstants.STATUS_PROCESSED); 
					taskProcessStatus = HunterConstants.STATUS_SUCCESS;
					addErrorsAndStatusToResultMap(results, taskProcessErrors, taskProcessStatus);
				}
				taskDao.update(task); 
				logger.debug("Done executing hunter client email!!"); 
			}else{
				logger.debug("Cannot send email as there is no emails configured for that task!");
			}
		} catch (MessagingException e) {
			e.printStackTrace();
			taskProcessErrors.add(e.getMessage());
			results.put(GatewayClient.TASK_PROCESS_STATUS, HunterConstants.STATUS_FAILED);
			results.put(GatewayClient.TASK_PROCESS_ERRORS, taskProcessErrors);
			task.setTaskDeliveryStatus(HunterConstants.STATUS_FAILED);
			task.setTaskLifeStatus(HunterConstants.STATUS_PROCESSED); 
			taskProcessStatus = HunterConstants.STATUS_SUCCESS;
			taskDao.update(task); 
			return results;
		}
		
		taskDao.update(task);
		return results;
	}
	
	private void addErrorsAndStatusToResultMap(Map<String, Object> results,List<String> taskProcessErrors, String taskProcessStatus){
		results.put(GatewayClient.TASK_PROCESS_ERRORS, taskProcessErrors);
		results.put(GatewayClient.TASK_PROCESS_STATUS, taskProcessStatus);
	}
	
	private void setOtherGateWayMsgValues(GateWayMessage gateWayMessage, Task task){
		
		gateWayMessage.setCreatedBy(task.getUpdatedBy());
		gateWayMessage.setCreatedOn(new Date());
		gateWayMessage.setgClient(HunterConstants.CLIENT_HUNTER_EMAIL);
		gateWayMessage.setMessageType(HunterConstants.MESSAGE_TYPE_EMAIL);
		gateWayMessage.setSendDate(new Date()); 
		gateWayMessage.setStatus(HunterConstants.STATUS_DRAFT);
		gateWayMessage.setTaskId(task.getTaskId());
		
		gateWayMessage.setDuration(null);
		gateWayMessage.setErrorText(null);
		gateWayMessage.setClntRspCode(null);
		gateWayMessage.setClntRspText(null);
		gateWayMessage.setRequestBody(null);
		gateWayMessage.setSubsRspnsCode(null);
		gateWayMessage.setSubsRspnsText(null);
	}
	
	@Override
	public Map<String, Object> readConfigurations() {
		return null;
	}


	
}
