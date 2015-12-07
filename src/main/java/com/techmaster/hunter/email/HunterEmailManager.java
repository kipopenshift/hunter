package com.techmaster.hunter.email;

import com.techmaster.hunter.obj.beans.Task;

public interface HunterEmailManager {

	public void send(Task task, String mailType);
	
}
