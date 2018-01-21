package com.fnag;

import com.fnag.domain.Product;
import com.fnag.domain.Report;
import com.fnag.domain.Sale;
import com.fnag.exception.CalculationException;
import com.fnag.service.TopSellerService;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

public class TopSellerServiceTests {

    private TopSellerService topSellerService = new TopSellerService();

    /** Test data **/

    private final Map<String, Product> products = Map.ofEntries(
            entry("LMUSB", new Product("LMUSB", BigDecimal.valueOf(20),"Lance-missile USB")),
            entry("MKB", new Product("MKB", BigDecimal.valueOf(200),"Clavier mécanique")),
            entry("T127", new Product("T127", BigDecimal.valueOf(14.99),"T-shirt 'no place like 127.0.0.1'"))
    );

    private final List<Sale> sales = Arrays.asList(
            new Sale("Paris", "Bob", "LMUSB", 1),
            new Sale("Lyon", "Alice", "MKB", 1),
            new Sale("Lyon", "Alice", "T127", 2),
            new Sale("Paris", "Bob", "T127", 1),
            new Sale("Paris", "Chuck", "T127", 1)
    );

    private final Report report = new Report(products, sales);

    /** Nominal case **/

    @Test
    public void calculateTopSellerWithOneResult() {
        Map<String, BigDecimal> topSellers = topSellerService.calculate(report);

        assertThat(topSellers).isNotNull();
        assertThat(topSellers.size()).isEqualTo(1);
        assertThat(topSellers.get("Lyon|Alice")).isEqualTo(BigDecimal.valueOf(229.98));
    }

    @Test
    public void calculateTopSellerWithTwoResults() {
        List<Sale> salesWithEquality = new ArrayList<>(sales);
        salesWithEquality.add(new Sale("Paris", "Chuck", "MKB", 1));
        salesWithEquality.add(new Sale("Paris", "Chuck", "T127", 1));
        Report reportWithEquality = new Report(products, salesWithEquality);

        Map<String, BigDecimal> topSellers = topSellerService.calculate(reportWithEquality);
        assertThat(topSellers).isNotNull();
        assertThat(topSellers.size()).isEqualTo(2);
        assertThat(topSellers.get("Lyon|Alice")).isEqualTo(BigDecimal.valueOf(229.98));
        assertThat(topSellers.get("Paris|Chuck")).isEqualTo(BigDecimal.valueOf(229.98));
    }

    /** Error case **/

    @Test
    public void returnsEmptyResultIfReportIsNull() {
        Map<String, BigDecimal> topSellers = topSellerService.calculate(null);
        assertThat(topSellers).isNotNull();
        assertThat(topSellers).isEmpty();
    }

    @Test
    public void returnsEmptyResultIfProductsIsNull() {
        Map<String, BigDecimal> topSellers = topSellerService.calculate(new Report(null, sales));
        assertThat(topSellers).isNotNull();
        assertThat(topSellers).isEmpty();
    }

    @Test
    public void returnsEmptyResultIfSalesIsNull() {
        Map<String, BigDecimal> topSellers = topSellerService.calculate(new Report(products, null));
        assertThat(topSellers).isNotNull();
        assertThat(topSellers).isEmpty();
    }

    @Test
    public void returnsEmptyResultIfProductsIsEmpty() {
        Map<String, BigDecimal> topSellers = topSellerService.calculate(new Report(new HashMap<>(), sales));
        assertThat(topSellers).isNotNull();
        assertThat(topSellers).isEmpty();
    }

    @Test
    public void returnsEmptyResultIfSalesIsEmpty() {
        Map<String, BigDecimal> topSellers = topSellerService.calculate(new Report(products, new ArrayList<>()));
        assertThat(topSellers).isNotNull();
        assertThat(topSellers).isEmpty();
    }

    @Test(expected = CalculationException.class)
    public void throwsCalculationExceptionIfProductNotExist() {
        Map<String, Product> productsWithOneProduct = Map.ofEntries(
                entry("LMUSB", new Product("LMUSB", BigDecimal.valueOf(20),"Lance-missile USB"))
        );
        Report reportWithOneProduct = new Report(productsWithOneProduct, sales);

        topSellerService.calculate(reportWithOneProduct);
    }
}
