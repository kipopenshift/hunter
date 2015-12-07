package com.techmaster.hunter.obj.converters;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.obj.beans.HunterJacksonMapper;
import com.techmaster.hunter.obj.beans.TaskMessageReceiver;
import com.techmaster.hunter.obj.beans.Message;
import com.techmaster.hunter.obj.beans.ReceiverRegion;
import com.techmaster.hunter.obj.beans.ServiceProvider;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TextMessage;
import com.techmaster.hunter.util.HunterLogFactory;
import com.techmaster.hunter.util.HunterUtility;

public class TaskConverter {
	
	private String requestBody;
	private Class<? extends Message> msgClss;
	private JSONObject taskJson = null;
	private static Logger logger = HunterLogFactory.getLog(TaskConverter.class);
	
	private HunterJacksonMapper hunterJacksonMapper = new HunterJacksonMapper();
	
	public TaskConverter(String requestBody, Class<? extends Message> msgClss) { 
		super();
		this.requestBody = requestBody;
		this.msgClss = msgClss;
		this.taskJson = new JSONObject(this.requestBody); 
	}

	public Task convert(){
		
		logger.debug("Beginning task conversion process.."); 
		
		Task task = getBasicTask();
		Message message = getMessageForClass();
		ReceiverRegion region = getTaskRegion();
		Set<ReceiverRegion> taskRegions = getTaskReceiverRegions();
		task.setTaskRegions(taskRegions); 
		 Set<TaskMessageReceiver> taskReceivers = getTaskReceivers();
		
		task.setTaskMessage(message); 
		task.setTaskRegion(region); 
		task.setTaskReceivers(taskReceivers); 
		
		logger.debug("Completed task conversion process successfully..");
		logger.debug("Resultant task >> " + task);
		
		return task;
	}
	
	public Task convertBasic(){
		return getBasicTask();
	}
	
	private Task getBasicTask(){
		
		Task task = new Task();
		
		String taskType = taskJson.getString("taskType");
		String taskName = taskJson.getString("taskName");
		String taskObjective = taskJson.getString("taskObjective");
		String description = taskJson.getString("description");
		long taskBudget = taskJson.getLong("taskBudget");
		float taskBudgetF = (float)taskBudget;
		long taskCost = taskJson.getLong("taskCost");
		float taskCostF = (float)taskCost;
		long taskId = taskJson.getLong("taskId");
		Boolean recurrentTask = taskJson.getBoolean("recurrentTask");
		Long clientId = taskJson.getLong("clientId");
		String taskDeliveryStatus = taskJson.has("taskDeliveryStatus") ? taskJson.get("taskDeliveryStatus") != null ? taskJson.get("taskDeliveryStatus").toString() : null : null;
		String taskLifeStatus = taskJson.has("taskLifeStatus") ? taskJson.get("taskLifeStatus") != null ? taskJson.get("taskLifeStatus").toString() : null : null;
		String taskDateline = taskJson.has("taskDateline") ? taskJson.get("taskDateline") != null ? taskJson.get("taskDateline").toString() : null : null;
		String taskApprover = taskJson.has("taskApprover") ? taskJson.get("taskApprover") != null ? taskJson.get("taskApprover").toString() : null : null;
		String gateWayClient = taskJson.has("gateWayClient") ? taskJson.get("gateWayClient") != null ? taskJson.get("gateWayClient").toString() : null : null;
		Boolean taskApproved = taskJson.getBoolean("taskApproved");
		String tskAgrmntLoc = taskJson.has("tskAgrmntLoc") ? taskJson.get("tskAgrmntLoc") != null ? taskJson.get("tskAgrmntLoc").toString() : null : null;
		
		int desiredReceiverCount = taskJson.has("desiredReceiverCount") ? taskJson.getInt("desiredReceiverCount") : 0 ;
		int availableReceiverCount = taskJson.has("availableReceiverCount") ? taskJson.getInt("availableReceiverCount") : 0 ;
		int confirmedReceiverCount = taskJson.has("confirmedReceiverCount") ? taskJson.getInt("confirmedReceiverCount") : 0 ;
		
		String cretDate_ = taskJson.has("cretDate") ? taskJson.get("cretDate") != null ? taskJson.get("cretDate").toString() : null : null;
		Date cretDate = HunterUtility.parseDate(cretDate_, HunterConstants.HUNTER_DATE_FORMAT_MIN);
		String createdBy =  taskJson.has("createdBy") ? taskJson.get("createdBy") != null ? taskJson.get("createdBy").toString() : null : null;
		
		task.setTaskId(taskId); 
		task.setTaskType(taskType);
		task.setTaskName(taskName);
		task.setTaskObjective(taskObjective);
		task.setDescription(description);
		task.setTaskBudget(taskBudgetF);
		task.setTaskCost(taskCostF);
		task.setRecurrentTask(recurrentTask);
		task.setTaskDeliveryStatus(taskDeliveryStatus); 
		task.setTaskLifeStatus(taskLifeStatus); 
		task.setTaskDateline(HunterUtility.parseDate(taskDateline, HunterConstants.HUNTER_DATE_FORMAT_MIN));
		task.setTaskApprover(taskApprover);
		task.setTaskApproved(taskApproved);
		task.setGateWayClient(gateWayClient); 
		task.setTskAgrmntLoc(tskAgrmntLoc); 
		
		task.setDesiredReceiverCount(desiredReceiverCount);
		task.setAvailableReceiverCount(availableReceiverCount);
		task.setConfirmedReceiverCount(confirmedReceiverCount); 
		
		task.setCretDate(cretDate);
		task.setLastUpdate(new Date());
		task.setCreatedBy(createdBy); 
		task.setUpdatedBy("hlangat01"); 
		task.setClientId(clientId); 
		
		logger.debug("Successfully created basic task from request body!\n  Request Body >> " + requestBody);
		logger.debug("Basic task created >> " + task);
		
		
		return task;
	}
	
