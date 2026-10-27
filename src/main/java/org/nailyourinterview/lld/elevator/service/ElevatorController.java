package org.nailyourinterview.lld.elevator.service;

import lombok.Getter;
import lombok.Setter;
import org.nailyourinterview.lld.elevator.enums.Direction;
import org.nailyourinterview.lld.elevator.model.Elevator;
import org.nailyourinterview.lld.elevator.model.Request;
import org.nailyourinterview.lld.elevator.strategy.SchedulingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ElevatorController {
    private static final int MAX_TICKS = 100;

    private final int topFloor;
    private final List<Elevator> elevators = new ArrayList<>();
    @Setter private SchedulingStrategy strategy;
    private int tick;

    // Floors are 0 .. floors - 1. All elevators start at floor 0.
    public ElevatorController(int floors, int elevatorCount, SchedulingStrategy strategy) {
        this.topFloor = floors - 1;
        this.strategy = strategy;
        for (int i = 1; i <= elevatorCount; i++) {
            elevators.add(new Elevator("E" + i, 0));
        }
    }

    // ----- hall call: a person on a floor presses UP or DOWN -----

    public void requestElevator(int floor, Direction direction) {
        if (!isValidFloor(floor)) return;
        if (direction == Direction.IDLE
                || (floor == topFloor && direction == Direction.UP)
                || (floor == 0 && direction == Direction.DOWN)) {
            System.out.println("No " + direction + " button on floor " + floor + ".");
            return;
        }

        Elevator elevator = strategy.select(elevators, new Request(floor, direction));
        if (elevator == null) {
            System.out.println("No elevator available. Try again later.");
            return;
        }
        elevator.addStop(floor);
        System.out.println("Hall call " + floor + " " + direction + " -> " + elevator.getId());
    }

    // ----- car call: a rider inside presses a floor button -----

    public void selectFloor(String elevatorId, int floor) {
        Elevator elevator = find(elevatorId);
        if (elevator == null || !isValidFloor(floor)) return;
        if (!elevator.isInService()) {
            System.out.println(elevatorId + " is out of service.");
            return;
        }
        elevator.addStop(floor);
        System.out.println("Inside " + elevatorId + ": floor " + floor);
    }

    // ----- operator -----

    public void setMaintenance(String elevatorId, boolean on) {
        Elevator elevator = find(elevatorId);
        if (elevator == null) return;
        if (on && elevator.hasStops()) {
            System.out.println(elevatorId + " has stops left. Finish them first.");
            return;
        }
        elevator.setInService(!on);
        System.out.println(elevatorId + (on ? " out of service." : " back in service."));
    }

    // ----- time: every elevator moves one floor per tick -----

    public void step() {
        tick++;
        elevators.forEach(Elevator::step);
        String line = elevators.stream().map(Elevator::status).collect(Collectors.joining(" | "));
        System.out.println(String.format("t%02d  %s", tick, line).stripTrailing());
    }

    public void runUntilIdle() {
        for (int i = 0; i < MAX_TICKS && elevators.stream().anyMatch(Elevator::hasStops); i++) {
            step();
        }
    }

    private Elevator find(String elevatorId) {
        for (Elevator e : elevators) {
            if (e.getId().equals(elevatorId)) return e;
        }
        System.out.println("No elevator " + elevatorId + ".");
        return null;
    }

    private boolean isValidFloor(int floor) {
        if (floor < 0 || floor > topFloor) {
            System.out.println("Invalid floor " + floor + ". Floors are 0-" + topFloor + ".");
            return false;
        }
        return true;
    }
}
