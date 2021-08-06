package com.asn.ftpclient.ftpServices;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

public class DELEFTPDelete {
	 public static void main(String[] args) {
	        FTPClient client = new FTPClient();

	        try {
	            client.connect("ftp.asnsoftware.com.br");
	            client.login("asnsoftware3-portal_contador@lwftp", "portal_contador@CONTADOR10");

	            // Delete file on the FTP server. When the FTP delete
	            // complete it returns true.
	            String filename = "asdasd.txt";
	            boolean deleted = client.deleteFile(filename);
	            if (deleted) {
	                System.out.printf("File %s was deleted...", filename);
	            } else {
	                System.out.println("No file was deleted...");
	            }

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
