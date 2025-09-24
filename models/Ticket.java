package parkinglot.models;

import java.time.LocalDateTime;

/**
 * Represents a parking ticket
 * Contains parking information and is used for billing
 */
public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double amount;
    private boolean isPaid;

    public Ticket(String ticketId, Vehicle vehicle, ParkingSpot parkingSpot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.entryTime = LocalDateTime.now();
        this.exitTime = null;
        this.amount = 0.0;
        this.isPaid = false;
    }

    // Getters
    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    // Setters
    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaid(boolean paid) {
        this.isPaid = paid;
    }

    /**
     * Calculate parking duration in hours
     * 
     * @return duration in hours
     */
    public long getParkingDurationHours() {
        LocalDateTime endTime = exitTime != null ? exitTime : LocalDateTime.now();
        return java.time.Duration.between(entryTime, endTime).toHours();
    }

    @Override
    public String toString() {
        return String.format("Ticket[%s] - %s at %s (Entry: %s)",
                ticketId, vehicle, parkingSpot.getSpotId(), entryTime);
    }
}