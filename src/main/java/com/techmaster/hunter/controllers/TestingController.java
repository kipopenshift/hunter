package com.techmaster.hunter.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/test")
public class TestingController {
	
	@RequestMapping(value="/task/action/taskPage" )
	public String goToNewTaskPage(){
		return "taskTesting"; 
	}

}
