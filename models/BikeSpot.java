package parkinglot.models;

import parkinglot.enums.SpotType;

/**
 * Concrete implementation for bike parking spots
 */
public class BikeSpot extends ParkingSpot {
    public BikeSpot(String spotId, int x, int y, int z) {
        super(spotId, SpotType.BIKE_SPOT, x, y, z);
    }
}