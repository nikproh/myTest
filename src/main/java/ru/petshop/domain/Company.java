package ru.petshop.domain;

import lombok.Data;

@Data
public class Company {

    private Integer companyId;
    private String companyINN;

    public Company(Integer companyId, String companyINN) {
        this.companyId = companyId;
        this.companyINN = companyINN;
    }
}
