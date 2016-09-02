package com.techmaster.hunter.obj.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HunterSocialSubTask {
	
	private Long subTaskId;
	private Long parentTaskId;
	private String status;
	private String comments;
	private float estimatedLabor;
	private String laborCurrency;
	private List<HunterSocialReceiver> socialReceivers = new ArrayList<>();
	private String assignedToUserName;
	private AuditInfo auditInfo;
	private Date dateline;
	
	
	public Long getSubTaskId() {
		return subTaskId;
	}
	public void setSubTaskId(Long subTaskId) {
		this.subTaskId = subTaskId;
	}
	public Long getParentTaskId() {
		return parentTaskId;
	}
	public void setParentTaskId(Long parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public List<HunterSocialReceiver> getSocialReceivers() {
		return socialReceivers;
	}
	public void setSocialReceivers(List<HunterSocialReceiver> socialReceivers) {
		this.socialReceivers = socialReceivers;
	}
	public String getAssignedToUserName() {
		return assignedToUserName;
	}
	public void setAssignedToUserName(String assignedToUserName) {
		this.assignedToUserName = assignedToUserName;
	}
	public AuditInfo getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
	}
	public Date getDateline() {
		return dateline;
	}
	public void setDateline(Date dateline) {
		this.dateline = dateline;
	}
	public float getEstimatedLabor() {
		return estimatedLabor;
	}
	public void setEstimatedLabor(float estimatedLabor) {
		this.estimatedLabor = estimatedLabor;
	}
	public String getLaborCurrency() {
		return laborCurrency;
	}
	public void setLaborCurrency(String laborCurrency) {
		this.laborCurrency = laborCurrency;
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
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((dateline == null) ? 0 : dateline.hashCode());
		result = prime * result
				+ ((parentTaskId == null) ? 0 : parentTaskId.hashCode());
		result = prime * result
				+ ((socialReceivers == null) ? 0 : socialReceivers.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((subTaskId == null) ? 0 : subTaskId.hashCode());
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
		HunterSocialSubTask other = (HunterSocialSubTask) obj;
		if (assignedToUserName == null) {
			if (other.assignedToUserName != null)
				return false;
		} else if (!assignedToUserName.equals(other.assignedToUserName))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (dateline == null) {
			if (other.dateline != null)
				return false;
		} else if (!dateline.equals(other.dateline))
			return false;
		if (parentTaskId == null) {
			if (other.parentTaskId != null)
				return false;
		} else if (!parentTaskId.equals(other.parentTaskId))
			return false;
		if (socialReceivers == null) {
			if (other.socialReceivers != null)
				return false;
		} else if (!socialReceivers.equals(other.socialReceivers))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (subTaskId == null) {
			if (other.subTaskId != null)
				return false;
		} else if (!subTaskId.equals(other.subTaskId))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "HunterSocialSubTask [subTaskId=" + subTaskId
				+ ", parentTaskId=" + parentTaskId + ", status=" + status
				+ ", comments=" + comments + ", estimatedLabor="
				+ estimatedLabor + ", laborCurrency=" + laborCurrency
				+ ", socialReceivers=" + socialReceivers
				+ ", assignedToUserName=" + assignedToUserName + ", auditInfo="
				+ auditInfo + ", dateline=" + dateline + "]";
	}
	
	
	
	
	
	
	
	

}
