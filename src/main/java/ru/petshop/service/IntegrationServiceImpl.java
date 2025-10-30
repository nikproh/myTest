package ru.petshop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IntegrationServiceImpl implements IntegrationService {

    @Autowired
    private CryptoProService cryptoProService;

    @Override
    public void auth() throws Exception {
        AuthResponse authResponse = getAuthResponse();
//        System.out.println(authResponse.getData());
//        System.out.println(authResponse.getUuid());
//
//        System.out.println("-------------------------");

        String cryptData = cryptoProService.crypt(authResponse.getData());
        TokenResponse tokenResponse = getTokenResponse(authResponse.getUuid(), cryptData, "7813450665");
        System.out.println(tokenResponse.token);
    }

    @Override
    public String getClientToken(String companyINN) throws Exception {
        AuthResponse authResponse = getAuthResponse();
//        System.out.println(authResponse.getData());
//        System.out.println(authResponse.getUuid());
//
//        System.out.println("-------------------------");

        String cryptData = cryptoProService.crypt(authResponse.getData());
        TokenResponse tokenResponse = getTokenResponse(authResponse.getUuid(), cryptData, companyINN);
        return tokenResponse.token;
    }

    @Data
    static class AuthResponse {
        private String uuid;
        private String data;
//
//        public String getUuid() {
//            return uuid;
//        }
//
//        public void setUuid(String uuid) {
//            this.uuid = uuid;
//        }
//
//        public String getData() {
//            return data;
//        }
//
//        public void setData(String data) {
//            this.data = data;
//        }
    }

    @Data
    static class TokenResponse {
        private String token;
//
//        public String getToken() {
//            return token;
//        }
//
//        public void setToken(String token) {
//            this.token = token;
//        }
    }

    private static AuthResponse getAuthResponse() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("https://markirovka.sandbox.crptech.ru/api/v3/true-api/auth/key");
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            String result = EntityUtils.toString(response.getEntity());
            AuthResponse authResponse = new ObjectMapper().readValue(result, AuthResponse.class);
            return authResponse;
        } finally {
            response.close();
        }
    }

    private static TokenResponse getTokenResponse(String uuid, String cryptData, String inn) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://markirovka.sandbox.crptech.ru/api/v3/true-api/auth/simpleSignIn");
        String json = "{\n" +
                "  \"uuid\":\"" + uuid + "\",\n" +
                "  \"data\":\"" + cryptData + "\",\n" +
                "  \"inn\":\"" + inn + "\"\n" +
                "}";

        StringEntity params = new StringEntity(json);
        httpPost.setEntity(params);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Accept", "application/json");
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            String result = EntityUtils.toString(response.getEntity());
//            System.out.println(111);
//            System.out.println(result);
//            System.out.println(222);
            TokenResponse tokenResponse = new ObjectMapper().readValue(result, TokenResponse.class);
            return tokenResponse;
        } finally {
            response.close();
        }
    }
}