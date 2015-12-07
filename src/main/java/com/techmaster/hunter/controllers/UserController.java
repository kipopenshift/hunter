package com.techmaster.hunter.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.dao.types.HunterUserDao;
import com.techmaster.hunter.obj.beans.HunterUser;
import com.techmaster.hunter.util.HunterLogFactory;


@Controller
@RequestMapping(value="/hunteruser")
public class UserController {
	
	@Autowired private HunterUserDao userDao;
	
	@RequestMapping(value="/action/create", method=RequestMethod.POST )
	@Produces("application/json")
	@Consumes("application/json")
	public @ResponseBody HunterUser createUser(@RequestBody Map<String,String> data){
		
		HunterUser hunterUser = new HunterUser();
		Long userId = userDao.getNextUserId();
		HunterLogFactory.getLog(getClass()).debug("Assigned the id to the user >> " + userId); 
		hunterUser.setUserId(userId);
		hunterUser.setAddresses(null); 
		hunterUser.setCreatedBy("hlangat01"); 
		hunterUser.setCreditCards(null); 
		hunterUser.setCretDate(new Date()); 
		hunterUser.setEmail(data.get("email")); 
		hunterUser.setFirstName(data.get("firstName")); 
		hunterUser.setMiddleName(data.get("middleName")); 
		hunterUser.setLastName(data.get("lastName")); 
		hunterUser.setLastUpdate(new Date()); 
		hunterUser.setLastUpdatedBy("hlangat01"); 
		hunterUser.setPassword(data.get("password"));
		hunterUser.setPhoneNumber(data.get("phoneNumber"));
		hunterUser.setProfilePhoto(null);
		hunterUser.setUserName(data.get("userName"));
		hunterUser.setUserType(HunterConstants.HUNTER_CLIENT_USER);  
		HunterLogFactory.getLog(getClass()).debug("created the following user >> " + hunterUser); 
		userDao.insertHunterUser(hunterUser); 
		
		return hunterUser;
	}
	
	@RequestMapping(value="/action/read/post", method=RequestMethod.POST )
	@Produces("application/json")
	public @ResponseBody List<HunterUser> readUsersPost(HttpServletResponse response){
		List<HunterUser> users = userDao.getAllUsers();
		return users;
	}
	
	@RequestMapping(value="/action/update", method=RequestMethod.POST )
	@Produces("application/json")
	@Consumes("application/json")
	public @ResponseBody HunterUser updateUser(@RequestBody HunterUser hunterUser){
		HunterLogFactory.getLog(getClass()).debug("Received user to update from kendo >> " + hunterUser); 
		userDao.updateUser(hunterUser); 
		return hunterUser;
	}
	
	@RequestMapping(value="/action/destroy", method=RequestMethod.POST )
	@Produces("application/json")
	@Consumes("application/json")
	public @ResponseBody HunterUser deleteUser(@RequestBody HunterUser hunterUser){
		HunterLogFactory.getLog(getClass()).debug("Received user delete from kendo >> " + hunterUser); 
		userDao.deleteHunterUser(hunterUser); 
		return hunterUser;
	}
	
	

}
