package com.techmaster.hunter.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.MessageDao;
import com.techmaster.hunter.dao.types.ServiceProviderDao;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.dao.types.TaskHistoryDao;
import com.techmaster.hunter.email.HunterEmailTemplateHandler;
import com.techmaster.hunter.enums.TaskHistoryEventEnum;
import com.techmaster.hunter.exception.HunterRunTimeException;
import com.techmaster.hunter.obj.beans.EmailMessage;
import com.techmaster.hunter.obj.beans.HunterJacksonMapper;
import com.techmaster.hunter.obj.beans.Message;
import com.techmaster.hunter.obj.beans.ServiceProvider;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskHistory;
import com.techmaster.hunter.obj.beans.TextMessage;
import com.techmaster.hunter.task.TaskManager;
import com.techmaster.hunter.util.HunterUtility;

@Controller
@RequestMapping(value="/message")
public class MessageConstroller extends HunterBaseController{
	
	@Autowired private ServiceProviderDao serviceProviderDao;
	@Autowired private HunterJDBCExecutor hunterJDBCExecutor;
	@Autowired private HunterJacksonMapper hunterJacksonMapper;
	@Autowired private TaskManager taskManager;
	@Autowired private TaskDao taskDao;
	@Autowired private MessageDao messageDao;
	@Autowired private TaskHistoryDao taskHistoryDao;
	
	private Logger logger = Logger.getLogger(MessageConstroller.class);
	
	@RequestMapping(value="/action/mainPage", method = RequestMethod.GET )
	public String mainPage(HttpServletRequest request, HttpServletResponse response){
		return "views/messageMainPage";
	}
	
	@RequestMapping(value="/action/read/all", method = RequestMethod.POST )
	public String readAll(HttpServletRequest request, HttpServletResponse response){
		return "views/messageMainPage";
	}
	
	@RequestMapping(value="/action/providers/read", method = RequestMethod.POST )
	public List<ServiceProvider> readAllProviders(HttpServletRequest request, HttpServletResponse response){
		List<ServiceProvider> providers = serviceProviderDao.getAllServiceProviders();
		return providers;
	}
	
