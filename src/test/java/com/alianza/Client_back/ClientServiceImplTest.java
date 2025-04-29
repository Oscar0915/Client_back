package com.alianza.Client_back;

import com.alianza.Client_back.dto.ContractRequest;
import com.alianza.Client_back.dto.ContractResponse;
import com.alianza.Client_back.dto.client.MOClient;
import com.alianza.Client_back.dto.client.SearchClientRequest;
import com.alianza.Client_back.entity.Client;
import com.alianza.Client_back.repository.ClientRepository;
import com.alianza.Client_back.service.implementacion.ClientServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    private ClientRepository clientRepository;
    private ClientServiceImpl clientService;
    private ContractRequest<SearchClientRequest> request ;
    
    @BeforeEach
    void setUp() {
        clientRepository = mock(ClientRepository.class);
        clientService = new ClientServiceImpl(clientRepository);
    }

    @Test
    void saveClient_successful() {
        MOClient moClient = new MOClient();
        moClient.setBusinessId("Oscar Ballesta");
        moClient.setEmail("oballesta@test.com");
        moClient.setPhoneNumber("1234567890");
        moClient.setStartDate(LocalDate.of(2023, 1, 1));
        moClient.setEndDate(LocalDate.of(2024, 1, 1));

        ContractRequest<MOClient> request = new ContractRequest<>();
        request.setData(moClient);

        when(clientRepository.existsById(anyString())).thenReturn(false);

        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ContractResponse<Client> response = clientService.saveClient(request);

        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertEquals("oballesta@test.com", response.getData().getEmail());
    }

    @Test
    void testSearchClients_success() {
        SearchClientRequest searchRequest = new SearchClientRequest();
        searchRequest.setBusinessId("Oscar Ballesta");
        request = new ContractRequest<SearchClientRequest>(searchRequest);

        Client client1 = new Client();
        client1.setBusinessId("Oscar Ballesta");
        List<Client> clientList = Collections.singletonList(client1);
        
        when(clientRepository.findAll(any(Specification.class))).thenReturn(clientList);
        
        ContractResponse<List<Client>> response = clientService.searchClients(request);
        
        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
    }

}
