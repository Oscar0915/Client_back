package com.alianza.Client_back.dto.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.alianza.Client_back.entity.Client;

public class ClientRowMapper implements RowMapper<Client> {
    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
        Client client = new Client();
        client.setSharedKey(rs.getString("shared_key"));
        client.setBusinessId(rs.getString("business_id"));
        client.setEmail(rs.getString("email"));
        client.setPhoneNumber(rs.getString("phone_number"));
        client.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        client.setStartDate(rs.getDate("start_date").toLocalDate());
        client.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
        return client;
    }
}

