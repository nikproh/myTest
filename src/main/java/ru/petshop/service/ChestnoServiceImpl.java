package ru.petshop.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.petshop.domain.Companies;
import ru.petshop.domain.WarehouseProductDataMatrix;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class ChestnoServiceImpl implements ChestnoService {

    @Autowired
    private CryptoProService cryptoProService;
    @Autowired
    private TokenService tokenService;

    @Override
    public void aggregation() throws Exception {

        /*
        {
            "participantId":"7813450665",
            "aggregationUnits":[
                {
                    "unitSerialNumber":"0104609997476100215Bd,=l",
                    "aggregationType":"AGGREGATION",
                    "sntins":[
                        "0104609997475899215Un1)8",
                        "0104609997475899215Uw=Qd"
                    ]
                }
            ]
        }

        {
            "participantId":"7813450665",
            "aggregationUnits":[
                {
                    "unitSerialNumber":"0104609997476100215Bd,=l",
                    "sntins":[
                        "0104609997475899215Un1)8",
                        "0104609997475899215Uw=Qd"
                    ]
                }
            ]
        }
        */

        String compactJson = "{\n" +
                "            \"participantId\":\"7813450665\",\n" +
                "            \"aggregationUnits\":[\n" +
                "                {\n" +
                "                    \"unitSerialNumber\":\"0104609997476100215Bd,=l\",\n" +
                "                    \"sntins\":[\n" +
                "                        \"0104609997475899215Un1)8\",\n" +
                "                        \"0104609997475899215Uw=Qd\"\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        }";

        System.out.println(compactJson);

        callToApi(4, compactJson, "SETS_AGGREGATION");
    }

    private static String getTrueZnakDate(String date) {
        return date.replace(".", "-");
    }

    @Override
    public void withdrawal(Map<Integer, List<WarehouseProductDataMatrix>> dataMap, String assignmentDate) throws Exception {

        for (Map.Entry<Integer, List<WarehouseProductDataMatrix>> entry : dataMap.entrySet()) {

            Integer companyId = entry.getKey();
            List<WarehouseProductDataMatrix> warehouseProductDataMatrixList = entry.getValue();

            String inn = Companies.getInn(companyId);
            if (inn != null) {
                for (WarehouseProductDataMatrix warehouseProductDataMatrix : warehouseProductDataMatrixList) {
                    String compactJson = "{\n" +
                            "            \"inn\":\"" + inn + "\",\n" +
                            "            \"action\":\"DISTANCE\",\n" +
                            "            \"action_date\":\"" + getTrueZnakDate(assignmentDate) + "\",\n" +
                            "            \"document_type\":\"CONSIGNMENT_NOTE\",\n" +
                            "            \"document_number\":\"" + warehouseProductDataMatrix.getInvoiceId() + "\",\n" +
                            "            \"document_date\":\"" + getTrueZnakDate(warehouseProductDataMatrix.getInvoiceCreationDate()) + "\",\n" +
                            "            \"products\":[\n" +
                            "                {\n" +
                            "                    \"cis\":\"" + warehouseProductDataMatrix.getWarehouseProductDataMatrixCode() + "\",\n" +
                            "                    \"product_cost\":\"" + warehouseProductDataMatrix.getInvoiceProductPrice() + "\"\n" +
                            "                }\n" +
                            "            ]\n" +
                            "        }";

                    System.out.println(compactJson);

                    callToApi(companyId, compactJson, "LK_RECEIPT");
                }
            }
        }
    }

    private void callToApi(Integer companyId, String compactJson, String docType) throws Exception {

        HttpPost httpPost = new HttpPost("https://markirovka.sandbox.crptech.ru/api/v3/true-api/lk/documents/create?pg=petfood");

        String token = tokenService.getCurrentByCompanyId(companyId).getToken();

        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Authorization", "Bearer " + token);

        String base64Document = Base64.getEncoder().encodeToString(compactJson.getBytes(StandardCharsets.UTF_8));
        String signature = cryptoProService.crypt(compactJson);

        String json = "{\n" +
                "  \"document_format\":\"MANUAL\",\n" +
                "  \"product_document\":\"" + base64Document + "\",\n" +
                "  \"type\":\"" + docType + "\",\n" +
                "  \"signature\":\"" + signature + "\"\n" +
                "}";

        System.out.println(json);

        StringEntity params = new StringEntity(json);
        httpPost.setEntity(params);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            String result = EntityUtils.toString(response.getEntity());
            System.out.println(result);
        } finally {
            response.close();
        }
    }
}