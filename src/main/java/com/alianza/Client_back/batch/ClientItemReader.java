package com.alianza.Client_back.batch;


import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alianza.Client_back.dto.mapper.ClientRowMapper;
import com.alianza.Client_back.entity.Client;


@Configuration
public class ClientItemReader {

    @Bean
    public JdbcCursorItemReader<Client> clientReader(DataSource dataSource) {
        JdbcCursorItemReader<Client> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT * FROM clients");
        reader.setRowMapper(new ClientRowMapper());
        return reader;
    }
}