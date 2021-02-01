package com.intercorp.client.service;

import com.intercorp.client.domain.Client;

import java.util.List;
import java.util.Map;

public interface IService {
    Client save(Client client);

    List<Client> listAll();

    Map<String, Double> getKpi();
}
