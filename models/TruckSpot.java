package parkinglot.models;

import parkinglot.enums.SpotType;

/**
 * Concrete implementation for truck parking spots
 */
public class TruckSpot extends ParkingSpot {
    public TruckSpot(String spotId, int x, int y, int z) {
        super(spotId, SpotType.TRUCK_SPOT, x, y, z);
    }
}