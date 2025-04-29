package com.alianza.Client_back.service.implementacion;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alianza.Client_back.repository.ClientRepository;
import com.alianza.Client_back.service.ClientService;
import com.alianza.Client_back.specification.ClientSpecification;
import com.alianza.Client_back.utils.ClientKeyUtil;
import com.alianza.Client_back.dto.ContractRequest;
import com.alianza.Client_back.dto.ContractResponse;
import com.alianza.Client_back.dto.client.MOClient;
import com.alianza.Client_back.dto.client.SearchClientRequest;
import com.alianza.Client_back.dto.errors.ErrorMessages;
import com.alianza.Client_back.entity.Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ContractResponse<Client> saveClient(ContractRequest<MOClient> clientRequest) {
        ContractResponse<Client> response = new ContractResponse<>();
        Client vClient = new Client();
        try {
            if (clientRequest == null || clientRequest.getData() == null) {
                response.setErrorMessage(ErrorMessages.MISSING_REQUIRED_FIELDS);
                response.setStatus("error");
                response.setData(null);
                return response;
            }

            MOClient client = clientRequest.getData();

            if (  client.getBusinessId() == null || client.getBusinessId().isEmpty() ||
                    client.getEmail() == null || client.getEmail().isEmpty()) {

                response.setErrorMessage(ErrorMessages.INVALID_CLIENT_DATA);
                response.setStatus("error");
                response.setData(null);
                return response;
            }

            vClient.setSharedKey(ClientKeyUtil.generateSharedKey(client.getBusinessId()));
            vClient.setBusinessId(client.getBusinessId());
            vClient.setEmail(client.getEmail());
            vClient.setPhoneNumber(client.getPhoneNumber());
            vClient.setEndDate(client.getEndDate());
            vClient.setStartDate(client.getStartDate());
            vClient.setCreatedAt(LocalDateTime.now());

        

            if (clientRepository.existsById(vClient.getSharedKey())) {
                response.setErrorMessage(ErrorMessages.CLIENT_ALREADY_EXISTS);
                response.setStatus("error");
                response.setData(null);
                return response;
            }

            Client savedClient = clientRepository.save(vClient);

            response.setSuccessMessage("Cliente guardado exitosamente.");
            response.setStatus("success");
            response.setData(savedClient);

        } catch (Exception ex) {
            response.setErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR + " Detalles: " + ex.getMessage());
            response.setStatus("error");
            response.setData(null);
        }

        return response;
    }


    @Override
    public ContractResponse<Client> getClientBySharedKey(ContractRequest<String> sharedKeyRequest) {
        ContractResponse<Client> response = new ContractResponse<>();

        try {
            if (sharedKeyRequest == null || sharedKeyRequest.getData() == null
                    || sharedKeyRequest.getData().isEmpty()) {
                response.setErrorMessage(ErrorMessages.MISSING_REQUIRED_FIELDS);
                response.setStatus("error");
                return response;
            }

            Optional<Client> optionalClient = clientRepository.findById(sharedKeyRequest.getData());

            if (optionalClient.isPresent()) {
                response.setSuccessMessage("Cliente encontrado.");
                response.setStatus("success");
                response.setData(optionalClient.get());
            } else {
                response.setErrorMessage(ErrorMessages.CLIENT_NOT_FOUND);
                response.setStatus("error");
            }

        } catch (Exception ex) {
            response.setErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR + " Detalles: " + ex.getMessage());
            response.setStatus("error");
        }

        return response;
    }


    @Override
    public ContractResponse<List<Client>> getAllClients() {
        ContractResponse<List<Client>> response = new ContractResponse<>();
        
        try {
            List<Client> clients = clientRepository.findAll();
    
            if (clients.isEmpty()) {
                response.setStatus("warning");
                response.setErrorMessage(ErrorMessages.CLIENT_NOT_FOUND);
                response.setData(null);  
            } else {
                
                response.setStatus("success");
                response.setSuccessMessage("Clientes obtenidos exitosamente.");
                response.setData(clients);  
            }
    
        } catch (Exception ex) {
           
            response.setStatus("error");
            response.setErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR + " Detalles: " + ex.getMessage());
            response.setData(null);
        }
    
        return response;
    }


    private File generateCsvFile() throws IOException {
        List<Client> clients = clientRepository.findAll();

        File directory = new File("files");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File csvFile = new File(directory, "clients.csv");

        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(csvFile), StandardCharsets.UTF_8))) {

            writer.println("sharedKey,businessId,email,phoneNumber,createdAt,startDate,endDate");

            for (Client client : clients) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s%n",
                        client.getSharedKey(),
                        client.getBusinessId(),
                        client.getEmail(),
                        client.getPhoneNumber(),
                        client.getCreatedAt(),
                        client.getStartDate(),
                        client.getEndDate()
                );
            }
        }

        return csvFile;
    }

    public ContractResponse<String> getCsvFileAsBase64() throws IOException {
        ContractResponse<String> response = new ContractResponse<>();   
        File csvFile = generateCsvFile();
        byte[] fileContent = Files.readAllBytes(csvFile.toPath());
        response.setData(Base64.getEncoder().encodeToString(fileContent));
        return response;
    }
    

    public ContractResponse<List<Client>> searchClients(ContractRequest<SearchClientRequest> request) {
        ContractResponse<List<Client>> response = new ContractResponse<>();

        try {
            SearchClientRequest searchRequest = request.getData();
            Specification<Client> specification = Specification.where(null);

            if (searchRequest.getBusinessId() != null && !searchRequest.getBusinessId().isEmpty()) {
                specification = specification.and(ClientSpecification.hasBusinessId(searchRequest.getBusinessId()));
            }
            if (searchRequest.getEmail() != null && !searchRequest.getEmail().isEmpty()) {
                specification = specification.and(ClientSpecification.hasEmail(searchRequest.getEmail()));
            }
            if (searchRequest.getStartDate() != null) {
                specification = specification.and(ClientSpecification.hasStartDate(searchRequest.getStartDate()));
            }
            if (searchRequest.getEndDate() != null) {
                specification = specification.and(ClientSpecification.hasEndDate(searchRequest.getEndDate()));
            }

            List<Client> clients = clientRepository.findAll(specification);

            if (clients.isEmpty()) {
                response.setStatus("error");
                response.setErrorMessage("No hay clientes con esos criterios de búsqueda.");
                response.setData(null);
            } else {
                response.setStatus("success");
                response.setData(clients);
            }
        } catch (Exception ex) {
            response.setErrorMessage("Internal Server Error: " + ex.getMessage());
            response.setStatus("error");
            response.setData(null);
        }

        return response;
    }

}
