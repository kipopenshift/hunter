package com.techmaster.hunter.obj.beans;

import java.util.HashSet;
import java.util.Set;

public class ReceiverGroup {
	
	private long groupId;
	private String ownerUserName;
	private String groupName;
	private String groupDesc;
	private Set<HunterMessageReceiver> hunterMessageReceivers = new HashSet<HunterMessageReceiver>();
	private AuditInfo auditInfo;
	
	public ReceiverGroup() {
		super();
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getOwnerUserName() {
		return ownerUserName;
	}

	public void setOwnerUserName(String ownerUserName) {
		this.ownerUserName = ownerUserName;
	}
	public Set<HunterMessageReceiver> getHunterMessageReceivers() {
		return hunterMessageReceivers;
	}
	public void setHunterMessageReceivers(Set<HunterMessageReceiver> hunterMessageReceivers) {
		this.hunterMessageReceivers = hunterMessageReceivers;
	}
	public AuditInfo getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((auditInfo == null) ? 0 : auditInfo.hashCode());
		result = prime * result
				+ ((groupDesc == null) ? 0 : groupDesc.hashCode());
		result = prime * result + (int) (groupId ^ (groupId >>> 32));
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime
				* result
				+ ((hunterMessageReceivers == null) ? 0
						: hunterMessageReceivers.hashCode());
		result = prime * result
				+ ((ownerUserName == null) ? 0 : ownerUserName.hashCode());
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
		ReceiverGroup other = (ReceiverGroup) obj;
		if (auditInfo == null) {
			if (other.auditInfo != null)
				return false;
		} else if (!auditInfo.equals(other.auditInfo))
			return false;
		if (groupDesc == null) {
			if (other.groupDesc != null)
				return false;
		} else if (!groupDesc.equals(other.groupDesc))
			return false;
		if (groupId != other.groupId)
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (hunterMessageReceivers == null) {
			if (other.hunterMessageReceivers != null)
				return false;
		} else if (!hunterMessageReceivers.equals(other.hunterMessageReceivers))
			return false;
		if (ownerUserName == null) {
			if (other.ownerUserName != null)
				return false;
		} else if (!ownerUserName.equals(other.ownerUserName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ReceiverGroup [groupId=" + groupId + ", ownerUserName="
				+ ownerUserName + ", groupName=" + groupName + ", groupDesc="
				+ groupDesc + ", hunterMessageReceivers="
				+ hunterMessageReceivers + ", auditInfo=" + auditInfo + "]";
	}


	
	

	
	
	
}
