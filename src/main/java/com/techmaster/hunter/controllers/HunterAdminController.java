package com.techmaster.hunter.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmaster.hunter.dao.impl.HunterDaoFactory;
import com.techmaster.hunter.dao.types.HunterAddressDao;
import com.techmaster.hunter.dao.types.HunterRawReceiverUserDao;
import com.techmaster.hunter.dao.types.HunterUserDao;
import com.techmaster.hunter.dao.types.UserRoleDao;
import com.techmaster.hunter.json.HunterUserJson;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.HunterAddress;
import com.techmaster.hunter.obj.beans.HunterRawReceiverUser;
import com.techmaster.hunter.obj.beans.HunterUser;
import com.techmaster.hunter.obj.beans.UserRole;
import com.techmaster.hunter.obj.converters.HunterUserConverter;
import com.techmaster.hunter.util.HunterUtility;

@Controller
@RequestMapping("/admin") 
public class HunterAdminController extends HunterBaseController{
	
	@Autowired private HunterUserDao hunterUserDao;
	@Autowired private HunterAddressDao hunterAddressDao;
	@Autowired private UserRoleDao userRoleDao;
	
	private Logger logger = Logger.getLogger(getClass());

	@RequestMapping(value="/action/home", method=RequestMethod.GET)
	public String goAdminHome(){
		return "views/adminHome";
	}
	
	@Produces("application/json")
	@RequestMapping(value="/action/user/create", method=RequestMethod.POST)
	public @ResponseBody HunterUserJson createHunterUser(@RequestBody HunterUserJson hunterUserJson){
		String userName = getUserName();
		AuditInfo auditInfo = HunterUtility.getAuditInfoFromRequestForNow(null, userName);
		HunterUser hunterUser = HunterUserConverter.getInstance().getNewHunterUserFromJson(hunterUserJson, auditInfo);
		hunterUserDao.insertHunterUser(hunterUser);
		hunterUserJson = HunterUserConverter.getInstance().createHunterUserJsonForUser(hunterUser);
		return hunterUserJson;
	}
	
	@Produces("application/json")
	@RequestMapping(value="/action/user/update", method=RequestMethod.POST)
	public @ResponseBody HunterUserJson updateHunterUsers(@RequestBody HunterUserJson hunterUserJson){
		String userName = getUserName();
		AuditInfo auditInfo = HunterUtility.getAuditInfoFromRequestForNow(null, userName);
		HunterUser hunterUser = HunterUserConverter.getInstance().getNewHunterUserFromJson(hunterUserJson, auditInfo);
		hunterUserDao.updateUser(hunterUser);
		hunterUserJson = HunterUserConverter.getInstance().createHunterUserJsonForUser(hunterUser);
		return hunterUserJson;
	}
	
