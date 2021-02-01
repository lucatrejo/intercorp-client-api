package com.intercorp.client.controller;

import com.intercorp.client.domain.Client;
import com.intercorp.client.dto.ClientRequest;
import com.intercorp.client.dto.ClientResponse;
import com.intercorp.client.dto.KpiResponse;
import com.intercorp.client.service.ClientService;
import com.intercorp.client.service.IService;
import com.intercorp.client.utils.Constants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private IService clientService;
    private ModelMapper modelMapper;

    @Autowired
    public ClientController(IService clientService, ModelMapper modelMapper) {
        this.clientService = clientService;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(
            value = "Insert Client",
            response = ClientResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request")
    })
    @PostMapping
    public ResponseEntity createClient(@Valid @RequestBody ClientRequest clientRequest) {
        Client client = modelMapper.map(clientRequest, Client.class);
        Client clientStored = clientService.save(client);
        ClientResponse clientResponse = modelMapper.map(clientStored, ClientResponse.class);
        return ResponseEntity.ok(clientResponse);
    }

    @ApiOperation(
            value = "Returns average age and standard deviation of all clients",
            response = KpiResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = ""),
            @ApiResponse(code = SC_NO_CONTENT, message = "No clients found")
    })
    @GetMapping("/kpi")
    public ResponseEntity getKpi() {
        Map<String, Double> calculatedKpi = clientService.getKpi();

        if (calculatedKpi.get(Constants.KPI_AVERAGE_KEY) == null && calculatedKpi.get(Constants.KPI_DEVIATION_KEY) == null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(KpiResponse.from(calculatedKpi));
    }

    @ApiOperation(
            value = "List all clients with estimated date of death",
            response = ClientResponse.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Success"),
            @ApiResponse(code = SC_NO_CONTENT, message = "No clients stored")
    })
    @GetMapping
    public ResponseEntity getClients() {
        List<Client> clients = clientService.listAll();

        if (clients.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(modelMapper.map(clients, new TypeToken<List<ClientResponse>>() {}.getType()));
    }


}
