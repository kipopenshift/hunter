package com.techmaster.hunter.test;

import com.techmaster.hunter.dao.impl.TaskDaoImpl;
import com.techmaster.hunter.dao.types.TaskDao;
import com.techmaster.hunter.gateway.beans.CMClientBean;
import com.techmaster.hunter.obj.beans.Task;

public class ClientMajorTest {
	
	public static TaskDao taskDao = new TaskDaoImpl();
	
	public static void main(String[] args) {
		
		Task task = taskDao.getTaskById(16L);
		CMClientBean cm = new CMClientBean(task);
		cm.execute(null);
		
		
	}

}
