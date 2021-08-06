package com.asn.ftpclient.ftpServices;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asn.ftpclient.Exception.CriptoExistException;
import com.asn.ftpclient.model.User;
import com.asn.ftpclient.repository.UserRepository;
import com.asn.ftpclient.util.Util;

@Service
public class UserServices {

	@Autowired
	UserRepository userRepository;
	
	public User login(String nome, String senha) throws ServiceExc, Exception {
		byte[] array = new byte[7];
		new Random().nextBytes(array);
		User userLogin = userRepository.searchUser(nome, Util.md5(senha));
		if (userLogin == null) {
			return null;
		}
		userRepository.save(userLogin);
		return userLogin;
	}
	
	public User register(User user) throws Exception {
		byte[] array = new byte[7];
		new Random().nextBytes(array);
		try {
			if (userRepository.findByUsers(user.getNome()) != null) {
				return null;
			}
			user.setSenha(Util.md5(user.getSenha()));
		} catch (NoSuchAlgorithmException e) {
			throw new CriptoExistException("Erro na criptografia da senha");
		}
		return userRepository.save(user);
	}
	
//	public void editCNPJ(Long codigo, String cnpj) {
//		userRepository.saveCNPJ(codigo, cnpj);
//	}
	
	public Boolean logged(String token) {
		if (userRepository.findByToken(token) != null) {
			return true;
		} else {
			return false;
		}
	}
}
