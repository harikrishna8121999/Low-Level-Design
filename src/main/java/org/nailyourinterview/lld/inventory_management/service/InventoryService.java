package org.nailyourinterview.lld.inventory_management.service;

import org.nailyourinterview.lld.inventory_management.enums.TransactionType;
import org.nailyourinterview.lld.inventory_management.model.Product;
import org.nailyourinterview.lld.inventory_management.model.StockTransaction;
import org.nailyourinterview.lld.inventory_management.model.Warehouse;
import org.nailyourinterview.lld.inventory_management.observer.StockObserver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {
    private final Map<String, Product> products = new HashMap<>();          // sku -> product
    private final Map<String, Warehouse> warehouses = new LinkedHashMap<>(); // id -> warehouse
    private final List<StockTransaction> history = new ArrayList<>();
    private final List<StockObserver> observers = new ArrayList<>();

    // ----- set up -----

    public void addProduct(Product product) {
        products.put(product.getSku(), product);
        System.out.println("Product " + product.getSku() + " added: " + product.getName());
    }

    public void addWarehouse(Warehouse warehouse) {
        warehouses.put(warehouse.getId(), warehouse);
        System.out.println("Warehouse " + warehouse.getId() + " added: " + warehouse.getCity());
    }

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    // ----- stock in: goods arrive from a supplier -----

    public void addStock(String warehouseId, String sku, int quantity) {
        Warehouse warehouse = findWarehouse(warehouseId);
        if (warehouse == null || findProduct(sku) == null || !isPositive(quantity)) return;

        int now = warehouse.addStock(sku, quantity);
        record(TransactionType.STOCK_IN, sku, "supplier", warehouseId, quantity);
        System.out.println("+" + quantity + " " + sku + " -> " + warehouseId + " (now " + now + ")");
    }

    // ----- stock out: an order ships to a customer -----

    public boolean removeStock(String warehouseId, String sku, int quantity) {
        Warehouse warehouse = findWarehouse(warehouseId);
        Product product = findProduct(sku);
        if (warehouse == null || product == null || !isPositive(quantity)) return false;

        int left = warehouse.removeStock(sku, quantity);
        if (left < 0) {
            System.out.println("Not enough " + sku + " in " + warehouseId + ": have "
                    + warehouse.getQuantity(sku) + ", need " + quantity + ".");
            return false;
        }
        record(TransactionType.STOCK_OUT, sku, warehouseId, "customer", quantity);
        System.out.println("-" + quantity + " " + sku + " <- " + warehouseId + " (now " + left + ")");
        checkLowStock(warehouse, product, left + quantity, left);
        return true;
    }

    // ----- transfer: move stock between two warehouses -----

    public boolean transfer(String fromId, String toId, String sku, int quantity) {
        if (fromId.equals(toId)) {
            System.out.println("Source and target are the same warehouse.");
            return false;
        }
        Warehouse from = findWarehouse(fromId);
        Warehouse to = findWarehouse(toId);
        Product product = findProduct(sku);
        if (from == null || to == null || product == null || !isPositive(quantity)) return false;

        int left = from.removeStock(sku, quantity);
        if (left < 0) {
            System.out.println("Not enough " + sku + " in " + fromId + " to move " + quantity + ".");
            return false;
        }
        int now = to.addStock(sku, quantity);
        record(TransactionType.TRANSFER, sku, fromId, toId, quantity);
        System.out.println("Moved " + quantity + " " + sku + ": " + fromId + " (now " + left + ") -> "
                + toId + " (now " + now + ")");
        checkLowStock(from, product, left + quantity, left);
        return true;
    }

    // ----- queries -----

    public int getTotalStock(String sku) {
        return warehouses.values().stream().mapToInt(w -> w.getQuantity(sku)).sum();
    }

    public void printStock() {
        StringBuilder head = new StringBuilder(String.format("%-6s %-9s", "SKU", "Name"));
        warehouses.keySet().forEach(id -> head.append(String.format(" %5s", id)));
        System.out.println(head.append("  Total"));

        List<Product> sorted = products.values().stream().sorted(Comparator.comparing(Product::getSku)).toList();
        for (Product p : sorted) {
            StringBuilder row = new StringBuilder(String.format("%-6s %-9s", p.getSku(), p.getName()));
            warehouses.values().forEach(w -> row.append(String.format(" %5d", w.getQuantity(p.getSku()))));
            System.out.println(row.append(String.format("  %5d", getTotalStock(p.getSku()))));
        }
    }

    public void printHistory(String sku) {
        System.out.println("History of " + sku + ":");
        history.stream().filter(t -> t.getSku().equals(sku)).forEach(t -> System.out.println("  " + t));
    }

    // ----- helpers -----

    // Fire once, when the stock crosses the reorder level (not on every sale below it)
    private void checkLowStock(Warehouse warehouse, Product product, int before, int after) {
        int level = product.getReorderLevel();
        if (before > level && after <= level) {
            observers.forEach(o -> o.onLowStock(warehouse, product, after));
        }
    }

    private void record(TransactionType type, String sku, String from, String to, int quantity) {
        history.add(new StockTransaction(history.size() + 1, type, sku, from, to, quantity));
    }

    private Warehouse findWarehouse(String id) {
        Warehouse warehouse = warehouses.get(id);
        if (warehouse == null) System.out.println("No warehouse " + id + ".");
        return warehouse;
    }

    private Product findProduct(String sku) {
        Product product = products.get(sku);
        if (product == null) System.out.println("No product " + sku + ".");
        return product;
    }

    private boolean isPositive(int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive, got " + quantity + ".");
            return false;
        }
        return true;
    }
}
