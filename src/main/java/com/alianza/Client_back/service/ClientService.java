package com.alianza.Client_back.service;

import java.util.List;

import com.alianza.Client_back.dto.ContractRequest;
import com.alianza.Client_back.dto.ContractResponse;
import com.alianza.Client_back.entity.Client;

public interface ClientService {
    ContractResponse<Client>  saveClient(ContractRequest<Client> client);
    ContractResponse<Client>  getClientBySharedKey(ContractRequest<String> sharedKey);
    ContractResponse<List<Client>> getAllClients();
    ContractResponse<Boolean> deleteClient(ContractRequest<String> sharedKey);
}

