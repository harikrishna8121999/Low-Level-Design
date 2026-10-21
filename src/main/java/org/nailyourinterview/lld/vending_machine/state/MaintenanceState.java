package org.nailyourinterview.lld.vending_machine.state;

import lombok.AllArgsConstructor;
import org.nailyourinterview.lld.vending_machine.enums.Denomination;
import org.nailyourinterview.lld.vending_machine.enums.MachineStatus;
import org.nailyourinterview.lld.vending_machine.service.VendingMachine;

@AllArgsConstructor
public class MaintenanceState implements VendingState {
    private final VendingMachine machine;

    @Override
    public void insertMoney(Denomination money) {
        System.out.println("Out of service. ₹" + money.getValue() + " returned.");
    }

    @Override
    public void selectProduct(String code) {
        System.out.println("Out of service.");
    }

    @Override
    public void dispense() {
        System.out.println("Out of service.");
    }

    @Override
    public void cancel() {
        System.out.println("Out of service.");
    }

    @Override
    public MachineStatus getStatus() {
        return MachineStatus.MAINTENANCE;
    }
}
