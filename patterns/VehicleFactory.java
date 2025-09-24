package parkinglot.patterns;

import parkinglot.models.*;
import parkinglot.enums.VehicleType;

/**
 * Factory pattern implementation for creating vehicles
 * Follows Factory Design Pattern and Open/Closed Principle
 */
public class VehicleFactory {

    /**
     * Create a vehicle based on type and license plate
     * 
     * @param vehicleType  the type of vehicle to create
     * @param licensePlate the license plate
     * @return the created vehicle
     */
    public static Vehicle createVehicle(VehicleType vehicleType, String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be null or empty");
        }

        switch (vehicleType) {
            case BIKE:
                return new Bike(licensePlate.trim().toUpperCase());
            case CAR:
                return new Car(licensePlate.trim().toUpperCase());
            case TRUCK:
                return new Truck(licensePlate.trim().toUpperCase());
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        }
    }

    /**
     * Create a vehicle based on type string and license plate
     * 
     * @param vehicleTypeStr the type of vehicle as string
     * @param licensePlate   the license plate
     * @return the created vehicle
     */
    public static Vehicle createVehicle(String vehicleTypeStr, String licensePlate) {
        if (vehicleTypeStr == null || vehicleTypeStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle type cannot be null or empty");
        }

        try {
            VehicleType vehicleType = VehicleType.valueOf(vehicleTypeStr.trim().toUpperCase());
            return createVehicle(vehicleType, licensePlate);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid vehicle type: " + vehicleTypeStr);
        }
    }
}