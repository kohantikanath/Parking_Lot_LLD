package parkinglot.enums;

/**
 * Enum representing different types of vehicles
 * Used for vehicle classification and spot assignment
 */
public enum VehicleType {
    BIKE(1),
    CAR(2),
    TRUCK(3);

    private final int size;

    VehicleType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    /**
     * Check if this vehicle type can park in a spot type
     * 
     * @param spotType the spot type to check
     * @return true if vehicle can park in the spot
     */
    public boolean canParkInSpot(SpotType spotType) {
        return this.size <= spotType.getCapacity();
    }
}