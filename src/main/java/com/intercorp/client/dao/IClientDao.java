package com.intercorp.client.dao;

import com.intercorp.client.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClientDao extends JpaRepository<Client, Long> { }
