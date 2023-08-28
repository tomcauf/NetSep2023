package be.helmo.netsep23.server;

import java.util.List;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import be.helmo.netsep23.parser.IMessageBuilder;
import be.helmo.netsep23.parser.IParser;
import be.helmo.netsep23.client.ClientRunnable;
import be.helmo.netsep23.model.AssignLicense;

public class ServerRunnable implements Runnable {
    private IServerCommand server;
    private List<ClientRunnable> clients;
    private IParser parser;
    private IMessageBuilder messageBuilder;
    private TLSSocketFactory tlsSocketFactory;
    private int unicastPort;

    public ServerRunnable(IServerCommand server, int unicastPort, IParser parser, IMessageBuilder messageBuilder,
            TLSSocketFactory tlsSocketFactory) {
        this.server = server;
        this.unicastPort = unicastPort;
        this.parser = parser;
        this.messageBuilder = messageBuilder;
        this.clients = Collections.synchronizedList(new ArrayList<>());
        this.tlsSocketFactory = tlsSocketFactory;
    }

    public void removeClient(ClientRunnable clientRunnable) {
        clients.remove(clientRunnable);
        clientRunnable.close();

    }

    @Override
    public void run() {
        SSLServerSocketFactory serverSocketFactory = tlsSocketFactory.getServerSocketFactory();

        try (SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(unicastPort)) {
            System.out.println("[*] ServerRunnable::run : Server started at "
                    + serverSocket.getInetAddress().getHostAddress() + ":" + unicastPort);
            while (true) {
                SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                System.out.println("[+] New client connected");
                ClientRunnable clientRunnable = new ClientRunnable(this, clientSocket, parser, messageBuilder);
                clients.add(clientRunnable);
                (new Thread(clientRunnable)).start();
            }
        } catch (Exception e) {
            System.out.println("[!] Error ServerRunnable::run : " + e.getMessage());
        }
    }

    public boolean licenseExists(String programId) {
        return server.licenseExists(programId);
    }

    public AssignLicense askLicense(String programId, int licenseCount) {
        if (licenseExists(programId) && server.numberOfLicensesAvailable(programId, licenseCount)) {
            AssignLicense license = server.assignLicense(programId, licenseCount);
            if (license != null) {
                System.out.println("[*] ServerRunnable::askLicense : license assigned");
                return license;
            }
        }
        return null;
    }

    public boolean freeLicense(AssignLicense license) {
        return server.freeLicense(license);
    }

    public boolean addLicense(String string, int licenseCount, Date expirationDate) {
        return server.addLicense(string, licenseCount, expirationDate);
    }
}
