package com.alianza.Client_back.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alianza.Client_back.dto.ContractRequest;
import com.alianza.Client_back.dto.ContractResponse;
import com.alianza.Client_back.dto.client.MOClient;
import com.alianza.Client_back.dto.client.SearchClientRequest;
import com.alianza.Client_back.entity.Client;
import com.alianza.Client_back.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Operaciones relacionadas con clientes")
public class ClientController {

    private final ClientService clientService;
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/save")
    @Operation(summary = "Guardar un nuevo cliente")
    public ResponseEntity<ContractResponse<Client>> saveClient(@RequestBody ContractRequest<MOClient> clientRequest) {
        logger.info("saveClient - Datos recibidos: {}", clientRequest);
        ContractResponse<Client> response = clientService.saveClient(clientRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/find")
    @Operation(summary = "Buscar cliente por sharedKey")
    public ResponseEntity<ContractResponse<Client>> getClientBySharedKey(
            @RequestBody ContractRequest<String> sharedKeyRequest) {

        ContractResponse<Client> response = clientService.getClientBySharedKey(sharedKeyRequest);
        logger.info("getClientBySharedKey - Datos devueltos: {}", response);
        return ResponseEntity.ok(response);

    }

    @GetMapping
    @Operation(summary = "Obtener todos los clientes")
    public ContractResponse<List<Client>> getAllClients() {
        ContractResponse<List<Client>> response = clientService.getAllClients();
        logger.info("getAllClients - Datos devueltos: {}", response);
        return response;
    }

    @GetMapping("/export")
    @Operation(summary = "Exportar listado para csv en base64 ")
    public ContractResponse<String> getClientsCsvAsBase64() throws Exception {
        ContractResponse<String> response = clientService.getCsvFileAsBase64();
        logger.info("getClientsCsvAsBase64 - Datos devueltos: {}", response);
        return response;
    }

    @PostMapping("/search")
    @Operation(summary = "Buscar clientes por criterios de búsqueda")
    public ResponseEntity<ContractResponse<List<Client>>> searchClients(
            @RequestBody ContractRequest<SearchClientRequest> request) {

        ContractResponse<List<Client>> response = clientService.searchClients(request);

        if ("success".equalsIgnoreCase(response.getStatus())) {
            logger.info("searchClients - Datos devueltos: {}", response);
            return ResponseEntity.ok(response);
        } else {
            logger.warn("searchClients - Error: {}", response);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
