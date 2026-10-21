package org.nailyourinterview.lld.vending_machine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Slot {
    private final String code;
    private final Product product;
    private int quantity;

    public boolean isEmpty() {
        return quantity == 0;
    }

    public void decrement() {
        if (quantity == 0) throw new IllegalStateException("Slot " + code + " is empty");
        quantity--;
    }

    public void restock(int count) {
        quantity += count;
    }
}
