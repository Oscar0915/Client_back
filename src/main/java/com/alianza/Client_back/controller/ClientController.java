package com.alianza.Client_back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alianza.Client_back.dto.ContractRequest;
import com.alianza.Client_back.dto.ContractResponse;
import com.alianza.Client_back.dto.client.MOClient;
import com.alianza.Client_back.entity.Client;
import com.alianza.Client_back.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Operaciones relacionadas con clientes")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/save")
    @Operation(summary = "Guardar un nuevo cliente")
    public ResponseEntity<ContractResponse<Client>> saveClient(@RequestBody ContractRequest<MOClient> clientRequest) {
        ContractResponse<Client> response = clientService.saveClient(clientRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar cliente por sharedKey")
    public ResponseEntity<ContractResponse<Boolean>> deleteClient(
            @RequestBody ContractRequest<String> sharedKeyRequest) {
        ContractResponse<Boolean> response = clientService.deleteClient(sharedKeyRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/find")
    @Operation(summary = "Buscar cliente por sharedKey")
    public ResponseEntity<ContractResponse<Client>> getClientBySharedKey(
            @RequestBody ContractRequest<String> sharedKeyRequest) {
        ContractResponse<Client> response = clientService.getClientBySharedKey(sharedKeyRequest);
        return ResponseEntity.ok(response);
        
    }

    @GetMapping
    @Operation(summary = "Obtener todos los clientes")
    public ContractResponse<List<Client>> getAllClients() {
        ContractResponse<List<Client>> response = clientService.getAllClients();
        return response;
    }



   @GetMapping("/export")
    public ContractResponse<String> getClientsCsvAsBase64() throws Exception {
        ContractResponse<String> response = clientService.getCsvFileAsBase64();
        return response;
    }

}
