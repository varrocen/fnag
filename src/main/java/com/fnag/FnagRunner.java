package com.fnag;

import com.fnag.domain.Report;
import com.fnag.reader.ReportReader;
import com.fnag.service.TopSaleService;
import com.fnag.service.TopSellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static com.fnag.util.Constants.LINE_BREAK;
import static com.fnag.util.Constants.SEPARATOR;

@Component
public class FnagRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(FnagApplication.class);

    @Autowired
    private ReportReader reportReader;

    @Autowired
    private TopSaleService topSaleService;

    @Autowired
    private TopSellerService topSellerService;

    @Value(value = "classpath:report.txt")
    private Resource resource;

    @Override
    public void run(String... args) {
        try {
            Optional<Report> report = reportReader.read(resource);

            if (report.isPresent()) {
                Map<String, Integer> topSales = topSaleService.calculate(report.get().getSales());

                StringBuilder sb = new StringBuilder();
                // Format top sales result
                for (Map.Entry<String, Integer> topSale : topSales.entrySet())
                {
                    String description = "";
                    if (report.get().getProducts().containsKey(topSale.getKey())) {
                        description = report.get().getProducts().get(topSale.getKey()).getDescription();
                    }
                    sb.append(LINE_BREAK);
                    sb.append("TOPSALE").append(SEPARATOR);
                    sb.append(topSale.getKey()).append(SEPARATOR);
                    sb.append(description).append(SEPARATOR);
                    sb.append(topSale.getValue());
                }

                Map<String, BigDecimal> topSellers = topSellerService.calculate(report.get());

                // Format top sellers result
                for (Map.Entry<String, BigDecimal> topSeller : topSellers.entrySet())
                {
                    sb.append(LINE_BREAK);
                    sb.append("TOPSELLER").append(SEPARATOR);
                    sb.append(topSeller.getKey()).append(SEPARATOR);
                    sb.append(topSeller.getValue());
                }

                log.info(sb.toString());
            }
        } catch (Exception e) {
            log.error("An exception occurred", e);
        }
    }
}
