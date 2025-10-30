package ru.petshop.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import ru.petshop.domain.Token;

@Mapper
@Component
public interface TokenMapper {
    Token selectCurrent(@Param("companyId") Integer companyId);
    void addCurrent(Token token);
}
