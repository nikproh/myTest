package ru.petshop.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import ru.petshop.domain.Setting;

@Component
public interface SettingMapper {
    Setting selectByName(@Param("name") String name);
}
