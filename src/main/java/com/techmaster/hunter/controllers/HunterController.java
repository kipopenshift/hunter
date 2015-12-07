package com.techmaster.hunter.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/hunter")
public class HunterController {
	
	@RequestMapping(value="/login/page", method=RequestMethod.GET)
	public String loginHome(){
		return "access/login";
	}
	
	@RequestMapping(value="/login/params", method=RequestMethod.POST)
	public String performLogin(){
		return "access/login";
	}
	
	
	@RequestMapping(value="/login/after", method=RequestMethod.GET)
	public String postLogin(){	
		return "access/postLogin";
	}
	
	@RequestMapping(value="/home", method=RequestMethod.GET)
	public String goHome(){
		return "/login/page";
	}
	

}
