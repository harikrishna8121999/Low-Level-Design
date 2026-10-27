package org.nailyourinterview.lld.inventory_management.observer;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.nailyourinterview.lld.inventory_management.model.Product;
import org.nailyourinterview.lld.inventory_management.model.Warehouse;
import org.nailyourinterview.lld.inventory_management.service.InventoryService;
import org.nailyourinterview.lld.inventory_management.strategy.ReplenishmentStrategy;

// Orders new stock from the supplier when stock is low. The strategy decides how much.
@AllArgsConstructor
public class AutoReplenisher implements StockObserver {
    private final InventoryService inventory;
    @Setter private ReplenishmentStrategy strategy;

    @Override
    public void onLowStock(Warehouse warehouse, Product product, int quantity) {
        int amount = strategy.reorderQuantity(product, quantity);
        if (amount <= 0) return;
        System.out.println("Reorder " + amount + " x " + product.getSku() + " for " + warehouse.getId()
                + " (" + strategy.getClass().getSimpleName() + ")");
        inventory.addStock(warehouse.getId(), product.getSku(), amount);
    }
}
