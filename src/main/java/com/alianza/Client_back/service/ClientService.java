package com.alianza.Client_back.service;

import java.util.List;

import com.alianza.Client_back.dto.ContractRequest;
import com.alianza.Client_back.dto.ContractResponse;
import com.alianza.Client_back.dto.client.MOClient;
import com.alianza.Client_back.dto.client.SearchClientRequest;
import com.alianza.Client_back.entity.Client;

public interface ClientService {
    ContractResponse<Client>  saveClient(ContractRequest<MOClient> client);
    ContractResponse<Client>  getClientBySharedKey(ContractRequest<String> sharedKey);
    ContractResponse<List<Client>> getAllClients();
    ContractResponse<String> getCsvFileAsBase64() throws Exception;
    ContractResponse<List<Client>> searchClients(ContractRequest<SearchClientRequest> request);
}

