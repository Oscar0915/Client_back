package com.alianza.Client_back.service.implementacion;

import org.springframework.stereotype.Service;

import com.alianza.Client_back.repository.ClientRepository;
import com.alianza.Client_back.service.ClientService;
import com.alianza.Client_back.utils.ClientKeyUtil;
import com.alianza.Client_back.dto.ContractRequest;
import com.alianza.Client_back.dto.ContractResponse;
import com.alianza.Client_back.dto.client.MOClient;
import com.alianza.Client_back.dto.errors.ErrorMessages;
import com.alianza.Client_back.entity.Client;

import java.io.ByteArrayOutputStream;
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
        int vAttempt = 1;
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

            vClient.setSharedKey(ClientKeyUtil.generateSharedKey(client.getBusinessId(), vAttempt));
            vClient.setBusinessId(client.getBusinessId());
            vClient.setEmail(client.getEmail());
            vClient.setPhoneNumber(client.getPhoneNumber());
            vClient.setEndDate(client.getEndDate());
            vClient.setStartDate(client.getStartDate());
            vClient.setCreatedAt(LocalDateTime.now());

            while (clientRepository.existsById(vClient.getSharedKey())) {
                vClient.setSharedKey(ClientKeyUtil.generateSharedKey(client.getBusinessId(), ++vAttempt));
            }

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

    public ContractResponse<Boolean> deleteClient(String sharedKey) {
        ContractResponse<Boolean> response = new ContractResponse<Boolean>();
        try {
            Optional<Client> client = clientRepository.findById(sharedKey);

            if (client.isPresent()) {
                clientRepository.deleteById(sharedKey);
                response.setSuccessMessage("Cliente eliminado exitosamente.");
                response.setStatus("success");
                response.setData(null);
            } else {
                response.setErrorMessage(ErrorMessages.CLIENT_NOT_FOUND);
                response.setStatus("error");
                response.setData(null);
            }

        } catch (Exception e) {
            response.setErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR);
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
    public ContractResponse<Boolean> deleteClient(ContractRequest<String> sharedKeyRequest) {
        ContractResponse<Boolean> response = new ContractResponse<>();

        try {
            String sharedKey = sharedKeyRequest.getData();

            if (sharedKey == null || sharedKey.isEmpty()) {
                response.setErrorMessage(ErrorMessages.MISSING_REQUIRED_FIELDS);
                response.setStatus("error");
                response.setData(false);
                return response;
            }

            Optional<Client> clientOpt = clientRepository.findById(sharedKey);
            if (clientOpt.isPresent()) {
                clientRepository.deleteById(sharedKey);
                response.setSuccessMessage("Cliente eliminado exitosamente.");
                response.setStatus("success");
                response.setData(true);
            } else {
                response.setErrorMessage(ErrorMessages.CLIENT_NOT_FOUND);
                response.setStatus("error");
                response.setData(false);
            }

        } catch (Exception ex) {
            response.setErrorMessage(ErrorMessages.INTERNAL_SERVER_ERROR + " Detalles: " + ex.getMessage());
            response.setStatus("error");
            response.setData(false);
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

            // Encabezados
            writer.println("sharedKey,businessId,email,phoneNumber,createdAt,startDate,endDate");

            // Filas
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
    

}
