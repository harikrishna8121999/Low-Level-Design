package org.nailyourinterview.lld.vending_machine;

import org.nailyourinterview.lld.vending_machine.enums.Denomination;
import org.nailyourinterview.lld.vending_machine.model.Product;
import org.nailyourinterview.lld.vending_machine.model.Slot;
import org.nailyourinterview.lld.vending_machine.service.VendingMachine;

public class Main {
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine("VM1");
        vm.addSlot(new Slot("A1", new Product("Coke", 25), 5));
        vm.addSlot(new Slot("A2", new Product("Lays", 20), 3));
        vm.addSlot(new Slot("B1", new Product("Water", 15), 0));
        vm.addSlot(new Slot("B2", new Product("KitKat", 30), 2));

        vm.loadCash(Denomination.COIN_1, 2);
        vm.loadCash(Denomination.COIN_2, 2);
        vm.loadCash(Denomination.COIN_5, 1);

        System.out.println("--- 1. Buy Coke (₹25) with ₹20 + ₹10 ---");
        vm.insertMoney(Denomination.NOTE_20);
        vm.insertMoney(Denomination.COIN_10);
        vm.selectProduct("A1");

        System.out.println("--- 2. Select before paying ---");
        vm.selectProduct("A2");

        System.out.println("--- 3. Not enough money, then top up ---");
        vm.insertMoney(Denomination.COIN_10);
        vm.selectProduct("A2");
        vm.insertMoney(Denomination.COIN_10);
        vm.selectProduct("A2");

        System.out.println("--- 4. Sold out, then cancel ---");
        vm.insertMoney(Denomination.NOTE_20);
        vm.selectProduct("B1");
        vm.cancel();

        System.out.println("--- 5. Machine cannot make change ---");
        vm.insertMoney(Denomination.NOTE_100);
        vm.selectProduct("B2");
        vm.cancel();

        System.out.println("--- 6. Maintenance: restock Water ---");
        vm.startMaintenance();
        vm.insertMoney(Denomination.COIN_10);
        vm.restock("B1", 4);
        vm.endMaintenance();

        System.out.println("--- 7. Buy Water (₹15) with ₹20 ---");
        vm.insertMoney(Denomination.NOTE_20);
        vm.selectProduct("B1");

        System.out.println("--- Stock ---");
        vm.getInventory().print();
    }
}
