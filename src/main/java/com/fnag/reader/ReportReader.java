package com.fnag.reader;

import com.fnag.domain.Product;
import com.fnag.domain.Report;
import com.fnag.domain.Sale;
import com.fnag.exception.InvalidReportFormatException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

import static com.fnag.util.Constants.SEPARATOR;

@Component
public class ReportReader {

    private static final Logger log = LoggerFactory.getLogger(ReportReader.class);

    /**
     * Reading the results of the stores
     *
     * @param resource the resource with the report of the results of the stores
     * @return the report with the sales list and the product map
     * @throws IOException if there is a problem reading the resource
     */
    public Optional<Report> read(Resource resource) throws IOException {
        Optional<Report> report = Optional.empty();

        try {
            if (resource != null) {
                log.info("Loading the file {}", resource.getFilename());

                List<String> lines = Files.readAllLines(Paths.get(resource.getURI()));

                if (CollectionUtils.isNotEmpty(lines)) {
                    // The first line contains an integer N indicating the number of products
                    Integer n = Integer.valueOf(lines.get(0));
                    log.info("Loading {} product(s)", n);
                    // The following N lines are product records
                    List<String> productRecords = lines.subList(1, n + 1);
                    // Transforms product records on map of products
                    Map<String, Product> products = new HashMap<>();
                    for (String productRecord : productRecords) {
                        String[] array = productRecord.split(Pattern.quote(SEPARATOR));
                        products.put(array[0], new Product(array[0], new BigDecimal(array[1]), array[2]));
                    }

                    // Follow sales results : first line contains an integer M indicating the number of sales
                    Integer m = Integer.valueOf(lines.get(n + 1));
                    log.info("Loading {} sale(s)", m);
                    // The following M lines are sales records
                    List<String> saleRecords = lines.subList(m, lines.size());
                    // Transforms sale records on list of sales
                    List<Sale> sales = new ArrayList<>();
                    for (String saleRecord : saleRecords) {
                        String[] array = saleRecord.split(Pattern.quote(SEPARATOR));
                        sales.add(new Sale(array[0], array[1], array[2], Integer.valueOf(array[3])));
                    }

                    report = Optional.of(new Report(products, sales));
                    log.info("Successful loading");
                }
            }
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new InvalidReportFormatException("The report is not formatted correctly", e);
        }

        return report;
    }
}
