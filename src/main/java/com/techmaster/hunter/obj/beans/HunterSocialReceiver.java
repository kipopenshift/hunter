package com.techmaster.hunter.obj.beans;

public class HunterSocialReceiver {
	
	private Long receiverId;
	private String screenName;
	private String firstName;
	private String lastName;
	private boolean active;
	private String emailAddress;
	private String socialSiteName;
	private String socialSiteMainURL;
	private String socialSiteFocusURL;
	private String hunterSocialGroupId;
	private AuditInfo auditInfo;
	
	public Long getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getSocialSiteName() {
		return socialSiteName;
	}
	public void setSocialSiteName(String socialSiteName) {
		this.socialSiteName = socialSiteName;
	}
	public String getSocialSiteMainURL() {
		return socialSiteMainURL;
	}
	public void setSocialSiteMainURL(String socialSiteMainURL) {
		this.socialSiteMainURL = socialSiteMainURL;
	}
	public String getSocialSiteFocusURL() {
		return socialSiteFocusURL;
	}
	public void setSocialSiteFocusURL(String socialSiteFocusURL) {
		this.socialSiteFocusURL = socialSiteFocusURL;
	}
	public String getHunterSocialGroupId() {
		return hunterSocialGroupId;
	}
	public void setHunterSocialGroupId(String hunterSocialGroupId) {
		this.hunterSocialGroupId = hunterSocialGroupId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime
				* result
				+ ((hunterSocialGroupId == null) ? 0 : hunterSocialGroupId
						.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((receiverId == null) ? 0 : receiverId.hashCode());
		result = prime * result
				+ ((screenName == null) ? 0 : screenName.hashCode());
		result = prime
				* result
				+ ((socialSiteFocusURL == null) ? 0 : socialSiteFocusURL
						.hashCode());
		result = prime
				* result
				+ ((socialSiteMainURL == null) ? 0 : socialSiteMainURL
						.hashCode());
		result = prime * result
				+ ((socialSiteName == null) ? 0 : socialSiteName.hashCode());
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
		HunterSocialReceiver other = (HunterSocialReceiver) obj;
		if (active != other.active)
			return false;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (hunterSocialGroupId == null) {
			if (other.hunterSocialGroupId != null)
				return false;
		} else if (!hunterSocialGroupId.equals(other.hunterSocialGroupId))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (receiverId == null) {
			if (other.receiverId != null)
				return false;
		} else if (!receiverId.equals(other.receiverId))
			return false;
		if (screenName == null) {
			if (other.screenName != null)
				return false;
		} else if (!screenName.equals(other.screenName))
			return false;
		if (socialSiteFocusURL == null) {
			if (other.socialSiteFocusURL != null)
				return false;
		} else if (!socialSiteFocusURL.equals(other.socialSiteFocusURL))
			return false;
		if (socialSiteMainURL == null) {
			if (other.socialSiteMainURL != null)
				return false;
		} else if (!socialSiteMainURL.equals(other.socialSiteMainURL))
			return false;
		if (socialSiteName == null) {
			if (other.socialSiteName != null)
				return false;
		} else if (!socialSiteName.equals(other.socialSiteName))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "HunterSocialReceiver [receiverId=" + receiverId
				+ ", screenName=" + screenName + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", active=" + active
				+ ", emailAddress=" + emailAddress + ", socialSiteName="
				+ socialSiteName + ", socialSiteMainURL=" + socialSiteMainURL
				+ ", socialSiteFocusURL=" + socialSiteFocusURL
				+ ", hunterSocialGroupId=" + hunterSocialGroupId
				+ ", auditInfo=" + auditInfo + "]";
	}
	
	
	
	

}
