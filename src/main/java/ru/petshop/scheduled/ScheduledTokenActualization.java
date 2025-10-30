package ru.petshop.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.petshop.bean.ApplicationLogger;
import ru.petshop.domain.Companies;
import ru.petshop.domain.Company;
import ru.petshop.domain.Token;
import ru.petshop.service.IntegrationService;
import ru.petshop.service.TokenService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTokenActualization {

    @Autowired
    private ApplicationLogger applicationLogger;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IntegrationService integrationService;

    private String DATE_TIME_STRING = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_STRING);
    private Long dateDifferenceLimit = 9 * 60 * 60 * 1000l;

    @Scheduled(fixedDelayString = "${application.actualizationFixedDelay}")
    public void actualization() {
        try {
            List<Company> companyList = Companies.getCompanies();
            for (Company company : companyList) {
                actualization(company);
            }
        } catch (Exception ex) {
            applicationLogger.error(ex);
        }
    }

    private void actualization(Company company) throws Exception {
        Token token = tokenService.getCurrentByCompanyId(company.getCompanyId());
        boolean needActualize = false;
        if (token != null) {
            Date createdAt = simpleDateFormat.parse(token.getCreatedAt());
            Date now = new Date();
            Long dateDifference = now.getTime() - createdAt.getTime();
            //System.out.println(createdAt);
            if (dateDifference > dateDifferenceLimit) {
                needActualize = true;
            }
        } else {
            needActualize = true;
        }

        if (needActualize) {
            actualizeToken(company);
        }
    }

    public void actualizeToken(Company company) throws Exception {
        Token token = new Token();

        String clientToken = integrationService.getClientToken(company.getCompanyINN());
        token.setToken(clientToken);

        String createdAt = simpleDateFormat.format(new Date());
        token.setCreatedAt(createdAt);

        token.setCompanyId(company.getCompanyId());

        tokenService.setCurrent(token);
    }
}
