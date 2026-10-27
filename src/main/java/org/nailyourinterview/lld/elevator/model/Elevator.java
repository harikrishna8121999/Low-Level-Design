package org.nailyourinterview.lld.elevator.model;

import lombok.Getter;
import lombok.Setter;
import org.nailyourinterview.lld.elevator.enums.Direction;

import java.util.TreeSet;

@Getter
public class Elevator {
    private final String id;
    private int currentFloor;
    private Direction direction = Direction.IDLE;
    private boolean doorsOpen;
    @Setter private boolean inService = true;
    private final TreeSet<Integer> stops = new TreeSet<>();

    public Elevator(String id, int startFloor) {
        this.id = id;
        this.currentFloor = startFloor;
    }

    public void addStop(int floor) {
        stops.add(floor);
        if (direction == Direction.IDLE && floor != currentFloor) {
            direction = floor > currentFloor ? Direction.UP : Direction.DOWN;
        }
    }

    public boolean hasStops() {
        return !stops.isEmpty();
    }

    // The farthest stop in the current direction: where this trip ends
    public int lastStop() {
        if (stops.isEmpty()) return currentFloor;
        return direction == Direction.DOWN ? stops.first() : stops.last();
    }

    // One tick = move one floor. LOOK: keep going while there are stops ahead, then turn.
    public void step() {
        doorsOpen = false;
        Integer target = nextStop();
        if (target == null) {
            direction = Direction.IDLE;
            return;
        }
        if (target == currentFloor) {
            stopHere();
            return;
        }
        direction = target > currentFloor ? Direction.UP : Direction.DOWN;
        currentFloor += direction == Direction.UP ? 1 : -1;
        if (stops.contains(currentFloor)) stopHere();
    }

    private Integer nextStop() {
        Integer above = stops.ceiling(currentFloor);
        Integer below = stops.floor(currentFloor);
        if (direction == Direction.UP && above != null) return above;
        if (direction == Direction.DOWN && below != null) return below;
        // Idle, or nothing left ahead: go to the nearest stop
        if (above == null) return below;
        if (below == null) return above;
        return above - currentFloor <= currentFloor - below ? above : below;
    }

    private void stopHere() {
        stops.remove(currentFloor);
        doorsOpen = true;
        if (stops.isEmpty()) direction = Direction.IDLE;
    }

    public String status() {
        String state = inService ? direction.name() : "OFF";
        return String.format("%s @%d %-4s %-10s %s", id, currentFloor, state, stops, doorsOpen ? "OPEN" : "    ");
    }
}
