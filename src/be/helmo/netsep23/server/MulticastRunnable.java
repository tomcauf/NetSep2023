package be.helmo.netsep23.server;

import java.util.List;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import be.helmo.netsep23.model.LicenseInfo;

import be.helmo.netsep23.parser.IMessageBuilder;

public class MulticastRunnable implements Runnable {
    private IMessageBuilder messageBuilder;
    private List<LicenseInfo> licenses;
    private int unicastPort;
    private String mcastAddress;
    private int mcastPort;

    public MulticastRunnable(int unicastPort, String mcastAddress, int mcastPort,
            IMessageBuilder messageBuilder, List<LicenseInfo> licences) {
        this.unicastPort = unicastPort;
        this.mcastAddress = mcastAddress;
        this.mcastPort = mcastPort;
        this.messageBuilder = messageBuilder;
        this.licenses = licences;
    }

    public void setLicences(List<LicenseInfo> licenses) {
        this.licenses = licenses;
    }

    public void addLicense(LicenseInfo license) {
        this.licenses.add(license);
    }

    @Override
    public void run() {
        System.out.println("[*] MulticastRunnable::run()");
        try {
            InetAddress multicastAddress = InetAddress.getByName(mcastAddress);
            InetSocketAddress multicastSocketAddress = new InetSocketAddress(multicastAddress, mcastPort);
            try (MulticastSocket multicastSocket = new MulticastSocket(mcastPort)) {
                multicastSocket.joinGroup(multicastSocketAddress, null);
                while (true) {
                    for (LicenseInfo licenseInfo : licenses) {
                        String message = messageBuilder.makeLICMessage(licenseInfo.getProgramId(), unicastPort);
                        byte[] messageBytes = message.getBytes();
                        multicastSocket.send(new DatagramPacket(messageBytes, messageBytes.length, multicastSocketAddress));
                    }
                    Thread.sleep(30000);
                }
            }
        } catch (Exception e) {
            System.out.println("[!] Erreur multicast: " + e.getMessage());
        }

    }
}
