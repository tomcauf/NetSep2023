package be.helmo.netsep23.server;

import java.nio.file.Paths;

import javax.net.ssl.SSLServerSocketFactory;

public class TLSSocketFactory {
    private String certificatePath;
    private String certificateName;
    private String password;

    public TLSSocketFactory(String certificatePath, String certificateName, String password) {
        this.certificatePath = certificatePath;
        this.certificateName = certificateName;
        this.password = password;
    }

    public SSLServerSocketFactory getServerSocketFactory() {
        System.setProperty("jdk.tls.server.protocols", "TLSv1.3");
        String certificateAbsolutePath = Paths.get(certificatePath, certificateName).toAbsolutePath().toString();
        System.setProperty("javax.net.ssl.keyStore", certificateAbsolutePath);
        System.setProperty("javax.net.ssl.keyStorePassword", password);
        return (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
    }
}
