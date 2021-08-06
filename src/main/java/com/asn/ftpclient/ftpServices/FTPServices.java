package com.asn.ftpclient.ftpServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Service;

import com.asn.ftpclient.model.Items;

@Service
public class FTPServices {

	FTPClient client = new FTPClient();

	private String urlFTP = "ftp.asnsoftware.com.br";

	private String userFTP = "asnsoftware3-portal_contador@lwftp";

	private String passwordFTP = "portal_contador@CONTADOR10";

	public Boolean conect(String user, String password) {
		try {
			client.connect(urlFTP);
			client.enterLocalPassiveMode();
			boolean login = client.login(userFTP, passwordFTP);
			client.logout();
			return login;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public ArrayList<Items> listFTP(String locale) {
		try {
			ArrayList<Items> items = new ArrayList<Items>();
			client.connect(urlFTP);
			client.enterLocalPassiveMode();
			client.login(userFTP, passwordFTP);
			if (client.isConnected()) {
				if (!client.changeWorkingDirectory(locale)) {
					return null;
				}
				client.cwd(locale);
				FTPFile[] ftpFiles = client.listFiles();
				for (FTPFile ftpFile : ftpFiles) {
					if (ftpFile.getType() == FTPFile.FILE_TYPE) {
						Items item = new Items(ftpFile.getName(), FileUtils.byteCountToDisplaySize(ftpFile.getSize()),
								ftpFile.getType(), client.printWorkingDirectory());
						items.add(item);
					} else {
						Items item = new Items(ftpFile.getName(), "null", ftpFile.getType(),
								client.printWorkingDirectory());
						items.add(item);
					}
				}
				return items;

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
		return null;
	}

	public String downloadFileFtp(String fileDownload, String newDirPath) throws IOException {
		String tempLocale = System.getProperty("java.io.tmpdir");
		String directoryTempDownload = tempLocale + "localetempdownloadftpasn";
		File directoriIsExist = new File(directoryTempDownload);
		@SuppressWarnings("unused")
		Path file = null;
		if (!directoriIsExist.exists()) {
			file = Files.createDirectory(Paths.get(directoryTempDownload));
		}
		FileUtils.forceDeleteOnExit(new File(directoryTempDownload + fileDownload));
		String[] fileTrue = fileDownload.split("/");
		String fileName = fileTrue[fileTrue.length - 1];
		FTPClient client = new FTPClient();
		try (OutputStream os = new FileOutputStream(tempLocale + "localetempdownloadftpasn\\" + fileName)) {
			client.connect(urlFTP);
			client.enterLocalPassiveMode();
			client.login(userFTP, passwordFTP);
			if (client.changeWorkingDirectory(fileDownload)) {
				if (new File(directoriIsExist + fileDownload).exists()) {
					FileUtils.deleteDirectory(new File(directoryTempDownload + fileDownload));
				}
				downloadDirectory(client, fileDownload, "", ((directoriIsExist + fileDownload).toString()));
				return null;
			} else {
				if (newDirPath != null) {
					try (OutputStream oss = new FileOutputStream(newDirPath)) {
						client.retrieveFile(fileDownload, oss);
					}
				} else {
					client.retrieveFile(fileDownload, os);
				}
			}
			String ark = (directoryTempDownload + "\\" + fileName);
			return ark;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void downloadDirectory(FTPClient ftpClient, String parentDir, String currentDir, String saveDir)
			throws IOException {
		String dirToList = parentDir;
		if (!currentDir.equals("")) {
			dirToList += "/" + currentDir;
		}

		FTPFile[] subFiles = ftpClient.listFiles(dirToList);

		if (subFiles != null && subFiles.length > 0) {
			for (FTPFile aFile : subFiles) {
				String currentFileName = aFile.getName();
				if (currentFileName.equals(".") || currentFileName.equals("..")) {
					continue;
				}
				String filePath = parentDir + "/" + currentDir + "/" + currentFileName;
				if (currentDir.equals("")) {
					filePath = parentDir + "/" + currentFileName;
				}

				String newDirPath = saveDir + parentDir + File.separator + currentDir + File.separator
						+ currentFileName;
				if (currentDir.equals("")) {
					newDirPath = saveDir + parentDir + File.separator + currentFileName;
				}

				if (aFile.isDirectory()) {
					File newDir = new File(newDirPath);
					newDir.mkdirs();
					downloadDirectory(ftpClient, dirToList, currentFileName, saveDir);
				} else {
					downloadFileFtp(filePath, newDirPath);
				}
			}
		}
	}
}
