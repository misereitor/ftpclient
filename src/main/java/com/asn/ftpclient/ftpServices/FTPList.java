package com.asn.ftpclient.ftpServices;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FTPList {
	public static void main(String[] args) {
        FTPClient client = new FTPClient();

        try {
            client.connect("ftp.asnsoftware.com.br");
            client.enterLocalPassiveMode();
            client.login("asnsoftware3-portal_contador@lwftp", "portal_contador@CONTADOR10");

            if (client.isConnected()) {
                // Obtain a list of filenames in the current working
                // directory. When no file found an empty array will
                // be returned.
                String[] names = client.listNames();
                for (String name : names) {
                    System.out.println("Name = " + name);
                }
                FTPFile[] ftpFiles = client.listFiles();
                for (FTPFile ftpFile : ftpFiles) {
                    // Check if FTPFile is a regular file
                	System.out.println(ftpFile.isDirectory());
                    if (ftpFile.getType() == FTPFile.FILE_TYPE) {
                        System.out.printf("FTPFile: %s; %s%n",
                            ftpFile.getName(),
                            FileUtils.byteCountToDisplaySize(ftpFile.getSize()));
                    }
                }
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
