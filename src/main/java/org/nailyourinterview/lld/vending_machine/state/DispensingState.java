package org.nailyourinterview.lld.vending_machine.state;

import lombok.AllArgsConstructor;
import org.nailyourinterview.lld.vending_machine.enums.Denomination;
import org.nailyourinterview.lld.vending_machine.enums.MachineStatus;
import org.nailyourinterview.lld.vending_machine.model.Slot;
import org.nailyourinterview.lld.vending_machine.service.VendingMachine;

import java.util.Map;

@AllArgsConstructor
public class DispensingState implements VendingState {
    private final VendingMachine machine;

    @Override
    public void insertMoney(Denomination money) {
        System.out.println("Dispensing. Please wait.");
    }

    @Override
    public void selectProduct(String code) {
        System.out.println("Dispensing. Please wait.");
    }

    @Override
    public void dispense() {
        Slot slot = machine.getSelectedSlot();
        int change = machine.getBalance() - slot.getProduct().getPrice();

        slot.decrement();
        System.out.println("Dispensed " + slot.getProduct().getName() + ".");

        if (change > 0) {
            Map<Denomination, Integer> coins = machine.getCashBox().makeChange(change);
            System.out.println("Change ₹" + change + ": " + coins);
        }

        machine.resetTransaction();
        machine.setState(new IdleState(machine));
    }

    @Override
    public void cancel() {
        System.out.println("Too late to cancel. Dispensing.");
    }

    @Override
    public MachineStatus getStatus() {
        return MachineStatus.DISPENSING;
    }
}
