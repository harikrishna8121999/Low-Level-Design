package org.nailyourinterview.lld.inventory_management.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Warehouse {
    @Getter private final String id;
    @Getter private final String city;
    private final Map<String, Integer> stock = new HashMap<>();   // sku -> quantity

    public Warehouse(String id, String city) {
        this.id = id;
        this.city = city;
    }

    public synchronized int getQuantity(String sku) {
        return stock.getOrDefault(sku, 0);
    }

    public synchronized int addStock(String sku, int quantity) {
        return stock.merge(sku, quantity, Integer::sum);
    }

    // Check and take in one step, so two orders cannot take the same units.
    // Returns the quantity left, or -1 when there is not enough.
    public synchronized int removeStock(String sku, int quantity) {
        int have = getQuantity(sku);
        if (have < quantity) return -1;
        stock.put(sku, have - quantity);
        return have - quantity;
    }
}
