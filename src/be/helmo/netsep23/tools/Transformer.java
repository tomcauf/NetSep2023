package be.helmo.netsep23.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transformer {
    private static Transformer instance = null;
    public static Transformer getInstance() {
        if(instance == null) instance = new Transformer();
        return instance;
    }
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /* Convertit une chaîne de caractères (sous la forme jj/mm/aaaa) en Date java */
    public Date dateFromString(String date) {
        try {
            return dateFormat.parse(date);
        } catch(ParseException ex) { ex.printStackTrace(); return null; }
    }

    /* Convertit une Date java en chaine de caractères (sous la forme jj/mm/aaaa) */
    public String dateFromDate(Date date) {
        return dateFormat.format(date);
    }

    /* Convertit une chaine de caractère rerprésentant le port en entier */
    public int portFromString(String value) { return Integer.parseInt(value); }

    /* Convertit un entier représentant le port en chaine de caractères */
    public String portToString(int port) { return String.valueOf(port); }

    /* Convertit une chaine de caractère rerprésentant le nombre de licences en entier */
    public int licenseCountFromString(String value) { return Integer.parseInt(value); }

    /* Convertit un entier représentant le nombre de licenses en chaine de caractères */
    public String licenseCountToString(int licenseCount) { return String.valueOf(licenseCount); }
}
