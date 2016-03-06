package com.techmaster.hunter.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.proc.ProcedureHandler;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.ReceiverRegionDao;
import com.techmaster.hunter.dao.types.TaskHistoryDao;
import com.techmaster.hunter.enums.TaskHistoryEventEnum;
import com.techmaster.hunter.imports.extractors.RegionExtractor;
import com.techmaster.hunter.json.ConstituencyJson;
import com.techmaster.hunter.json.ConstituencyWardJson;
import com.techmaster.hunter.json.CountryJson;
import com.techmaster.hunter.json.CountyJson;
import com.techmaster.hunter.json.ReceiverRegionJson;
import com.techmaster.hunter.obj.beans.Constituency;
import com.techmaster.hunter.obj.beans.ConstituencyWard;
import com.techmaster.hunter.obj.beans.County;
import com.techmaster.hunter.obj.beans.RegionHierarchy;
import com.techmaster.hunter.obj.beans.TaskHistory;
import com.techmaster.hunter.region.RegionHierarchyAdapter;
import com.techmaster.hunter.region.RegionHierarchyNavBean;
import com.techmaster.hunter.region.RegionService;
import com.techmaster.hunter.task.TaskManager;
import com.techmaster.hunter.util.HunterUtility;

@Controller
@RequestMapping(value="/region")
public class RegionController {
	
	@Autowired private ReceiverRegionDao receiverRegionDao;
	@Autowired private HunterJDBCExecutor hunterJDBCExecutor;
	@Autowired private RegionService regionService;
	@Autowired private ProcedureHandler get_region_names_for_ids;
	@Autowired private TaskManager taskManager;
	@Autowired private TaskHistoryDao taskHistoryDao;
	
	RegionHierarchyAdapter regionHierarchyAdapter = RegionHierarchyAdapter.getInstance(); 
	private static final Logger logger = Logger.getLogger(RegionController.class);
	
	@RequestMapping(value="/action/region/home" )
	public String goToNewTaskPage(){
		return "/views/regionView"; 
	}
	
	@RequestMapping(value="/action/receiverRegion/home" )
	public String goToReceiverRegionView(){
		return "/views/receiverRegions"; 
	}
	
	@RequestMapping(value="/action/countries/read", method=RequestMethod.POST) 
	@ResponseBody public List<CountryJson> getCountries(){
		List<CountryJson>  jsons = receiverRegionDao.getCountryJsonsForAllCountries();
		logger.debug("Returning countries >> " + HunterUtility.stringifyList(jsons));  
		return jsons;
	}
	
	@RequestMapping(value="/action/counties/read/{selCountry}", method=RequestMethod.POST) 
	@ResponseBody public List<CountyJson> getCounties(@PathVariable("selCountry") String selCounty){
		if(selCounty == null || selCounty.trim().equals("") || selCounty.equals("UNSELECTED")){ 
			return new ArrayList<>(); 
		}
		List<CountyJson> countiesJSON = receiverRegionDao.getCountyJsonsForSelCountry(selCounty);
		return countiesJSON;
	}
	
	@RequestMapping(value="/action/counties/all/read", method=RequestMethod.POST) 
	@ResponseBody public List<CountyJson> getAllCounties(){
		List<CountyJson> countiesJSON = new ArrayList<>();
		List<County> counties = receiverRegionDao.getAllCounties();
		for(County county : counties){
			CountyJson json = new CountyJson(county.getCountyName(), county.getCountyId(), county.getCountryId());
			countiesJSON.add(json);
		}
		logger.debug("returning county jsons >> " + HunterUtility.stringifyList(countiesJSON));
		return countiesJSON;
	}


	@RequestMapping(value="/action/constituencies/read/{selCountyId}", method=RequestMethod.POST) 
	@ResponseBody public List<ConstituencyJson> getConstituenciesForCounty(@PathVariable("selCountyId") String selCountyId){
		if(selCountyId == null || selCountyId.trim().equals("") || selCountyId.equals("UNSELECTED")){ 
			return new ArrayList<>(); 
		}
		List<ConstituencyJson> constituencyJsons = receiverRegionDao.getConstituencyJsonsForSelCounty(selCountyId);
		logger.debug("Returning constituencies >> " + HunterUtility.stringifyList(constituencyJsons)); 
		return constituencyJsons;
	}
	
