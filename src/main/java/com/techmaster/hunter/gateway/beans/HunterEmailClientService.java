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
import com.techmaster.hunter.constants.TaskProcessConstants;
import com.techmaster.hunter.constants.UIMessageConstants;
import com.techmaster.hunter.dao.impl.HunterDaoFactory;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.EmailMessage;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskProcessJob;
import com.techmaster.hunter.task.process.TaskProcessJobHandler;
import com.techmaster.hunter.task.process.TaskProcessor;
import com.techmaster.hunter.util.HunterUtility;

public class HunterEmailClientService extends AbstractGateWayClientService{
	
	public Logger logger = Logger.getLogger(getClass());
	private TaskDao taskDao = HunterDaoFactory.getInstance().getDaoObject(TaskDao.class);
	private boolean submitted = false;

	@Override
	public Map<String, Object> execute(Map<String, Object> params) {
		
		logger.debug("Starting to execute hunter email client service..."); 
		Map<String, Object> results = new HashMap<String, Object>();
		List<String> taskProcessErrors = new ArrayList<>();
		String taskProcessStatus = null;
		
		Task task = (Task)params.get(TASK_BEAN);
		AuditInfo auditInfo = (AuditInfo)params.get(GateWayClientService.PARAM_AUDIT_INFO);
		String currStatus = task.getTaskDeliveryStatus();
		
		
		if(!( task.getTaskMessage() instanceof EmailMessage )){
			String message = HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_MSG_004); 
			logger.debug(message); 
			taskProcessErrors.add(message);
			addErrorsAndStatusToResultMap(results, taskProcessErrors, taskProcessStatus); 
			GateWayClientHelper.getInstance().lockTask(task.getTaskId(), currStatus); 
			return results;
		}
		
		Set<GateWayMessage> allMsgs = createGtwayMsgsFrAllTskGtwyMssgs(task, auditInfo);
		String[] receivers = null;
		
		if(allMsgs != null && !allMsgs.isEmpty()){
			for(GateWayMessage message : allMsgs){
				receivers = HunterUtility.initArrayAndInsert(receivers, message.getContact());
			}
		}else{
			String message = "No receivers found for task : " + task.getTaskName();
			logger.debug(message); 
			taskProcessErrors.add(message);
			taskProcessStatus = HunterConstants.STATUS_FAILED;
			addErrorsAndStatusToResultMap(results, taskProcessErrors, taskProcessStatus); 
			GateWayClientHelper.getInstance().lockTask(task.getTaskId(), currStatus); 
			return results;
		}
		
		logger.debug("Preparing task process job bean..."); 
		String processJobKey = TaskProcessor.getInstance().getNextProcessJobKey(task.getTaskId());
		Map<String,String> contextParams = getDefaultProcessJobContextParams(allMsgs.size(), HunterConstants.CLIENT_HUNTER_EMAIL, 1);
		String messageContacts = GateWayClientHelper.getInstance().getMessageContacts(allMsgs);
		Map<String,String> workerParams = getDefaultProcessWorkerParams(task, messageContacts, allMsgs.size());
		TaskProcessJob processJob = TaskProcessJobHandler.getInstance().createNewTaskProcessJob(task.getTaskId(), processJobKey, contextParams, auditInfo);
		
		Long startPoint = System.currentTimeMillis();
		Long endPoint = 0L;
		Map<String,Object>  sendEmailResults = new HashMap<>();
		
