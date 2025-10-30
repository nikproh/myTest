package ru.petshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseProductDataMatrix {
    private String warehouseProductDataMatrixCode;
    private Integer invoiceProductId;
    private BigDecimal invoiceProductPrice;
    private String productArticle;
    private Integer invoiceId;
    private String invoiceCreationDate;
    private Integer companyId;
}
