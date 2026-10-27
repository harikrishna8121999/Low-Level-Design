package org.nailyourinterview.lld.inventory_management.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nailyourinterview.lld.inventory_management.enums.Category;

@Getter
@AllArgsConstructor
public class Product {
    private final String sku;
    private final String name;
    private final Category category;
    private final double price;
    private final int reorderLevel;   // at or below this, stock is low
}
