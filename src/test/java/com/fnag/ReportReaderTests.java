package com.fnag;

import com.fnag.domain.Product;
import com.fnag.domain.Report;
import com.fnag.domain.Sale;
import com.fnag.reader.ReportReader;
import com.fnag.exception.InvalidReportFormatException;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportReaderTests {

    private ReportReader reportReader = new ReportReader();

    /** Nominal case **/

    @Test
    public void readReportWithSuccess() throws IOException {
        Product product = new Product("LMUSB", BigDecimal.valueOf(20), "Lance-missile USB");
        Sale sale = new Sale("Paris", "Bob", "LMUSB", 1);

        Optional<Report> report = reportReader.read(new ClassPathResource("report-ok.txt"));

        assertThat(report.isPresent()).isTrue();
        assertThat(report.get().getProducts()).isNotNull();
        assertThat(report.get().getProducts().size()).isEqualTo(3);
        assertThat(report.get().getProducts().get("LMUSB")).isEqualTo(product);
        assertThat(report.get().getProducts().get("LMUSB").getReference()).isEqualTo(product.getReference());
        assertThat(report.get().getProducts().get("LMUSB").getPrice()).isEqualTo(product.getPrice());
        assertThat(report.get().getProducts().get("LMUSB").getDescription()).isEqualTo(product.getDescription());
        assertThat(report.get().getSales()).isNotNull();
        assertThat(report.get().getSales().size()).isEqualTo(5);
        assertThat(report.get().getSales().get(0)).isEqualTo(sale);
        assertThat(report.get().getSales().get(0).getStore()).isEqualTo(sale.getStore());
        assertThat(report.get().getSales().get(0).getSeller()).isEqualTo(sale.getSeller());
        assertThat(report.get().getSales().get(0).getProductReference()).isEqualTo(sale.getProductReference());
        assertThat(report.get().getSales().get(0).getQuantity()).isEqualTo(sale.getQuantity());
    }

    /** Error case **/

    @Test(expected = InvalidReportFormatException.class)
    public void throwsReportInvalidReportFormatExceptionIfResourceFormatIsInvalid() throws IOException {
        reportReader.read(new ClassPathResource("report-ko.txt"));
    }

    @Test(expected = IOException.class)
    public void throwsIOExceptionIfResourceIsInvalid() throws IOException {
        reportReader.read(new ClassPathResource(""));
    }

    @Test
    public void returnsEmptyReportIfResourceIsNull() throws IOException {
        Optional<Report> report = reportReader.read(null);
        assertThat(report.isPresent()).isFalse();
    }

    @Test
    public void returnsEmptyReportIfResourceIsEmpty() throws IOException {
        Optional<Report> report = reportReader.read(new ClassPathResource("report-empty.txt"));
        assertThat(report.isPresent()).isFalse();
    }
}
