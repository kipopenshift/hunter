package com.techmaster.hunter.obj.beans;



public class HunterRawReceiverUser extends HunterUser{

	private static final long serialVersionUID = -1035397971180469615L;
	
	private String rawUserName;
	private float compensation;
	private int allContctNo;
	private int vrfdContctNo;
	private String country;
	private String county;
	private String constituency;
	private String consWard;
	private String village;
	
	public float getCompensation() {
		return compensation;
	}
	public void setCompensation(float compensation) {
		this.compensation = compensation;
	}
	public int getAllContctNo() {
		return allContctNo;
	}
	public void setAllContctNo(int allContctNo) {
		this.allContctNo = allContctNo;
	}
	public int getVrfdContctNo() {
		return vrfdContctNo;
	}
	public void setVrfdContctNo(int vrfdContctNo) {
		this.vrfdContctNo = vrfdContctNo;
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
	public String getRawUserName() {
		return rawUserName;
	}
	public void setRawUserName(String rawUserName) {
		this.rawUserName = rawUserName;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + allContctNo;
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
		result = prime * result + vrfdContctNo;
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
		HunterRawReceiverUser other = (HunterRawReceiverUser) obj;
		if (allContctNo != other.allContctNo)
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
		if (vrfdContctNo != other.vrfdContctNo)
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "HunterRawReceiverUser [rawUserName=" + rawUserName
				+ ", compensation=" + compensation + ", allContctNo="
				+ allContctNo + ", vrfdContctNo=" + vrfdContctNo + ", country="
				+ country + ", county=" + county + ", constituency="
				+ constituency + ", consWard=" + consWard + ", village="
				+ village + "]";
	}
	
	
	
	
	
	
	
	
	
	

}
