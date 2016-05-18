package com.techmaster.hunter.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techmaster.hunter.util.HunterUtility;

@Controller
@RequestMapping("/hunter")
public class HunterController {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@RequestMapping(value="/login/page", method=RequestMethod.GET)
	public String loginHome(){
		return "access/login";
	}
	

	@RequestMapping(value="/home", method=RequestMethod.GET)
	public String home(){
		return "views/home";
	}
	
	@RequestMapping(value="/login/params", method=RequestMethod.POST)
	public String performLogin(){
		return "access/login";
	}
	
	
	@RequestMapping(value="/login/after", method=RequestMethod.GET)
	public String postLogin(){
		//HunterBusinessEmailService.getInstance().send(HunterConstants.MAIL_TYPE_TASK_PROCESS_NOTIFICATION, null, HunterConstants.MAIL_TYPE_TASK_PROCESS_NOTIFICATION);
		return "access/postLogin";
	}
	
	@RequestMapping(value="/login/logout", method=RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null){
			logger.debug("User was authenticated before. Logging out..."); 
			new SecurityContextLogoutHandler().logout(request, response, authentication);
			logger.debug("Done Logging out..."); 
		}else{
			logger.debug("User was NOT authenticated before. Returning...");
		}
		SecurityContextHolder.getContext().setAuthentication(null);
		HunterUtility.getSessionForRequest(request).invalidate();
	     return "access/logout";
	}
	

}
