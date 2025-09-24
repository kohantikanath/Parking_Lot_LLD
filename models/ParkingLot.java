package parkinglot.models;

import parkinglot.services.ParkingService;
import parkinglot.services.PaymentService;
import parkinglot.strategies.ParkingAllocationStrategy;
import parkinglot.patterns.ParkingLotObserver;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Main ParkingLot class implementing Singleton pattern
 * Facade pattern - provides simplified interface to complex subsystem
 * Follows Single Responsibility Principle
 */
public class ParkingLot {

    // Singleton instance
    private static volatile ParkingLot instance;
    private static final Object lock = new Object();

    // Core components
    private final List<ParkingSpot> allSpots;
    private final List<Gate> gates;
    private final ParkingService parkingService;
    private final PaymentService paymentService;
    private final String name;

    // Private constructor for singleton
    private ParkingLot(String name, ParkingAllocationStrategy allocationStrategy) {
        this.name = name;
        this.allSpots = new ArrayList<>();
        this.gates = new ArrayList<>();
        this.parkingService = new ParkingService(allocationStrategy);
        this.paymentService = new PaymentService();
    }

    /**
     * Get singleton instance of ParkingLot
     * Thread-safe double-checked locking
     * 
     * @param name               name of the parking lot
     * @param allocationStrategy allocation strategy to use
     * @return singleton instance
     */
    public static ParkingLot getInstance(String name, ParkingAllocationStrategy allocationStrategy) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ParkingLot(name, allocationStrategy);
                }
            }
        }
        return instance;
    }

    /**
     * Get existing singleton instance (throws exception if not initialized)
     * 
     * @return singleton instance
     */
    public static ParkingLot getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ParkingLot not initialized. Call getInstance(name, strategy) first.");
        }
        return instance;
    }

    // Add spots and gates
    public void addParkingSpot(ParkingSpot spot) {
        if (spot != null && !allSpots.contains(spot)) {
            allSpots.add(spot);
        }
    }

    public void addGate(Gate gate) {
        if (gate != null && !gates.contains(gate)) {
            gates.add(gate);
        }
    }

    public void addObserver(ParkingLotObserver observer) {
        parkingService.addObserver(observer);
    }

    public void removeObserver(ParkingLotObserver observer) {
        parkingService.removeObserver(observer);
    }

    /**
     * Park a vehicle - Facade method
     * 
     * @param vehicle the vehicle to park
     * @return parking ticket if successful
     */
    public Ticket parkVehicle(Vehicle vehicle) {
        List<ParkingSpot> availableSpots = getAvailableSpots();
        return parkingService.parkVehicle(vehicle, availableSpots);
    }

    /**
     * Exit a vehicle - Facade method
     * 
     * @param licensePlate license plate of the vehicle
     * @return ticket of exited vehicle
     */
    public Ticket exitVehicle(String licensePlate) {
        return parkingService.exitVehicle(licensePlate, allSpots);
    }

    /**
     * Process payment for a ticket
     * 
     * @param ticket     the ticket to pay
     * @param amountPaid amount paid
     * @return true if payment successful
     */
    public boolean processPayment(Ticket ticket, double amountPaid) {
        return paymentService.processPayment(ticket, amountPaid);
    }

    /**
     * Calculate fee for a ticket
     * 
     * @param ticket the ticket
     * @return calculated fee
     */
    public double calculateFee(Ticket ticket) {
        return paymentService.calculateFee(ticket);
    }

    /**
     * Get active ticket for a vehicle
     * 
     * @param licensePlate license plate
     * @return active ticket or null
     */
    public Ticket getActiveTicket(String licensePlate) {
        return parkingService.getActiveTicket(licensePlate);
    }

    /**
     * Get available parking spots
     * 
     * @return list of available spots
     */
    public List<ParkingSpot> getAvailableSpots() {
        return allSpots.stream()
                .filter(spot -> !spot.isOccupied())
                .collect(Collectors.toList());
    }

    /**
     * Get occupied parking spots
     * 
     * @return list of occupied spots
     */
    public List<ParkingSpot> getOccupiedSpots() {
        return allSpots.stream()
                .filter(ParkingSpot::isOccupied)
                .collect(Collectors.toList());
    }

    /**
     * Get all parking spots
     * 
     * @return list of all spots
     */
    public List<ParkingSpot> getAllSpots() {
        return new ArrayList<>(allSpots);
    }

    /**
     * Get all gates
     * 
     * @return list of all gates
     */
    public List<Gate> getGates() {
        return new ArrayList<>(gates);
    }

    /**
     * Get parking statistics
     * 
     * @return statistics string
     */
    public String getParkingStats() {
        return parkingService.getParkingStats(allSpots);
    }

    /**
     * Get pricing information
     * 
     * @return pricing details
     */
    public String getPricingInfo() {
        return paymentService.getPricingInfo();
    }

    /**
     * Check if parking lot is full
     * 
     * @return true if full
     */
    public boolean isFull() {
        return getAvailableSpots().isEmpty();
    }

    /**
     * Get parking lot name
     * 
     * @return name of the parking lot
     */
    public String getName() {
        return name;
    }

    /**
     * Get total capacity
     * 
     * @return total number of spots
     */
    public int getTotalCapacity() {
        return allSpots.size();
    }

    /**
     * Get current occupancy
     * 
     * @return number of occupied spots
     */
    public int getCurrentOccupancy() {
        return getOccupiedSpots().size();
    }

    /**
     * Reset singleton instance (for testing purposes)
     */
    public static void resetInstance() {
        synchronized (lock) {
            instance = null;
        }
    }

    @Override
    public String toString() {
        return String.format("ParkingLot[%s] - Capacity: %d, Occupied: %d, Available: %d",
                name, getTotalCapacity(), getCurrentOccupancy(), getAvailableSpots().size());
    }
}