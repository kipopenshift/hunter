package com.techmaster.hunter.obj.beans;

import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class HunterSocialAssociate extends HunterUser implements Serializable{
	
	private static final long serialVersionUID = -1854756247137634176L;
	
	private Long associatedId;
	private boolean active;
	private String associateType;
	private List<HunterSocialSubTask> subTasks = new ArrayList<HunterSocialSubTask>();
	private String rawUserName;
	private float compensation;
	private String country;
	private String county;
	private String constituency;
	private String consWard;
	private String village;
	private Blob profilePicture;
	private Blob resume;
	private AuditInfo auditInfo;
	
	
	public Long getAssociatedId() {
		return associatedId;
	}
	public void setAssociatedId(Long associatedId) {
		this.associatedId = associatedId;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getAssociateType() {
		return associateType;
	}
	public void setAssociateType(String associateType) {
		this.associateType = associateType;
	}
	public List<HunterSocialSubTask> getSubTasks() {
		return subTasks;
	}
	public void setSubTasks(List<HunterSocialSubTask> subTasks) {
		this.subTasks = subTasks;
	}
	public String getRawUserName() {
		return rawUserName;
	}
	public void setRawUserName(String rawUserName) {
		this.rawUserName = rawUserName;
	}
	public float getCompensation() {
		return compensation;
	}
	public void setCompensation(float compensation) {
		this.compensation = compensation;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getConstituency() {
		return constituency;
	}
	public void setConstituency(String constituency) {
		this.constituency = constituency;
	}
	public String getConsWard() {
		return consWard;
	}
	public void setConsWard(String consWard) {
		this.consWard = consWard;
	}
	public String getVillage() {
		return village;
	}
	public void setVillage(String village) {
		this.village = village;
	}
	public Blob getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(Blob profilePicture) {
		this.profilePicture = profilePicture;
	}
	public Blob getResume() {
		return resume;
	}
	public void setResume(Blob resume) {
		this.resume = resume;
	}
	public AuditInfo getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((associateType == null) ? 0 : associateType.hashCode());
		result = prime * result
				+ ((associatedId == null) ? 0 : associatedId.hashCode());
		result = prime * result + Float.floatToIntBits(compensation);
		result = prime * result
				+ ((consWard == null) ? 0 : consWard.hashCode());
		result = prime * result
				+ ((constituency == null) ? 0 : constituency.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((county == null) ? 0 : county.hashCode());
		result = prime * result
				+ ((rawUserName == null) ? 0 : rawUserName.hashCode());
		result = prime * result + ((village == null) ? 0 : village.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HunterSocialAssociate other = (HunterSocialAssociate) obj;
		if (active != other.active)
			return false;
		if (associateType == null) {
			if (other.associateType != null)
				return false;
		} else if (!associateType.equals(other.associateType))
			return false;
		if (associatedId == null) {
			if (other.associatedId != null)
				return false;
		} else if (!associatedId.equals(other.associatedId))
			return false;
		if (Float.floatToIntBits(compensation) != Float
				.floatToIntBits(other.compensation))
			return false;
		if (consWard == null) {
			if (other.consWard != null)
				return false;
		} else if (!consWard.equals(other.consWard))
			return false;
		if (constituency == null) {
			if (other.constituency != null)
				return false;
		} else if (!constituency.equals(other.constituency))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (county == null) {
			if (other.county != null)
				return false;
		} else if (!county.equals(other.county))
			return false;
		if (rawUserName == null) {
			if (other.rawUserName != null)
				return false;
		} else if (!rawUserName.equals(other.rawUserName))
			return false;
		if (village == null) {
			if (other.village != null)
				return false;
		} else if (!village.equals(other.village))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "HunterSocialAssociate [associatedId=" + associatedId
				+ ", active=" + active + ", associateType=" + associateType
				+ ", subTasks=" + subTasks + ", rawUserName=" + rawUserName
				+ ", compensation=" + compensation + ", country=" + country
				+ ", county=" + county + ", constituency=" + constituency
				+ ", consWard=" + consWard + ", village=" + village
				+ ", auditInfo=" + auditInfo + "]";
	}
	
	
	

	
	
	
	
	
	
}
