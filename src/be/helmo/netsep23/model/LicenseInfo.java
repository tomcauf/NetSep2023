package be.helmo.netsep23.model;

import java.util.Date;

import be.helmo.netsep23.tools.Transformer;

public class LicenseInfo implements Comparable<LicenseInfo>{
	private String programId;
	private int numberOfLicenses;
	private Date expiration;
	
	public LicenseInfo(String programId, int numberOfLicenses) {
		this.programId = programId;
		this.numberOfLicenses = numberOfLicenses;
	}
	
	public LicenseInfo(String programId, int numberOfLicenses, Date expiration) {
		this(programId, numberOfLicenses);
		this.expiration = expiration;
	}
	
	public String getProgramId() { return this.programId; }
	public int getNumberofLicenses() { return this.numberOfLicenses; }
	public Date getExpiration() { return this.expiration; }
	public void setProgramId(String programId) { this.programId = programId; }
	public void setNumberOfLicenses(int numberOfLicenses) { this.numberOfLicenses= numberOfLicenses; }
	public void setExpiration(Date expiration) { this.expiration = expiration; }
	public boolean isExpired() {
		Date today = new Date();
		return today.after(expiration);
	}
	
	public boolean equals(Object o) {
		if(o instanceof LicenseInfo) {
			return programId.equals(((LicenseInfo) o).programId);
		}
		return false;
	}
	
	public int hashCode() { return programId.hashCode(); }
	public String toString() { 
		return String.format("programId: %s -- number of licenses: %s -- expiration: %s", 
			this.programId, this.numberOfLicenses, Transformer.getInstance().dateFromDate(expiration));
	}
	
	public int compareTo(LicenseInfo o) { return this.programId.compareTo(o.programId); }
	
}
