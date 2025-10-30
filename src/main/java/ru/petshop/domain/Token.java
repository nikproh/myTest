package ru.petshop.domain;

import lombok.Data;

@Data
public class Token {
    private String token;
    private String createdAt;
    private Integer companyId;
}
