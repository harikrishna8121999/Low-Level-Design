package org.nailyourinterview.lld.vending_machine.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Inventory {
    private final Map<String, Slot> slots = new LinkedHashMap<>();

    public void addSlot(Slot slot) {
        slots.put(slot.getCode(), slot);
    }

    public Slot getSlot(String code) {
        return slots.get(code);
    }

    public void restock(String code, int count) {
        Slot slot = slots.get(code);
        if (slot == null) {
            System.out.println("No slot " + code);
            return;
        }
        slot.restock(count);
    }

    public void print() {
        slots.values().forEach(s -> System.out.println(" " + s.getCode() + " " + s.getProduct().getName()
                + " ₹" + s.getProduct().getPrice() + " x" + s.getQuantity()));
    }
}
