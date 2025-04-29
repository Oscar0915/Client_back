package com.alianza.Client_back.batch;

import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.stereotype.Component;

import com.alianza.Client_back.entity.Client;

public class ClientItemWriter {
   
@Component
public class ClientLineAggregator implements LineAggregator<Client> {
    @Override
    public String aggregate(Client item) {
        return item.getSharedKey() + "," + item.getBusinessId() + "," + item.getEmail() + ","
                + item.getPhoneNumber() + "," + item.getCreatedAt() + "," + item.getStartDate() + ","
                + item.getEndDate();
    }
}



}
