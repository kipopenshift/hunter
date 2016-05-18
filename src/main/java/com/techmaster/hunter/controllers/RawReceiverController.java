package com.techmaster.hunter.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmaster.hunter.dao.impl.HunterDaoFactory;
import com.techmaster.hunter.dao.proc.ProcedureHandler;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.HunterRawReceiverDao;
import com.techmaster.hunter.dao.types.HunterRawReceiverUserDao;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.HunterRawReceiver;
import com.techmaster.hunter.obj.beans.HunterRawReceiverUser;
import com.techmaster.hunter.rawreceivers.RawReceiverService;
import com.techmaster.hunter.region.RegionService;
import com.techmaster.hunter.util.HunterUtility;

@Controller
@RequestMapping("/rawReceiver")
public class RawReceiverController extends HunterBaseController{
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired private HunterRawReceiverDao hunterRawReceiverDao;
	@Autowired private HunterRawReceiverUserDao hunterRawReceiverUserDao;
	@Autowired private RawReceiverService rawReceiverService;
	@Autowired private RegionService regionService;
	@Autowired private ProcedureHandler get_region_names_for_ids;

	@RequestMapping(value="/login/page", method=RequestMethod.GET)
	@ResponseBody
	public String loginHome(){
		return "access/login";
	}
	
	@Produces("application/json")
	@RequestMapping(value="/action/raw/getUsersContacts", method=RequestMethod.GET)
	public @ResponseBody String getUsersContacts(HttpServletRequest request){
		
		JSONArray ja = new JSONArray();
		HunterRawReceiverUser rawReceiverUser = hunterRawReceiverUserDao.getRawUserByUserName(getUserName());
		List<HunterRawReceiver> receivers = rawReceiverService.getAllRawReceiversForUser(rawReceiverUser);
		
		for(int i=0;i<receivers.size();i++){
			HunterRawReceiver receiver = receivers.get(i);
			JSONObject jo = new JSONObject();
			jo.put("rawReceiverId", receiver.getRawReceiverId());
			jo.put("receiverContact", receiver.getReceiverContact());
			jo.put("receiverType", receiver.getReceiverType());
			jo.put("firstName", receiver.getFirstName());
			jo.put("lastName", receiver.getLastName());
			jo.put("countryName", receiver.getCountryName());
			jo.put("countyName", receiver.getCountyName());
			jo.put("consName", receiver.getConsName());
			jo.put("consWardName", receiver.getConsWardName());
			jo.put("verified", receiver.isVerified()); 
			jo.put("givenByUserName", receiver.getGivenByUserName());
			jo.put("edit", "edit");
			jo.put("delete", "delete");
			jo.put("countryId", receiver.getCountryId());
			jo.put("countyId", receiver.getCountyId());
			jo.put("constituencyId", receiver.getConsId());
			jo.put("consWardId", receiver.getConsWardId());
			ja.put(jo);
		}
		JSONObject mainObj = new JSONObject();
		mainObj.put("data", ja);
		return mainObj.toString();
	}
	
	@Produces("application/json")
	@Consumes("application/json") 
	@RequestMapping(value="/action/delete/rawReceiver", method=RequestMethod.POST)
	public @ResponseBody String deleteRawReceiver(@RequestBody Map<String,Long> rawReceiverId){
		JSONObject json = new JSONObject();
		try{
			if(rawReceiverId != null && rawReceiverId.get("rawReceiverId") != null){
				System.out.println(rawReceiverId.get("rawReceiverId")); 
				Long receiverId  = Long.parseLong(rawReceiverId.get("rawReceiverId") + ""); 
				hunterRawReceiverDao.deleteHunterRawReceiverById(receiverId);  
				HunterUtility.setJSONObjectForSuccess(json, "Successfully deleted contact!");
			}else{
				HunterUtility.setJSONObjectForFailure(json, "This receiver has not receiver Id.");
			}
		}catch(Exception e){
			e.printStackTrace();
			HunterUtility.setJSONObjectForFailure(json, "An exception was encountered while deleting Contact. Please contact Hunter support.");
		}
		logger.debug(json); 
		return json.toString();
	}
	
	@Produces("application/json")
	@Consumes("application/json") 
	@RequestMapping(value="/action/create/rawReceiver", method=RequestMethod.POST)
	public @ResponseBody String createRawReceiver(@RequestBody Map<String,String> rawReceiverData){
		AuditInfo auditInfo = HunterUtility.getAuditInfoFromRequestForNow(null, getUserName());
		return rawReceiverService.createOrUpdateRawReceiver(rawReceiverData, auditInfo).toString();
	}
	
	
	@Produces("application/json")
	@Consumes("application/json") 
	@RequestMapping(value="/action/edit/rawReceiver", method=RequestMethod.POST)
	public @ResponseBody String editRawReceiver(@RequestBody Map<String,String> rawReceiverData){
		AuditInfo auditInfo = HunterUtility.getAuditInfoFromRequestForNow(null, getUserName());
		return rawReceiverService.createOrUpdateRawReceiver(rawReceiverData, auditInfo).toString();
	}
	
	@Produces("application/json")
	@RequestMapping(value="/action/read/rawReceiverUser", method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> getRawReceiverUserDetails(){
		HunterJDBCExecutor hunterJDBCExecutor = HunterDaoFactory.getInstance().getDaoObject(HunterJDBCExecutor.class);
		String query = hunterJDBCExecutor.getQueryForSqlId("getRawReceiverUserData");
		List<Object> values = new ArrayList<>();
		values.add(getUserName());
		Map<String, Object> firstRow = hunterJDBCExecutor.executeQueryFirstRowMap(query, values);
		return firstRow;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
