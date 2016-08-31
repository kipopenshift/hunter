package com.techmaster.hunter.obj.beans;

public class HunterSocialGroup {
	
	private Long groupId;
	private String groupName;
	private String groupDescription;
	private String socialType;
	private boolean hunterOwned;
	private String clientUserName;
	private String hunterGroupAdmin;
	private String countryName;
	private String countyName;
	private String consName;
	private String wardName;
	private AuditInfo auditInfo;
	private boolean active;
	private HunterSocialGroupCredentials groupCredentials;
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDescription() {
		return groupDescription;
	}
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	public String getSocialType() {
		return socialType;
	}
	public void setSocialType(String socialType) {
		this.socialType = socialType;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getConsName() {
		return consName;
	}
	public void setConsName(String consName) {
		this.consName = consName;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	public AuditInfo getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
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
	public String getHunterGroupAdmin() {
		return hunterGroupAdmin;
	}
	public void setHunterGroupAdmin(String hunterGroupAdmin) {
		this.hunterGroupAdmin = hunterGroupAdmin;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public HunterSocialGroupCredentials getGroupCredentials() {
		return groupCredentials;
	}
	public void setGroupCredentials(HunterSocialGroupCredentials groupCredentials) {
		this.groupCredentials = groupCredentials;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((clientUserName == null) ? 0 : clientUserName.hashCode());
		result = prime * result
				+ ((consName == null) ? 0 : consName.hashCode());
		result = prime * result
				+ ((countryName == null) ? 0 : countryName.hashCode());
		result = prime * result
				+ ((countyName == null) ? 0 : countyName.hashCode());
		result = prime
				* result
				+ ((groupDescription == null) ? 0 : groupDescription.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime
				* result
				+ ((hunterGroupAdmin == null) ? 0 : hunterGroupAdmin.hashCode());
		result = prime * result + (hunterOwned ? 1231 : 1237);
		result = prime * result
				+ ((socialType == null) ? 0 : socialType.hashCode());
		result = prime * result
				+ ((wardName == null) ? 0 : wardName.hashCode());
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
		HunterSocialGroup other = (HunterSocialGroup) obj;
		if (active != other.active)
			return false;
		if (clientUserName == null) {
			if (other.clientUserName != null)
				return false;
		} else if (!clientUserName.equals(other.clientUserName))
			return false;
		if (consName == null) {
			if (other.consName != null)
				return false;
		} else if (!consName.equals(other.consName))
			return false;
		if (countryName == null) {
			if (other.countryName != null)
				return false;
		} else if (!countryName.equals(other.countryName))
			return false;
		if (countyName == null) {
			if (other.countyName != null)
				return false;
		} else if (!countyName.equals(other.countyName))
			return false;
		if (groupDescription == null) {
			if (other.groupDescription != null)
				return false;
		} else if (!groupDescription.equals(other.groupDescription))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (hunterGroupAdmin == null) {
			if (other.hunterGroupAdmin != null)
				return false;
		} else if (!hunterGroupAdmin.equals(other.hunterGroupAdmin))
			return false;
		if (hunterOwned != other.hunterOwned)
			return false;
		if (socialType == null) {
			if (other.socialType != null)
				return false;
		} else if (!socialType.equals(other.socialType))
			return false;
		if (wardName == null) {
			if (other.wardName != null)
				return false;
		} else if (!wardName.equals(other.wardName))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "HunterSocialGroup [groupId=" + groupId + ", groupName="
				+ groupName + ", groupDescription=" + groupDescription
				+ ", socialType=" + socialType + ", hunterOwned=" + hunterOwned
				+ ", clientUserName=" + clientUserName + ", hunterGroupAdmin="
				+ hunterGroupAdmin + ", countryName=" + countryName
				+ ", countyName=" + countyName + ", consName=" + consName
				+ ", wardName=" + wardName + ", auditInfo=" + auditInfo
				+ ", active=" + active + ", groupCredentials="
				+ groupCredentials + "]";
	}
	
	
	
	
	

}
