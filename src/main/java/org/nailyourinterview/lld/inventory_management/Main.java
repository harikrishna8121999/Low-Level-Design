package org.nailyourinterview.lld.inventory_management;

import org.nailyourinterview.lld.inventory_management.enums.Category;
import org.nailyourinterview.lld.inventory_management.model.Product;
import org.nailyourinterview.lld.inventory_management.model.Warehouse;
import org.nailyourinterview.lld.inventory_management.observer.AutoReplenisher;
import org.nailyourinterview.lld.inventory_management.observer.LowStockAlert;
import org.nailyourinterview.lld.inventory_management.service.InventoryService;
import org.nailyourinterview.lld.inventory_management.strategy.FixedQuantityStrategy;
import org.nailyourinterview.lld.inventory_management.strategy.TopUpStrategy;

public class Main {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        AutoReplenisher replenisher = new AutoReplenisher(inventory, new FixedQuantityStrategy(20));
        inventory.addObserver(new LowStockAlert());
        inventory.addObserver(replenisher);

        System.out.println("--- 1. Catalog and warehouses ---");
        inventory.addProduct(new Product("LAP-1", "Laptop", Category.ELECTRONICS, 55000, 5));
        inventory.addProduct(new Product("PHN-1", "Phone", Category.ELECTRONICS, 20000, 10));
        inventory.addProduct(new Product("RCE-5", "Rice 5kg", Category.GROCERY, 450, 30));
        inventory.addWarehouse(new Warehouse("BLR", "Bengaluru"));
        inventory.addWarehouse(new Warehouse("DEL", "Delhi"));

        System.out.println("--- 2. Stock arrives ---");
        inventory.addStock("BLR", "LAP-1", 12);
        inventory.addStock("BLR", "PHN-1", 25);
        inventory.addStock("BLR", "RCE-5", 100);
        inventory.addStock("DEL", "LAP-1", 4);
        inventory.addStock("DEL", "PHN-1", 40);
        inventory.printStock();

        System.out.println("--- 3. Orders ship ---");
        inventory.removeStock("BLR", "LAP-1", 3);
        inventory.removeStock("BLR", "RCE-5", 60);

        System.out.println("--- 4. Cross the reorder level: alert + reorder ---");
        inventory.removeStock("BLR", "LAP-1", 5);
        inventory.removeStock("BLR", "LAP-1", 2);

        System.out.println("--- 5. Transfer between warehouses ---");
        inventory.transfer("BLR", "DEL", "LAP-1", 10);
        inventory.transfer("BLR", "DEL", "PHN-1", 16);

        System.out.println("--- 6. Swap the strategy at runtime ---");
        replenisher.setStrategy(new TopUpStrategy(3));
        inventory.removeStock("BLR", "RCE-5", 15);

        System.out.println("--- 7. Invalid input ---");
        inventory.removeStock("DEL", "LAP-1", 50);
        inventory.removeStock("BLR", "XYZ-9", 1);
        inventory.addStock("MUM", "LAP-1", 5);
        inventory.addStock("BLR", "PHN-1", 0);
        inventory.transfer("DEL", "DEL", "LAP-1", 1);

        System.out.println("--- 8. Report and audit trail ---");
        inventory.printStock();
        System.out.println("Total LAP-1: " + inventory.getTotalStock("LAP-1"));
        inventory.printHistory("LAP-1");
    }
}
