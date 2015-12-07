package com.techmaster.hunter.obj.beans;


public class EmailMessage extends Message {
	
	private String eSubject;
	private String eBody;
	private String eFooter;
	private String toList;
	private String eFrom;
	private String ccList;
	
	public EmailMessage() {
		super();
	}

	public String geteSubject() {
		return eSubject;
	}

	public void seteSubject(String eSubject) {
		this.eSubject = eSubject;
	}

	public String geteBody() {
		return eBody;
	}

	public void seteBody(String eBody) {
		this.eBody = eBody;
	}

	public String geteFooter() {
		return eFooter;
	}

	public void seteFooter(String eFooter) {
		this.eFooter = eFooter;
	}

	public String getToList() {
		return toList;
	}

	public void setToList(String toList) {
		this.toList = toList;
	}

	public String geteFrom() {
		return eFrom;
	}

	public void seteFrom(String eFrom) {
		this.eFrom = eFrom;
	}

	public String getCcList() {
		return ccList;
	}

	public void setCcList(String ccList) {
		this.ccList = ccList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ccList == null) ? 0 : ccList.hashCode());
		result = prime * result + ((eBody == null) ? 0 : eBody.hashCode());
		result = prime * result + ((eFooter == null) ? 0 : eFooter.hashCode());
		result = prime * result + ((eFrom == null) ? 0 : eFrom.hashCode());
		result = prime * result
				+ ((eSubject == null) ? 0 : eSubject.hashCode());
		result = prime * result + ((toList == null) ? 0 : toList.hashCode());
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
		EmailMessage other = (EmailMessage) obj;
		if (ccList == null) {
			if (other.ccList != null)
				return false;
		} else if (!ccList.equals(other.ccList))
			return false;
		if (eBody == null) {
			if (other.eBody != null)
				return false;
		} else if (!eBody.equals(other.eBody))
			return false;
		if (eFooter == null) {
			if (other.eFooter != null)
				return false;
		} else if (!eFooter.equals(other.eFooter))
			return false;
		if (eFrom == null) {
			if (other.eFrom != null)
				return false;
		} else if (!eFrom.equals(other.eFrom))
			return false;
		if (eSubject == null) {
			if (other.eSubject != null)
				return false;
		} else if (!eSubject.equals(other.eSubject))
			return false;
		if (toList == null) {
			if (other.toList != null)
				return false;
		} else if (!toList.equals(other.toList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EmailMessage [eSubject=" + eSubject + ", eBody=" + eBody
				+ ", eFooter=" + eFooter + ", toList=" + toList + ", eFrom="
				+ eFrom + ", ccList=" + ccList + "]";
	}

	
		
	
}
