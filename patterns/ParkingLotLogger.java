package parkinglot.patterns;

import parkinglot.models.Vehicle;
import parkinglot.models.ParkingSpot;

/**
 * Concrete implementation of ParkingLotObserver
 * Logs parking lot events to console
 */
public class ParkingLotLogger implements ParkingLotObserver {

    @Override
    public void onVehicleParked(Vehicle vehicle, ParkingSpot spot) {
        System.out.println(String.format(
                "[PARK] Vehicle %s parked at spot %s (%d,%d,%d)",
                vehicle.getLicensePlate(),
                spot.getSpotId(),
                spot.getX(),
                spot.getY(),
                spot.getZ()));
    }

    @Override
    public void onVehicleExit(Vehicle vehicle, ParkingSpot spot) {
        System.out.println(String.format(
                "[EXIT] Vehicle %s left from spot %s (%d,%d,%d)",
                vehicle.getLicensePlate(),
                spot.getSpotId(),
                spot.getX(),
                spot.getY(),
                spot.getZ()));
    }

    @Override
    public void onParkingLotFull() {
        System.out.println("[ALERT] Parking lot is now FULL!");
    }

    @Override
    public void onParkingLotAvailable() {
        System.out.println("[INFO] Parking lot has available spaces.");
    }
}