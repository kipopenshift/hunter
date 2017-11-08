package com.techmaster.hunter.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/test")
public class TestingController extends HunterBaseController{
	
	@RequestMapping(value="/task/action/taskPage" )
	public String goToNewTaskPage(){
		return "taskTesting"; 
	}
	
	@RequestMapping(value="/task/action/newTextMsg" )
	public String getNewTextMsgView(){
		return "templates/taskTesting"; 
	}
	
	@RequestMapping(value="bootstrap/home" )
	public String jQueryUIHome(){
		return "bootstrap/home"; 
	}
	
	@RequestMapping(value="bootstrap/login" )
	public String login(){
		return "bootstrap/login"; 
	}
	
	@RequestMapping(value="bootstrap/demo" )
	public String demo(){
		return "bootstrap/demo"; 
	}
	
	@RequestMapping(value="bootstrap/dataTable" )
	public String dataTable(){
		return "bootstrap/dataTable"; 
	}


}
