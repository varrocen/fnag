package com.fnag.domain;

import java.util.Objects;

public class Sale {

    private final String store;

    private final String seller;

    private final String productReference;

    private final Integer quantity;

    public Sale(final String store, final String seller, final String productReference, final Integer quantity) {
        this.store = store;
        this.seller = seller;
        this.productReference = productReference;
        this.quantity = quantity;
    }

    public String getStore() {
        return store;
    }

    public String getSeller() {
        return seller;
    }

    public String getProductReference() {
        return productReference;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(store, sale.store) &&
                Objects.equals(seller, sale.seller);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, seller);
    }

    @Override
    public String toString() {
        return "Sale{" +
                "store='" + store + '\'' +
                ", seller='" + seller + '\'' +
                ", productReference='" + productReference + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
