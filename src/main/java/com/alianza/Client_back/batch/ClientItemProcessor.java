package com.alianza.Client_back.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.alianza.Client_back.entity.Client;

@Component
public class ClientItemProcessor implements ItemProcessor<Client, Client>{
    @Override
    public Client process(Client client) throws Exception {
    if (client.getEmail() == null || !client.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        client.setEmail("default@domain.com"); 
    }

    return client;
    }
}
