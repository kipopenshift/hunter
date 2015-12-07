package com.techmaster.hunter.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.email.HunterEmailManager;
import com.techmaster.hunter.gateway.beans.GatewayClient;
import com.techmaster.hunter.obj.beans.HunterJacksonMapper;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TextMessage;
import com.techmaster.hunter.obj.converters.TaskConverter;
import com.techmaster.hunter.task.TaskManager;
import com.techmaster.hunter.util.HunterLogFactory;
import com.techmaster.hunter.util.HunterUtility;

@Controller
@RequestMapping(value="/task")
public class TaskController {
	
	@Autowired private TaskDao taskDao;
	@Autowired private HunterJacksonMapper hunterJacksonMapper;
	@Autowired private TaskManager taskManager;
	@Autowired private HunterJDBCExecutor hunterJDBCExecutor;
	@Autowired private HunterEmailManager hunterEmailManager;
	
	private static final Logger logger = HunterLogFactory.getLog(TaskController.class);
	
	@RequestMapping(value="/action/read/getTasksForClientId/{clientId}")
	@Produces("application/json")
	@Consumes("application/json")
	@ResponseBody public List<Task> getTaskForClientId(@PathVariable Long clientId){
		List<Task> tasks = taskDao.getTaskForClientId(clientId);
		HunterLogFactory.getLog(getClass()).debug("Returning Tasks for client >> " + tasks); 
		return tasks;
	}
	
	@RequestMapping(value="/action/create/createTaskForClientId", method=RequestMethod.POST)
	@Produces("application/json")
	@Consumes("application/json")
	@ResponseBody public Task createTaskForCilentId(HttpServletRequest request){
		
		String bodyString = null;
		Task task = new Task();
		
		try {
			
			bodyString = HunterUtility.getRequestBodyAsString(request);
			task = new TaskConverter(bodyString, TextMessage.class).convertBasic();
			task.setCretDate(new Date());
			task.setLastUpdate(new Date());
			task.setCreatedBy("hlangat01"); 
			task.setUpdatedBy("hlangat01"); 
			task.setTaskDateline(new Date());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		String parameters = HunterUtility.getParamNamesAsStringsFrmRqst(request);
		HunterLogFactory.getLog(getClass()).debug("Request parameters >> " + parameters);
		taskDao.insertTask(task);
		HunterLogFactory.getLog(getClass()).debug("Returning new task >> " + task); 
		return task;
	}
	
	@RequestMapping(value="action/update/updateTaskForClientId", method=RequestMethod.POST)
	@Produces("application/json")
	@Consumes("application/json")
	@ResponseBody public Task updateTaskForClient(HttpServletRequest request){
		String requestBody = null;
		try {
			requestBody = HunterUtility.getRequestBodyAsString(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Task task = new TaskConverter(requestBody, TextMessage.class).convert();
		taskDao.update(task); 
		HunterLogFactory.getLog(getClass()).debug("Returning updated task >> " + task); 
		return task; 
	}
	
	@RequestMapping(value="/action/destroy/destroyTaskForClientId", method=RequestMethod.POST)
	@Produces("application/json")
	@Consumes("application/json")
	@ResponseBody public Task destroyTaskForClient(HttpServletRequest request){
		String requestBody = null;
		try {
			requestBody = HunterUtility.getRequestBodyAsString(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Task task = new TaskConverter(requestBody, TextMessage.class).convert();
		taskDao.deleteTask(task); 
		return task;
	}
	
	@RequestMapping(value="/action/destroy/processTask", method=RequestMethod.POST)
	@Produces("application/json")
	@Consumes("application/json")
	@ResponseBody public String processTask(HttpServletRequest request){
		
		JSONObject errorsJs = new JSONObject();
		String requestBody = null;
		
		
		try {
			requestBody = HunterUtility.getRequestBodyAsString(request);
			HunterUtility.selectivelyCopyJSONObject(new JSONObject(requestBody), new String[]{"taskMessage", "taskRegion", "taskReceivers"});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Task task = new TaskConverter(requestBody, TextMessage.class).convert();
		Map<String, Object> result = taskManager.processTask(task); 
		
		@SuppressWarnings("unchecked") List<String> validationErrors = (List<String>)result.get(GatewayClient.TASK_VALIDATION_ERRORS); 
		if(validationErrors != null && !validationErrors.isEmpty()){
			StringBuilder errBuilder = new StringBuilder();
			for(String error : validationErrors){
				errBuilder.append(error).append(","); 
			}
			String errors = errBuilder.toString();
			errors = errors.substring(0, errors.length() - 1);
			logger.warn("Task( "+ task.getTaskId() +" ) processing returned errors >> " + errors);
			errorsJs.put("errors", errors);
			errorsJs.put("status", HunterConstants.STATUS_FAILED);
			logger.debug("returned js >> " + errorsJs); 
		}
		
		return errorsJs.toString();
	}

}
