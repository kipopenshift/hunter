package com.techmaster.hunter.controllers;

import javax.ws.rs.Produces;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmaster.hunter.cache.HunterCacheUtil;
import com.techmaster.hunter.constants.HunterConstants;

@Controller
@RequestMapping("/cache") 
public class HunterCacheController extends HunterBaseController {
	
	@Produces("application/json")
	@RequestMapping(value="/action/refresh/{cacheServiceName}", method=RequestMethod.GET)
	@ResponseBody
	public String refreshCache(@PathVariable("cacheServiceName") String cacheServiceName){
		switch (cacheServiceName) {
		case HunterConstants.ALL_XML_SERVICES:
			HunterCacheUtil.getInstance().refreshAllXMLServices();
			break;
		default:
			break;
		}
		return "Succes";
	}
	
}
