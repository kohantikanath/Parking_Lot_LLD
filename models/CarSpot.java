package parkinglot.models;

import parkinglot.enums.SpotType;

/**
 * Concrete implementation for car parking spots
 */
public class CarSpot extends ParkingSpot {
    public CarSpot(String spotId, int x, int y, int z) {
        super(spotId, SpotType.CAR_SPOT, x, y, z);
    }
}