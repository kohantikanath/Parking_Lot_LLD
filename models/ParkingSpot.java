package parkinglot.models;

import parkinglot.enums.SpotType;

/**
 * Abstract base class for parking spots
 * Includes 3D coordinates (x, y, z) where z represents floor
 * Implements Comparable for BST operations
 */
public abstract class ParkingSpot implements Comparable<ParkingSpot> {
    private final String spotId;
    private final SpotType spotType;
    private final int x; // X coordinate relative to entry gate
    private final int y; // Y coordinate relative to entry gate
    private final int z; // Floor number (0 = ground floor)

    private boolean isOccupied;
    private Vehicle parkedVehicle;

    public ParkingSpot(String spotId, SpotType spotType, int x, int y, int z) {
        this.spotId = spotId;
        this.spotType = spotType;
        this.x = x;
        this.y = y;
        this.z = z;
        this.isOccupied = false;
        this.parkedVehicle = null;
    }

    // Getters
    public String getSpotId() {
        return spotId;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    /**
     * Check if a vehicle can park in this spot
     * 
     * @param vehicle the vehicle to check
     * @return true if vehicle can park here
     */
    public boolean canParkVehicle(Vehicle vehicle) {
        return !isOccupied && vehicle.getVehicleType().canParkInSpot(spotType);
    }

    /**
     * Park a vehicle in this spot
     * 
     * @param vehicle the vehicle to park
     * @return true if parking was successful
     */
    public synchronized boolean parkVehicle(Vehicle vehicle) {
        if (canParkVehicle(vehicle)) {
            this.isOccupied = true;
            this.parkedVehicle = vehicle;
            return true;
        }
        return false;
    }

    /**
     * Remove vehicle from this spot
     * 
     * @return the vehicle that was parked, null if spot was empty
     */
    public synchronized Vehicle removeVehicle() {
        if (isOccupied) {
            Vehicle vehicle = this.parkedVehicle;
            this.isOccupied = false;
            this.parkedVehicle = null;
            return vehicle;
        }
        return null;
    }

    /**
     * Calculate distance from entry gate (0, 0, 0)
     * Used for BST ordering - closer spots are preferred
     * 
     * @return distance from entry gate
     */
    public double getDistanceFromEntry() {
        return Math.sqrt(x * x + y * y + z * z * 10); // Z weighted more for floor preference
    }

    @Override
    public int compareTo(ParkingSpot other) {
        // Primary: Compare by distance from entry
        double thisDistance = this.getDistanceFromEntry();
        double otherDistance = other.getDistanceFromEntry();

        if (thisDistance != otherDistance) {
            return Double.compare(thisDistance, otherDistance);
        }

        // Secondary: Compare by floor (prefer lower floors)
        if (this.z != other.z) {
            return Integer.compare(this.z, other.z);
        }

        // Tertiary: Compare by spot ID for consistent ordering
        return this.spotId.compareTo(other.spotId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ParkingSpot that = (ParkingSpot) obj;
        return spotId.equals(that.spotId);
    }

    @Override
    public int hashCode() {
        return spotId.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s[%s](%d,%d,%d) - %s",
                spotType, spotId, x, y, z, isOccupied ? "OCCUPIED" : "FREE");
    }
}