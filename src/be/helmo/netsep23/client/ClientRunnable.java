package be.helmo.netsep23.client;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLSocket;

import be.helmo.netsep23.model.AssignLicense;
import be.helmo.netsep23.parser.IMessageBuilder;
import be.helmo.netsep23.parser.IParser;
import be.helmo.netsep23.server.ServerRunnable;
import be.helmo.netsep23.tools.Transformer;

public class ClientRunnable implements Runnable, Closeable {
    private ServerRunnable serverRunnable;
    private SSLSocket clientSocket;
    private IParser parser;
    private IMessageBuilder messageBuilder;
    private Transformer transformer;
    private BufferedReader in;
    private PrintWriter out;
    private List<AssignLicense> assignedLicenses;

    public ClientRunnable(ServerRunnable serverRunnable, SSLSocket clientSocket, IParser parser,
            IMessageBuilder messageBuilder) {
        this.serverRunnable = serverRunnable;
        this.clientSocket = clientSocket;
        this.parser = parser;
        this.messageBuilder = messageBuilder;
        this.transformer = Transformer.getInstance();
        this.assignedLicenses = Collections.synchronizedList(new ArrayList<>());
        try {
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            this.out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8),
                    true);
        } catch (Exception e) {
            System.out.println("[!] Error ClientRunnable::ClientRunnable : " + e.getMessage());
        }
    }

    public void handleMessage(String message) {
        int codeMessage = parser.parse(message, false);

        switch (codeMessage) {
            case 1:
                // ASK
                var askMessage = parser.analyzeASK(message);
                if (askMessage != null) {
                    handleASK(askMessage);
                }
                break;
            case 2:
                // FREE
                var freeMessage = parser.analyzeFREE(message);
                if (freeMessage != null) {
                    handleFREE(freeMessage);
                }
                break;
            case 7:
                // ADD
                var addMessage = parser.analyzeADD(message);
                if (addMessage != null) {
                    handleADD(addMessage);
                }
                break;
            default:
                break;
        }
    }

    private void handleASK(String[] askMessage) {
        try {
            int licenseCount = transformer.licenseCountFromString(askMessage[1]);
            AssignLicense license = serverRunnable.askLicense(askMessage[0], licenseCount);

            if (license != null) {
                assignedLicenses.add(license);
                sendMessage(messageBuilder.makeASKOKMessage(license.getLicenseId()));
            } else {
                sendMessage(messageBuilder.makeASKERRMessage());
            }
        } catch (Exception e) {
            sendMessage(messageBuilder.makeASKERRMessage());
        }
    }

    private void handleFREE(String[] freeMessage) {
        try {
            var license = assignedLicenses.stream().filter(l -> l.getLicenseId().equals(freeMessage[0])).findFirst()
                    .get();
            if (license != null) {
                if (serverRunnable.freeLicense(license)) {
                    assignedLicenses.remove(license);
                    sendMessage(messageBuilder.makeFREEOKMessage());
                } else {
                    sendMessage(messageBuilder.makeFREEERRMessage());
                }
            }
        } catch (Exception e) {
            sendMessage(messageBuilder.makeFREEERRMessage());
        }
    }

    private void handleADD(String[] addMessage) {
        try {
            var licenseCount = transformer.licenseCountFromString(addMessage[1]);
            var expirationDate = transformer.dateFromString(addMessage[2]);
            if (!serverRunnable.licenseExists(addMessage[0]) && licenseCount > 1 && expirationDate != null
                    && expirationDate.after(new Date())) {
                if (serverRunnable.addLicense(addMessage[0], licenseCount, expirationDate)) {
                    sendMessage(messageBuilder.makeADDOKMessage());
                } else {
                    sendMessage(messageBuilder.makeADDERRMessage());
                }
            } else {
                sendMessage(messageBuilder.makeADDERRMessage());
            }

        } catch (Exception e) {
            sendMessage(messageBuilder.makeADDERRMessage());
        }
    }

    private void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        System.out.println("[*] ClientRunnable::run : Client thread started");
        while (clientSocket.isConnected()) {
            try {
                String message = in.readLine();
                if (message != null) {
                    System.out.println("[*] ClientRunnable::run : Message received : " + message);
                    handleMessage(message);
                }
            } catch (Exception e) {
                System.out.println("[!] Error ClientRunnable::run : " + e.getMessage());
            }
        }
        serverRunnable.removeClient(this);
    }

    @Override
    public void close() {
        try {
            clientSocket.close();
            System.out.println("[-] Disconnect client");
        } catch (Exception e) {
            System.out.println("[!] Error ClientRunnable.close: " + e.getMessage());
        }
    }

}
