package be.helmo.netsep23.tools;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import be.helmo.netsep23.model.LicenseInfo;

public class DataManager implements IDataManager {
    private String jsonPath;

    public DataManager(String source) {
        jsonPath = source;
    }

    public List<LicenseInfo> readData() {
        Gson gson = new GsonBuilder()
                .setDateFormat("dd-MM-yyyy")
                .create();
        File sourceFile = new File(jsonPath);
        if (sourceFile != null && sourceFile.exists()) {
            FileReader reader = null;
            try {
                reader = new FileReader(sourceFile);
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            LicenseInfo[] licenseInfos = gson.fromJson(reader, LicenseInfo[].class);
            List<LicenseInfo> fromFile = Arrays.asList(licenseInfos);
            List<LicenseInfo> returnList = new ArrayList<>();
            for (LicenseInfo l : fromFile) {
                if (!l.isExpired())
                    returnList.add(l);
            }
            return returnList;

        } else {
            System.out.println("[DataManager]::readData() => Fichier JSON non trouvé");
            return null;
        }
    }

    public void writeData(List<LicenseInfo> licenseInfos) {
        try {
            Gson gson = new GsonBuilder()
                    .setDateFormat("dd-MM-yyyy")
                    .create();
            File destinationFile = new File(jsonPath);
            LicenseInfo[] tab = licenseInfos.toArray(new LicenseInfo[licenseInfos.size()]);
            FileWriter writer = new FileWriter(destinationFile);
            gson.toJson(tab, writer);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
