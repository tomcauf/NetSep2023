package be.helmo.netsep23.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.helmo.netsep23.model.AssignLicense;
import be.helmo.netsep23.model.LicenseInfo;
import be.helmo.netsep23.parser.IMessageBuilder;
import be.helmo.netsep23.parser.IParser;
import be.helmo.netsep23.tools.IDataManager;

public class Server implements IServerCommand {
    private IDataManager dataManager;
    private List<LicenseInfo> licenses;
    private Map<String, List<AssignLicense>> assignedLicenses;
    private MulticastRunnable multicastRunnable;
    private ServerRunnable serverRunnable;

    public Server(IDataManager dataManager, int unicastPort, String mcastAddress, int mcastPort, IParser parser,
            IMessageBuilder messageBuilder, TLSSocketFactory tlsSocketFactory) {
        this.dataManager = dataManager;
        this.licenses = Collections.synchronizedList(dataManager.readData());
        this.multicastRunnable = new MulticastRunnable(unicastPort, mcastAddress, mcastPort, messageBuilder,
                licenses);
        this.serverRunnable = new ServerRunnable(this, unicastPort, parser, messageBuilder, tlsSocketFactory);
        this.assignedLicenses = Collections.synchronizedMap(new HashMap<>());
    }

    public void setLicences(List<LicenseInfo> licenses) {
        this.licenses = licenses;
    }

    public void addLicense(LicenseInfo license) {
        this.licenses.add(license);
    }

    public void removeLicense(LicenseInfo license) {
        this.licenses.remove(license);
    }

    public List<LicenseInfo> getLicenses() {
        return this.licenses;
    }

    public void run() {
        (new Thread(multicastRunnable)).start();
        (new Thread(serverRunnable)).start();
    }

    @Override
    public boolean licenseExists(String programId) {
        return licenses.stream().anyMatch(license -> license.getProgramId().equals(programId));
    }

    @Override
    public boolean numberOfLicensesAvailable(String programId, int numberOfLicenses) {
        return licenses.stream().filter(license -> license.getProgramId().equals(programId)).findFirst().get()
                .getNumberofLicenses() >= numberOfLicenses;
    }

    private AssignLicense addAssignedLicense(String programId, String licenseId, int numberOfLicenses) {
        if (!assignedLicenses.containsKey(programId)) {
            assignedLicenses.put(programId, Collections.synchronizedList(new ArrayList<>()));
        }
        AssignLicense assignLicense = new AssignLicense(programId, licenseId, numberOfLicenses);
        assignedLicenses.get(programId).add(assignLicense);
        return assignLicense;

    }

    private String getNextLicenseId(String programId) {
        return String.format("%s-%05d", programId,
                assignedLicenses.getOrDefault(programId, Collections.emptyList()).size() + 1);
    }

    @Override
    public AssignLicense assignLicense(String programId, int numberOfLicenses) {
        var license = licenses.stream().filter(l -> l.getProgramId().equals(programId)).findFirst().get();
        license.setNumberOfLicenses(license.getNumberofLicenses() - numberOfLicenses);
        dataManager.writeData(licenses);

        var licenseId = getNextLicenseId(programId);
        return addAssignedLicense(programId, licenseId, numberOfLicenses);
    }

    @Override
    public boolean freeLicense(AssignLicense license) {
        var programId = license.getProgramId();
        var licenseId = license.getLicenseId();
        var numberOfLicenses = license.getNumberOfLicenses();
        var licenseList = assignedLicenses.getOrDefault(programId, new ArrayList<>());
        var licenseToRemove = licenseList.stream().filter(l -> l.getLicenseId().equals(licenseId)).findFirst()
                .orElse(null);

        if (licenseToRemove != null) {
            licenseList.remove(licenseToRemove);
            licenses.stream().filter(l -> l.getProgramId().equals(programId)).findFirst()
                    .ifPresent(l -> l.setNumberOfLicenses(l.getNumberofLicenses() + numberOfLicenses));
            dataManager.writeData(licenses);
            return true;
        }

        return false;
    }

    @Override
    public boolean addLicense(String string, int licenseCount, Date expirationDate) {
        var license = new LicenseInfo(string, licenseCount, expirationDate);
        if (!licenses.contains(license)) {
            licenses.add(license);
            dataManager.writeData(licenses);
            return true;
        }
        return false;
    }
}
