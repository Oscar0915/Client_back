package com.alianza.Client_back.dto;

public class ContractRequest <T> {
    public String token;
    public T data;
    public ContractRequest(T data) {
        this.data = data;
    }
    public ContractRequest() {
    }
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }    

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
