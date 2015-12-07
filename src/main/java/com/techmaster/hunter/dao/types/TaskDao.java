package com.techmaster.hunter.dao.types;

import java.util.List;

import com.techmaster.hunter.obj.beans.Task;

public interface TaskDao {
	
	public Long getNextTaskId();
	public void insertTask(Task task);
	public void update(Task task);
	public void insertTasks(List<Task> tasks);
	public Task getTaskById(Long taskId);
	public List<Task> getTasksForClientUserName(String userName);
	public List<Task> getTaskForClientId(Long clientId);
	public void deleteTask(Task task);
	
	

}
