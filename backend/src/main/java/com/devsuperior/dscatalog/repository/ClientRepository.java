package com.devsuperior.dscatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dscatalog.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
