package ru.petshop.service;

import ru.petshop.domain.Token;

public interface TokenService {
    Token getCurrentByCompanyId(Integer companyId);
    void setCurrent(Token token);
}
