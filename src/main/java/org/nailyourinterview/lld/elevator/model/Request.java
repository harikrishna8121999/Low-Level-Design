package org.nailyourinterview.lld.elevator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.nailyourinterview.lld.elevator.enums.Direction;

// A hall call: a person on a floor presses UP or DOWN
@Getter
@AllArgsConstructor
public class Request {
    private final int floor;
    private final Direction direction;
}
