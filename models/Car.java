package parkinglot.models;

import parkinglot.enums.VehicleType;

/**
 * Represents a car vehicle
 * Concrete implementation of Vehicle class
 */
public class Car extends Vehicle {
    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
}