package com.intercorp.client.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.intercorp.client.domain.Client;
import com.intercorp.client.dto.ClientRequest;
import com.intercorp.client.service.IService;
import com.intercorp.client.utils.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ClientControllerTest {
    private static final String CLIENTS_PATH = "/api/client";
    private static final String CLIENTS_KPI_PATH = "/api/client/kpi";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Mock
    IService clientsService;
    @Spy
    ModelMapper modelMapper;
    @InjectMocks
    private ClientController clientController;
    private MockMvc mock;

    @BeforeAll
    static void setUpForAllTests() {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @BeforeEach
    void setUp() {
        initMocks(this);
        mock = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    void put_clients_not_allowed_method() throws Exception {
        mock.perform(put(CLIENTS_PATH)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void post_clients_invalid_json() throws Exception {
        mock.perform(post(CLIENTS_PATH)
                .content("invalid_json")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void post_clients_with_null_values() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setLastName("name");
        clientRequest.setName("last_name");
        clientRequest.setAge(31);
        mock.perform(post(CLIENTS_PATH)
                .content(OBJECT_MAPPER.writeValueAsString(clientRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void post_clients_invalid_age() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("name");
        clientRequest.setLastName("last_name");
        clientRequest.setAge(-31);
        clientRequest.setBirthDate(Instant.now());
        mock.perform(post(CLIENTS_PATH)
                .content(OBJECT_MAPPER.writeValueAsString(clientRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void post_clients_invalid_birthdate() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("name");
        clientRequest.setLastName("last_name");
        clientRequest.setBirthDate(Instant.ofEpochMilli(652676400000L));
        ZonedDateTime dateTime = clientRequest.getBirthDate().atZone(ZoneOffset.UTC);
        int currentYear = LocalDate.now().getYear();
        clientRequest.setAge(currentYear - dateTime.getYear() + 1);
        mock.perform(post(CLIENTS_PATH)
                .content(OBJECT_MAPPER.writeValueAsString(clientRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void post_clients_ok() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setName("name");
        clientRequest.setLastName("last_name");
        clientRequest.setBirthDate(Instant.ofEpochMilli(652676400000L));
        ZonedDateTime dateTime = clientRequest.getBirthDate().atZone(ZoneOffset.UTC);
        int currentYear = LocalDate.now().getYear();
        clientRequest.setAge(currentYear - dateTime.getYear() - 1);
        when(clientsService.save(any(Client.class))).thenReturn(new Client());
        mock.perform(post(CLIENTS_PATH)
                .content(OBJECT_MAPPER.writeValueAsString(clientRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk());
    }

    @Test
    void get_clients_no_records() throws Exception {
        when(clientsService.listAll()).thenReturn(Collections.emptyList());
        mock.perform(get(CLIENTS_PATH))
                .andExpect(status().isNoContent());
    }

    @Test
    void get_clients_ok() throws Exception {
        Client clientOne = Client.builder()
                .id(1L)
                .name("name")
                .lastName("last_name")
                .age(31)
                .birthDate(LocalDate.of(1989, 11, 10))
                .build();
        Client clientTwo = Client.builder()
                .id(2L)
                .name("name")
                .lastName("last_name")
                .age(28)
                .birthDate(LocalDate.of(1992, 7, 9))
                .build();
        when(clientsService.listAll()).thenReturn(Arrays.asList(clientOne, clientTwo));
        mock.perform(get(CLIENTS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("name")))
                .andExpect(jsonPath("$[0].last_name", is("last_name")))
                .andExpect(jsonPath("$[0].age", is(31)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("name")))
                .andExpect(jsonPath("$[1].last_name", is("last_name")))
                .andExpect(jsonPath("$[1].age", is(28)));

    }

    @Test
    void get_kpi_no_clients() throws Exception {
        when(clientsService.getKpi()).thenReturn(new HashMap());
        mock.perform(get(CLIENTS_KPI_PATH))
                .andExpect(status().isNoContent());
    }

    @Test
    void get_kpi_ok() throws Exception {
        Map<String, Double> kpiMap = new HashMap<>();
        kpiMap.put(Constants.KPI_AVERAGE_KEY, 20.5);
        kpiMap.put(Constants.KPI_DEVIATION_KEY, 2.5);
        when(clientsService.getKpi()).thenReturn(kpiMap);
        mock.perform(get(CLIENTS_KPI_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.average_age", is(20.5)))
                .andExpect(jsonPath("$.standard_deviation", is(2.5)));
    }

}
