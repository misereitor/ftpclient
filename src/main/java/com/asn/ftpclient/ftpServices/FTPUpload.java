package com.asn.ftpclient.ftpServices;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

public class FTPUpload {
	public static void main(String[] args) {
        FTPClient client = new FTPClient();
        String filename = "application.properties";

        // Read the file from resources folder.
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(filename)) {
            client.connect("ftp.asnsoftware.com.br");
            client.enterLocalPassiveMode();
            client.login("asnsoftware3-portal_contador@lwftp", "portal_contador@CONTADOR10");
            // Store file to server
            client.storeFile(filename, is);
            client.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
