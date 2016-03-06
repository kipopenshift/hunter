package com.techmaster.hunter.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.types.HunterClientDao;
import com.techmaster.hunter.dao.types.HunterUserDao;
import com.techmaster.hunter.obj.beans.HunterClient;
import com.techmaster.hunter.util.HunterUtility;

@Controller
@RequestMapping(value="/client")
public class ClientController extends HunterBaseController{
	
	@Autowired private HunterClientDao hunterClientDao;
	@Autowired private HunterUserDao hunterUserDao;
	private Logger logger = Logger.getLogger(ClientController.class);
	
	@RequestMapping(value="/action/read", method = RequestMethod.GET)
	@Produces("application/json") 
	public @ResponseBody String readHunterClientUser(){
		logger.debug("Loading clients from db..."); 
		List<HunterClient> clients = hunterClientDao.getAllclients();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		DateFormat df = new SimpleDateFormat(HunterConstants.HUNTER_DATE_FORMAT_MILIS );
		mapper.setDateFormat(df);
		String clientString = null;
		try {
			clientString = mapper.writeValueAsString(clients);
			System.out.println(clientString);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.debug("Successfully returned clients >> " + HunterUtility.stringifyList(clients));
		logger.debug("Finished loading clients from db..."); 
		return clientString;
	}
	
	@RequestMapping(value="/action/create", method = RequestMethod.POST)
	public String createHunterClient(){
		return "clientCreate";
	}
	
	@RequestMapping(value="/action/update", method = RequestMethod.POST)
	public String updateHunterClient(){
		return "clientCreate";
	}
	
	@RequestMapping(value="/action/destroy", method = RequestMethod.POST)
	public String destroyHunterClient(){
		return "clientCreate";
	}
	
	@RequestMapping(value="/action/getClientForUserId", method = RequestMethod.POST)
	@Produces("application/json") 
	public @ResponseBody String getClientForUserId(HttpServletRequest request){
		
		String paramNames = HunterUtility.getParamNamesAsStringsFrmRqst(request);
		logger.debug("param names > " + paramNames);
		Object userId = request.getParameter("userId");
		logger.debug("Getting client for  user id >> " + userId);  
		HunterClient client = hunterClientDao.getHunterClientForUserId(HunterUtility.getLongFromObject(userId)); 
		System.out.println(client);
		logger.debug("Obtained client >> " + client);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		DateFormat df = new SimpleDateFormat(HunterConstants.HUNTER_DATE_FORMAT_SECS );
		mapper.setDateFormat(df);
		String clientString = null;
		
		try {
			clientString = mapper.writeValueAsString(client);
			logger.debug("client String >> " + clientString); 
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.debug("Successfully returned client >> " + clientString);  
		return clientString;
		
	}
	
	@RequestMapping(value="/action/editHunterClient", method = RequestMethod.POST)
	@Produces("application/json")
	public @ResponseBody String editHunterClientDetails(HttpServletRequest request){
		
		JSONObject json = new JSONObject();
		
		try {
			
			String paramNames = HunterUtility.getParamNamesAsStringsFrmRqst(request);
			logger.debug("param names " + paramNames); 
			
			Long clientId = HunterUtility.getLongFromObject(request.getParameter("clientId"));  
			Float budget = Float.parseFloat(request.getParameter("clientTotalBudget").toString());  
			boolean isReceiver = Boolean.parseBoolean(request.getParameter("receiver").toString()); 

			logger.debug("editHunterClientDetails . Client id = " + clientId);
			
			HunterClient client =  hunterClientDao.editReceiverAndBudget(clientId, budget, isReceiver);
			
			json.append("receiver", client.isReceiver() );
			json.append("clientTotalBudget", client.getClientTotalBudget());
			json.append("cretDate", client.getCretDate());
			json.append("createdBy", client.getCreatedBy());
			json.append("lastUpdate", client.getLastUpdate());
			json.append("lastUpdatedBy", client.getLastUpdatedBy());
			json.append("status", "Success");
			json.append("message", "Successfully saved the changes!");

		} catch (Exception e) {
			e.printStackTrace();
			json.append("status", "Failure");
			json.append("message", e.getMessage());
		}
		
		return json.toString(); 
	}
	
	@RequestMapping(value="/action/mainPage")
	public String mainPage(){
		return "views/clientMainPage";
	}
	

}
