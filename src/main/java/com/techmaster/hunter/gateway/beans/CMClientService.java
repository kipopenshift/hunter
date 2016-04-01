package com.techmaster.hunter.gateway.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.techmaster.hunter.cache.HunterCacheUtil;
import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.constants.UIMessageConstants;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskClientConfigBean;
import com.techmaster.hunter.task.process.TaskProcessor;
import com.techmaster.hunter.util.HunterUtility;

public class CMClientService extends AbstractGateWayClientService{
	
	private static Logger logger = Logger.getLogger(CMClientService.class);
	
	
	@Override
	public void setRequestBody(Task task, Set<GateWayMessage> messages) {
		TaskClientConfigBean configBean = HunterCacheUtil.getInstance().getTaskClientConfigBean(HunterConstants.CLIENT_CM);
		String activeMethod = configBean.getActiveMethod();
		if(HunterUtility.notNullNotEmpty(activeMethod) && HunterConstants.METHOD_POST.equals(activeMethod)){
			Set<GateWayMessage> inMsgs = new HashSet<>();
			for(GateWayMessage message : messages){
				inMsgs.add(message);
				String body = GateWayClientHelper.getInstance().getCMPostRequestBody(configBean, inMsgs);
				inMsgs.clear();
				message.setRequestBody(body != null ? body.getBytes() : null); 
			}
		}else{
			
			Map<String, Object> params = new HashMap<>();
			String baseUrl = configBean.getMethodURL();
			String token = configBean.getConfigs().get(HunterConstants.CONFIG_UUID).toString();
			String from = configBean.getConfigs().get(HunterConstants.CONFIG_SENDER_KEY);
			
			params.put(HunterConstants.BASE_URL, baseUrl);
			params.put(HunterConstants.TOKEN, token);
			params.put(HunterConstants.FROM, from);
			
			for(GateWayMessage message : messages){
				
				String to = message.getContact();
				String reference = message.getClientTagKey(); 
				
				params.put(HunterConstants.BODY, message.getText());
				params.put(HunterConstants.REFERENCE, reference);
				params.put(HunterConstants.TO, to);
				
				String getString = GateWayClientHelper.getInstance().createGetStringFromParams(params);
				message.setRequestBody(getString != null ? getString.getBytes() : null);
			}
		}
	}


	@Override
	public Map<String, Object> execute(Map<String, Object> executeParams) {
		
		Map<String, Object> results = new HashMap<String, Object>();
		List<String> procErrors = new ArrayList<>();
		String procStatus = HunterConstants.STATUS_FAILED;
		
		try{
			logger.debug("Starting to process cm client task.");
			Task task = getObjFromExecParams(Task.class, TASK_BEAN, executeParams);
			TaskClientConfigBean configBean = readConfigurations(HunterConstants.CLIENT_CM);
			AuditInfo auditInfo = (AuditInfo)executeParams.get(PARAM_AUDIT_INFO);
			Set<GateWayMessage> allMsgs = createGtwayMsgsFrAllTskGtwyMssgs(task, auditInfo, true);
			
			if(allMsgs == null || allMsgs.isEmpty()){
				procErrors.add(HunterCacheUtil.getInstance().getUIMsgTxtForMsgId(UIMessageConstants.MSG_TASK_011)); 
				putStatusAndErrors(procErrors, procStatus, results);
				return results;
			}
			
			setRequestBody(task, allMsgs);
			doBatchSaveOrUpdate(allMsgs);
			
			logger.debug("Total gateway messages : " + allMsgs.size());
			Map<String,Object> processResults = TaskProcessor.getInstance().process(task, configBean, allMsgs, auditInfo);
			putStatusAndErrors(procErrors, procStatus, results);
			
			return processResults;
		}catch(Exception e){
			procErrors.add(e.getMessage());
			putStatusAndErrors(procErrors, procStatus, results);
			e.printStackTrace();
			return results;
		}
	}
}