	private ServiceProvider getServiceProviderForMsgJson(JSONObject msgJson){
		
		logger.debug("Constructing provider for messageJSON >> " + msgJson); 
		
		Long providerId = msgJson.getLong("providerId");
		String providerName = msgJson.getString("providerName"); 
		float cstPrAudMsg = (float)msgJson.getInt("cstPrAudMsg");
		float cstPrTxtMsg = (float)msgJson.getInt("cstPrTxtMsg");
		
		ServiceProvider provider = new ServiceProvider();
		
		provider.setProviderId(providerId);
		provider.setProviderName(providerName);
		provider.setCstPrAudMsg(cstPrAudMsg);
		provider.setCstPrTxtMsg(cstPrTxtMsg); 
		
		logger.debug("Successfully consted provider >> " + provider); 
		
		return provider;
	}
	
	private TextMessage getTextMessage(){
		
		TextMessage message = new TextMessage();
		JSONObject msgJson = null;
		
		try {
			msgJson = taskJson.getJSONObject("taskMessage");
		} catch (JSONException e) {
			logger.error("Failed to get task message. Exception >> " + e.getMessage()); 
			return null;
		}
		
		if(msgJson == null) {
			logger.warn("Null task message!! Returning null!");
			return null;
		}
		
		logger.debug("Creating textMessage for json >> " + msgJson); 
		
		long msgId = msgJson.getLong("msgId");
		String msgDeliveryStatus = msgJson.getString("msgDeliveryStatus"); 
		String msgLifeStatus = msgJson.getString("msgLifeStatus"); 
		String msgSendDate_ = msgJson.getString("msgSendDate");
		Date msgSendDate = HunterUtility.parseDate(msgSendDate_, HunterConstants.HUNTER_DATE_FORMAT_MIN);
		String msgTaskType = msgJson.getString("msgTaskType"); 
		String msgText = msgJson.getString("msgText"); 
		int desiredReceivers = msgJson.getInt("desiredReceivers");
		int actualReceivers = msgJson.getInt("actualReceivers");
		int confirmedReceivers = msgJson.getInt("confirmedReceivers");
		String msgOwner = msgJson.getString("msgOwner");
		String text = msgJson.getString("text");
		String disclaimer = msgJson.getString("disclaimer"); 
		String fromPhone = msgJson.getString("fromPhone");
		String toPhone = msgJson.getString("toPhone");
		int pageWordCount = msgJson.getInt("pageWordCount");
		
		String cretDate_ = msgJson.getString("cretDate");
		Date cretDate = HunterUtility.parseDate(cretDate_, HunterConstants.HUNTER_DATE_FORMAT_MIN);
		String lastUpdate_ = msgJson.getString("cretDate");
		Date lastUpdate = HunterUtility.parseDate(lastUpdate_, HunterConstants.HUNTER_DATE_FORMAT_MIN);
		String createdBy = msgJson.getString("createdBy"); 
		String lastUpdatedBy = msgJson.getString("lastUpdatedBy"); 
		
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
		
		ServiceProvider provider = getServiceProviderForMsgJson(msgJson.getJSONObject("provider"));
		message.setProvider(provider); 
		
		logger.debug("Successfully created textMessage >> " + message); 
		
		
		return message;
	}
	
