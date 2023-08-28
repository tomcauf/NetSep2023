package be.helmo.netsep23.tools;

import java.util.List;

import be.helmo.netsep23.model.LicenseInfo;

public interface IDataManager {
    /* Cette méthode permet de lire le fichier JSON. Le chemin vers la source est donnée dans le constructeur de la classe
     * Retourne:
     *   List<LicenseInfo> : retourne la liste de tous les programmes (non expirés) du fichier JSON
     *   null : dans le cas où le fichier ne peut être trouvé
     */
    List<LicenseInfo> readData();

    /* Cette méthode permet d'écrire dans le fichier JSON. Le chemin vers le fichier JSON est donné dans le constructeur
     * de la classe. Le fichier est écrasé avec toutes les informations données en paramètre. 
     * 
     * LE FICHIER DOIT EXISTER AU PREALABLE.
     *
     * Ne retourne rien. En cas de problème lors de l'écriture, un message est affiché en console.
     */
    void writeData(List<LicenseInfo> licenseInfos);
}
