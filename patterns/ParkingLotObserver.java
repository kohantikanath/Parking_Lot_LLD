package parkinglot.patterns;

import parkinglot.models.Vehicle;
import parkinglot.models.ParkingSpot;

/**
 * Observer interface for parking lot events
 * Follows Observer Design Pattern
 */
public interface ParkingLotObserver {

    /**
     * Called when a vehicle is parked
     * 
     * @param vehicle the parked vehicle
     * @param spot    the parking spot used
     */
    void onVehicleParked(Vehicle vehicle, ParkingSpot spot);

    /**
     * Called when a vehicle exits
     * 
     * @param vehicle the exiting vehicle
     * @param spot    the parking spot freed
     */
    void onVehicleExit(Vehicle vehicle, ParkingSpot spot);

    /**
     * Called when parking lot becomes full
     */
    void onParkingLotFull();

    /**
     * Called when parking lot has available spaces (was full before)
     */
    void onParkingLotAvailable();
}