	private Message getMessageForClass(){
		if(this.msgClss == TextMessage.class){
			logger.debug("Returning message type >> " + msgClss.getSimpleName()); 
			return getTextMessage();
		}
		
		return null;
	}
	
	private Set<ReceiverRegion> getTaskReceiverRegions(){
		
		Set<ReceiverRegion> taskRegions = new HashSet<>();
		JSONArray jsonArray = null;
		
		try {
			jsonArray = taskJson.getJSONArray("taskRegions");
		} catch (JSONException e) {
			logger.error("Failed to get task region. Exception >> " + e.getMessage()); 
			return null;
		}
		
		if(jsonArray != null){
			for(int i=0; i<jsonArray.length(); i++){
				Object regionJson = jsonArray.get(i);
				if(regionJson != null){
					JSONObject jsonObject =  new JSONObject(regionJson.toString());
					ReceiverRegion receiverRegion = getTaskRegion(jsonObject);
					taskRegions.add(receiverRegion);
				}
			}
		}
		
		return taskRegions;
	}
	
	private ReceiverRegion getTaskRegion(JSONObject rgnJson) {
		
		if(rgnJson == null) {
			logger.warn("Null task region!! Returning null!");
			return null;
		}
		
		logger.debug("Constructing receiver region for regionJson >> " + rgnJson); 
		
		Long regionId = rgnJson.getLong("regionId"); 
		String country = rgnJson.getString("country");
		String state = rgnJson.has("state") ? rgnJson.get("state") != null ? rgnJson.get("state").toString() : null : null;
		boolean hasState = rgnJson.getBoolean("hasState"); 
		String county = rgnJson.has("county") ? rgnJson.get("county") != null ? rgnJson.get("county").toString() : null : null;
		String constituency = rgnJson.has("constituency") ? rgnJson.get("constituency") != null ? rgnJson.get("constituency").toString() : null : null;
		String city = rgnJson.has("city") ? rgnJson.get("city") != null ? rgnJson.get("city").toString() : null : null;
		String ward = rgnJson.has("ward") ? rgnJson.get("ward") != null ? rgnJson.get("ward").toString() : null : null;
		String village = rgnJson.has("village") ? rgnJson.get("village") != null ? rgnJson.get("village").toString() : null : null;
		double longitude = rgnJson.has("longitude") ? rgnJson.getLong("longitude") : 0;
		double latitude =  rgnJson.has("latitude") ?  rgnJson.getLong("latitude") : 0; 
		String currentLevel = rgnJson.has("currentLevel") ? rgnJson.get("currentLevel") != null ? rgnJson.get("currentLevel").toString() : null : null;
		String borderLongLats = rgnJson.has("borderLongLats") ? rgnJson.get("borderLongLats") != null ? rgnJson.get("borderLongLats").toString() : null : null;
		
		ReceiverRegion region = new ReceiverRegion();
		region.setRegionId(regionId);
		region.setCountry(country);
		region.setState(state);
		region.setHasState(hasState);
		region.setCounty(county);
		region.setConstituency(constituency);
		region.setCity(city);
		region.setWard(ward);
		region.setVillage(village);
		region.setLongitude(longitude);
		region.setLatitude(latitude);
		region.setCurrentLevel(currentLevel);
		region.setBorderLongLats(borderLongLats); 
		
		logger.debug("Successfully constructed receiver region >> " + region); 
		
		return region;
	}
	
