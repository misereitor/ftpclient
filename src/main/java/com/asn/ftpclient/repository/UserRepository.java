package com.asn.ftpclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.asn.ftpclient.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long > {
	
	@Query("select i from User i where i.nome = :nome")
	public User findByUsers(String nome);
	
	@Query(value = "select * from contadorusuario where nome = :nome and senha = :senha",
			nativeQuery = true)
	public User searchUser(String nome, String senha);
	
	@Query(value = "select * from contadorusuario where token = :token",
			nativeQuery = true)
	public User findByToken(String token);
	
}
