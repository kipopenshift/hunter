package com.techmaster.hunter.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.techmaster.hunter.dao.types.ServiceProviderDao;
import com.techmaster.hunter.obj.beans.ServiceProvider;

@Controller
@RequestMapping(value="/message")
public class MessageConstroller {
	
	@Autowired ServiceProviderDao serviceProviderDao;
	
	@RequestMapping(value="/action/mainPage", method = RequestMethod.GET )
	public String mainPage(HttpServletRequest request, HttpServletResponse response){
		return "views/messageMainPage";
	}
	
	@RequestMapping(value="/action/read/all", method = RequestMethod.POST )
	public String readAll(HttpServletRequest request, HttpServletResponse response){
		return "views/messageMainPage";
	}
	
	@RequestMapping(value="/action/providers/read", method = RequestMethod.POST )
	public List<ServiceProvider> readAllProviders(HttpServletRequest request, HttpServletResponse response){
		List<ServiceProvider> providers = serviceProviderDao.getAllServiceProviders();
		return providers;
	}
	
	

}
