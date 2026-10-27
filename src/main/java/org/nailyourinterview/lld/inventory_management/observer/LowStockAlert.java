package org.nailyourinterview.lld.inventory_management.observer;

import org.nailyourinterview.lld.inventory_management.model.Product;
import org.nailyourinterview.lld.inventory_management.model.Warehouse;

public class LowStockAlert implements StockObserver {

    @Override
    public void onLowStock(Warehouse warehouse, Product product, int quantity) {
        System.out.println("ALERT: " + product.getName() + " is low in " + warehouse.getId()
                + " (" + quantity + " left, reorder level " + product.getReorderLevel() + ")");
    }
}
