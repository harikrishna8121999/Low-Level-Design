package org.nailyourinterview.lld.inventory_management.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nailyourinterview.lld.inventory_management.enums.TransactionType;

// One line in the audit trail. Never changed after it is written.
@Getter
@AllArgsConstructor
public class StockTransaction {
    private final int id;
    private final TransactionType type;
    private final String sku;
    private final String from;
    private final String to;
    private final int quantity;

    @Override
    public String toString() {
        return String.format("#%-2d %-9s %-6s %4d  %s -> %s", id, type, sku, quantity, from, to);
    }
}
