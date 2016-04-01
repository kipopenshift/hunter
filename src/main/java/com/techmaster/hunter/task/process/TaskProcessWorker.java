package com.techmaster.hunter.task.process;

import java.util.Set;

import com.techmaster.hunter.obj.beans.GateWayMessage;


public interface TaskProcessWorker extends Runnable{
	
	public HunterProcessorConnection getConnection();
	public void processResponse(String responseCode, String responseText);
	public String getRequestBody();
	public void setDuration(Long duration,Set<GateWayMessage> messages);
	
}
