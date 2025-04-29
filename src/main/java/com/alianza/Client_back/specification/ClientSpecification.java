package com.alianza.Client_back.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.alianza.Client_back.entity.Client;


public class ClientSpecification {

    public static Specification<Client> hasBusinessId(String businessId) {
        return (root, query, criteriaBuilder) -> {
            if (businessId == null || businessId.isEmpty()) {
                return criteriaBuilder.conjunction(); 
            }
            return criteriaBuilder.like(root.get("businessId"), "%" + businessId + "%");
        };
    }

    public static Specification<Client> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("email"), "%" + email + "%"); 
        };
    }

    public static Specification<Client> hasStartDate(LocalDate startDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate);
        };
    }

    public static Specification<Client> hasEndDate(LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (endDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate);
        };
    }

}
