package com.techmaster.hunter.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.imports.extractors.HunterMsgReceiverExtractor;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.util.HunterUtility;

@Controller
@RequestMapping(value="/messageReceiver")
public class MessageReceiverController{
	
	private Logger logger = Logger.getLogger(MessageReceiverController.class);

	@RequestMapping(value = "/action/import/receivers/post/save", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object>  importMessageReceivers(HttpServletRequest request, HttpServletResponse response){
		
		logger.debug("Extracting workbook and creating message receiver extractor.."); 
		
		Object[] wbkExtracts = HunterUtility.getWorkbookFromRequest(request);
		Workbook workbook = (Workbook)wbkExtracts[0];
		String fileName = (String)wbkExtracts[1];
		
		AuditInfo auditInfo = HunterUtility.getAuditInfoFromRequestForNow(request, HunterConstants.HUNTER_ADMIN_USER_NAME);
		HunterMsgReceiverExtractor hntMsgRcvrExrctr = new HunterMsgReceiverExtractor(workbook, auditInfo, fileName);
		
		logger.debug("Done Extracting workbook and creating message receiver extractor!!"); 
		
		@SuppressWarnings("unused")
		Map<String, Object> bundle = hntMsgRcvrExrctr.execute();
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("status", HunterConstants.STATUS_SUCCESS);
		
		return results;
		
	}

	
}