		try {
			if(receivers != null && receivers.length > 0){
				
				sendEmailResults = new HunterEmailClientSenderBean(HunterConstants.CONFIG_HUNTER_DEFAULT_EMAIL_CONFIG_NAME, task, receivers).sendEmail();
				logger.debug("Send email results : " + HunterUtility.stringifyMap(sendEmailResults));   
				
				submitted = true;
				endPoint = System.currentTimeMillis();
				String endDate = HunterUtility.formatDate(new Date(), HunterConstants.DATE_FORMAT_STRING);
				contextParams.put(TaskProcessConstants.END_DATE, endDate);
						
				if(!sendEmailResults.isEmpty()){
					@SuppressWarnings("unchecked")
					List<String> errors = (List<String>)sendEmailResults.get(TASK_PROCESS_ERRORS);
					if(errors!= null && !errors.isEmpty()){
						logger.debug("Errors from sending email : " + HunterUtility.stringifyList(errors));  
					}
					if(errors != null && !errors.isEmpty())
						taskProcessErrors.addAll(errors);
					task.setTaskDeliveryStatus(HunterConstants.STATUS_PARTIAL);
					task.setTaskLifeStatus(HunterConstants.STATUS_PROCESSED);
					taskProcessStatus = HunterConstants.STATUS_FAILED;
					addErrorsAndStatusToResultMap(results, taskProcessErrors, taskProcessStatus);
					
					contextParams.put(TaskProcessConstants.GEN_STATUS, sendEmailResults.get(TaskProcessConstants.WORKER_STATUS) + "");
					
				}else{
					task.setTaskDeliveryStatus(HunterConstants.STATUS_PROCESSED);
					task.setTaskLifeStatus(HunterConstants.STATUS_PROCESSED); 
					taskProcessStatus = HunterConstants.STATUS_SUCCESS;
					addErrorsAndStatusToResultMap(results, taskProcessErrors, taskProcessStatus);
				}
				GateWayClientHelper.getInstance().lockTask(task.getTaskId(), HunterConstants.STATUS_PROCESSED); 
				taskDao.update(task); 
				logger.debug("Done executing hunter client email!!"); 
			}else{
				logger.debug("Cannot send email as there is no emails configured for that task!");
			}
			return results;
		} catch (MessagingException e) {
			e.printStackTrace();
			taskProcessErrors.add(e.getMessage());
			results.put(GatewayClient.TASK_PROCESS_STATUS, HunterConstants.STATUS_FAILED);
			results.put(GatewayClient.TASK_PROCESS_ERRORS, taskProcessErrors);
			task.setTaskDeliveryStatus(HunterConstants.STATUS_FAILED);
			task.setTaskLifeStatus(HunterConstants.STATUS_PROCESSED); 
			taskProcessStatus = HunterConstants.STATUS_SUCCESS;
			GateWayClientHelper.getInstance().lockTask(task.getTaskId(), HunterConstants.STATUS_FAILED); 
			taskDao.update(task);
			return results;
		}finally{
			if(endPoint == 0) endPoint = System.currentTimeMillis();
			Long duration = endPoint - startPoint;
			workerParams.put(TaskProcessConstants.DURATION, Long.toString(duration)); 
			replaceParamsInWorkerMap(workerParams,sendEmailResults);
			contextParams.put(TaskProcessConstants.GEN_DURATION, Long.toString(duration)); 
			workerParams.put(TaskProcessConstants.END_POINT, Long.toString(endPoint)); 
			String workerName = HunterConstants.MESSAGE_TYPE_EMAIL + "_" + task.getTaskName() + "_" + task.getTaskId();
			TaskProcessJobHandler.getInstance().addTaskProcessWorker(workerName, processJobKey, workerParams);
			TaskProcessJobHandler.getInstance().saveOrUpdateProcessJob(processJob);
			String taskStatus = sendEmailResults.get(TaskProcessConstants.WORKER_STATUS) + "";
			if(HunterConstants.STATUS_SUCCESS.equals(taskStatus)){ 
				task.setTaskDeliveryStatus(HunterConstants.STATUS_PROCESSED);
			}else{
				task.setTaskDeliveryStatus(HunterConstants.STATUS_FAILED);
			}
			task.setTaskLifeStatus(HunterConstants.STATUS_PROCESSED);
			taskDao.update(task);
		}
	}
	
	public void replaceParamsInWorkerMap( Map<String,String> workerParams,Map<String,Object>  sendEmailResults ){
		if(sendEmailResults != null && !sendEmailResults.isEmpty()){
			logger.debug("Copying email results to worker params..."); 
			for(Map.Entry<String, Object> entry2 : sendEmailResults.entrySet()){
				String cntxtKey = entry2.getKey();
				Object rsltVal = entry2.getValue();
				for(Map.Entry<String, String> entry : workerParams.entrySet()){
					String rsltKey = entry.getKey();
					if(cntxtKey.equals(rsltKey)){
						workerParams.put(cntxtKey, rsltVal != null ? rsltVal.toString() : "");
					}
				}
			}
		}else{
			workerParams.put( TaskProcessConstants.ERROR_TYPE,TaskProcessConstants.ERROR_TYPE_EXCEPTION );
			workerParams.put( TaskProcessConstants.ERROR_TEXT,submitted ? "Submitted but unknow exception occurred." : "No submitted. Error occurred" );
			workerParams.put( TaskProcessConstants.WORKER_STATUS,HunterConstants.STATUS_FAILED );
		}
		logger.debug("worker params after copying : " + HunterUtility.stringifyMap(workerParams));
	}
	
	public Map<String,String> getDefaultProcessJobContextParams(int messageSize, String clientName, int numberOfWorkers){
		Map<String, String> jobContextParams = new HashMap<>();
		String date = HunterUtility.formatDate(new Date(), HunterConstants.DATE_FORMAT_STRING);
		jobContextParams.put(TaskProcessConstants.GEN_STATUS, HunterConstants.STATUS_SUCCESS);
		jobContextParams.put(TaskProcessConstants.TOTAL_MSGS, messageSize+"");
		jobContextParams.put(TaskProcessConstants.CLIENT_NAME, clientName);
		jobContextParams.put(TaskProcessConstants.GEN_DURATION, Integer.toString(0)); 
		jobContextParams.put(TaskProcessConstants.START_DATE, date); 
		jobContextParams.put(TaskProcessConstants.END_DATE, date);
		jobContextParams.put(TaskProcessConstants.NUMBER_OF_WORKERS, Integer.toString(numberOfWorkers));
		logger.debug("Retrieved default context params : " + HunterUtility.stringifyMap(jobContextParams));  
		return jobContextParams;
	}
	
	public Map<String,String> getDefaultProcessWorkerParams(Task task, String messagesIds, int messageSize){
		
		Map<String, String> values = new HashMap<>();
		
		values.put(TaskProcessConstants.WORKER_NAME, "email_task_worker_" + task.getTaskName()+ "_" + task.getTaskId()); 
		values.put(TaskProcessConstants.MESSAGE_IDS, messagesIds);
		values.put(TaskProcessConstants.START_POINT, Long.toString(System.currentTimeMillis()));
		values.put(TaskProcessConstants.WORKER_MSG_COUNT, Integer.toString(messageSize));

		values.put(TaskProcessConstants.ERROR_TYPE, "N/A");
		values.put(TaskProcessConstants.ERROR_TEXT, "N/A");
		values.put(TaskProcessConstants.END_POINT, "");
		values.put(TaskProcessConstants.RESPONSE_CODE, "Hunter Email Response");
		values.put(TaskProcessConstants.DURATION, "");
		values.put(TaskProcessConstants.RESPONSE_TEXT, "N/A");
		values.put(TaskProcessConstants.CONN_STATUS, HunterConstants.STATUS_FAILED);
		values.put(TaskProcessConstants.WORKER_STATUS, null);
		
		logger.debug("Retrieved default worker params : " + HunterUtility.stringifyMap(values));  
		return values;
	}
	
	

}
