package com.techmaster.hunter.obj.beans;

public class TaskMessageReceiver {
	
	private long receiverId;
	private String receiverContact;
	private String receiverType;
	private String receiverRegionLevel;
	private boolean blocked;
	private boolean active;
	private boolean random;
	private Long taskId;
	
	public TaskMessageReceiver() {
		super();
	}

	public long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverContact() {
		return receiverContact;
	}

	public void setReceiverContact(String receiverContact) {
		this.receiverContact = receiverContact;
	}

	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public String getReceiverRegionLevel() {
		return receiverRegionLevel;
	}

	public void setReceiverRegionLevel(String receiverRegionLevel) {
		this.receiverRegionLevel = receiverRegionLevel;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isRandom() {
		return random;
	}

	public void setRandom(boolean random) {
		this.random = random;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + (blocked ? 1231 : 1237);
		result = prime * result + (random ? 1231 : 1237);
		result = prime * result
				+ ((receiverContact == null) ? 0 : receiverContact.hashCode());
		result = prime * result + (int) (receiverId ^ (receiverId >>> 32));
		result = prime
				* result
				+ ((receiverRegionLevel == null) ? 0 : receiverRegionLevel
						.hashCode());
		result = prime * result
				+ ((receiverType == null) ? 0 : receiverType.hashCode());
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
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
		TaskMessageReceiver other = (TaskMessageReceiver) obj;
		if (active != other.active)
			return false;
		if (blocked != other.blocked)
			return false;
		if (random != other.random)
			return false;
		if (receiverContact == null) {
			if (other.receiverContact != null)
				return false;
		} else if (!receiverContact.equals(other.receiverContact))
			return false;
		if (receiverId != other.receiverId)
			return false;
		if (receiverRegionLevel == null) {
			if (other.receiverRegionLevel != null)
				return false;
		} else if (!receiverRegionLevel.equals(other.receiverRegionLevel))
			return false;
		if (receiverType == null) {
			if (other.receiverType != null)
				return false;
		} else if (!receiverType.equals(other.receiverType))
			return false;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaskMessageReceiver [receiverId=" + receiverId
				+ ", receiverContact=" + receiverContact + ", receiverType="
				+ receiverType + ", receiverRegionLevel=" + receiverRegionLevel
				+ ", blocked=" + blocked + ", active=" + active + ", random="
				+ random + ", taskId=" + taskId + "]";
	}

	

	

	
	
	
	

}
