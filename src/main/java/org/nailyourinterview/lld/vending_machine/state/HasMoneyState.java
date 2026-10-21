package org.nailyourinterview.lld.vending_machine.state;

import lombok.AllArgsConstructor;
import org.nailyourinterview.lld.vending_machine.enums.Denomination;
import org.nailyourinterview.lld.vending_machine.enums.MachineStatus;
import org.nailyourinterview.lld.vending_machine.model.CashBox;
import org.nailyourinterview.lld.vending_machine.model.Slot;
import org.nailyourinterview.lld.vending_machine.service.VendingMachine;

@AllArgsConstructor
public class HasMoneyState implements VendingState {
    private final VendingMachine machine;

    @Override
    public void insertMoney(Denomination money) {
        machine.addMoney(money);
        System.out.println("Inserted ₹" + money.getValue() + ". Balance: ₹" + machine.getBalance());
    }

    @Override
    public void selectProduct(String code) {
        Slot slot = machine.getInventory().getSlot(code);
        if (slot == null) {
            System.out.println("Invalid code " + code + ".");
            return;
        }
        if (slot.isEmpty()) {
            System.out.println(slot.getProduct().getName() + " is sold out. Pick another or cancel.");
            return;
        }

        int price = slot.getProduct().getPrice();
        int balance = machine.getBalance();
        if (balance < price) {
            System.out.println(slot.getProduct().getName() + " costs ₹" + price + ". Insert ₹" + (price - balance) + " more.");
            return;
        }

        // The inserted money can be part of the change, so add it to the box before checking
        CashBox cashBox = machine.getCashBox();
        cashBox.addAll(machine.getInsertedMoney());
        if (!cashBox.canMakeChange(balance - price)) {
            cashBox.removeAll(machine.getInsertedMoney());
            System.out.println("Cannot return ₹" + (balance - price) + " change. Insert exact amount or cancel.");
            return;
        }

        machine.setSelectedSlot(slot);
        machine.setState(new DispensingState(machine));
        machine.dispense();
    }

    @Override
    public void dispense() {
        System.out.println("Select a product first.");
    }

    @Override
    public void cancel() {
        System.out.println("Cancelled. Refund: " + machine.getInsertedMoney());
        machine.resetTransaction();
        machine.setState(new IdleState(machine));
    }

    @Override
    public MachineStatus getStatus() {
        return MachineStatus.HAS_MONEY;
    }
}
