package ru.petshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.petshop.bean.ApplicationLogger;
import ru.petshop.domain.WarehouseProductDataMatrix;
import ru.petshop.service.ChestnoService;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@ApiIgnore
@Controller
public class ApiController {

    @Autowired
    private ApplicationLogger applicationLogger;
    @Autowired
    private ChestnoService chestnoService;

    @Data
    private static class WithdrawalData {
        private Map<Integer, List<WarehouseProductDataMatrix>> dataMap;
        private String assignmentDate;
    }

    @PostMapping("/withdrawal")
    @ResponseBody
    String withdrawal(HttpServletRequest request) {
        try {
            String dataStr = request.getParameter("data");

            ObjectMapper objectMapper = new ObjectMapper();
            WithdrawalData data = objectMapper.readValue(dataStr, WithdrawalData.class);

//            System.out.println(data.assignmentDate);
//            System.out.println(data.getDataMap());

            chestnoService.withdrawal(data.getDataMap(), data.assignmentDate);
        } catch (Exception ex) {
            applicationLogger.error(ex);
        }
        return "OK";
    }

    @GetMapping("/aggregation")
    @ResponseBody
    String aggregation() {
        try {
            //chestnoService.aggregation();
        } catch (Exception ex) {
            applicationLogger.error(ex);
        }
        return "OK";
    }
}
