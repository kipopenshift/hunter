package com.techmaster.hunter.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/socialAssociate")
public class HunterSocialAssociateController extends HunterBaseController { 

	@RequestMapping(value = "/action/profileHome", method = RequestMethod.GET)
	public String goToProfile(){
		return "views/hunterSocialAssociate";
	}
	
}
