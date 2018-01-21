package com.fnag.service;

import com.fnag.domain.Report;
import com.fnag.domain.Sale;
import com.fnag.exception.CalculationException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fnag.util.Constants.SEPARATOR;

@Service
public class TopSellerService {

    /**
     * Calculates the best seller, with the total amount of his sales
     *
     * @param report the report with the sales list and the product map
     * @return the best seller, with the total amount of his sales
     */
    public Map<String, BigDecimal> calculate(Report report) {

        Map<String, BigDecimal> result = new HashMap<>();

        if (report != null && CollectionUtils.isNotEmpty(report.getSales()) && MapUtils.isNotEmpty(report.getProducts())) {
            // Aggregates seller by store|seller key and calculates the total amount of his sales
            Map<String, BigDecimal> topSellers = new HashMap<>();
            for(Sale sale : report.getSales()) {
                if (!report.getProducts().containsKey(sale.getProductReference())) {
                    throw new CalculationException("No product for the key: " + sale.getProductReference());
                }

                String key = sale.getStore() + SEPARATOR + sale.getSeller();
                BigDecimal price = report.getProducts().get(sale.getProductReference()).getPrice();
                if(topSellers.containsKey(key)) {
                    topSellers.put(key, topSellers.get(key).add(price.multiply(BigDecimal.valueOf(sale.getQuantity()))));
                } else {
                    topSellers.put(key, price.multiply(BigDecimal.valueOf(sale.getQuantity())));
                }
            }

            // Gets max sale
            BigDecimal max = topSellers
                        .entrySet().stream()
                        .max(
                                Map.Entry.comparingByValue(BigDecimal::compareTo)
                        ).get().getValue();

            // Gets all sellers with max sale
            result = topSellers
                    .entrySet().stream()
                    .filter(map -> max.equals(map.getValue()))
                    .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));
        }

        return result;
    }
}
