package be.helmo.netsep23.parser;

import java.util.Date;

import be.helmo.netsep23.tools.Transformer;

public class MessageBuilder implements IMessageBuilder {
    private static final String LIC_MESSAGE_STR = "LIC <program-id> <unicast-port>\r\n";
    private static final String ASK_MESSAGE_STR = "ASK <program-id> <license_count>\r\n";
    private static final String FREE_MESSAGE_STR = "FREE <license-id>\r\n";
    private static final String ASKOK_MESSAGE_STR ="ASKOK <license-id>\r\n";
    private static final String ASKERR_MESSAGE_STR = "ASKERR\r\n";
    private static final String FREEOK_MESSAGE_STR = "FREEOK\r\n";
    private static final String FREEERR_MESSAGE_STR = "FREEERR\r\n";
    private static final String ADD_MESSAGE_STR = "ADD <program-id> <license-count> <expiration-date>\r\n";
    private static final String ADDOK_MESSAGE_STR = "ADDOK\r\n";
    private static final String ADDERR_MESSAGE_STR = "ADDERR\r\n";
	
    public String makeLICMessage(String programID, int port) {
        return LIC_MESSAGE_STR
                .replace("<program-id>", programID)
                .replace("<unicast-port>", Transformer.getInstance().portToString((port)));
    }

    public String makeASKMessage(String programID, int licenseCount) {
        return ASK_MESSAGE_STR
                .replace("<program-id>",programID)
                .replace("<license_count>", Transformer.getInstance().licenseCountToString(licenseCount));
    }

    public String makeFREEMessage(String licenseID) {
        return FREE_MESSAGE_STR
                .replace("<license-id>", licenseID);
    }

    public String makeASKOKMessage(String licenseID) {
        return ASKOK_MESSAGE_STR
                .replace("<license-id>", licenseID);
    }

    public String makeASKERRMessage() { return ASKERR_MESSAGE_STR; }
    public String makeFREEOKMessage() { return FREEOK_MESSAGE_STR; }
    public String makeFREEERRMessage() { return FREEERR_MESSAGE_STR; }

    public String makeADDMessage(String programID, int licenseCount, Date expirationDate) {
        return ADD_MESSAGE_STR
                .replace("<program-id>", programID)
                .replace("<license-count>", Transformer.getInstance().licenseCountToString(licenseCount))
                .replace("<expiration-date>", Transformer.getInstance().dateFromDate(expirationDate));
    }

    public String makeADDOKMessage() { return ADDOK_MESSAGE_STR; }
    public String makeADDERRMessage() { return ADDERR_MESSAGE_STR; }

}
