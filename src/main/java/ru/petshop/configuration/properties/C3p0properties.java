package ru.petshop.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "c3p0")
public class C3p0properties {
    private int minPoolSize;
    private int maxPoolSize;
    private int maxIdleTime;
    private String driverClassName;
    private String password;
    private String username;
    private String url;
    private String dataSourceName;
}
