package parkinglot.models;

import parkinglot.enums.VehicleType;

/**
 * Represents a truck vehicle
 * Concrete implementation of Vehicle class
 */
public class Truck extends Vehicle {
    public Truck(String licensePlate) {
        super(licensePlate, VehicleType.TRUCK);
    }
}