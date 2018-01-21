package com.fnag;

import com.fnag.domain.Sale;
import com.fnag.service.TopSaleService;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class TopSaleServiceTests {

    private TopSaleService topSaleService = new TopSaleService();

    /** Test data **/

    private final List<Sale> sales = Arrays.asList(
            new Sale("Paris", "Bob", "LMUSB", 1),
            new Sale("Lyon", "Alice", "MKB", 1),
            new Sale("Lyon", "Alice", "T127", 2),
            new Sale("Paris", "Bob", "T127", 1),
            new Sale("Paris", "Chuck", "T127", 1)
    );

    /** Nominal case **/

    @Test
    public void calculateTopSaleWithOneResult() {
        Map<String, Integer> topSales = topSaleService.calculate(sales);

        assertThat(topSales).isNotNull();
        assertThat(topSales.size()).isEqualTo(1);
        assertThat(topSales.get("T127")).isEqualTo(4);
    }

    @Test
    public void calculateTopSaleWithTwoResults() {
        List<Sale> salesWithEquality = new ArrayList<>(sales);
        salesWithEquality.add(new Sale("Paris", "Chuck", "MKB", 3));

        Map<String, Integer> topSales = topSaleService.calculate(salesWithEquality);
        assertThat(topSales).isNotNull();
        assertThat(topSales.size()).isEqualTo(2);
        assertThat(topSales.get("T127")).isEqualTo(4);
        assertThat(topSales.get("MKB")).isEqualTo(4);
    }

    /** Error case **/

    @Test
    public void returnsEmptyResultIfSalesIsNull() {
        Map<String, Integer> topSales = topSaleService.calculate(null);
        assertThat(topSales).isNotNull();
        assertThat(topSales).isEmpty();
    }

    @Test
    public void returnsEmptyResultIfSalesIsEmpty() {
        Map<String, Integer> topSales = topSaleService.calculate(new ArrayList<>());
        assertThat(topSales).isNotNull();
        assertThat(topSales).isEmpty();
    }
}
