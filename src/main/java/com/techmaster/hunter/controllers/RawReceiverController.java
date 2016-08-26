package com.techmaster.hunter.controllers;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.techmaster.hunter.cache.HunterCacheUtil;
import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.constants.HunterURLConstants;
import com.techmaster.hunter.dao.impl.HunterDaoFactory;
import com.techmaster.hunter.dao.proc.ProcedureHandler;
import com.techmaster.hunter.dao.types.HunterJDBCExecutor;
import com.techmaster.hunter.dao.types.HunterRawReceiverDao;
import com.techmaster.hunter.dao.types.HunterRawReceiverUserDao;
import com.techmaster.hunter.dao.types.HunterUserDao;
import com.techmaster.hunter.exception.HunterRunTimeException;
import com.techmaster.hunter.imports.extractors.RawReceiverExtractor;
import com.techmaster.hunter.json.HunterRawReceiverJson;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.HunterRawReceiver;
import com.techmaster.hunter.obj.beans.HunterRawReceiverUser;
import com.techmaster.hunter.obj.beans.HunterUser;
import com.techmaster.hunter.obj.beans.UserProfPhoto;
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
	@Autowired private HunterUserDao hunterUserDao;
	
	
	@RequestMapping(value="/login/page", method=RequestMethod.GET)
	@ResponseBody
	public String loginHome(){
		return "access/login";
	}
	
	@Produces("application/json")
	@Consumes("application/json")
	@RequestMapping(value="/action/raw/getRawReceiversForValidation", method=RequestMethod.POST)
	public @ResponseBody List<HunterRawReceiverJson> getRawReceiversForValidation(HttpServletRequest request, HttpServletResponse response){
		
		String 
		getMode = request.getParameter("getMode"),
		modeVal = request.getParameter("modeVal"),
		bodyStr = HunterUtility.getParamNamesAsStringsFrmRqst(request);
		
		logger.debug( bodyStr );
		logger.debug("Get Parameters : " + getMode + "," + modeVal); 
		
		List<HunterRawReceiverJson> rawReceiverJsons = new ArrayList<>();
		
		return rawReceiverJsons;
	}
	
	@Produces("application/json")
	@RequestMapping(value="/action/raw/getUsersContacts", method=RequestMethod.GET)
	public @ResponseBody String getUsersContacts(HttpServletRequest request, HttpServletResponse response){
		
		
		String body = HunterUtility.getParamNamesAsStringsFrmRqst(request);
		logger.debug(body); 
		
		JSONArray ja = new JSONArray();
		HunterRawReceiverUser rawReceiverUser = hunterRawReceiverUserDao.getRawUserByUserName(getUserName());
		JSONObject mainObj = new JSONObject();
		
		if(rawReceiverUser == null){
			mainObj.put("data", ja);
			return mainObj.toString();
		}
		
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
				Long receiverId  = Long.parseLong(rawReceiverId.get("rawReceiverId") + ""); 
				hunterRawReceiverDao.deleteHunterRawReceiverById(receiverId);  
				HunterDaoFactory.getInstance().getDaoObject(RawReceiverService.class).updateRawReceiverCountsForUser(getUserName());
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
	public @ResponseBody String createRawReceiver(HttpServletRequest request,@RequestBody Map<String,String> rawReceiverData){
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
		HunterUtility.threadSleepFor(500); 
		HunterJDBCExecutor hunterJDBCExecutor = HunterDaoFactory.getInstance().getDaoObject(HunterJDBCExecutor.class);
		String query = hunterJDBCExecutor.getQueryForSqlId("getRawReceiverUserData");
		List<Object> values = new ArrayList<>();
		values.add(getUserName());
		Map<String, Object> firstRow = hunterJDBCExecutor.executeQueryFirstRowMap(query, values);
		return firstRow;
	}
	
	
	@Produces("application/json")
	@RequestMapping(value="/action/read/rawProfData", method=RequestMethod.POST)
	public @ResponseBody String getRawReceiverProfileData(@RequestBody Map<String,String> profileData){
		
		JSONObject results = new JSONObject();
		
		if( profileData != null && !profileData.isEmpty() ){
			
			try{
				
				String 
				firstName = profileData.get("firstName"),
				lastName  = profileData.get("lastName"),
				phoneNum  = profileData.get("phoneNum"),
				emailAdd  = profileData.get("emailAdd");
						   
				Long 
				country   = HunterUtility.getLongFromObject(profileData.get("country")), 
				county    = HunterUtility.getLongFromObject(profileData.get("county")), 
				cons      = HunterUtility.getLongFromObject(profileData.get("cons")), 
				ward      = HunterUtility.getLongFromObject(profileData.get("ward"));
				
				Map<String, Long> regionIds = new HashMap<String, Long>();
				regionIds.put(HunterConstants.RECEIVER_LEVEL_COUNTRY, country);
				regionIds.put(HunterConstants.RECEIVER_LEVEL_COUNTY, county);
				regionIds.put(HunterConstants.RECEIVER_LEVEL_CONSITUENCY, cons);
				regionIds.put(HunterConstants.RECEIVER_LEVEL_WARD, ward);
				
				Map<Long, String> country_ = HunterCacheUtil.getInstance().getNameIdForId(HunterConstants.RECEIVER_LEVEL_COUNTRY, regionIds);
				Map<Long, String> county_ = HunterCacheUtil.getInstance().getNameIdForId(HunterConstants.RECEIVER_LEVEL_COUNTY, regionIds);
				Map<Long, String> cons_ = HunterCacheUtil.getInstance().getNameIdForId(HunterConstants.RECEIVER_LEVEL_CONSITUENCY, regionIds);
				Map<Long, String> ward_ = HunterCacheUtil.getInstance().getNameIdForId(HunterConstants.RECEIVER_LEVEL_WARD, regionIds);
				
				String 
				countryName = country_.get(country),
				countyName 	= county_.get(county),
				conName 	= cons_.get(cons),
				wardName 	= ward_.get(ward);
				
				HunterRawReceiverUser rawReceiverUser = hunterRawReceiverUserDao.getRawUserByUserName(getUserName());
				
				rawReceiverUser.setCountry(countryName);
				rawReceiverUser.setCounty(countyName);
				rawReceiverUser.setConstituency(conName);
				rawReceiverUser.setConsWard(wardName);
				rawReceiverUser.setAuditInfo(HunterUtility.getAuditInfoFromRequestForNow(null, getUserName())); 
				
				logger.debug("Updating raw receiver..."); 
				hunterRawReceiverUserDao.updateRawUser(rawReceiverUser); 
				
				HunterUser hunterUser = hunterUserDao.getUserByUserName(getUserName());
				hunterUser.setFirstName(firstName);
				hunterUser.setLastName(lastName);
				hunterUser.setEmail(emailAdd);
				hunterUser.setPhoneNumber(phoneNum);
				
				logger.debug("Updating hunter user..."); 
				hunterUserDao.updateUser(hunterUser); 
				
				HunterUtility.setJSONObjectForSuccess(results, "Success : Successfully updated profile");
				
			}catch(Exception e){
				e.printStackTrace();
				HunterUtility.setJSONObjectForFailure(results, "Error : Application error occurred while saving profile data!");
			}
			
			
		}else{
			String message = "Error : No data submitted for profile update!";
			logger.debug(message);
			HunterUtility.setJSONObjectForFailure(results, message); 
		}
		
		return results.toString();
		
	}
	
	
	@RequestMapping(value="/action/rawReceiver/import/rawReceiver", method=RequestMethod.POST)
	public String importRawReceivers(MultipartHttpServletRequest request, HttpServletResponse response){
		
		Workbook workbook = null;
		 Iterator<String> itr =  request.getFileNames();
		 MultipartFile mpf = request.getFile(itr.next());
		 
		 try {
			InputStream is = mpf.getInputStream();
			workbook = WorkbookFactory.create(is);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		 
		 if( workbook != null ){
			String fileName = mpf.getOriginalFilename();
			RawReceiverExtractor regionExtractor = new RawReceiverExtractor(workbook, getAuditInfo(), fileName);
			
			@SuppressWarnings("unused")
			Map<String, Object> bundle = regionExtractor.execute();
		 }
		 
		 return "redirect:/rawReceiver/action/go/home";
	}
	
	@RequestMapping(value="/action/download/template", method=RequestMethod.GET)
	public byte[] downloadRawReceiverTemplate(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException{
		
		
		byte[] bytes = null;
		String fileName = "HunterRawContacts.xlsx";
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		String path = HunterURLConstants.RESOURCE_BASE_WORKBOOK_PATH + fileName;
		Workbook workbook = null;
		
		try {
			
			logger.debug("Writing the results to the response...");
			workbook = WorkbookFactory.create( new FileInputStream(path));
			if(workbook != null){
				workbook.write(outByteStream);
				bytes = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(bytes.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				response.setHeader("Content-Disposition", "attachment; filename="+ fileName); 
				OutputStream outStream = response.getOutputStream();
				outStream.write(bytes);
				outStream.flush();
			}else{
				response.getWriter().write("Error. Please make sure that you selected the write file.");
				response.getWriter().flush();
				throw new HunterRunTimeException("Error. Please make sure that you selected the write file.");
			}
			
			logger.debug("Successfully wrote the workbook to response!!"); 
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		
		return bytes;
		
		
	}
	
	
	@RequestMapping(value="/action/upload/profilePhoto",headers = "content-type=multipart/*", method=RequestMethod.POST)
	public String  uploadUserProfilePhoto(MultipartHttpServletRequest  request, HttpServletResponse response) throws FileNotFoundException, SerialException{
		
		JSONObject results = new JSONObject();
		HunterUtility.setJSONObjectForFailure(results, "Successfully uploaded profile photo!");
		//0. notice, we have used MultipartHttpServletRequest
		 
	     //1. get the files from the request object
	     Iterator<String> itr =  request.getFileNames();
	     UserProfPhoto profPhoto = new UserProfPhoto();
	 
	     MultipartFile mpf = request.getFile(itr.next());
	 
	     try {
	        
	    	 //just temporary save file info into ufile
	    	profPhoto.setSize(mpf.getBytes().length); 
	    	profPhoto.setSizeFormat(HunterConstants.FORMAT_BYTES);
	        try {
				Blob photoBlob = new SerialBlob(mpf.getBytes());
				profPhoto.setPhotoBlob(photoBlob); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        profPhoto.setPhotoFormat(mpf.getContentType()); 
	        profPhoto.setOriginalName(mpf.getOriginalFilename()); 
	        profPhoto.setCategory(HunterConstants.PHOTO_CAT_USER_PROFILE);
	        profPhoto.setAuditInfo(getAuditInfo()); 
	        
	        logger.debug(profPhoto);
	        HunterUser user = hunterUserDao.getUserByUserName(getUserName());
	        user.setUserProfPhoto(profPhoto);
	        profPhoto.setUserId(user.getUserId());
	        profPhoto.setPhotoId(user.getUserId()); 
	        hunterUserDao.updateUser(user); 
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	 
	     //2. send it back to the client as <img> that calls get method
	     //we are using getTimeInMillis to avoid server cached image 
	 
	     return "redirect:/rawReceiver/action/go/home";
	}
	
	
	@RequestMapping(value="/action/go/home",method=RequestMethod.GET)
	public String  goHOme(HttpServletRequest  request, HttpServletResponse response) throws FileNotFoundException, SerialException{
		return "views/fieldProfile";
	}
	
	@Produces("application/json")
	@RequestMapping(value="/action/raw/pagination", method=RequestMethod.GET)
	public @ResponseBody String getUserForPagination(HttpServletRequest request, HttpServletResponse response){
		
		String body = HunterUtility.getParamNamesAsStringsFrmRqst(request);
		logger.debug(body); 
		
		JSONArray ja = new JSONArray();
		HunterRawReceiverUser rawReceiverUser = hunterRawReceiverUserDao.getRawUserByUserName(getUserName());
		JSONObject mainObj = new JSONObject();
		
		if(rawReceiverUser == null){
			mainObj.put("data", ja);
			return mainObj.toString();
		}
		
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
		mainObj.put("data", ja);
		return mainObj.toString();
		
		
	}
	
	
	
	
	
	
	
}
