package ru.petshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.petshop.domain.Token;
import ru.petshop.mapper.TokenMapper;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenMapper tokenMapper;

    @Override
    public Token getCurrentByCompanyId(Integer companyId) {
        return tokenMapper.selectCurrent(companyId);
    }

    @Override
    public void setCurrent(Token token) {
        tokenMapper.addCurrent(token);
    }
}