package org.nailyourinterview.lld.elevator.strategy;

import org.nailyourinterview.lld.elevator.model.Elevator;
import org.nailyourinterview.lld.elevator.model.Request;

import java.util.List;

public interface SchedulingStrategy {
    // Returns null when no elevator can take the request
    Elevator select(List<Elevator> elevators, Request request);
}
