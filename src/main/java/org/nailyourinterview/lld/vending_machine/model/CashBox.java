package org.nailyourinterview.lld.vending_machine.model;

import org.nailyourinterview.lld.vending_machine.enums.Denomination;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CashBox {
    private final Map<Denomination, Integer> counts = new EnumMap<>(Denomination.class);

    public CashBox() {
        for (Denomination d : Denomination.values()) counts.put(d, 0);
    }

    public void add(Denomination d, int count) {
        counts.merge(d, count, Integer::sum);
    }

    public void addAll(List<Denomination> money) {
        money.forEach(d -> add(d, 1));
    }

    public void removeAll(List<Denomination> money) {
        money.forEach(d -> add(d, -1));
    }

    public boolean canMakeChange(int amount) {
        return plan(amount) != null;
    }

    // Removes the coins/notes from the box and returns them
    public Map<Denomination, Integer> makeChange(int amount) {
        Map<Denomination, Integer> change = plan(amount);
        if (change == null) throw new IllegalStateException("Cannot make change for " + amount);
        change.forEach((d, n) -> add(d, -n));
        return change;
    }

    // Greedy: largest denomination first, limited by what is in the box
    private Map<Denomination, Integer> plan(int amount) {
        Map<Denomination, Integer> change = new LinkedHashMap<>();
        Denomination[] all = Denomination.values();
        for (int i = all.length - 1; i >= 0 && amount > 0; i--) {
            Denomination d = all[i];
            int use = Math.min(amount / d.getValue(), counts.get(d));
            if (use > 0) {
                change.put(d, use);
                amount -= use * d.getValue();
            }
        }
        return amount == 0 ? change : null;
    }
}
