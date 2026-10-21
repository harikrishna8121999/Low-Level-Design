package org.nailyourinterview.lld.vending_machine.state;

import org.nailyourinterview.lld.vending_machine.enums.Denomination;
import org.nailyourinterview.lld.vending_machine.enums.MachineStatus;

public interface VendingState {
    void insertMoney(Denomination money);
    void selectProduct(String code);
    void dispense();
    void cancel();
    MachineStatus getStatus();
}
