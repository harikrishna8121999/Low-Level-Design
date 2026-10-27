package org.nailyourinterview.lld.inventory_management.strategy;

import lombok.AllArgsConstructor;
import org.nailyourinterview.lld.inventory_management.model.Product;

// Fill back up to factor x reorder level
@AllArgsConstructor
public class TopUpStrategy implements ReplenishmentStrategy {
    private final int factor;

    @Override
    public int reorderQuantity(Product product, int currentQuantity) {
        return product.getReorderLevel() * factor - currentQuantity;
    }
}
