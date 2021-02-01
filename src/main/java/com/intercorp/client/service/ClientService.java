package com.intercorp.client.service;

import com.intercorp.client.dao.IClientDao;
import com.intercorp.client.domain.Client;
import com.intercorp.client.utils.Constants;
import com.intercorp.client.utils.Helper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientService implements IService {

    private IClientDao clientsDao;

    public ClientService(IClientDao clientDao) {
        this.clientsDao = clientDao;
    }

    @Override
    public Client save(Client client) {
        return clientsDao.save(client);
    }

    @Override
    public List<Client> listAll() {
        List<Client> clients = clientsDao.findAll();
        clients.forEach(client -> client.setProbablyDeathDate(calculateDateOfDeath(client.getBirthDate())));
        return clients;
    }

    @Override
    public Map<String, Double> getKpi() {
        final List<Client> clients = clientsDao.findAll();

        Map<String, Double> kpi = new HashMap<>();
        List<Integer> ages = clients.stream().mapToInt(Client::getAge).boxed().collect(Collectors.toList());

        double average = Helper.getAverage(ages);
        double deviation = Helper.getStandardDeviation(ages);

        kpi.put(Constants.KPI_AVERAGE_KEY, average);
        kpi.put(Constants.KPI_DEVIATION_KEY, deviation);

        return kpi;
    }



    private LocalDate calculateDateOfDeath(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        final int plus = Math.abs(Constants.AVERAGE_AGE_OF_DEATH - Period.between(birthDate, now).getYears());
        return now.plusDays(plus).plusYears(plus);
    }
}
