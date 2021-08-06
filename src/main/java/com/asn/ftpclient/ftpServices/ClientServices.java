package com.asn.ftpclient.ftpServices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asn.ftpclient.model.Client;
import com.asn.ftpclient.repository.ClientRepository;

@Service
public class ClientServices {
	@Autowired
	ClientRepository clientRepository;
	
	public List<Client> listClientByCounter(Long contador) {
		List<Client> listClient = clientRepository.findAllByCounter(contador);
		return listClient;
	}
}
