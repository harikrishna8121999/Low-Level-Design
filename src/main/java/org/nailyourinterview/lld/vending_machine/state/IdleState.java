package org.nailyourinterview.lld.vending_machine.state;

import lombok.AllArgsConstructor;
import org.nailyourinterview.lld.vending_machine.enums.Denomination;
import org.nailyourinterview.lld.vending_machine.enums.MachineStatus;
import org.nailyourinterview.lld.vending_machine.service.VendingMachine;

@AllArgsConstructor
public class IdleState implements VendingState {
    private final VendingMachine machine;

    @Override
    public void insertMoney(Denomination money) {
        machine.addMoney(money);
        System.out.println("Inserted ₹" + money.getValue() + ". Balance: ₹" + machine.getBalance());
        machine.setState(new HasMoneyState(machine));
    }

    @Override
    public void selectProduct(String code) {
        System.out.println("Insert money first.");
    }

    @Override
    public void dispense() {
        System.out.println("Insert money and select a product first.");
    }

    @Override
    public void cancel() {
        System.out.println("Nothing to cancel.");
    }

    @Override
    public MachineStatus getStatus() {
        return MachineStatus.IDLE;
    }
}
