package ru.petshop.service;

import ru.petshop.domain.WarehouseProductDataMatrix;

import java.util.List;
import java.util.Map;

public interface ChestnoService {
    void aggregation() throws Exception;
    void withdrawal(Map<Integer, List<WarehouseProductDataMatrix>> dataMap, String assignmentDate) throws Exception;
}
