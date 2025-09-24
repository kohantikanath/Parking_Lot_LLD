package parkinglot.strategies;

import parkinglot.models.ParkingSpot;
import parkinglot.models.Vehicle;
import java.util.List;

/**
 * Strategy interface for parking spot allocation
 * Follows Strategy Design Pattern
 */
public interface ParkingAllocationStrategy {

    /**
     * Find the best available parking spot for a vehicle
     * 
     * @param availableSpots list of available parking spots
     * @param vehicle        the vehicle to park
     * @return the best parking spot, or null if none available
     */
    ParkingSpot findParkingSpot(List<ParkingSpot> availableSpots, Vehicle vehicle);

    /**
     * Get the name of this allocation strategy
     * 
     * @return strategy name
     */
    String getStrategyName();

    /**
     * Get the time complexity of this strategy
     * 
     * @return time complexity description
     */
    String getTimeComplexity();
}