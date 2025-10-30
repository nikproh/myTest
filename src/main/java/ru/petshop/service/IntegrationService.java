package ru.petshop.service;

public interface IntegrationService {
    void auth() throws Exception;
    String getClientToken(String companyINN) throws Exception;
}
