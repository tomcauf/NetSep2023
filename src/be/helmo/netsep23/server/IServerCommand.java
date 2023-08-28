package be.helmo.netsep23.server;

import java.util.Date;

import be.helmo.netsep23.model.AssignLicense;

public interface IServerCommand {
    boolean licenseExists(String programId);
    boolean numberOfLicensesAvailable(String programId, int numberOfLicenses);
    AssignLicense assignLicense(String programId, int numberOfLicenses);
    boolean freeLicense(AssignLicense license);
    boolean addLicense(String string, int licenseCount, Date expirationDate);
}
