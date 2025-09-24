package parkinglot.models;

/**
 * Represents an entry/exit gate with 3D coordinates
 * Used for calculating distances to parking spots
 */
public class Gate {
    private final String gateId;
    private final int x; // X coordinate
    private final int y; // Y coordinate
    private final int z; // Floor number (0 = ground floor)
    private final GateType gateType;

    public enum GateType {
        ENTRY, EXIT, BOTH
    }

    public Gate(String gateId, int x, int y, int z, GateType gateType) {
        this.gateId = gateId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.gateType = gateType;
    }

    // Getters
    public String getGateId() {
        return gateId;
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

    public GateType getGateType() {
        return gateType;
    }

    /**
     * Calculate distance to a parking spot
     * 
     * @param spot the parking spot
     * @return distance to the spot
     */
    public double getDistanceToSpot(ParkingSpot spot) {
        int dx = this.x - spot.getX();
        int dy = this.y - spot.getY();
        int dz = this.z - spot.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz * 10); // Z weighted more
    }

    @Override
    public String toString() {
        return String.format("Gate[%s](%d,%d,%d) - %s", gateId, x, y, z, gateType);
    }
}