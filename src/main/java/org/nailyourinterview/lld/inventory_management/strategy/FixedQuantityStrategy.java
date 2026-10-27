package org.nailyourinterview.lld.inventory_management.strategy;

import lombok.AllArgsConstructor;
import org.nailyourinterview.lld.inventory_management.model.Product;

// Always order the same batch size
@AllArgsConstructor
public class FixedQuantityStrategy implements ReplenishmentStrategy {
    private final int quantity;

    @Override
    public int reorderQuantity(Product product, int currentQuantity) {
        return quantity;
    }
}
