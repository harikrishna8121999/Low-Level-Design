package org.nailyourinterview.lld.vending_machine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Denomination {
    COIN_1(1),
    COIN_2(2),
    COIN_5(5),
    COIN_10(10),
    NOTE_20(20),
    NOTE_50(50),
    NOTE_100(100);

    private final int value;
}
