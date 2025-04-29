package com.alianza.Client_back.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alianza.Client_back.dto.ContractRequest;
import com.alianza.Client_back.dto.ContractResponse;
import com.alianza.Client_back.entity.Client;
import com.alianza.Client_back.service.ClientService;
import com.alianza.Client_back.service.implementacion.BatchJobService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Operaciones relacionadas con clientes")
public class ClientController {

    private final ClientService clientService;
    private final BatchJobService batchJobService;

    public ClientController(ClientService clientService, BatchJobService batchJobService) {
        this.clientService = clientService;
        this.batchJobService = batchJobService;
    }

   @PostMapping("/save")
    @Operation(summary = "Guardar un nuevo cliente")
    public ResponseEntity<ContractResponse<Client>> saveClient(@RequestBody ContractRequest<Client> clientRequest) {
        ContractResponse<Client> response = clientService.saveClient(clientRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar cliente por sharedKey")
    public ResponseEntity<ContractResponse<Boolean>> deleteClient(@RequestBody ContractRequest<String> sharedKeyRequest) {
        ContractResponse<Boolean> response = clientService.deleteClient(sharedKeyRequest);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/find")
    @Operation(summary = "Buscar cliente por sharedKey")
    public ResponseEntity<ContractResponse<Client>> getClientBySharedKey(@RequestBody ContractRequest<String> sharedKeyRequest) {
        ContractResponse<Client> response = clientService.getClientBySharedKey(sharedKeyRequest);
        return ResponseEntity.ok(response);
    }

   @GetMapping
   public ResponseEntity<ContractResponse<List<Client>>> getAllClients() {
    
       ContractResponse<List<Client>> response = clientService.getAllClients();
    
       if ("success".equals(response.getStatus())) {
           return ResponseEntity.ok(response); 
       } else if ("warning".equals(response.getStatus())) {
           return ResponseEntity.status(204).body(response); 
       } else {
           return ResponseEntity.status(500).body(response); 
       }
   }


    @PostMapping("/export")
    public ResponseEntity<String> exportClients() {
        try {
            batchJobService.runExportJob();
            return ResponseEntity.ok("Exportación exitosa");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error durante la exportación");
        }
    }
}
