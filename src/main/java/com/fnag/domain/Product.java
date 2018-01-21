package com.fnag.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    /**
     * Product reference is unique
     */
    private final String reference;

    private final BigDecimal price;

    private final String description;

    public Product(final String reference, final BigDecimal price, final String description) {
        this.reference = reference;
        this.price = price;
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(reference, product.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference);
    }

    @Override
    public String toString() {
        return "Product{" +
                "reference='" + reference + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
