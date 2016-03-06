package com.techmaster.hunter.gateway.beans;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.Task;
import com.techmaster.hunter.obj.beans.TaskMessageReceiver;

public interface GatewayClient {
	
	public static final String GATE_WAY_MESSAGES = "GATE_WAY_MESSAGES";
	public static final String TASK_PROCESS_ERRORS = "TASK_PROCESS_ERRORS";
	public static final String TASK_PROCESS_STATUS = "TASK_PROCESS_STATUS";
	public static final String TASK_BEAN = "TASK_BEAN";
	public static final String TASK_HISTORY_BEAN = "TASK_HISTORY_BEAN";
	
	public static final String TASK_VALIDATION_ERRORS = "TASK_VALIDATION_ERRORS";
	public static final String TASK_VALIDATION_STATUS = "TASK_VALIDATION_STATUS";
	
	public Map<String, Object> execute(Map<String, Object> params);
	public void prepareTaskAndCreateGateWayMessages(Task task);
	public Set<TaskMessageReceiver> getTskMsgRcvrsFrTskId(Long taskId);
	public Set<String> getUniqueContactForTaskGroups(Long taskId);
	public Object assemble();
	public void refresh();
	public Map<String, Object> readConfigurations();
	public String createRequestBody(Map<String, Object> params, GateWayMessage gateWayMessage);
	public String createRequestBody(Map<String, Object> params, List<GateWayMessage> gateWayMessage);
	public String doPost(Map<String, Object> params, GateWayMessage gateWayMessage);
	public String doGet(Map<String, Object> params, GateWayMessage gateWayMessage);
}