	@RequestMapping(value="/action/constituencies/all/read", method=RequestMethod.POST) 
	@ResponseBody public List<ConstituencyJson> getAllConstituencies(){
		List<ConstituencyJson> countiesJSON = new ArrayList<>();
		List<Constituency> constituencies = receiverRegionDao.getAllConstituencies();
		for(Constituency constituency : constituencies){
			ConstituencyJson json = new ConstituencyJson(constituency.getCnsttncyName(), constituency.getCnsttncyId(), constituency.getCountyId());
			countiesJSON.add(json);
		}
		logger.debug("returning constituencies jsons >> " + HunterUtility.stringifyList(countiesJSON));
		return countiesJSON;
	}
	
	@RequestMapping(value="/action/constituencyWards/read/{selConstituencyId}", method=RequestMethod.POST) 
	@ResponseBody public List<ConstituencyWardJson> getConstituencyWardForConstituency(@PathVariable("selConstituencyId") String selConstituencyId){
		logger.debug("Selected constituency >> " + selConstituencyId ); 
		if(selConstituencyId == null || selConstituencyId.trim().equals("") || selConstituencyId.equals("UNSELECTED")){ 
			return new ArrayList<>();
		}
		List<ConstituencyWardJson> constituencyWardsJson = receiverRegionDao.getAllconsConstituencyWardJsonsForSelCons(selConstituencyId);
		logger.debug(constituencyWardsJson); 
		return constituencyWardsJson;
		
	}
	

