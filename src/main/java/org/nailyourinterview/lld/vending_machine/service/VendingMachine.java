package org.nailyourinterview.lld.vending_machine.service;

import lombok.Getter;
import lombok.Setter;
import org.nailyourinterview.lld.vending_machine.enums.Denomination;
import org.nailyourinterview.lld.vending_machine.enums.MachineStatus;
import org.nailyourinterview.lld.vending_machine.model.CashBox;
import org.nailyourinterview.lld.vending_machine.model.Inventory;
import org.nailyourinterview.lld.vending_machine.model.Slot;
import org.nailyourinterview.lld.vending_machine.state.IdleState;
import org.nailyourinterview.lld.vending_machine.state.MaintenanceState;
import org.nailyourinterview.lld.vending_machine.state.VendingState;

import java.util.ArrayList;
import java.util.List;

@Getter
public class VendingMachine {
    private final String id;
    private final Inventory inventory = new Inventory();
    private final CashBox cashBox = new CashBox();
    private final List<Denomination> insertedMoney = new ArrayList<>();
    @Setter private VendingState state;
    @Setter private Slot selectedSlot;

    public VendingMachine(String id) {
        this.id = id;
        this.state = new IdleState(this);
    }

    // ----- user actions: the current state decides what happens -----

    public void insertMoney(Denomination money) {
        state.insertMoney(money);
    }

    public void selectProduct(String code) {
        state.selectProduct(code);
    }

    public void dispense() {
        state.dispense();
    }

    public void cancel() {
        state.cancel();
    }

    // ----- operator actions -----

    public void startMaintenance() {
        if (state.getStatus() != MachineStatus.IDLE) {
            System.out.println("Finish the current transaction first.");
            return;
        }
        System.out.println("Maintenance started.");
        state = new MaintenanceState(this);
    }

    public void endMaintenance() {
        if (state.getStatus() != MachineStatus.MAINTENANCE) return;
        System.out.println("Maintenance finished.");
        state = new IdleState(this);
    }

    public void addSlot(Slot slot) {
        inventory.addSlot(slot);
    }

    public void restock(String code, int count) {
        if (state.getStatus() != MachineStatus.MAINTENANCE) {
            System.out.println("Restock only in maintenance mode.");
            return;
        }
        inventory.restock(code, count);
        System.out.println("Restocked " + code + " +" + count + ".");
    }

    public void loadCash(Denomination d, int count) {
        cashBox.add(d, count);
    }

    // ----- helpers used by the states -----

    public void addMoney(Denomination money) {
        insertedMoney.add(money);
    }

    public int getBalance() {
        return insertedMoney.stream().mapToInt(Denomination::getValue).sum();
    }

    public void resetTransaction() {
        insertedMoney.clear();
        selectedSlot = null;
    }

    public MachineStatus getStatus() {
        return state.getStatus();
    }
}