	@Produces("application/json")
	@Consumes("application/json")
	@RequestMapping(value="/action/user/destroy", method=RequestMethod.POST)
	public @ResponseBody String destroyHunterUsers(HttpServletRequest request){
		String requestBody = null;
		JSONObject body = null;
		JSONObject result = new JSONObject();
		try {
			requestBody = HunterUtility.getRequestBodyAsString(request);
			body = new JSONObject(requestBody);
			String userIdStr = HunterUtility.getStringOrNullFromJSONObj(body, "userId"); 
			if(userIdStr != null && HunterUtility.isNumeric(userIdStr)){ 
				String deleteErrors = hunterUserDao.validateAndDeleteById(HunterUtility.getLongFromObject(userIdStr));
				if(deleteErrors != null){
					result = HunterUtility.setJSONObjectForFailure(result, deleteErrors);
				}else{
					result = HunterUtility.setJSONObjectForSuccess(result, "User deleted successfully");
				}
			}else{
				result = HunterUtility.setJSONObjectForFailure(result, "Invalid user id ( " + userIdStr + " )"); 
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = HunterUtility.setJSONObjectForFailure(result, "Appplicatiation error occured!");
		}
		return result.toString();
	}
	
	
	@Produces("application/json")
	@ResponseBody 
	@RequestMapping(value="/action/user/read", method=RequestMethod.POST)
	public List<HunterUserJson> getAllHunterUsers(){
		List<HunterUserJson> users = hunterUserDao.getAllUserJson();
		return users;
	}
	
	@RequestMapping(value="/action/templates/userDetailsTab", method=RequestMethod.GET)
	public String getHunterUserDetailsTabsrip(){
		return "templates/taskUserDetails";
	}
	
	@RequestMapping(value="/action/templates/userAddressesTab", method=RequestMethod.GET)
	public String getHunterUserAddressesTabsrip(){
		return "templates/taskUserAddresses";
	}
	
	@RequestMapping(value="/action/templates/userRolesTab", method=RequestMethod.GET)
	public String getHunterUserRolesTabsrip(){
		return "templates/taskUserRoles";
	}
	
	@Produces("application/json")
	@ResponseBody 
	@RequestMapping(value="/action/user/addresses/read/{userId}", method=RequestMethod.POST)
	public List<HunterAddress> getUserAddresses(@PathVariable("userId")Long userId){
		List<HunterAddress> usersAddresses = hunterAddressDao.getAddressesByUserId(userId);
		return usersAddresses;
	}
	
	@Produces("application/json")
	@ResponseBody 
	@RequestMapping(value="/action/user/addresses/create/{userId}", method=RequestMethod.POST)
	public HunterAddress createTaskUserAddress(@PathVariable("userId")Long userId, @RequestBody HunterAddress hunterAddress ){
		return hunterAddress;
	}
	
	@Produces("application/json")
	@ResponseBody 
	@RequestMapping(value="/action/user/addresses/update/{userId}", method=RequestMethod.POST)
	public HunterAddress updateTaskUserAddress(@PathVariable("userId")Long userId, @RequestBody HunterAddress hunterAddress ){
		return hunterAddress;
	}
	
	@Produces("application/json")
	@Consumes("application/json") 
	@RequestMapping(value="/action/user/roles/getAll", method=RequestMethod.POST)
	public @ResponseBody List<UserRole> getAllUserRoles(HttpServletRequest request){
		List<UserRole> userRoles = userRoleDao.getAllUserRole();
		return userRoles;
	}
	
	@Produces("application/json")
	@Consumes("application/json") 
	@RequestMapping(value="/action/user/roles/remove", method=RequestMethod.POST)
	public @ResponseBody String removeRoleFromUser(HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		JSONObject requestBodyJ = null;
		try {
			String requestBody = HunterUtility.getRequestBodyAsString(request);
			requestBodyJ = new JSONObject(requestBody);
			Long userId = HunterUtility.getLongFromObject(HunterUtility.getNullOrValFromJSONObj(requestBodyJ, "userId"));
			Long userRoleId = HunterUtility.getLongFromObject(HunterUtility.getNullOrValFromJSONObj(requestBodyJ, "userRoleId"));
			String message = userRoleDao.removeRoleToUser(userId, userRoleId);
			if(message == null){
				return HunterUtility.setJSONObjectForSuccess(jsonObject, "Successfully removed role.").toString();
			}else{
				logger.debug(message); 
				return HunterUtility.setJSONObjectForFailure(jsonObject, message).toString();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return HunterUtility.setJSONObjectForFailure(jsonObject, "Application error occurred!").toString();
		} catch (IOException e) {
			e.printStackTrace();
			return HunterUtility.setJSONObjectForFailure(jsonObject, "Application error occurred!").toString();
		}
	}
	
	@Produces("application/json")
	@Consumes("application/json") 
	@RequestMapping(value="/action/user/roles/add", method=RequestMethod.POST)
	public @ResponseBody String addRoleToUser(HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		JSONObject requestBodyJ = null;
		try {
			String requestBody = HunterUtility.getRequestBodyAsString(request);
			requestBodyJ = new JSONObject(requestBody);
			Long userId = HunterUtility.getLongFromObject(HunterUtility.getNullOrValFromJSONObj(requestBodyJ, "userId"));
			Long userRoleId = HunterUtility.getLongFromObject(HunterUtility.getNullOrValFromJSONObj(requestBodyJ, "userRoleId"));
			String message = userRoleDao.addRoleToUser(userId, userRoleId);
			if(message == null){
				return HunterUtility.setJSONObjectForSuccess(jsonObject, "Successfully added role.").toString();
			}else{
				logger.debug(message); 
				return HunterUtility.setJSONObjectForFailure(jsonObject, message).toString();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return HunterUtility.setJSONObjectForFailure(jsonObject, "Application error occurred!").toString();
		} catch (IOException e) {
			e.printStackTrace();
			return HunterUtility.setJSONObjectForFailure(jsonObject, "Application error occurred!").toString();
		}
	}
	
	@Produces("application/json")
	@Consumes("application/json") 
	@RequestMapping(value="/action/user/roles/get/{userId}", method=RequestMethod.POST)
	public @ResponseBody List<UserRole> getRolesForUser(@PathVariable("userId") Long userId){
		List<UserRole> userRoles = userRoleDao.getUserRolesForUserId(userId);
		return userRoles;
	}
	
	@RequestMapping(value="/action/fieldProfile", method=RequestMethod.GET)
	public String goToFieldProfile(HttpServletResponse response){
		HunterRawReceiverUserDao userDao = HunterDaoFactory.getInstance().getDaoObject(HunterRawReceiverUserDao.class);
		HunterRawReceiverUser rawReceiverUser = userDao.getRawUserByUserName(getUserName());
		if(rawReceiverUser == null){
			return "views/fieldProfileNotFound";
		}
		return "views/fieldProfile";
	}
	
	@RequestMapping(value="/action/fieldSample", method=RequestMethod.GET)
	public String goToSample(){
		return "views/fieldSample";
	}
	
	
	
}