	private ReceiverRegion getTaskRegion() {
		
		JSONObject rgnJson = null;
		
		try {
			rgnJson = taskJson.getJSONObject("taskRegion");
		} catch (JSONException e) {
			logger.error("Failed to get task region. Exception >> " + e.getMessage()); 
			return null;
		}
		
		if(rgnJson == null) {
			logger.warn("Null task region!! Returning null!");
			return null;
		}
		
		logger.debug("Constructing receiver region for regionJson >> " + rgnJson); 
		
		Long regionId = rgnJson.getLong("regionId"); 
		String country = rgnJson.getString("country");
		String state = rgnJson.has("state") ? rgnJson.get("state") != null ? rgnJson.get("state").toString() : null : null;
		boolean hasState = rgnJson.getBoolean("hasState"); 
		String county = rgnJson.has("county") ? rgnJson.get("county") != null ? rgnJson.get("county").toString() : null : null;
		String constituency = rgnJson.has("constituency") ? rgnJson.get("constituency") != null ? rgnJson.get("constituency").toString() : null : null;
		String city = rgnJson.has("city") ? rgnJson.get("city") != null ? rgnJson.get("city").toString() : null : null;
		String ward = rgnJson.has("ward") ? rgnJson.get("ward") != null ? rgnJson.get("ward").toString() : null : null;
		String village = rgnJson.has("village") ? rgnJson.get("village") != null ? rgnJson.get("village").toString() : null : null;
		double longitude = rgnJson.has("longitude") ? (double)rgnJson.get("longitude") : 0;
		double latitude =  rgnJson.has("latitude") ?  (double)rgnJson.get("latitude") : 0; 
		String currentLevel = rgnJson.has("currentLevel") ? rgnJson.get("currentLevel") != null ? rgnJson.get("currentLevel").toString() : null : null;
		String borderLongLats = rgnJson.has("borderLongLats") ? rgnJson.get("borderLongLats") != null ? rgnJson.get("borderLongLats").toString() : null : null;
		
		ReceiverRegion region = new ReceiverRegion();
		region.setRegionId(regionId);
		region.setCountry(country);
		region.setState(state);
		region.setHasState(hasState);
		region.setCounty(county);
		region.setConstituency(constituency);
		region.setCity(city);
		region.setWard(ward);
		region.setVillage(village);
		region.setLongitude(longitude);
		region.setLatitude(latitude);
		region.setCurrentLevel(currentLevel);
		region.setBorderLongLats(borderLongLats); 
		
		logger.debug("Successfully constructed receiver region >> " + region); 
		
		return region;
	}
	
	private Set<TaskMessageReceiver> getTaskReceivers(){
		
		Set<TaskMessageReceiver> taskReceivers = new HashSet<>();
		JSONArray receivers = null;
		
		try {
			receivers = taskJson.getJSONArray("taskReceivers");
		} catch (JSONException e1) {
			logger.error("Failed to get task receivers. Exception >> " + e1.getMessage()); 
			return null;
		}
		
		if(receivers == null) {
			logger.warn("Null receivers!! Returning empty Set!");
			return new HashSet<>();
		}
		
		logger.debug("constructing set of receivers from json array >> " + receivers); 
		
		for(int i=0; i<receivers.length(); i++){
			
			JSONObject receiverJ = receivers.getJSONObject(i);
			TaskMessageReceiver receiver = null;
			
			if(hunterJacksonMapper == null) continue;
			
			try {
				receiver = hunterJacksonMapper.readValue(receiverJ.toString(), TaskMessageReceiver.class);
			} catch (JsonParseException e) {
				logger.error("HunterJacksonMapper failed to get task receiver. Exception >> " + e.getMessage()); 
				continue;
			} catch (JsonMappingException e) {
				logger.error("HunterJacksonMapper failed  to get task receiver. Exception >> " + e.getMessage()); 
				continue;
			} catch (IOException e) {
				logger.error("HunterJacksonMapper failed  to get task receiver. Exception >> " + e.getMessage()); 
				continue;
			} 
			
			taskReceivers.add(receiver);
			
		}
		 
		logger.debug("Successfully constructed receivers set >> " + HunterUtility.stringifySet(taskReceivers));  
		
		return taskReceivers;
	}

	
	
	
	
	
	
	
	
	
	
	
	
}

