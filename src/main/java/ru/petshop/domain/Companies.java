package ru.petshop.domain;

import java.util.ArrayList;
import java.util.List;

public enum Companies {
    IN_RETAIL(4, "7813450665"),
    SMART_FAMILY(21, "7721788616");

    private final Integer companyId;
    private final String companyINN;

    Companies(Integer companyId, String companyINN) {
        this.companyId = companyId;
        this.companyINN = companyINN;
    }

    public static List<Company> getCompanies() {
        List<Company> companies = new ArrayList<>();
        for (Companies c : Companies.values()) {
            companies.add(new Company(c.companyId, c.companyINN));
        }
        return companies;
    }

    public static String getInn(Integer companyId) {
        for (Companies c : Companies.values()) {
            if (c.companyId.equals(companyId)) {
                return c.companyINN;
            }
        }
        return null;
    }
}
