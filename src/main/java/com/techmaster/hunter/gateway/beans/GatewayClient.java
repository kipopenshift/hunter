package com.techmaster.hunter.gateway.beans;

import java.util.Map;

import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.Task;

public interface GatewayClient {
	
	public static final String GATE_WAY_MESSAGES = "GATE_WAY_MESSAGES";
	public static final String TASK_PROCESS_ERRORS = "TASK_PROCESS_ERRORS";
	public static final String TASK_PROCESS_STATUS = "TASK_PROCESS_STATUS";
	
	public static final String TASK_VALIDATION_ERRORS = "TASK_VALIDATION_ERRORS";
	public static final String TASK_VALIDATION_STATUS = "TASK_VALIDATION_STATUS";
	
	public Map<String, Object> execute(Map<String, Object> params);
	public void prepareTaskAndCreateGateWayMessages(Task task);
	public Object assemble();
	public void refresh();
	public Map<String, Object> readConfigurations();
	public String createRequestBody(Map<String, Object> params, GateWayMessage gateWayMessage);
	public String doPost(Map<String, Object> params, GateWayMessage gateWayMessage);
	public String doGet(Map<String, Object> params, GateWayMessage gateWayMessage);
}
