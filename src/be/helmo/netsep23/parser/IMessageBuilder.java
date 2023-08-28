package be.helmo.netsep23.parser;

import java.util.Date;

/* Cette interface permet de construire les messages conforment au protocole */
public interface IMessageBuilder {
	/* La méthode makeLICMessage() permet de construire un message LIC. Le message construit contient un CRLF
	 * à la fin.
	 * 
	 * En paramètre, il faut lui fournir les informations nécessaires à la construction du message:
	 *   programID = identifiant du programme
	 *   port = port à mentionner
	 * */
    String makeLICMessage(String programID, int port);
    
	/* La méthode makeASKMessage() permet de construire un message ASK. Le message construit contient un CRLF
	 * à la fin.
	 * 
	 * En paramètre, il faut lui fournir les informations nécessaires à la construction du message:
	 *   programID = identifiant du programme
	 *   licenseCount = nombre de licences
	 * */
    String makeASKMessage(String programID, int licenseCount);
    
	/* La méthode makeFREEMessage() permet de construire un message FREE. Le message construit contient un CRLF
	 * à la fin.
	 * 
	 * En paramètre, il faut lui fournir les informations nécessaires à la construction du message:
	 *   licenseID = identifiant du groupe de licences
	 * */   
    String makeFREEMessage(String licenseID);
    
	/* La méthode makeASKOKMessage() permet de construire un message ASKOK. Le message construit contient un CRLF
	 * à la fin.
	 * 
	 * En paramètre, il faut lui fournir les informations nécessaires à la construction du message:
	 *   licenseID = identifiant du groupe de licences
	 * */     
    String makeASKOKMessage(String licenseID);
    
    /* La méthode makeASKERRMessage() permet de construire un message ASKERR. Le message construit contient un CRLF
	 * à la fin.
	 * */
    String makeASKERRMessage();
    
    /* La méthode makeFREEOKMessage() permet de construire un message FREEOK. Le message construit contient un CRLF
	 * à la fin.
	 * */
    String makeFREEOKMessage();
    
    /* La méthode makeFREEERRMessage() permet de construire un message FREEERR. Le message construit contient un CRLF
	 * à la fin.
	 * */
    String makeFREEERRMessage();
    
	/* La méthode makeADDMessage() permet de construire un message ADD. Le message construit contient un CRLF
	 * à la fin.
	 * 
	 * En paramètre, il faut lui fournir les informations nécessaires à la construction du message:
	 *   programID = identifiant du programme
	 *   licenseCount = nombre de licences
	 *   expirationDate = date d'expiration du programme/licences
	 * */
    String makeADDMessage(String programID, int licenseCount, Date expirationDate);
    
    /* La méthode makeADDOKMessage() permet de construire un message ADDOK. Le message construit contient un CRLF
	 * à la fin.
	 * */
    String makeADDOKMessage();
    
    /* La méthode makeADDERRMessage() permet de construire un message ADDERR. Le message construit contient un CRLF
	 * à la fin.
	 * */
    String makeADDERRMessage();

}
