package parkinglot.patterns;

import parkinglot.models.*;
import parkinglot.strategies.ParkingAllocationStrategy;
import parkinglot.strategies.BasicParkingAllocationStrategy;
import parkinglot.enums.SpotType;

/**
 * Builder pattern implementation for creating ParkingLot configurations
 * Follows Builder Design Pattern for complex object creation
 */
public class ParkingLotBuilder {

    private String name = "Default Parking Lot";
    private ParkingAllocationStrategy strategy = new BasicParkingAllocationStrategy();
    private ParkingLot parkingLot;

    public ParkingLotBuilder() {
        // Reset singleton to create fresh instance
        ParkingLot.resetInstance();
    }

    /**
     * Set the name of the parking lot
     * 
     * @param name parking lot name
     * @return builder instance
     */
    public ParkingLotBuilder withName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
        return this;
    }

    /**
     * Set the allocation strategy
     * 
     * @param strategy parking allocation strategy
     * @return builder instance
     */
    public ParkingLotBuilder withStrategy(ParkingAllocationStrategy strategy) {
        if (strategy != null) {
            this.strategy = strategy;
        }
        return this;
    }

    /**
     * Add observers to the parking lot
     * 
     * @param observers array of observers
     * @return builder instance
     */
    public ParkingLotBuilder withObservers(ParkingLotObserver... observers) {
        if (parkingLot == null) {
            build();
        }

        for (ParkingLotObserver observer : observers) {
            if (observer != null) {
                parkingLot.addObserver(observer);
            }
        }
        return this;
    }

    /**
     * Add gates to the parking lot
     * 
     * @param gates array of gates
     * @return builder instance
     */
    public ParkingLotBuilder withGates(Gate... gates) {
        if (parkingLot == null) {
            build();
        }

        for (Gate gate : gates) {
            if (gate != null) {
                parkingLot.addGate(gate);
            }
        }
        return this;
    }

    /**
     * Add a floor with specified number of spots per type
     * 
     * @param floorNumber floor number (z coordinate)
     * @param bikeSpots   number of bike spots
     * @param carSpots    number of car spots
     * @param truckSpots  number of truck spots
     * @return builder instance
     */
    public ParkingLotBuilder withFloor(int floorNumber, int bikeSpots, int carSpots, int truckSpots) {
        if (parkingLot == null) {
            build();
        }

        // Add bike spots
        for (int i = 1; i <= bikeSpots; i++) {
            ParkingSpot spot = ParkingSpotFactory.createParkingSpot(
                    SpotType.BIKE_SPOT,
                    String.format("B-%d-%03d", floorNumber, i),
                    i * 5,
                    10 + floorNumber * 5,
                    floorNumber);
            parkingLot.addParkingSpot(spot);
        }

        // Add car spots
        for (int i = 1; i <= carSpots; i++) {
            ParkingSpot spot = ParkingSpotFactory.createParkingSpot(
                    SpotType.CAR_SPOT,
                    String.format("C-%d-%03d", floorNumber, i),
                    i * 8,
                    20 + floorNumber * 5,
                    floorNumber);
            parkingLot.addParkingSpot(spot);
        }

        // Add truck spots
        for (int i = 1; i <= truckSpots; i++) {
            ParkingSpot spot = ParkingSpotFactory.createParkingSpot(
                    SpotType.TRUCK_SPOT,
                    String.format("T-%d-%03d", floorNumber, i),
                    i * 12,
                    30 + floorNumber * 5,
                    floorNumber);
            parkingLot.addParkingSpot(spot);
        }

        return this;
    }

    /**
     * Add multiple floors with same configuration
     * 
     * @param startFloor         starting floor number
     * @param numberOfFloors     number of floors to add
     * @param bikeSpotsPerFloor  bike spots per floor
     * @param carSpotsPerFloor   car spots per floor
     * @param truckSpotsPerFloor truck spots per floor
     * @return builder instance
     */
    public ParkingLotBuilder withFloors(int startFloor, int numberOfFloors,
            int bikeSpotsPerFloor, int carSpotsPerFloor, int truckSpotsPerFloor) {
        for (int floor = startFloor; floor < startFloor + numberOfFloors; floor++) {
            withFloor(floor, bikeSpotsPerFloor, carSpotsPerFloor, truckSpotsPerFloor);
        }
        return this;
    }

    /**
     * Add individual parking spots
     * 
     * @param spots array of parking spots
     * @return builder instance
     */
    public ParkingLotBuilder withSpots(ParkingSpot... spots) {
        if (parkingLot == null) {
            build();
        }

        for (ParkingSpot spot : spots) {
            if (spot != null) {
                parkingLot.addParkingSpot(spot);
            }
        }
        return this;
    }

    /**
     * Build the parking lot instance
     * 
     * @return configured ParkingLot instance
     */
    public ParkingLot build() {
        if (parkingLot == null) {
            parkingLot = ParkingLot.getInstance(name, strategy);
        }
        return parkingLot;
    }

    /**
     * Create a small parking lot configuration
     * 
     * @return builder with small lot setup
     */
    public static ParkingLotBuilder createSmallLot() {
        return new ParkingLotBuilder()
                .withName("Small Parking Lot")
                .withFloor(0, 5, 10, 2);
    }

    /**
     * Create a medium parking lot configuration
     * 
     * @return builder with medium lot setup
     */
    public static ParkingLotBuilder createMediumLot() {
        return new ParkingLotBuilder()
                .withName("Medium Parking Lot")
                .withFloors(0, 2, 10, 20, 5);
    }

    /**
     * Create a large parking lot configuration
     * 
     * @return builder with large lot setup
     */
    public static ParkingLotBuilder createLargeLot() {
        return new ParkingLotBuilder()
                .withName("Large Parking Lot")
                .withFloors(0, 5, 20, 50, 10);
    }
}