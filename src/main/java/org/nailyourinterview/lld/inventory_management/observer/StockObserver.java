package org.nailyourinterview.lld.inventory_management.observer;

import org.nailyourinterview.lld.inventory_management.model.Product;
import org.nailyourinterview.lld.inventory_management.model.Warehouse;

public interface StockObserver {
    void onLowStock(Warehouse warehouse, Product product, int quantity);
}
