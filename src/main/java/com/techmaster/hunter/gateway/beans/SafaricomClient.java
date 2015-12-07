package com.techmaster.hunter.gateway.beans;

import java.util.Map;

import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.Task;

public class SafaricomClient extends AbsractGatewayClient {
	
	private Task task;
	
	public SafaricomClient(Task task) {
		super();
		this.task = task;
	}

	@Override
	public Map<String, Object>  execute(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepareTaskAndCreateGateWayMessages(Task task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object assemble() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> readConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createRequestBody(Map<String, Object> params, GateWayMessage gateWayMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doPost(Map<String, Object> params, GateWayMessage gateWayMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doGet(Map<String, Object> params, GateWayMessage gateWayMessage) {
		// TODO Auto-generated method stub
		return null;
	}

}
