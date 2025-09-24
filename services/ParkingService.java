package parkinglot.services;

import parkinglot.models.*;
import parkinglot.strategies.ParkingAllocationStrategy;
import parkinglot.patterns.ParkingLotObserver;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Core service for parking operations
 * Integrates allocation strategies and observer pattern
 * Follows Single Responsibility and Dependency Inversion principles
 */
public class ParkingService {

    private final ParkingAllocationStrategy allocationStrategy;
    private final List<ParkingLotObserver> observers;
    private final Map<String, Ticket> activeTickets; // license plate -> ticket
    private final AtomicInteger ticketCounter;
    private boolean wasFull;

    public ParkingService(ParkingAllocationStrategy allocationStrategy) {
        this.allocationStrategy = allocationStrategy;
        this.observers = new ArrayList<>();
        this.activeTickets = new HashMap<>();
        this.ticketCounter = new AtomicInteger(1);
        this.wasFull = false;
    }

    /**
     * Add an observer to the service
     * 
     * @param observer the observer to add
     */
    public void addObserver(ParkingLotObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Remove an observer from the service
     * 
     * @param observer the observer to remove
     */
    public void removeObserver(ParkingLotObserver observer) {
        observers.remove(observer);
    }

    /**
     * Park a vehicle in the parking lot
     * 
     * @param vehicle        the vehicle to park
     * @param availableSpots list of available parking spots
     * @return parking ticket if successful, null otherwise
     */
    public synchronized Ticket parkVehicle(Vehicle vehicle, List<ParkingSpot> availableSpots) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        // Check if vehicle is already parked
        if (activeTickets.containsKey(vehicle.getLicensePlate())) {
            System.out.println("Vehicle " + vehicle.getLicensePlate() + " is already parked!");
            return null;
        }

        // Find parking spot using allocation strategy
        ParkingSpot spot = allocationStrategy.findParkingSpot(availableSpots, vehicle);

        if (spot == null) {
            System.out.println("No available parking spot for vehicle: " + vehicle);
            notifyParkingLotFull(availableSpots);
            return null;
        }

        // Park the vehicle
        if (spot.parkVehicle(vehicle)) {
            // Generate ticket
            String ticketId = generateTicketId();
            Ticket ticket = new Ticket(ticketId, vehicle, spot);
            activeTickets.put(vehicle.getLicensePlate(), ticket);

            // Notify observers
            notifyVehicleParked(vehicle, spot);

            // Check if this was the last available spot
            checkAndNotifyIfFull(availableSpots);

            return ticket;
        }

        return null;
    }

    /**
     * Remove a vehicle from parking lot
     * 
     * @param licensePlate   the license plate of the vehicle
     * @param availableSpots list of available spots (will be updated)
     * @return the ticket of the exited vehicle, null if not found
     */
    public synchronized Ticket exitVehicle(String licensePlate, List<ParkingSpot> availableSpots) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be null or empty");
        }

        Ticket ticket = activeTickets.remove(licensePlate.trim().toUpperCase());

        if (ticket == null) {
            System.out.println("No active parking ticket found for vehicle: " + licensePlate);
            return null;
        }

        ParkingSpot spot = ticket.getParkingSpot();
        Vehicle vehicle = spot.removeVehicle();

        if (vehicle != null) {
            // Notify observers
            notifyVehicleExit(vehicle, spot);

            // Check if parking lot was full and now has space
            checkAndNotifyIfAvailable(availableSpots);

            return ticket;
        }

        return null;
    }

    /**
     * Get active ticket for a vehicle
     * 
     * @param licensePlate the license plate
     * @return the active ticket or null
     */
    public Ticket getActiveTicket(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            return null;
        }
        return activeTickets.get(licensePlate.trim().toUpperCase());
    }

    /**
     * Get all active tickets
     * 
     * @return map of license plate to ticket
     */
    public Map<String, Ticket> getAllActiveTickets() {
        return new HashMap<>(activeTickets);
    }

    /**
     * Get parking statistics
     * 
     * @param allSpots all parking spots in the lot
     * @return statistics string
     */
    public String getParkingStats(List<ParkingSpot> allSpots) {
        int totalSpots = allSpots.size();
        int occupiedSpots = activeTickets.size();
        int availableSpots = totalSpots - occupiedSpots;

        return String.format(
                "Parking Stats - Total: %d, Occupied: %d, Available: %d\nStrategy: %s (%s)",
                totalSpots, occupiedSpots, availableSpots,
                allocationStrategy.getStrategyName(),
                allocationStrategy.getTimeComplexity());
    }

    private void notifyVehicleParked(Vehicle vehicle, ParkingSpot spot) {
        for (ParkingLotObserver observer : observers) {
            observer.onVehicleParked(vehicle, spot);
        }
    }

    private void notifyVehicleExit(Vehicle vehicle, ParkingSpot spot) {
        for (ParkingLotObserver observer : observers) {
            observer.onVehicleExit(vehicle, spot);
        }
    }

    private void notifyParkingLotFull(List<ParkingSpot> availableSpots) {
        if (availableSpots.isEmpty() && !wasFull) {
            wasFull = true;
            for (ParkingLotObserver observer : observers) {
                observer.onParkingLotFull();
            }
        }
    }

    private void checkAndNotifyIfFull(List<ParkingSpot> availableSpots) {
        long availableCount = availableSpots.stream().filter(spot -> !spot.isOccupied()).count();
        if (availableCount == 0 && !wasFull) {
            wasFull = true;
            for (ParkingLotObserver observer : observers) {
                observer.onParkingLotFull();
            }
        }
    }

    private void checkAndNotifyIfAvailable(List<ParkingSpot> availableSpots) {
        if (wasFull) {
            long availableCount = availableSpots.stream().filter(spot -> !spot.isOccupied()).count();
            if (availableCount > 0) {
                wasFull = false;
                for (ParkingLotObserver observer : observers) {
                    observer.onParkingLotAvailable();
                }
            }
        }
    }

    private String generateTicketId() {
        return String.format("TKT-%06d", ticketCounter.getAndIncrement());
    }
}