package parkinglot.strategies;

import parkinglot.models.ParkingSpot;
import parkinglot.models.Vehicle;
import parkinglot.enums.SpotType;
import java.util.List;

/**
 * Basic O(N) parking allocation strategy
 * Iterates through all available spots linearly
 * Prioritizes exact spot type match, then allows smaller vehicles in larger
 * spots
 */
public class BasicParkingAllocationStrategy implements ParkingAllocationStrategy {

    @Override
    public ParkingSpot findParkingSpot(List<ParkingSpot> availableSpots, Vehicle vehicle) {
        if (availableSpots == null || availableSpots.isEmpty()) {
            return null;
        }

        // First pass: Find exact spot type match
        ParkingSpot exactMatch = findExactSpotTypeMatch(availableSpots, vehicle);
        if (exactMatch != null) {
            return exactMatch;
        }

        // Second pass: Find larger spot that can accommodate the vehicle
        return findLargerSpotMatch(availableSpots, vehicle);
    }

    /**
     * Find a spot that exactly matches the vehicle type
     * Time Complexity: O(N)
     */
    private ParkingSpot findExactSpotTypeMatch(List<ParkingSpot> availableSpots, Vehicle vehicle) {
        SpotType preferredSpotType = getPreferredSpotType(vehicle);

        for (ParkingSpot spot : availableSpots) {
            if (!spot.isOccupied() &&
                    spot.getSpotType() == preferredSpotType &&
                    spot.canParkVehicle(vehicle)) {
                return spot;
            }
        }
        return null;
    }

    /**
     * Find a larger spot that can accommodate the vehicle
     * Time Complexity: O(N)
     */
    private ParkingSpot findLargerSpotMatch(List<ParkingSpot> availableSpots, Vehicle vehicle) {
        ParkingSpot bestSpot = null;
        double minDistance = Double.MAX_VALUE;

        for (ParkingSpot spot : availableSpots) {
            if (!spot.isOccupied() && spot.canParkVehicle(vehicle)) {
                double distance = spot.getDistanceFromEntry();
                if (distance < minDistance) {
                    minDistance = distance;
                    bestSpot = spot;
                }
            }
        }

        return bestSpot;
    }

    /**
     * Get the preferred spot type for a vehicle
     */
    private SpotType getPreferredSpotType(Vehicle vehicle) {
        switch (vehicle.getVehicleType()) {
            case BIKE:
                return SpotType.BIKE_SPOT;
            case CAR:
                return SpotType.CAR_SPOT;
            case TRUCK:
                return SpotType.TRUCK_SPOT;
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicle.getVehicleType());
        }
    }

    @Override
    public String getStrategyName() {
        return "Basic Linear Search Strategy";
    }

    @Override
    public String getTimeComplexity() {
        return "O(N) - Linear search through all available spots";
    }
}