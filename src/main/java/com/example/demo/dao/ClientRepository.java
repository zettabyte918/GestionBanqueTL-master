package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Client;

public interface ClientRepository extends JpaRepository<Client,Long>{
	/*@Query("SELECT c FROM Client c WHERE c.code = :code")
    Client findByCode(@Param("code") Long code);*/
	Client findByCode(@Param("code") Long code);
	void deleteByCode(@Param("code") Long code);
	
}
