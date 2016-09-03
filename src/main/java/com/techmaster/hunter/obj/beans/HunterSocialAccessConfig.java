package com.techmaster.hunter.obj.beans;

import java.sql.Blob;

import com.techmaster.hunter.util.HunterUtility;


public class HunterSocialAccessConfig {
	
	private Long configId;
	private String configName;
	private String configDesc;
	private String groupName;
	private Blob configContent;
	
	public Long getConfigId() {
		return configId;
	}
	public void setConfigId(Long configId) {
		this.configId = configId;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getConfigDesc() {
		return configDesc;
	}
	public void setConfigDesc(String configDesc) {
		this.configDesc = configDesc;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Blob getConfigContent() {
		return configContent;
	}
	public void setConfigContent(Blob configContent) {
		this.configContent = configContent;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((configDesc == null) ? 0 : configDesc.hashCode());
		result = prime * result
				+ ((configId == null) ? 0 : configId.hashCode());
		result = prime * result
				+ ((configName == null) ? 0 : configName.hashCode());
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
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
		HunterSocialAccessConfig other = (HunterSocialAccessConfig) obj;
		if (configDesc == null) {
			if (other.configDesc != null)
				return false;
		} else if (!configDesc.equals(other.configDesc))
			return false;
		if (configId == null) {
			if (other.configId != null)
				return false;
		} else if (!configId.equals(other.configId))
			return false;
		if (configName == null) {
			if (other.configName != null)
				return false;
		} else if (!configName.equals(other.configName))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "HunterSocialAccessConfig [configId=" + configId
				+ ", configName=" + configName + ", configDesc=" + configDesc
				+ ", groupName=" + groupName + ", configContent="
				+ HunterUtility.getBlobStr(configContent) + "]"; 
	}
	
	
	

}
