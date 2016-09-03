package com.techmaster.hunter.obj.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HunterSocialTask {
	
	private Long taskId;
	private int desiredReceivers;
	private int actualReceivers;
	private String taskName;
	private String taskDescription;
	private String taskStatus;
	private String taskProgress;
	private Date dateLine;
	private String assignedToUserName;
	private boolean hunterOwned;
	private String clientUserName;
	private String subTaskSpecs;
	private String originalRequest;
	private List<HunterSocialAssociate> selectedAssociates = new ArrayList<>();
	private List<HunterSocialGroup> socialGroups = new ArrayList<>(); 
	private List<HunterSocialMedia> socialMedias = new ArrayList<>();
	private List<ReceiverRegion> selReceiverRegions = new ArrayList<>();
	private AuditInfo auditInfo;
	
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getTaskProgress() {
		return taskProgress;
	}
	public void setTaskProgress(String taskProgress) {
		this.taskProgress = taskProgress;
	}
	public Date getDateLine() {
		return dateLine;
	}
	public void setDateLine(Date dateLine) {
		this.dateLine = dateLine;
	}
	public String getAssignedToUserName() {
		return assignedToUserName;
	}
	public void setAssignedToUserName(String assignedToUserName) {
		this.assignedToUserName = assignedToUserName;
	}
	public boolean isHunterOwned() {
		return hunterOwned;
	}
	public void setHunterOwned(boolean hunterOwned) {
		this.hunterOwned = hunterOwned;
	}
	public String getClientUserName() {
		return clientUserName;
	}
	public void setClientUserName(String clientUserName) {
		this.clientUserName = clientUserName;
	}
	public List<HunterSocialGroup> getSocialGroups() {
		return socialGroups;
	}
	public void setSocialGroups(List<HunterSocialGroup> socialGroups) {
		this.socialGroups = socialGroups;
	}
	public AuditInfo getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
	}
	public String getSubTaskSpecs() {
		return subTaskSpecs;
	}
	public void setSubTaskSpecs(String subTaskSpecs) {
		this.subTaskSpecs = subTaskSpecs;
	}
	public String getOriginalRequest() {
		return originalRequest;
	}
	public void setOriginalRequest(String originalRequest) {
		this.originalRequest = originalRequest;
	}
	public List<HunterSocialMedia> getSocialMedias() {
		return socialMedias;
	}
	public void setSocialMedias(List<HunterSocialMedia> socialMedias) {
		this.socialMedias = socialMedias;
	}
	public List<HunterSocialAssociate> getSelectedAssociates() {
		return selectedAssociates;
	}
	public void setSelectedAssociates(List<HunterSocialAssociate> selectedAssociates) {
		this.selectedAssociates = selectedAssociates;
	}
	public List<ReceiverRegion> getSelReceiverRegions() {
		return selReceiverRegions;
	}
	public void setSelReceiverRegions(List<ReceiverRegion> selReceiverRegions) {
		this.selReceiverRegions = selReceiverRegions;
	}
	public int getDesiredReceivers() {
		return desiredReceivers;
	}
	public void setDesiredReceivers(int desiredReceivers) {
		this.desiredReceivers = desiredReceivers;
	}
	public int getActualReceivers() {
		return actualReceivers;
	}
	public void setActualReceivers(int actualReceivers) {
		this.actualReceivers = actualReceivers;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((assignedToUserName == null) ? 0 : assignedToUserName
						.hashCode());
		result = prime * result
				+ ((clientUserName == null) ? 0 : clientUserName.hashCode());
		result = prime * result
				+ ((dateLine == null) ? 0 : dateLine.hashCode());
		result = prime * result + (hunterOwned ? 1231 : 1237);
		result = prime * result
				+ ((socialGroups == null) ? 0 : socialGroups.hashCode());
		result = prime * result
				+ ((subTaskSpecs == null) ? 0 : subTaskSpecs.hashCode());
		result = prime * result
				+ ((taskDescription == null) ? 0 : taskDescription.hashCode());
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
		result = prime * result
				+ ((taskName == null) ? 0 : taskName.hashCode());
		result = prime * result
				+ ((taskProgress == null) ? 0 : taskProgress.hashCode());
		result = prime * result
				+ ((taskStatus == null) ? 0 : taskStatus.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HunterSocialTask other = (HunterSocialTask) obj;
		if (assignedToUserName == null) {
			if (other.assignedToUserName != null)
				return false;
		} else if (!assignedToUserName.equals(other.assignedToUserName))
			return false;
		if (clientUserName == null) {
			if (other.clientUserName != null)
				return false;
		} else if (!clientUserName.equals(other.clientUserName))
			return false;
		if (dateLine == null) {
			if (other.dateLine != null)
				return false;
		} else if (!dateLine.equals(other.dateLine))
			return false;
		if (hunterOwned != other.hunterOwned)
			return false;
		if (socialGroups == null) {
			if (other.socialGroups != null)
				return false;
		} else if (!socialGroups.equals(other.socialGroups))
			return false;
		if (subTaskSpecs == null) {
			if (other.subTaskSpecs != null)
				return false;
		} else if (!subTaskSpecs.equals(other.subTaskSpecs))
			return false;
		if (taskDescription == null) {
			if (other.taskDescription != null)
				return false;
		} else if (!taskDescription.equals(other.taskDescription))
			return false;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		if (taskName == null) {
			if (other.taskName != null)
				return false;
		} else if (!taskName.equals(other.taskName))
			return false;
		if (taskProgress == null) {
			if (other.taskProgress != null)
				return false;
		} else if (!taskProgress.equals(other.taskProgress))
			return false;
		if (taskStatus == null) {
			if (other.taskStatus != null)
				return false;
		} else if (!taskStatus.equals(other.taskStatus))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "HunterSocialTask [taskId=" + taskId + ", taskName=" + taskName
				+ ", taskDescription=" + taskDescription + ", taskStatus="
				+ taskStatus + ", taskProgress=" + taskProgress + ", dateLine="
				+ dateLine + ", assignedToUserName=" + assignedToUserName
				+ ", hunterOwned=" + hunterOwned + ", clientUserName="
				+ clientUserName + ", subTaskSpecs=" + subTaskSpecs
				+ ", socialGroups=" + socialGroups + ", auditInfo=" + auditInfo
				+ ", originalRequest=" + originalRequest
				+ ", socialMedias=" + socialMedias
				+ ", selectedAssociates=" + selectedAssociates
				+ ", desiredReceivers=" + desiredReceivers
				+ ", actualReceivers=" + actualReceivers
				+ "]";
	}
	
	
	
	
	
	
	

}
