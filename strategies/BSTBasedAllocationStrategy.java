package parkinglot.strategies;

import parkinglot.models.ParkingSpot;
import parkinglot.models.Vehicle;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.List;

/**
 * BST-based O(log N) parking allocation strategy
 * Uses TreeSet (Red-Black Tree) for efficient spot lookup
 * Maintains sorted order by distance from entry for optimal allocation
 */
public class BSTBasedAllocationStrategy implements ParkingAllocationStrategy {

    // TreeSets for each spot type, sorted by distance from entry
    private final TreeSet<ParkingSpot> bikeSpots;
    private final TreeSet<ParkingSpot> carSpots;
    private final TreeSet<ParkingSpot> truckSpots;

    /**
     * Custom comparator for parking spots
     * Orders by: distance from entry -> floor -> spot ID
     */
    private static final Comparator<ParkingSpot> SPOT_COMPARATOR = (spot1, spot2) -> {
        // Primary: Compare by distance from entry
        double distance1 = spot1.getDistanceFromEntry();
        double distance2 = spot2.getDistanceFromEntry();

        if (distance1 != distance2) {
            return Double.compare(distance1, distance2);
        }

        // Secondary: Compare by floor (prefer lower floors)
        if (spot1.getZ() != spot2.getZ()) {
            return Integer.compare(spot1.getZ(), spot2.getZ());
        }

        // Tertiary: Compare by spot ID for consistent ordering
        return spot1.getSpotId().compareTo(spot2.getSpotId());
    };

    public BSTBasedAllocationStrategy() {
        this.bikeSpots = new TreeSet<>(SPOT_COMPARATOR);
        this.carSpots = new TreeSet<>(SPOT_COMPARATOR);
        this.truckSpots = new TreeSet<>(SPOT_COMPARATOR);
    }

    /**
     * Initialize the BST with available spots
     * Should be called when parking lot is set up or spots are updated
     */
    public void initializeWithSpots(List<ParkingSpot> allSpots) {
        bikeSpots.clear();
        carSpots.clear();
        truckSpots.clear();

        for (ParkingSpot spot : allSpots) {
            if (!spot.isOccupied()) {
                addSpotToTree(spot);
            }
        }
    }

    /**
     * Add a spot to the appropriate tree
     */
    public void addSpot(ParkingSpot spot) {
        if (!spot.isOccupied()) {
            addSpotToTree(spot);
        }
    }

    /**
     * Remove a spot from the appropriate tree
     */
    public void removeSpot(ParkingSpot spot) {
        removeSpotFromTree(spot);
    }

    private void addSpotToTree(ParkingSpot spot) {
        switch (spot.getSpotType()) {
            case BIKE_SPOT:
                bikeSpots.add(spot);
                break;
            case CAR_SPOT:
                carSpots.add(spot);
                break;
            case TRUCK_SPOT:
                truckSpots.add(spot);
                break;
        }
    }

    private void removeSpotFromTree(ParkingSpot spot) {
        switch (spot.getSpotType()) {
            case BIKE_SPOT:
                bikeSpots.remove(spot);
                break;
            case CAR_SPOT:
                carSpots.remove(spot);
                break;
            case TRUCK_SPOT:
                truckSpots.remove(spot);
                break;
        }
    }

    @Override
    public ParkingSpot findParkingSpot(List<ParkingSpot> availableSpots, Vehicle vehicle) {
        // Update BST with current available spots
        initializeWithSpots(availableSpots);

        // First try: Find exact spot type match
        ParkingSpot exactMatch = findExactSpotTypeMatch(vehicle);
        if (exactMatch != null) {
            return exactMatch;
        }

        // Second try: Find larger spot that can accommodate the vehicle
        return findLargerSpotMatch(vehicle);
    }

    /**
     * Find exact spot type match using BST
     * Time Complexity: O(1) - TreeSet.first() operation
     */
    private ParkingSpot findExactSpotTypeMatch(Vehicle vehicle) {
        TreeSet<ParkingSpot> targetTree = getTreeForVehicleType(vehicle);

        if (targetTree != null && !targetTree.isEmpty()) {
            // Return the closest available spot (first in sorted order)
            return targetTree.first();
        }

        return null;
    }

    /**
     * Find larger spot that can accommodate the vehicle
     * Time Complexity: O(1) for each spot type check
     */
    private ParkingSpot findLargerSpotMatch(Vehicle vehicle) {
        switch (vehicle.getVehicleType()) {
            case BIKE:
                // Bike can use car or truck spots if bike spots are full
                if (!carSpots.isEmpty()) {
                    return carSpots.first();
                }
                if (!truckSpots.isEmpty()) {
                    return truckSpots.first();
                }
                break;

            case CAR:
                // Car can use truck spots if car spots are full
                if (!truckSpots.isEmpty()) {
                    return truckSpots.first();
                }
                break;

            case TRUCK:
                // Truck can only use truck spots
                break;
        }

        return null;
    }

    /**
     * Get the appropriate tree for a vehicle type
     */
    private TreeSet<ParkingSpot> getTreeForVehicleType(Vehicle vehicle) {
        switch (vehicle.getVehicleType()) {
            case BIKE:
                return bikeSpots;
            case CAR:
                return carSpots;
            case TRUCK:
                return truckSpots;
            default:
                return null;
        }
    }

    @Override
    public String getStrategyName() {
        return "BST-Based Allocation Strategy";
    }

    @Override
    public String getTimeComplexity() {
        return "O(log N) - Binary Search Tree operations with TreeSet";
    }

    /**
     * Get statistics about current spot availability
     */
    public String getAvailabilityStats() {
        return String.format("Available spots - Bikes: %d, Cars: %d, Trucks: %d",
                bikeSpots.size(), carSpots.size(), truckSpots.size());
    }
}