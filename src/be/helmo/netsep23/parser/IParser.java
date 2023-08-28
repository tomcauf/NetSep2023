package be.helmo.netsep23.parser;

public interface IParser {
	/* Constantes identifiant le type de message trouvé */
    int U_UNKNOWN = -1;
    int LIC_MESSAGE = 0;
    int ASK_MESSAGE = 1;
    int FREE_MESSAGE = 2;
    int ASKOK_MESSAGE = 3;
    int ASKERR_MESSAGE = 4;
    int FREEOK_MESSAGE = 5;
    int FREEERR_MESSAGE = 6;
    int ADD_MESSAGE = 7;
    int ADDOK_MESSAGE = 8;
    int ADDERR_MESSAGE = 9;
    
    
	/* La méthode parse() permet de reconnaître le type de message reçu
	 * 
	 * En paramètre, il faut lui fournir la chaîne à analyser et un flag qui active ou non les informations de debug
	 * Elle retourne un entier (dont la valeur est U_UNKNOWN, LIC_MESSAGE, ASK_MESSAGE, FREE_MESSAGE, ASKOK_MESSAGE,
	 * ASKERR_MESSAGE, FREEOK_MESSAGE, FREEERR_MESSAGE, ADD_MESSAGE, ADDOK_MESSAGE, ADDERR_MESSAGE) identifiant le
	 * type de message découvert. Grâce à cette information, il est possible d'utiliser les méthodes d'analyse plus
	 * précise.
	 * */
    int parse(String ligne, boolean debug);

    /* La méthode analyzeLIC() permet d'analyser le contenu d'un message LIC
     * 
     * En paramètre, il faut lui donner un message LIC à analyser
     * 
     * Elle retourne un tableau de String reprenant les éléments trouvés:
     *   [0] => identifiant du programme
     *   [1] => port annoncé
     */
    String[] analyzeLIC(String message);
    
    /* La méthode analyzeASK() permet d'analyser le contenu d'un message ASK
     * 
     * En paramètre, il faut lui donner un message ASK à analyser
     * 
     * Elle retourne un tableau de String reprenant les éléments trouvés:
     *   [0] => identifiant du programme
     *   [1] => nombre de licences demandé
     */
    String[] analyzeASK(String message);
    
    /* La méthode analyzeFREE() permet d'analyser le contenu d'un message FREE
     * 
     * En paramètre, il faut lui donner un message FREE à analyser
     * 
     * Elle retourne un tableau de String reprenant les éléments trouvés:
     *   [0] => identifiant du groupe de licenses
     */
    String[] analyzeFREE(String message);
    
    /* La méthode analyzeASKOK() permet d'analyser le contenu d'un message ASKOK
     * 
     * En paramètre, il faut lui donner un message ASKOK à analyser
     * 
     * Elle retourne un tableau de String reprenant les éléments trouvés:
     *   [0] => identifiant du groupe de licenses
     */   
    String[] analyzeASKOK(String message);
    
    /* La méthode analyzeADD() permet d'analyser le contenu d'un message ADD
     * 
     * En paramètre, il faut lui donner un message ADD à analyser
     * 
     * Elle retourne un tableau de String reprenant les éléments trouvés:
     *   [0] => identifiant du programme
     *   [1] => nombre de licences 
     *   [2] => Date d'expiration (sous la forme JJ/MM/AAAA)
     */
    String[] analyzeADD(String message);
}
