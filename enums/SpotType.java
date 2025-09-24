package parkinglot.enums;

/**
 * Enum representing different types of parking spots
 * Each spot type has a capacity that determines which vehicles can park there
 */
public enum SpotType {
    BIKE_SPOT(1),
    CAR_SPOT(2),
    TRUCK_SPOT(3);

    private final int capacity;

    SpotType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * Get the corresponding vehicle type for this spot type
     * 
     * @return VehicleType that this spot is designed for
     */
    public VehicleType getDesignedVehicleType() {
        switch (this) {
            case BIKE_SPOT:
                return VehicleType.BIKE;
            case CAR_SPOT:
                return VehicleType.CAR;
            case TRUCK_SPOT:
                return VehicleType.TRUCK;
            default:
                throw new IllegalStateException("Unknown spot type: " + this);
        }
    }
}