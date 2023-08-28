package be.helmo.netsep23.model;

public class AssignLicense {
    private String programId;
    private String licenseId;
    private int numberOfLicenses;

    public AssignLicense(String programId, String licenseId, int numberOfLicenses) {
        this.programId = programId;
        this.licenseId = licenseId;
        this.numberOfLicenses = numberOfLicenses;
    }

    public String getProgramId() {
        return programId;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public int getNumberOfLicenses() {
        return numberOfLicenses;
    }
}