	@RequestMapping(value="/action/tskMsg/create/{taskId}", method = RequestMethod.POST )
	@Produces("application/json")
	@Consumes("application/json") 
	public @ResponseBody Message createTaskMessage(@PathVariable("taskId") Long taskId, HttpServletRequest request){
		
		logger.debug("Creating text message from json...");
		String requestBody = null;
		
		try {
			requestBody = HunterUtility.getRequestBodyAsString(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String tskMsgType = taskDao.getTaskMsgType(taskId);
		logger.debug("Message type for task : " + tskMsgType); 
		
		Message message = null;
		
		if(tskMsgType != null && tskMsgType.equals(HunterConstants.MESSAGE_TYPE_TEXT)){  
			message = taskManager.convertTextMessage(requestBody);
			message.setMsgTaskType(tskMsgType);
		}else {
			throw new HunterRunTimeException("No data found for taskId : " + taskId + " and message \n " + message);
		}
		
		logger.debug("Task Message obtained >> " + message);
		Task task = taskDao.getTaskById(taskId);
		
		if(task.getTaskMessage() != null){
			message.setMsgId(taskId); 
			messageDao.updateMessage(message); 
		}else{
			message.setMsgId(taskId); 
			task.setTaskMessage(message);
			taskDao.update(task);
		}
		
		return message;
	}
	
	@RequestMapping(value="/action/tskMsg/email/deleteEmail/{taskId}", method = RequestMethod.POST )
	@Produces("application/json") 
	public @ResponseBody String deleteTaskEmailMsg(HttpServletRequest request, HttpServletResponse response, @PathVariable("taskId") Long taskId){
		
		JSONObject results = new JSONObject();
		List<Object>  errors = taskManager.validateMessageDelete(taskId);
		
		String userName = getUserName();
		TaskHistory taskHistory = taskManager.getNewTaskHistoryForEventName(taskId, TaskHistoryEventEnum.DELETE_MESSAGE.getEventName(), userName);
		
		if(errors != null && !errors.isEmpty()){
			String	lifeStsMsg =  errors.get(0) + "";
			String	delStsMsg =  errors.get(1) + "";
			String message = "";
			if(!lifeStsMsg.equals("Delete")){
				message = lifeStsMsg;
			}
			if(!delStsMsg.equals("Delete")){
				message = message == null ? "" : message;
				message += "<br/>" + delStsMsg;
			}
			if((!delStsMsg.equals("Delete") || !lifeStsMsg.equals("Delete"))){  
				logger.debug("Cannot delete message because : " + message);
				taskManager.setTaskHistoryStatusAndMessage(taskHistory, HunterConstants.STATUS_FAILED, "Cannot delete message because : " + message); 
				taskHistoryDao.insertTaskHistory(taskHistory); 
				return HunterUtility.setJSONObjectForFailure(results, message ).toString();
			}else{
				Task task = taskDao.getTaskById(taskId);
				Message msg = task.getTaskMessage();
				messageDao.deleteMessage(msg);
				task.setTaskMessage(null); 
				taskDao.update(task); 
				logger.debug("Successfully deleted message for task id : " + taskId); 
				taskManager.setTaskHistoryStatusAndMessage(taskHistory, HunterConstants.STATUS_FAILED, "Successfully deleted message for task id : " + taskId); 
				taskHistoryDao.insertTaskHistory(taskHistory); 
			}
		}
		return HunterUtility.setJSONObjectForSuccess(results, "Successfully deleted message" ).toString();
	}
	
	@RequestMapping(value="/action/tskMsg/email/createOrUpdate", method = RequestMethod.POST )
	public @ResponseBody String createOrUpdateEmailMsg(HttpServletRequest request, HttpServletResponse response){
		
		JSONObject results = new JSONObject();
		JSONObject input =  null;
		
		logger.debug("Creating text message from json...");
		String requestBody = null;
		Long taskId = null;
		String emailTemplateName = null;
		boolean hasAttachment = false;
		String tskEmailMsgStatus = null;
		String taskEmailMsgSendDateStr = null;
		Date msgSendDate = null;
		String taskEmailHtml = null;
		String subject = null;
		
		try {
			requestBody = HunterUtility.getRequestBodyAsString(request);
			input = new JSONObject(requestBody); 
			String taskIdStr = HunterUtility.getStringOrNullFromJSONObj(input, "tskId");
			taskId = HunterUtility.getLongFromObject(taskIdStr);
			emailTemplateName = HunterUtility.getStringOrNullFromJSONObj(input, "emailTemplateName");
			hasAttachment = HunterUtility.getStringOrNullFromJSONObj(input, "hasAttachment").equals("true") ? true : false;
			tskEmailMsgStatus = HunterUtility.getStringOrNullFromJSONObj(input, "tskEmailMsgStatus");
			taskEmailMsgSendDateStr = HunterUtility.getStringOrNullFromJSONObj(input, "taskEmailMsgSendDate");
			taskEmailMsgSendDateStr = taskEmailMsgSendDateStr.replaceAll("T", " ");
			taskEmailMsgSendDateStr = taskEmailMsgSendDateStr.replaceAll("Z", "");
			msgSendDate = HunterUtility.parseDate(taskEmailMsgSendDateStr+":00", HunterConstants.DATE_FORMAT_STRING);
			taskEmailHtml = HunterUtility.getStringOrNullFromJSONObj(input, "taskEmailHtml");
			subject = HunterUtility.getStringOrNullFromJSONObj(input, "emailSubject");
		} catch (IOException e) {
			e.printStackTrace();
			return HunterUtility.setJSONObjectForFailure(results, e.getMessage()).toString();
		}
		
		Task task = taskDao.getTaskById(taskId);
		EmailMessage message = (EmailMessage)task.getTaskMessage();
		String userName = getUserName();
		TaskHistory taskHistory = taskManager.getNewTaskHistoryForEventName(taskId, TaskHistoryEventEnum.ADD_MESSAGE.getEventName(), userName);
		
		if(message == null){
			logger.debug("No email message configured for task. Creating email message...");  
			message = new EmailMessage();
			message.setMsgId(task.getTaskId());
			message.setActualReceivers(0); 
			message.setConfirmedReceivers(0);
			message.setCreatedBy(userName);
			message.setCretDate(new Date()); 
			message.setDesiredReceivers(0); 
			message.seteSubject(subject);
			message.setLastUpdate(new Date());
			message.setLastUpdatedBy(userName);
			message.setMsgDeliveryStatus(HunterConstants.STATUS_CONCEPTUAL); 
			message.setMsgLifeStatus(HunterConstants.STATUS_DRAFT);
			message.setMsgOwner(taskDao.getUserNameForTaskOwnerId(taskId)); 
			message.setMsgSendDate(msgSendDate);
			message.setMsgTaskType(HunterConstants.MESSAGE_TYPE_EMAIL); 
			message.setMsgText(taskEmailHtml);
			ServiceProvider serviceProvider = serviceProviderDao.getServiceProviderByName("Hunter Email Provider");
			message.setProvider(serviceProvider);
			message.setEmailTemplateName(emailTemplateName);
			message.setHasAttachment(hasAttachment);
			messageDao.insertMessage(message); 
			logger.debug("Finished creating email message... : \n " + message);
		}else{
			message.setLastUpdate(new Date());
			message.setLastUpdatedBy(userName);
			message.setMsgLifeStatus(HunterConstants.STATUS_DRAFT);
			message.setMsgSendDate(msgSendDate);
			message.setMsgText(taskEmailHtml);
			message.setMsgLifeStatus(tskEmailMsgStatus); 
			message.setEmailTemplateName(emailTemplateName);
			message.setHasAttachment(hasAttachment);
			message.seteSubject(subject); 
		}
		task.setTaskMessage(message);
		taskDao.update(task);
		taskManager.setTaskHistoryStatusAndMessage(taskHistory, HunterConstants.STATUS_FAILED, "Successfully added message to task with task id : " + taskId); 
		taskHistoryDao.insertTaskHistory(taskHistory);
		logger.debug("Finished updating email message... : \n " + message);
		
		return HunterUtility.setJSONObjectForSuccess(results, "Successfully saved message" ).toString();
	}
	
	@RequestMapping(value="/action/tskMsg/email/getEmailTemplateForName/{templateName}", method = RequestMethod.GET )
	@Produces("application/html")
	public @ResponseBody String getEmailTemplateForName(HttpServletRequest request, HttpServletResponse response, @PathVariable("templateName") String templateName){
		new URLDecoder();
		String templateNameDecoded;
		String html = null;
		try {
			templateNameDecoded = URLDecoder.decode(templateName, "UTF-8");
			html = HunterEmailTemplateHandler.getInstance().getTemplateForName(templateNameDecoded); 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "<span style='color:red;font-weight:bolder;font-size:13px;'> Error occurred!! <br/> ( "+ e.getMessage() +" ) </span>";
		}
		return html;
	}
	
	@RequestMapping(value="/action/tskMsg/email/getAllTemplateNames", method = RequestMethod.POST )
	@Produces("application/json")
	public @ResponseBody List<String> getAllExistingTemplateNames(HttpServletRequest request, HttpServletResponse response){
		List<String> existingNames = HunterEmailTemplateHandler.getInstance().getAllExistingTemplatesNames();
		return existingNames;
	}
	
	@RequestMapping(value="/action/email/getRefreshValues/{msgId}", method = RequestMethod.POST )
	@Produces("application/json") 
	public @ResponseBody Map<String,Object> getEmailMsgRefreshVals(HttpServletRequest request, HttpServletResponse response, @PathVariable("msgId") Long msgId){
		Map<String,Object> data = messageDao.getEmailMsgRefreshData(msgId);
		return data;
	}
	
	@RequestMapping(value="/action/tskMsg/getPrcssTxtMssgeDtls/{msgId}", method = RequestMethod.POST )
	@Consumes("application/json") 
	@Produces("application/json") 
	public @ResponseBody Map<String,Object> getPrcssTxtMssgeDtls(HttpServletRequest request, HttpServletResponse response, @PathVariable("msgId") Long msgId){
		Map<String,Object> data = messageDao.getPrcssTxtMssgeDtls(msgId);
		HunterUtility.threadSleepFor(500);
		return data;
	}
	
	@RequestMapping(value="/action/tskMsg/updateTxtMsg", method = RequestMethod.POST )
	@Consumes("application/json") 
	@Produces("application/json") 
	@ResponseBody
	public String updateTextMessage(@RequestBody Map<String,String> params){
		
		logger.debug(HunterUtility.stringifyMap(params)); 
		HunterUtility.threadSleepFor(400); 
		JSONObject results = new JSONObject();
		
		try{
			
			String userName = getUserName();
			Long taskId = HunterUtility.getLongFromObject(params.get("taskId"));
			String owner = HunterUtility.getNullOrStrimgOfObj(params.get("msgOwner"));
			Long providerId = HunterUtility.getLongFromObject(params.get("providerId"));
			String msgSts = HunterUtility.getNullOrStrimgOfObj(params.get("msgSts"));
			String msgText = HunterUtility.getNullOrStrimgOfObj(params.get("msgText"));
			Task task = taskDao.getTaskById(taskId);
			
			if(task != null && HunterConstants.MESSAGE_TYPE_TEXT.equals(task.getTskMsgType())){
				ServiceProvider provider = serviceProviderDao.getServiceProviderById(providerId);
				TextMessage txtMsg = (TextMessage)task.getTaskMessage();
				boolean insert = false;
				if(txtMsg == null){
					txtMsg = taskManager.getDefaultTextMessage(task, getAuditInfo());
					insert = true;
				}
				txtMsg.setMsgOwner(owner);
				txtMsg.setLastUpdatedBy(userName);
				txtMsg.setLastUpdate(new Date()); 
				txtMsg.setMsgDeliveryStatus(HunterConstants.STATUS_CONCEPTUAL);
				txtMsg.setMsgLifeStatus(msgSts);
				txtMsg.setProvider(provider); 
				txtMsg.setMsgText(msgText); 
				txtMsg.setMsgTaskType(HunterConstants.MESSAGE_TYPE_TEXT);
				if(insert){
					messageDao.insertMessage(txtMsg); 
				}else{
					messageDao.updateMessage(txtMsg);
				}
				taskDao.update(task); 
				HunterUtility.setJSONObjectForSuccess(results, "Successfully updated text message");
			}else{
				HunterUtility.setJSONObjectForFailure(results, "No text task found for task id : " + taskId);
			}
			
			return results.toString();
			
		}catch(Exception e){
			e.printStackTrace(); 
			HunterUtility.setJSONObjectForFailure(results, e.getMessage());
		}
		return results.toString();
	}
	
}
