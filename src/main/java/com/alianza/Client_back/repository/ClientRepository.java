package com.alianza.Client_back.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import com.alianza.Client_back.entity.Client;
@Repository
public interface ClientRepository extends JpaRepository<Client, String> , JpaSpecificationExecutor<Client>{
    
    
}

