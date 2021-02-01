package com.intercorp.client.service;

import com.intercorp.client.dao.IClientDao;
import com.intercorp.client.domain.Client;
import com.intercorp.client.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class ClientsServiceTest {

    @InjectMocks
    ClientService clientsService;
    @Mock
    IClientDao clientsDao;


    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void list_all_ok() {
        List<Client> clientsWithoutDeathDate = Collections.singletonList(
                Client.builder()
                        .name("name")
                        .lastName("last_name")
                        .age(31)
                        .birthDate(LocalDate.of(1989, 7, 9))
                        .build());
        when(clientsDao.findAll()).thenReturn(clientsWithoutDeathDate);
        assertNull(clientsWithoutDeathDate.get(0).getProbablyDeathDate());
        List<Client> clients = clientsService.listAll();
        assertNotNull(clients.get(0).getProbablyDeathDate());
    }

    @Test
    void list_all_empty() {
        when(clientsDao.findAll()).thenReturn(Collections.emptyList());
        Assertions.assertEquals(Collections.EMPTY_LIST, clientsService.listAll());
    }

    @Test
    void get_kpi_ok(){
        Client clientOne = Client.builder()
                .name("name")
                .lastName("last_name")
                .age(31)
                .birthDate(LocalDate.of(1989,9,07))
                .build();
        when(clientsDao.findAll()).thenReturn(Collections.singletonList(clientOne));
        assertEquals(31.0, clientsService.getKpi().get(Constants.KPI_AVERAGE_KEY));
        assertEquals(0.0, clientsService.getKpi().get(Constants.KPI_DEVIATION_KEY));
    }

}
