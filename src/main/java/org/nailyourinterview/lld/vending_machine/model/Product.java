package org.nailyourinterview.lld.vending_machine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Product {
    private final String name;
    private final int price;
}
