package com.asn.ftpclient.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.asn.ftpclient.ftpServices.ClientServices;
import com.asn.ftpclient.ftpServices.FTPServices;
import com.asn.ftpclient.ftpServices.ServiceExc;
import com.asn.ftpclient.ftpServices.UserServices;
import com.asn.ftpclient.model.Client;
import com.asn.ftpclient.model.Items;
import com.asn.ftpclient.model.User;
import com.asn.ftpclient.util.Compactador;

@Controller
public class HomeController {
	@Autowired
	FTPServices ftpServices;

	@Autowired
	UserServices userServices;

	@Autowired
	ClientServices clientServices;

	static final int TAMANHO_BUFFER = 4096;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpSession session) {
		if (session.getAttribute("userLogged") == null) {
			return "views/index";
		}
		return "redirect:/cnpjlist";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String login(Model model, @RequestParam("user") String user, @RequestParam("password") String password,
			HttpSession session) throws ServiceExc, Exception {
		User login = userServices.login(user, password);
		if (login == null) {
			model.addAttribute("erro", "Usuário ou senhas incorretas");
			return "views/index";
		}
		session.setAttribute("userLogged", login);
		return "redirect:/cnpjlist";
	}

//	@RequestMapping(value = "/register", method = RequestMethod.GET)
//	public String register(HttpSession session) {
//		if (session.getAttribute("userLogged") == null) {
//			return "views/register";
//		}
//		return "redirect:/cnpjlist";
//	}
//
//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	public String register(Model model, User user) {
//		try {
//			User register = userServices.register(user);
//			if (register == null) {
//				model.addAttribute("erro", "Usuário já cadastrado");
//				return "views/register";
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "redirect:/";
//	}

	@RequestMapping(value = "/cnpjlist", method = RequestMethod.GET)
	public String listCNPJ(Model model, HttpSession session) {
		if (session.getAttribute("userLogged") == null) {
			return "redirect:/";
		}
		Object user = session.getAttribute("userLogged");
		Long codigo = ((User) user).getCodigo();
		List<Client> listClient = clientServices.listClientByCounter(codigo);
		model.addAttribute("clientLink", listClient);
		return "views/listcnpj";
	}

	@RequestMapping(value = "/folder", method = RequestMethod.GET)
	public String listFTP(Model model, String folder, HttpSession session) {
		if (session.getAttribute("userLogged") == null || folder == "") {
			return "redirect:/";
		}
		Object user = session.getAttribute("userLogged");
		Long codigo = ((User) user).getCodigo();
		List<Client> listClient = clientServices.listClientByCounter(codigo);
		ArrayList<Items> items = ftpServices.listFTP(folder);
		Boolean cnpjIsCount = false;
		for (int i = 0; i < listClient.size(); i++) {
			if (folder.split("/")[0].equals("")) {
				if (listClient.get(i).getCpfcnpj().equals(folder.split("/")[1])) {
					cnpjIsCount = true;
				}
			} else {
				if (listClient.get(i).getCpfcnpj().equals(folder.split("/")[0])) {
					cnpjIsCount = true;
				}
			}
		}
		if (!cnpjIsCount) {
			return "redirect:/";
		}
		model.addAttribute("files", items);
		model.addAttribute("directory", folder);
		return "views/listftp";
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public HttpEntity<byte[]> downloadFTPFile(Model model, String filedownload, HttpSession session,
			HttpServletResponse response) throws Exception {
		if (session.getAttribute("userLogged") == null) {
			return null;
		}
		String file = ftpServices.downloadFileFtp(filedownload, null);
		HttpHeaders httpHeaders = new HttpHeaders();
		String[] fileTrue = filedownload.split("/");
		String fileName = fileTrue[fileTrue.length - 1];
		if (file == null) {
			String tempLocale = System.getProperty("java.io.tmpdir");
			String directoryTempDownload = tempLocale + "localetempdownloadftpasn";

			Compactador.zipFolder((directoryTempDownload + filedownload),
					(directoryTempDownload + filedownload + ".zip"));
			byte[] zipFile = Files
					.readAllBytes(Paths.get(new File(directoryTempDownload + filedownload + ".zip").toURI()));
			httpHeaders.add("Content-Disposition", "attachment;filename=\"" + fileName + ".zip");
			HttpEntity<byte[]> entity = new HttpEntity<byte[]>(zipFile, httpHeaders);
			return entity;
		}
		File fileDirectory = new File(file);
		byte[] arquivo = Files.readAllBytes(Paths.get(fileDirectory.toURI()));
		httpHeaders.add("Content-Disposition", "attachment;filename=\"" + fileName);
		HttpEntity<byte[]> entity = new HttpEntity<byte[]>(arquivo, httpHeaders);
		return entity;
	}

	@RequestMapping(value = "/downloadfolder", method = RequestMethod.GET)
	public String downloadFouder(HttpSession session) {
		if (session.getAttribute("userLogged") == null) {
			return "redirect:/cnpjlist";
		}
		return "views/downloadfolder";
	}

//	@RequestMapping(value = "/registercnpj", method = RequestMethod.GET)
//	public String registerCNPJViewer(HttpSession session) {
//		if (session.getAttribute("userLogged") == null) {
//			return "redirect:/";
//		}
//		return "views/registercnpj";
//	}
//	
//	@RequestMapping(value = "/registercnpj", method = RequestMethod.POST)
//	public String registerCNPJ(String cnpj, HttpSession session) {
//		if (session.getAttribute("userLogged") == null) {
//			return "redirect:/";
//		}
//		Object user = session.getAttribute("userLogged");
//		Long codigo = ((User) user).getCodigo();
//		((User) user).setCnpj(cnpj);
//		userServices.editCNPJ(codigo, cnpj);
//		session.setAttribute("userLogged", user);
//		return "views/registercnpj";
//	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
