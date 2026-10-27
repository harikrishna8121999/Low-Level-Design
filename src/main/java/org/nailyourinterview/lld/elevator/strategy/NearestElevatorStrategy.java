package org.nailyourinterview.lld.elevator.strategy;

import org.nailyourinterview.lld.elevator.enums.Direction;
import org.nailyourinterview.lld.elevator.model.Elevator;
import org.nailyourinterview.lld.elevator.model.Request;

import java.util.Comparator;
import java.util.List;

public class NearestElevatorStrategy implements SchedulingStrategy {

    @Override
    public Elevator select(List<Elevator> elevators, Request request) {
        return elevators.stream()
                .filter(Elevator::isInService)
                .min(Comparator.comparingInt(e -> cost(e, request)))
                .orElse(null);
    }

    // Floors the elevator must travel before it reaches the caller
    private int cost(Elevator e, Request r) {
        int here = e.getCurrentFloor();
        int target = r.getFloor();
        if (e.getDirection() == Direction.IDLE) return Math.abs(here - target);

        boolean onTheWay = e.getDirection() == r.getDirection()
                && (r.getDirection() == Direction.UP ? here <= target : here >= target);
        if (onTheWay) return Math.abs(here - target);

        // It must finish its trip first, then come back
        int end = e.lastStop();
        return Math.abs(here - end) + Math.abs(end - target);
    }
}
