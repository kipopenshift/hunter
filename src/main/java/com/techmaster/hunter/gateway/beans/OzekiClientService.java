package com.techmaster.hunter.gateway.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.techmaster.hunter.constants.HunterConstants;
import com.techmaster.hunter.obj.beans.AuditInfo;
import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskClientConfigBean;

public class OzekiClientService extends AbstractGateWayClientService {
	
	private Logger logger = Logger.getLogger(getClass());

	@Override
	public Map<String, Object> execute(Map<String, Object> executeParams) {
		Map<String, Object> results = new HashMap<String, Object>();
		List<String> procErrors = new ArrayList<>();
		String procStatus = HunterConstants.STATUS_FAILED;
		
		try{
			
			logger.debug("Starting to process cm client task.");
			Task task = (Task)executeParams.get(TASK_BEAN);
			TaskClientConfigBean configBean = readConfigurations(HunterConstants.CLIENT_CM);
			AuditInfo auditInfo = (AuditInfo)executeParams.get(PARAM_AUDIT_INFO);
			Set<GateWayMessage> allMsgs = createGtwayMsgsFrAllTskGtwyMssgs(task, auditInfo, true);
			
			
		}catch(Exception e){
			
		}
		
		return null;
	}

	@Override
	public void setRequestBody(Task task, Set<GateWayMessage> messages) {
		// TODO Auto-generated method stub
		
	}

}
