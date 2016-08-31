package com.techmaster.hunter.obj.beans;

import java.util.Date;


public class HunterSocialGroupCredentials {
	
	private Long id;
	private String userName;
	private String password;
	private String linkedEmail;
	private String linkedPhone;
	private String ruleDesc;
	private Date dateLine;
	private String passwordHint;
	private AuditInfo auditInfo;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLinkedEmail() {
		return linkedEmail;
	}
	public void setLinkedEmail(String linkedEmail) {
		this.linkedEmail = linkedEmail;
	}
	public String getLinkedPhone() {
		return linkedPhone;
	}
	public void setLinkedPhone(String linkedPhone) {
		this.linkedPhone = linkedPhone;
	}
	public String getRuleDesc() {
		return ruleDesc;
	}
	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}
	public Date getDateLine() {
		return dateLine;
	}
	public void setDateLine(Date dateLine) {
		this.dateLine = dateLine;
	}
	public AuditInfo getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
	}
	public String getPasswordHint() {
		return passwordHint;
	}
	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((linkedEmail == null) ? 0 : linkedEmail.hashCode());
		result = prime * result
				+ ((linkedPhone == null) ? 0 : linkedPhone.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((passwordHint == null) ? 0 : passwordHint.hashCode());
		result = prime * result
				+ ((ruleDesc == null) ? 0 : ruleDesc.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		HunterSocialGroupCredentials other = (HunterSocialGroupCredentials) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (linkedEmail == null) {
			if (other.linkedEmail != null)
				return false;
		} else if (!linkedEmail.equals(other.linkedEmail))
			return false;
		if (linkedPhone == null) {
			if (other.linkedPhone != null)
				return false;
		} else if (!linkedPhone.equals(other.linkedPhone))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (passwordHint == null) {
			if (other.passwordHint != null)
				return false;
		} else if (!passwordHint.equals(other.passwordHint))
			return false;
		if (ruleDesc == null) {
			if (other.ruleDesc != null)
				return false;
		} else if (!ruleDesc.equals(other.ruleDesc))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "HunterSocialGroupCredentials [id=" + id + ", userName="
				+ userName + ", password=" + password + ", linkedEmail="
				+ linkedEmail + ", linkedPhone=" + linkedPhone + ", ruleDesc="
				+ ruleDesc + ", dateLine=" + dateLine + ", passwordHint="
				+ passwordHint + ", auditInfo=" + auditInfo + "]";
	}
	
	

}
