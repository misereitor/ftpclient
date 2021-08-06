package com.asn.ftpclient.ftpServices;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

public class FTPConnect {
	public static void main(String[] args) {
        FTPClient client = new FTPClient();

        try {
            client.connect("ftp.asnsoftware.com.br");

            // When login success the login method returns true.
            boolean login = client.login("asnsoftware3-portal_contador@lwftp", "portal_contador@CONTADOR10");
            if (login) {
                System.out.println("Login success...");

                // When logout success the logout method returns true.
                boolean logout = client.logout();
                if (logout) {
                    System.out.println("Logout from FTP server...");
                }
            } else {
                System.out.println("Login fail...");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Closes the connection to the FTP server
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
