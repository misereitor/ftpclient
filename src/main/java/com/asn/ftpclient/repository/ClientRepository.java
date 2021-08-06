package com.asn.ftpclient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.asn.ftpclient.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	
	@Query(value = "select * from cliente where contador = :contador",
			nativeQuery = true)
	List<Client> findAllByCounter(Long contador);

}
