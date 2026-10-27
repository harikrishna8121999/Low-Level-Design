package org.nailyourinterview.lld.inventory_management.strategy;

import org.nailyourinterview.lld.inventory_management.model.Product;

public interface ReplenishmentStrategy {
    // How many units to order when stock is low
    int reorderQuantity(Product product, int currentQuantity);
}
