package com.techmaster.hunter.obj.converters;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

import com.techmaster.hunter.json.TaskProcessJobJson;
import com.techmaster.hunter.json.TaskProcessWorkerJson;
import com.techmaster.hunter.obj.beans.TaskProcessJob;
import com.techmaster.hunter.task.process.TaskProcessJobHandler;
import com.techmaster.hunter.util.HunterUtility;
import com.techmaster.hunter.xml.XMLService;

public class TaskProcessJobConverter {
	
	private static TaskProcessJobConverter instance;
	private static Logger logger = Logger.getLogger(TaskProcessJobConverter.class);
	
	static{
		if(instance == null){
			synchronized (TaskProcessJobConverter.class) {
				instance = new TaskProcessJobConverter();
			}
		}
	}
	
	public static TaskProcessJobConverter getInstance(){
		return instance;
	}
	
	public List<TaskProcessJobJson> getProcessJobJsonsForTask(Long taskId){
		logger.debug("Starting to extract proces job jsons for task id : " + taskId); 
		List<TaskProcessJob> processJobs = TaskProcessJobHandler.getInstance().getAllTaskProcessJobsForTaskId(taskId);
		List<TaskProcessJobJson> taskProcessJobJsons = getTaskProcessJobJsons(processJobs);
		return taskProcessJobJsons;
	}
	
	public List<TaskProcessJobJson> getTaskProcessJobJsons(List<TaskProcessJob> processJobs){
		List<TaskProcessJobJson> processJsonJsons = new ArrayList<>();
		if(processJobs != null && !processJobs.isEmpty()){
			for(TaskProcessJob processJob : processJobs){
				XMLService xmlService = processJob.getXmlService();
				TaskProcessJobJson processJobJson = new TaskProcessJobJson();
				processJobJson.setClientName(xmlService.getTextValue("//context/clientName"));
				processJobJson.setEndDate(xmlService.getTextValue("//context/endDate")); 
				processJobJson.setGenDuration(HunterUtility.getLongFromObject(xmlService.getTextValue("//context/genDuration")));  
				processJobJson.setNumberOfWorkers(xmlService.getTextValue("//context/numberOfWorkers")); 
				processJobJson.setProcessJobId(processJob.getJobId());  
				processJobJson.setStartDate(xmlService.getTextValue("//context/startDate"));
				String msgCnt = xmlService.getTextValue("//context/totalMsgs");
				processJobJson.setTotalMsgs(Integer.parseInt(msgCnt)); 
				List<TaskProcessWorkerJson> workers = getProcessJobWorkerJson(processJob);
				logger.debug("Number of workers found for this process job : " + workers.size()); 
				processJobJson.setWorkerJsons(workers);
				processJsonJsons.add(processJobJson);
			}
		}
		return processJsonJsons;
	}
	
	public int getWorkerCount(TaskProcessJob processJob){
		NodeList nodeList = processJob.getXmlService().getElementsByTagName("worker");
		if(nodeList == null || nodeList.getLength() == 0) return 0;
		int workerCount = nodeList.getLength();
		logger.debug("Number of workers for process job( "+ processJob.getJobId() +" )  = " + workerCount); 
		return workerCount;
	}
	
	public List<TaskProcessWorkerJson> getProcessJobWorkerJson(TaskProcessJob processJob){
		logger.debug("Retrieving task process job jsons..."); 
		List<TaskProcessWorkerJson> processJsons = new ArrayList<TaskProcessWorkerJson>();
		if(processJob != null ){
			int workerSize = getWorkerCount(processJob);
			for(int i=0; i<workerSize;i++){
				TaskProcessWorkerJson jobJson = new TaskProcessWorkerJson();
				String workerPath = "//worker["+ (1+i) +"]/";
				XMLService xmlService = processJob.getXmlService();
				jobJson.setProcessJobId(processJob.getJobId());
				jobJson.setCnnStatus(xmlService.getTextValue(workerPath + "connStatus"));
				jobJson.setDuration(xmlService.getTextValue(workerPath + "duration"));
				jobJson.setErrorType(xmlService.getTextValue(workerPath + "errors/type"));
				jobJson.setErrorText(xmlService.getTextValue(workerPath + "errors/text"));
				jobJson.setMsgCount(Integer.parseInt(xmlService.getTextValue(workerPath + "workerMsgCount"))); 
				jobJson.setWorkerName(xmlService.getTextValue(workerPath + "@workerName"));
				jobJson.setResponseCode(xmlService.getTextValue(workerPath + "response/code"));
				jobJson.setResponseText(xmlService.getTextValue(workerPath + "response/text"));
				jobJson.setWorkerStatus(xmlService.getTextValue(workerPath + "workerStatus"));
				jobJson.setMsgIds(xmlService.getTextValue(workerPath + "messageIds"));
				jobJson.setWorkerNo(i+1); 
				jobJson.setTaskId(processJob.getTaskId());
				processJsons.add(jobJson);
			}
		}
		logger.debug("Successfully retrieved ( " + processJsons.size() + " task process job jsons)");  
		return processJsons;
	}
	
	

}
