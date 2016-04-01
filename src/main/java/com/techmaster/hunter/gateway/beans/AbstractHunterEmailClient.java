package com.techmaster.hunter.gateway.beans;

import java.util.List;
import java.util.Map;

import com.techmaster.hunter.obj.beans.GateWayMessage;
import com.techmaster.hunter.obj.beans.Task;

public abstract class AbstractHunterEmailClient extends AbsractGatewayClient{
	
	@Override
	public String doPost(Map<String, Object> params, GateWayMessage gateWayMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepareTaskAndCreateGateWayMessages(Task task) {
				
	}

	@Override
	public String createRequestBody(Map<String, Object> params, GateWayMessage gateWayMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createRequestBody(Map<String, Object> params, List<GateWayMessage> gateWayMessage) {
		// TODO Auto-generated method stub
		return null;
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
	
	
	
}
