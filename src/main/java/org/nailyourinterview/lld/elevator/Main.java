package org.nailyourinterview.lld.elevator;

import org.nailyourinterview.lld.elevator.enums.Direction;
import org.nailyourinterview.lld.elevator.service.ElevatorController;
import org.nailyourinterview.lld.elevator.strategy.NearestElevatorStrategy;

public class Main {
    public static void main(String[] args) {
        ElevatorController building = new ElevatorController(10, 2, new NearestElevatorStrategy());

        System.out.println("--- 1. Two hall calls: the strategy picks ---");
        building.requestElevator(4, Direction.UP);
        building.requestElevator(2, Direction.DOWN);
        building.runUntilIdle();

        System.out.println("--- 2. Riders press floors inside ---");
        building.selectFloor("E1", 7);
        building.selectFloor("E2", 0);
        building.runUntilIdle();

        System.out.println("--- 3. Pick up on the way ---");
        building.selectFloor("E2", 9);
        building.step();
        building.step();
        building.requestElevator(4, Direction.UP);
        building.runUntilIdle();

        System.out.println("--- 4. Stops above and below: keep direction, then turn ---");
        building.selectFloor("E1", 2);
        building.selectFloor("E1", 9);
        building.selectFloor("E1", 5);
        building.runUntilIdle();

        System.out.println("--- 5. Maintenance ---");
        building.setMaintenance("E2", true);
        building.requestElevator(6, Direction.DOWN);
        building.setMaintenance("E1", true);
        building.selectFloor("E2", 1);
        building.runUntilIdle();

        System.out.println("--- 6. Invalid input ---");
        building.requestElevator(12, Direction.UP);
        building.requestElevator(9, Direction.UP);
        building.selectFloor("E3", 2);

        System.out.println("--- 7. No elevator in service ---");
        building.setMaintenance("E1", true);
        building.requestElevator(3, Direction.UP);
        building.setMaintenance("E1", false);
        building.setMaintenance("E2", false);
        building.requestElevator(3, Direction.UP);
        building.runUntilIdle();
    }
}
