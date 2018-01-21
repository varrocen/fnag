package com.fnag.domain;

import java.util.List;
import java.util.Map;

public class Report {

    private final Map<String, Product> products;

    private final List<Sale> sales;

    public Report(Map<String, Product> products, List<Sale> sales) {
        this.products = products;
        this.sales = sales;
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public List<Sale> getSales() {
        return sales;
    }
}
