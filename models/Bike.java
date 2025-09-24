package parkinglot.models;

import parkinglot.enums.VehicleType;

/**
 * Represents a bike vehicle
 * Concrete implementation of Vehicle class
 */
public class Bike extends Vehicle {
    public Bike(String licensePlate) {
        super(licensePlate, VehicleType.BIKE);
    }
}