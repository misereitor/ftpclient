package com.asn.ftpclient.ftpServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.net.ftp.FTPClient;

public class FTPDownload {
    public static void main(String[] args) throws IOException {
        String filename = "Nova pasta/SGI.jar";
        String tempLocale = System.getProperty("java.io.tmpdir");
        String directoryTempDownload = tempLocale + "localetempdownloadftpasn";
        File directoriIsExist = new File(directoryTempDownload);
        @SuppressWarnings("unused")
		Path file = null;
        if (!directoriIsExist.exists()) {
        	file = Files.createDirectory(Paths.get(directoryTempDownload));        	
        }
        FTPClient client = new FTPClient();
        try (OutputStream os = new FileOutputStream(tempLocale+ "localetempdownloadftpasn\\" + filename)) {
            client.connect("ftp.asnsoftware.com.br");
            client.enterLocalPassiveMode();
            client.login("asnsoftware3-portal_contador@lwftp", "portal_contador@CONTADOR10");

            boolean status = client.retrieveFile(filename, os);
            System.out.println("status = " + status);
            System.out.println("reply  = " + client.getReplyString());
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