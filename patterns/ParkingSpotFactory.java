package parkinglot.patterns;

import parkinglot.models.*;
import parkinglot.enums.SpotType;

/**
 * Factory pattern implementation for creating parking spots
 * Follows Factory Design Pattern
 */
public class ParkingSpotFactory {

    /**
     * Create a parking spot based on type and coordinates
     * 
     * @param spotType the type of spot to create
     * @param spotId   unique identifier for the spot
     * @param x        X coordinate
     * @param y        Y coordinate
     * @param z        floor number (Z coordinate)
     * @return the created parking spot
     */
    public static ParkingSpot createParkingSpot(SpotType spotType, String spotId, int x, int y, int z) {
        if (spotId == null || spotId.trim().isEmpty()) {
            throw new IllegalArgumentException("Spot ID cannot be null or empty");
        }

        switch (spotType) {
            case BIKE_SPOT:
                return new BikeSpot(spotId.trim().toUpperCase(), x, y, z);
            case CAR_SPOT:
                return new CarSpot(spotId.trim().toUpperCase(), x, y, z);
            case TRUCK_SPOT:
                return new TruckSpot(spotId.trim().toUpperCase(), x, y, z);
            default:
                throw new IllegalArgumentException("Unknown spot type: " + spotType);
        }
    }

    /**
     * Create a parking spot based on type string and coordinates
     * 
     * @param spotTypeStr the type of spot as string
     * @param spotId      unique identifier for the spot
     * @param x           X coordinate
     * @param y           Y coordinate
     * @param z           floor number (Z coordinate)
     * @return the created parking spot
     */
    public static ParkingSpot createParkingSpot(String spotTypeStr, String spotId, int x, int y, int z) {
        if (spotTypeStr == null || spotTypeStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Spot type cannot be null or empty");
        }

        try {
            SpotType spotType = SpotType.valueOf(spotTypeStr.trim().toUpperCase());
            return createParkingSpot(spotType, spotId, x, y, z);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid spot type: " + spotTypeStr);
        }
    }
}