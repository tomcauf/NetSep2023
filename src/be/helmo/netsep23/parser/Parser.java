package be.helmo.netsep23.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser implements IParser {
    private static final String DIGIT = "[0-9]";
    private static final String LETTER = "[a-zA-Z]";
    private static final String DIGIT_LETTER = "(" + DIGIT + "|" + LETTER +")";
    private static final String DIGIT_LETTER_MINUS = "(" + DIGIT + "|" + LETTER +"|" + "-" + ")";
    private static final String SPACE = " ";
    private static final String CRLF = "(\\x0d\\x0a){0,1}";

    private static final String PROGRAMID = "(" + DIGIT_LETTER+"{3,15})";
    private static final String LICENSEID = "(" + DIGIT_LETTER_MINUS+"{1,30})";
    private static final String EXPIRATION = "(" + DIGIT+"{2}/"+DIGIT+"{2}/"+DIGIT+"{4})";
    private static final String LICENSECOUNT = "(" + DIGIT+"{1,5})";
    private static final String UNICASTPORT = "(" + DIGIT+"{1,5})";

    private static final String LIC_RE = "LIC" + SPACE + PROGRAMID + SPACE + UNICASTPORT + CRLF;
    private static final String ASK_RE = "ASK" + SPACE + PROGRAMID + SPACE + LICENSECOUNT + CRLF;
    private static final String FREE_RE = "FREE" + SPACE + LICENSEID + CRLF;
    private static final String ASKOK_RE = "ASKOK" + LICENSEID + CRLF;
    private static final String ASKERR_RE = "ASKERR" + CRLF;
    private static final String FREEOK_RE = "FREEOK" + CRLF;
    private static final String FREEERR_RE = "FREEERR" + CRLF;
    private static final String ADD_RE = "ADD" + SPACE + PROGRAMID + SPACE + LICENSECOUNT + SPACE + EXPIRATION + CRLF;
    private static final String ADDOK_RE = "ADDOK" + CRLF;
    private static final String ADDERR_RE = "ADDERR" + CRLF;
    private static final String[] ALL_REGEXP= { LIC_RE, ASK_RE, FREE_RE, ASKOK_RE, ASKERR_RE, FREEOK_RE, FREEERR_RE, ADD_RE, ADDOK_RE, ADDERR_RE };



    public int parse(String ligne, boolean debug) {
        for(int i= 0; i< ALL_REGEXP.length; ++i) {
            Pattern pattern = Pattern.compile(ALL_REGEXP[i], Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(ligne);
            if(matcher.matches()) {
                if (debug)
                    System.out.println("REGEXP:" + ALL_REGEXP[i] + " OK");
                return i;
            }else {
                if(debug) System.out.println("REGEXP:" + ALL_REGEXP[i] + " KO");
            }
        }
        return U_UNKNOWN;
    }

    public String[] analyzeLIC(String message) {
        if(parse(message, false) == LIC_MESSAGE) {
            return getElementsFromRegExp(message, LIC_RE, new int[] {1,3});
        }
        return null;
    }

    public String[] analyzeASK(String message) {
        if(parse(message, false) == ASK_MESSAGE) {
            return getElementsFromRegExp(message,ASK_RE, new int[] {1,3});
        }
        return null;
    }

    public String[] analyzeFREE(String message) {
        if(parse(message, false) == FREE_MESSAGE) {
            return getElementsFromRegExp(message, FREE_RE, new int[] { 1 });
        }
        return null;
    }

    public String[] analyzeASKOK(String message) {
        if(parse(message, false) == ASKOK_MESSAGE ) {
            return getElementsFromRegExp(message, ASKOK_RE, new int[] { 1 });
        }
        return null;
    }

    public String[] analyzeADD(String message) {
        if(parse(message, false) == ADD_MESSAGE) {
            return getElementsFromRegExp(message, ADD_RE, new int[] {1, 3, 4});
        }
        return null;
    }

    private String[] getElementsFromRegExp(String message, String regexp, int[] elements) {
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(message);
        if(matcher.matches()) {
            String[] result = new String[elements.length];
            for(int i=0; i< result.length; ++i) {
                result[i] = matcher.group(elements[i]);
            }
            return result;
        }
        return null;
    }
}