	@RequestMapping(value="/action/constituencyWards/all/read", method=RequestMethod.POST) 
	@ResponseBody public List<ConstituencyWardJson> getAllConstituencyWards(){
		List<ConstituencyWardJson> constituencyWardsJson = new ArrayList<>();
		List<ConstituencyWard> constituencyWards = receiverRegionDao.getAllConstituencyWards();
		for(ConstituencyWard constituencyWard : constituencyWards){
			ConstituencyWardJson json = new ConstituencyWardJson(constituencyWard.getWardName(), constituencyWard.getWardId(), constituencyWard.getConstituencyId());
			constituencyWardsJson.add(json);
		}
		logger.debug("returning constituencyWards jsons >> " + HunterUtility.stringifyList(constituencyWardsJson));
		return constituencyWardsJson;
	}
	
	
	@RequestMapping(value="/action/import/receiverRegions", method=RequestMethod.POST) 
	@ResponseBody public byte[] importReceiverRegions(HttpServletRequest request, HttpServletResponse response){
		
		Object[] wbkExtracts = HunterUtility.getWorkbookFromRequest(request);
		Workbook workbook = (Workbook)wbkExtracts[0];
		String fileName = (String)wbkExtracts[1];
		
		RegionExtractor regionExtractor = new RegionExtractor(workbook);
		
		@SuppressWarnings("unused")
		Map<String, Object> bundle = regionExtractor.execute();
		
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		byte[] bytes = null;
		
		try {
			
			logger.debug("Writing the results to the response..."); 
			
			workbook.write(outByteStream);
			bytes = outByteStream.toByteArray();
			response.setContentType("application/ms-excel");
			response.setContentLength(bytes.length);
			response.setHeader("Expires:", "0"); // eliminates browser caching
			response.setHeader("Content-Disposition", "attachment; filename="+ fileName); 
			OutputStream outStream = response.getOutputStream();
			outStream.write(bytes);
			outStream.flush();
			
			logger.debug("Successfully wrote the workbook to response!!"); 
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}
	
	@Produces("application/json") 
	@RequestMapping(value="/action/regions/populateRandomReceivers/{countyName}/{countyNum}/{consId}/{consNum}/{consWrdName}/{consWrdNum}", method=RequestMethod.GET) 
	@ResponseBody public Map<String, String> populateRandomHunterReceivers(HttpServletRequest request, HttpServletResponse response){
		Map<String, String>  results = new HashMap<>();
		results.put(HunterConstants.STATUS_STRING, HunterConstants.STATUS_SUCCESS);
		return results;
	}
	

	@Produces("application/json") 
	@RequestMapping(value="/action/regions/hierarchies/action/home", method=RequestMethod.GET) 
	public String goHome(HttpServletRequest request, HttpServletResponse response){
		return "views/regionHierarchy";
	}
	
	@Produces("application/json")
	@Consumes("application/json")
	@RequestMapping(value="/action/regions/hierarchies/action/read/post/{country}", method=RequestMethod.POST) 
	@ResponseBody public List<RegionHierarchy> readHierarchicalRegionsPost(HttpServletRequest request,@PathVariable("country") String countryName){
		
		HttpSession session = HunterUtility.getSessionForRequest(request);
		Object rgnHrrchyNavBeanObj = HunterUtility.getSessionAttribute(request, RegionHierarchyNavBean.NAV_SESSION_BEAN);
		RegionHierarchyNavBean regionHierarchyNavBean = rgnHrrchyNavBeanObj != null ? (RegionHierarchyNavBean)rgnHrrchyNavBeanObj : null;
		
		if(countryName == null || countryName.equals("null")){
			countryName = "Kenya";
		}
		
		if(regionHierarchyNavBean == null){
			regionHierarchyNavBean = new RegionHierarchyNavBean(receiverRegionDao, hunterJDBCExecutor);
			session.setAttribute(RegionHierarchyNavBean.NAV_SESSION_BEAN, regionHierarchyNavBean);
		}
		
		List<RegionHierarchy> defaultList = regionHierarchyNavBean.getDefault(countryName);
		logger.debug("Final size (" + defaultList.size() +")");
		return defaultList;
	
		
	}
	
	@Produces("application/json")
	@Consumes("application/json")
	@RequestMapping(value="/action/regions/hierarchies/action/read/get/{navDir}/{country}", method=RequestMethod.POST) 
	@ResponseBody public List<RegionHierarchy> readHierarchicalRegionsGet(HttpServletRequest request,@PathVariable("navDir") String navDir, @PathVariable("country") String countryName){
		
		HttpSession session = HunterUtility.getSessionForRequest(request);
		Object rgnHrrchyNavBeanObj = HunterUtility.getSessionAttribute(request, RegionHierarchyNavBean.NAV_SESSION_BEAN);
		RegionHierarchyNavBean regionHierarchyNavBean = rgnHrrchyNavBeanObj != null ? (RegionHierarchyNavBean)rgnHrrchyNavBeanObj : null;
		
		if(countryName == null || countryName.equals("null")){
			countryName = "Kenya";
		}
		
		if(regionHierarchyNavBean == null){
			regionHierarchyNavBean = new RegionHierarchyNavBean(receiverRegionDao, hunterJDBCExecutor);
			session.setAttribute(RegionHierarchyNavBean.NAV_SESSION_BEAN, regionHierarchyNavBean);
			List<RegionHierarchy> defaultList = regionHierarchyNavBean.getDefault(countryName);
			logger.debug("Final size (" + defaultList.size() +")"); 
			//return defaultList;
		}
		
		List<RegionHierarchy> defaultList = regionHierarchyNavBean.getDefault(countryName);
		logger.debug("Final size (" + defaultList.size() +")");
		return defaultList;
	
		
	}
	
	@Produces("application/json")
	@Consumes("application/json")
	@RequestMapping(value="/action/regions/hierarchies/action/update", method=RequestMethod.POST) 
	public String readHierarchicalRegions(@RequestBody RegionHierarchy regionHierarchy){
		return "regionHierarchy";
	}
	
	@Produces("application/json")
	@Consumes("application/json")
	@RequestMapping(value="/action/regions/hierarchies/action/destroy", method=RequestMethod.POST) 
	public String destroyHierarchicalRegions(@RequestBody RegionHierarchy regionHierarchy){
		return "regionHierarchy";
	}
	
	
	
	/*     Task Regions       */
	
	

	@RequestMapping(value="/action/task/regions/read/{taskId}", method=RequestMethod.POST) 
	@ResponseBody public List<ReceiverRegionJson> getRegionsForTask(@PathVariable("taskId") String taskIdStr){
		if(taskIdStr == null || taskIdStr.equalsIgnoreCase("null") || !HunterUtility.isNumeric(taskIdStr)){
			 return new ArrayList<>();
		}
		Long taskId = HunterUtility.getLongFromObject(taskIdStr);
		List<ReceiverRegionJson> receiverRegionsJson = receiverRegionDao.getReceiverRegionsJsonByTaskId(taskId);
		return receiverRegionsJson;
	}
	
	
	@Consumes("applicaiton/json")
	@Produces("applicaiton/json")
	@RequestMapping(value="/action/task/regions/delete/{selTaskId}", method=RequestMethod.POST) 
	@ResponseBody public ReceiverRegionJson deleteTaskRegion(@PathVariable("selTaskId") Long selTaskId, @RequestBody ReceiverRegionJson receiverRegionJson){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		TaskHistory taskHistory = taskManager.getNewTaskHistoryForEventName(selTaskId, TaskHistoryEventEnum.REMOVE_REGION.getEventName(), userName);
		
		logger.debug("Selected task : " + selTaskId);
		if(selTaskId != 0){
			logger.debug("Removing region : " + receiverRegionJson);
			logger.debug("From task with task Id : " + selTaskId); 
			regionService.removeTaskRegion(selTaskId, receiverRegionJson.getRegionId()); 
		}
		
		String regionName = receiverRegionJson.getCountry() + ", " + receiverRegionJson.getCounty() + ", " + receiverRegionJson.getConstituency() + ", " + receiverRegionJson.getWard();
		taskManager.setTaskHistoryStatusAndMessage(taskHistory, HunterConstants.STATUS_SUCCESS, "Successsfully removed region ( "+ regionName +" ) from task");
		taskHistoryDao.insertTaskHistory(taskHistory);
		
		return receiverRegionJson;
	}
	
	@Consumes("applicaiton/json")
	@Produces("applicaiton/json")
	@RequestMapping(value="/action/task/regions/delete/requestBody", method=RequestMethod.POST) 
	@ResponseBody public String deleteTaskRegionRequestBody(HttpServletRequest request){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		TaskHistory taskHistory = taskManager.getNewTaskHistoryForEventName(null, TaskHistoryEventEnum.REMOVE_REGION.getEventName(), userName);
		
		JSONObject jsonObject = new JSONObject();
		
		try{
			
			String body = HunterUtility.getRequestBodyAsString(request);
			JSONObject reqBody = new JSONObject(body);
			String taskIdStr = HunterUtility.getStringOrNullFromJSONObj(reqBody, "taskId");
			String regionIdStr = HunterUtility.getStringOrNullFromJSONObj(reqBody, "regionId");
			
			if(!HunterUtility.isNumeric(taskIdStr) || !HunterUtility.isNumeric(regionIdStr)){
				jsonObject.put(HunterConstants.STATUS_STRING, HunterConstants.STATUS_FAILED);
				jsonObject.put(HunterConstants.MESSAGE_STRING, "Task ID or region ID is not invalid!");
				return jsonObject.toString();
			}
			
			Long taskId = HunterUtility.getLongFromObject(taskIdStr);
			taskHistory.setTaskId(taskId);
			Long regionId = HunterUtility.getLongFromObject(regionIdStr);
			logger.debug("Getting ready to remove region ( " + regionId + " ) from task ( " + taskId + " )");  
			String results = regionService.removeTaskRegion(taskId, regionId);
			
			if(results != null){
				
				jsonObject.put(HunterConstants.STATUS_STRING, HunterConstants.STATUS_FAILED);
				jsonObject.put(HunterConstants.MESSAGE_STRING, results);
				
				taskManager.setTaskHistoryStatusAndMessage(taskHistory, HunterConstants.STATUS_FAILED, "Failed to remove region ( "+ regionId +" ) from task. " + results);
				taskHistoryDao.insertTaskHistory(taskHistory);
				
				return jsonObject.toString();
			}
			
			taskManager.setTaskHistoryStatusAndMessage(taskHistory, HunterConstants.STATUS_SUCCESS, "Failed to remove region ( "+ regionId +" ) from task");
			taskHistoryDao.insertTaskHistory(taskHistory);
			
			Object count = regionService.getTrueHntrMsgRcvrCntFrTaskRgns(taskId)[0];
			
			jsonObject.put(HunterConstants.STATUS_STRING, HunterConstants.STATUS_SUCCESS);
			jsonObject.put(HunterConstants.MESSAGE_STRING, "Successfully removed region from task!");
			jsonObject.put("regionCount", count);
			return jsonObject.toString();
			
		}catch(Exception e){
			jsonObject.put(HunterConstants.STATUS_STRING, HunterConstants.STATUS_FAILED);
			jsonObject.put(HunterConstants.MESSAGE_STRING, e.getMessage());
			return jsonObject.toString();
		}
		
		
		
		
	}
	
	@Consumes("applicaiton/json")
	@Produces("applicaiton/json")
	@RequestMapping(value="/action/task/regions/receivers/uniqueCount/{taskId}", method=RequestMethod.POST) 
	@ResponseBody public Integer getUniqueTaskRegionsReceiversCount(@PathVariable("taskId") Long taskId){
		logger.debug("Loading unique receivers count for Task Id : " + taskId);
		Object [] receiversNumberObj = regionService.getTrueHntrMsgRcvrCntFrTaskRgns(taskId);
		int count = Integer.parseInt(receiversNumberObj[0]+"");
		logger.debug("Count obtained ( " + count + " )");
		return count;
	}
	

	@Consumes("applicaiton/json")
	@Produces("applicaiton/json")
	@RequestMapping(value="/action/task/regions/addTotask", method=RequestMethod.POST) 
	@ResponseBody public String addRegionsToTask(HttpServletRequest request){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		TaskHistory taskHistory = taskManager.getNewTaskHistoryForEventName(null, TaskHistoryEventEnum.ADD_REGION.getEventName(), userName);

		
		JSONObject jsonObject = new JSONObject();
		
		try {
			
			String body = HunterUtility.getRequestBodyAsString(request);
			logger.debug("Adding regions Task. Region Ids passed in request body: " + body); 
			JSONObject taskJson = new JSONObject(body);
			
			String taskStr = HunterUtility.getStringOrNullFromJSONObj(taskJson,"selTaskId");
			Long taskId = HunterUtility.getLongFromObject(taskStr);
			String countryStr = HunterUtility.getStringOrNullFromJSONObj(taskJson,"selCountry");
			String countyStr = HunterUtility.getStringOrNullFromJSONObj(taskJson,"selCounty");
			String constituencyStr = HunterUtility.getStringOrNullFromJSONObj(taskJson,"selConstituency");
			String wardStr = HunterUtility.getStringOrNullFromJSONObj(taskJson,"selConstituencyWard");
			
			taskHistory.setTaskId(taskId); 
			
			Long countryId = HunterUtility.getLongFromObject(HunterUtility.isNumeric(countryStr) ? countryStr : null);
			Long countyId = HunterUtility.getLongFromObject(HunterUtility.isNumeric(countyStr) ? countyStr : null);
			Long constituencyId = HunterUtility.getLongFromObject(HunterUtility.isNumeric(constituencyStr) ? constituencyStr : null);
			Long wardId = HunterUtility.getLongFromObject(HunterUtility.isNumeric(wardStr) ? wardStr : null);
			
			Map<String,Object> inParams = new HashMap<>();
			inParams.put("country_id", countryId);
			inParams.put("county_id", countyId);
			inParams.put("constituency_id", constituencyId);
			inParams.put("constituency_ward_id", wardId);
			
			Map<String, Object> regionNamesMap = get_region_names_for_ids.execute_(inParams);
			String country = regionNamesMap.get("country_name") == null ? null : regionNamesMap.get("country_name").toString();
			String county = regionNamesMap.get("county_name") == null ? null : regionNamesMap.get("county_name").toString();
			String constituency = regionNamesMap.get("constituency_name") == null ? null : regionNamesMap.get("constituency_name").toString();
			String ward = regionNamesMap.get("constituency_ward_name") == null ? null : regionNamesMap.get("constituency_ward_name").toString();
			
			logger.debug(HunterUtility.stringifyMap(regionNamesMap));
			
			String regionName = country + ", " + county + ", " + constituency + ", " + ward;
			
			String result = regionService.addRegionToTask(HunterUtility.getLongFromObject(taskId), country, county, constituency, ward);
			if(result != null){
				jsonObject.put(HunterConstants.STATUS_STRING, HunterConstants.STATUS_FAILED);
				jsonObject.put(HunterConstants.MESSAGE_STRING, result);
				taskManager.setTaskHistoryStatusAndMessage(taskHistory, HunterConstants.STATUS_FAILED, "Failed to add region ( "+ regionName +" ) to task");
				taskHistoryDao.insertTaskHistory(taskHistory);
				return jsonObject.toString();
			}
			
			Object [] objs = regionService.getTrueHntrMsgRcvrCntFrTaskRgns(taskId);
			int count = Integer.parseInt(objs[0]+""); 
			logger.debug("Total unique count for task regions : " + count);
			int groupCount = taskManager.getTotalTaskGroupsReceivers(taskId);
			
			jsonObject.put("groupCount", groupCount);
			jsonObject.put("count", count);
			jsonObject.put(HunterConstants.STATUS_STRING, HunterConstants.STATUS_SUCCESS);
			jsonObject.put(HunterConstants.MESSAGE_STRING, "Successfully added region to task!");
			
			taskManager.setTaskHistoryStatusAndMessage(taskHistory, HunterConstants.STATUS_SUCCESS, "Successfully added region ( "+ regionName +" ) to task");
			taskHistoryDao.insertTaskHistory(taskHistory);
			
			return jsonObject.toString();
			
		} catch (IOException e) {
			e.printStackTrace(); 
			jsonObject.put(HunterConstants.STATUS_STRING, HunterConstants.STATUS_FAILED);
			jsonObject.put(HunterConstants.MESSAGE_STRING, e.getMessage());
			taskManager.setTaskHistoryStatusAndMessage(taskHistory, HunterConstants.STATUS_SUCCESS, "Successfully added region to task. " + e.getMessage());
			taskHistoryDao.insertTaskHistory(taskHistory);
			return jsonObject.toString();
		}
	}
	

}
