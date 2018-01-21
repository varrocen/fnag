package com.fnag.service;

import com.fnag.domain.Sale;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TopSaleService {

    /**
     * Calculates the best-selling product, with its number of sales
     *
     * @param sales the sales list
     * @return the best-selling product, with its number of sales
     */
    public Map<String, Integer> calculate(List<Sale> sales) {

        Map<String, Integer> result = new HashMap<>();

        if (CollectionUtils.isNotEmpty(sales)) {
            // Aggregates sales by product reference and sum quantities
            Map<String, Integer> sumQuantitiesByProducts = sales.stream().collect(
                    Collectors.groupingBy(Sale::getProductReference,
                            Collectors.summingInt(Sale::getQuantity)));

            // Gets max quantity
            Integer max = sumQuantitiesByProducts
                    .entrySet().stream()
                    .max(
                            Map.Entry.comparingByValue(Integer::compareTo)
                    ).get().getValue();

            // Gets all sales with max quantity
            result = sumQuantitiesByProducts
                    .entrySet().stream()
                    .filter(map -> max.equals(map.getValue()))
                    .collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue()));
        }

        return result;
    }
}
