package parkinglot;

import parkinglot.models.*;
import parkinglot.enums.*;
import parkinglot.patterns.*;
import parkinglot.strategies.*;
import java.util.Scanner;

/**
 * Main class demonstrating the Parking Lot Management System
 * Showcases all design patterns and features
 */
public class Main {

    private static ParkingLot parkingLot;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Welcome to Smart Parking Lot Management System ===\n");

        // Demo both allocation strategies
        demonstrateAllocationStrategies();

        // Interactive mode
        runInteractiveMode();
    }

    /**
     * Demonstrate both O(N) and O(log N) allocation strategies
     */
    private static void demonstrateAllocationStrategies() {
        System.out.println("DEMO: Comparing Allocation Strategies\n");

        // Test with Basic O(N) Strategy
        System.out.println("1. Testing Basic O(N) Allocation Strategy:");
        testWithStrategy(new BasicParkingAllocationStrategy());

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Test with BST O(log N) Strategy
        System.out.println("2. Testing BST O(log N) Allocation Strategy:");
        testWithStrategy(new BSTBasedAllocationStrategy());

        System.out.println("\n" + "=".repeat(60) + "\n");
    }

    /**
     * Test parking lot with a specific allocation strategy
     */
    private static void testWithStrategy(ParkingAllocationStrategy strategy) {
        // Reset singleton for testing different strategies
        ParkingLot.resetInstance();

        // Create parking lot with strategy
        parkingLot = ParkingLot.getInstance("Smart Parking Center", strategy);

        // Add observer for logging
        parkingLot.addObserver(new ParkingLotLogger());

        // Setup parking lot infrastructure
        setupParkingLotInfrastructure();

        // Show strategy info
        System.out.println("Strategy: " + strategy.getStrategyName());
        System.out.println("Time Complexity: " + strategy.getTimeComplexity());
        System.out.println("Parking Lot: " + parkingLot);
        System.out.println("Pricing: " + parkingLot.getPricingInfo());
        System.out.println();

        // Perform demo operations
        performDemoOperations();
    }

    /**
     * Setup parking lot infrastructure with spots and gates
     */
    private static void setupParkingLotInfrastructure() {
        // Add gates with coordinates
        parkingLot.addGate(new Gate("ENTRY-1", 0, 0, 0, Gate.GateType.ENTRY));
        parkingLot.addGate(new Gate("EXIT-1", 0, 100, 0, Gate.GateType.EXIT));
        parkingLot.addGate(new Gate("BOTH-1", 50, 0, 1, Gate.GateType.BOTH));

        // Ground Floor (z=0) - Close to entry gate
        // Bike spots
        for (int i = 1; i <= 5; i++) {
            parkingLot.addParkingSpot(
                    ParkingSpotFactory.createParkingSpot(SpotType.BIKE_SPOT, "B-G-" + i, i * 5, 10, 0));
        }

        // Car spots
        for (int i = 1; i <= 10; i++) {
            parkingLot.addParkingSpot(
                    ParkingSpotFactory.createParkingSpot(SpotType.CAR_SPOT, "C-G-" + i, i * 10, 20, 0));
        }

        // Truck spots
        for (int i = 1; i <= 3; i++) {
            parkingLot.addParkingSpot(
                    ParkingSpotFactory.createParkingSpot(SpotType.TRUCK_SPOT, "T-G-" + i, i * 15, 30, 0));
        }

        // First Floor (z=1) - Further from entry
        // Bike spots
        for (int i = 1; i <= 8; i++) {
            parkingLot.addParkingSpot(
                    ParkingSpotFactory.createParkingSpot(SpotType.BIKE_SPOT, "B-1-" + i, i * 5, 15, 1));
        }

        // Car spots
        for (int i = 1; i <= 15; i++) {
            parkingLot.addParkingSpot(
                    ParkingSpotFactory.createParkingSpot(SpotType.CAR_SPOT, "C-1-" + i, i * 8, 25, 1));
        }

        // Truck spots
        for (int i = 1; i <= 5; i++) {
            parkingLot.addParkingSpot(
                    ParkingSpotFactory.createParkingSpot(SpotType.TRUCK_SPOT, "T-1-" + i, i * 12, 35, 1));
        }
    }

    /**
     * Perform demonstration operations
     */
    private static void performDemoOperations() {
        System.out.println("=== Demo Operations ===");

        // Create various vehicles using factory pattern
        Vehicle bike1 = VehicleFactory.createVehicle(VehicleType.BIKE, "B001");
        Vehicle car1 = VehicleFactory.createVehicle(VehicleType.CAR, "C001");
        Vehicle truck1 = VehicleFactory.createVehicle(VehicleType.TRUCK, "T001");

        // Park vehicles and show allocation
        Ticket ticket1 = parkingLot.parkVehicle(bike1);
        Ticket ticket2 = parkingLot.parkVehicle(car1);
        parkingLot.parkVehicle(truck1); // Park truck (ticket not needed for demo)

        System.out.println("\nParking Status:");
        System.out.println(parkingLot.getParkingStats());

        // Demonstrate smaller vehicle parking in larger spots
        System.out.println("\n--- Testing smaller vehicles in larger spots ---");

        // Fill up bike spots first
        for (int i = 3; i <= 13; i++) {
            Vehicle bike = VehicleFactory.createVehicle(VehicleType.BIKE, "B" + String.format("%03d", i));
            Ticket ticket = parkingLot.parkVehicle(bike);
            if (ticket == null) {
                System.out.println("Failed to park bike " + bike.getLicensePlate());
                break;
            }
        }

        // Try to park another bike (should use car spot)
        Vehicle extraBike = VehicleFactory.createVehicle(VehicleType.BIKE, "B999");
        Ticket bikeInCarSpot = parkingLot.parkVehicle(extraBike);

        if (bikeInCarSpot != null) {
            System.out.println("SUCCESS: Bike parked in car spot due to bike spots being full");
            System.out.println("Spot details: " + bikeInCarSpot.getParkingSpot());
        }

        System.out.println("\nUpdated Parking Status:");
        System.out.println(parkingLot.getParkingStats());

        // Simulate payment process
        if (ticket1 != null) {
            System.out.println("\n--- Payment Demo ---");
            simulatePayment(ticket1);
        }

        // Exit vehicle
        if (ticket2 != null) {
            System.out.println("\n--- Vehicle Exit Demo ---");
            simulateVehicleExit(ticket2.getVehicle().getLicensePlate());
        }
    }

    /**
     * Simulate payment process
     */
    private static void simulatePayment(Ticket ticket) {
        System.out.println("Processing payment for: " + ticket);

        // Simulate some parking time
        try {
            Thread.sleep(1000); // Simulate 1 second = 1 hour for demo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        double fee = parkingLot.calculateFee(ticket);
        System.out.println("Calculated fee: $" + fee);

        // Pay the exact amount
        boolean paymentSuccess = parkingLot.processPayment(ticket, fee);
        System.out.println("Payment result: " + (paymentSuccess ? "SUCCESS" : "FAILED"));
    }

    /**
     * Simulate vehicle exit
     */
    private static void simulateVehicleExit(String licensePlate) {
        System.out.println("Vehicle " + licensePlate + " is exiting...");

        Ticket exitedTicket = parkingLot.exitVehicle(licensePlate);
        if (exitedTicket != null) {
            System.out.println("Exit successful: " + exitedTicket);
            System.out.println("Parking duration: " + exitedTicket.getParkingDurationHours() + " hour(s)");
        } else {
            System.out.println("Exit failed - vehicle not found");
        }

        System.out.println("Updated status: " + parkingLot);
    }

    /**
     * Interactive mode for user to test the system
     */
    private static void runInteractiveMode() {
        System.out.println("=== Interactive Mode ===");
        System.out.println("Choose allocation strategy:");
        System.out.println("1. Basic O(N) Strategy");
        System.out.println("2. BST O(log N) Strategy");
        System.out.print("Enter choice (1-2): ");

        int strategyChoice = getIntInput();
        ParkingAllocationStrategy selectedStrategy;

        if (strategyChoice == 1) {
            selectedStrategy = new BasicParkingAllocationStrategy();
        } else {
            selectedStrategy = new BSTBasedAllocationStrategy();
        }

        // Setup interactive parking lot
        ParkingLot.resetInstance();
        parkingLot = ParkingLot.getInstance("Interactive Parking Lot", selectedStrategy);
        parkingLot.addObserver(new ParkingLotLogger());
        setupParkingLotInfrastructure();

        System.out.println("\nParking Lot initialized with " + selectedStrategy.getStrategyName());
        System.out.println(parkingLot);

        // Interactive menu
        while (true) {
            System.out.println("\n=== Parking Lot Management Menu ===");
            System.out.println("1. Park Vehicle");
            System.out.println("2. Exit Vehicle");
            System.out.println("3. Check Vehicle Status");
            System.out.println("4. View Parking Statistics");
            System.out.println("5. View Pricing Information");
            System.out.println("6. Process Payment");
            System.out.println("7. Exit System");
            System.out.print("Enter your choice (1-7): ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    handleParkVehicle();
                    break;
                case 2:
                    handleExitVehicle();
                    break;
                case 3:
                    handleCheckVehicleStatus();
                    break;
                case 4:
                    System.out.println("\n" + parkingLot.getParkingStats());
                    break;
                case 5:
                    System.out.println("\n" + parkingLot.getPricingInfo());
                    break;
                case 6:
                    handleProcessPayment();
                    break;
                case 7:
                    System.out.println("Thank you for using Smart Parking Lot Management System!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Handle park vehicle operation
     */
    private static void handleParkVehicle() {
        System.out.println("\n=== Park Vehicle ===");
        System.out.println("Vehicle Types: 1=BIKE, 2=CAR, 3=TRUCK");
        System.out.print("Enter vehicle type (1-3): ");
        int typeChoice = getIntInput();

        VehicleType vehicleType;
        switch (typeChoice) {
            case 1:
                vehicleType = VehicleType.BIKE;
                break;
            case 2:
                vehicleType = VehicleType.CAR;
                break;
            case 3:
                vehicleType = VehicleType.TRUCK;
                break;
            default:
                System.out.println("Invalid vehicle type.");
                return;
        }

        System.out.print("Enter license plate: ");
        String licensePlate = scanner.nextLine().trim();

        if (licensePlate.isEmpty()) {
            System.out.println("License plate cannot be empty.");
            return;
        }

        try {
            Vehicle vehicle = VehicleFactory.createVehicle(vehicleType, licensePlate);
            Ticket ticket = parkingLot.parkVehicle(vehicle);

            if (ticket != null) {
                System.out.println("Parking successful!");
                System.out.println("Ticket: " + ticket);
                System.out.println("Spot: " + ticket.getParkingSpot());
            } else {
                System.out.println("Parking failed. Lot might be full or vehicle already parked.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handle exit vehicle operation
     */
    private static void handleExitVehicle() {
        System.out.println("\n=== Exit Vehicle ===");
        System.out.print("Enter license plate: ");
        String licensePlate = scanner.nextLine().trim();

        if (licensePlate.isEmpty()) {
            System.out.println("License plate cannot be empty.");
            return;
        }

        Ticket ticket = parkingLot.exitVehicle(licensePlate);
        if (ticket != null) {
            System.out.println("Vehicle exit successful!");
            System.out.println("Ticket: " + ticket);
            System.out.println("Duration: " + ticket.getParkingDurationHours() + " hour(s)");

            double fee = parkingLot.calculateFee(ticket);
            System.out.println("Total fee: $" + fee);

            if (!ticket.isPaid()) {
                System.out.println("WARNING: Payment not processed yet!");
            }
        } else {
            System.out.println("Vehicle not found or exit failed.");
        }
    }

    /**
     * Handle check vehicle status operation
     */
    private static void handleCheckVehicleStatus() {
        System.out.println("\n=== Check Vehicle Status ===");
        System.out.print("Enter license plate: ");
        String licensePlate = scanner.nextLine().trim();

        if (licensePlate.isEmpty()) {
            System.out.println("License plate cannot be empty.");
            return;
        }

        Ticket ticket = parkingLot.getActiveTicket(licensePlate);
        if (ticket != null) {
            System.out.println("Vehicle found!");
            System.out.println("Ticket: " + ticket);
            System.out.println("Spot: " + ticket.getParkingSpot());
            System.out.println("Duration: " + ticket.getParkingDurationHours() + " hour(s)");

            double currentFee = parkingLot.calculateFee(ticket);
            System.out.println("Current fee: $" + currentFee);
            System.out.println("Payment status: " + (ticket.isPaid() ? "PAID" : "PENDING"));
        } else {
            System.out.println("Vehicle not found in parking lot.");
        }
    }

    /**
     * Handle process payment operation
     */
    private static void handleProcessPayment() {
        System.out.println("\n=== Process Payment ===");
        System.out.print("Enter license plate: ");
        String licensePlate = scanner.nextLine().trim();

        if (licensePlate.isEmpty()) {
            System.out.println("License plate cannot be empty.");
            return;
        }

        Ticket ticket = parkingLot.getActiveTicket(licensePlate);
        if (ticket == null) {
            System.out.println("No active ticket found for this vehicle.");
            return;
        }

        if (ticket.isPaid()) {
            System.out.println("Payment already processed for this ticket.");
            return;
        }

        double fee = parkingLot.calculateFee(ticket);
        System.out.println("Total fee: $" + fee);
        System.out.print("Enter payment amount: $");

        double payment = getDoubleInput();

        boolean success = parkingLot.processPayment(ticket, payment);
        if (success) {
            System.out.println("Payment processed successfully!");
        } else {
            System.out.println("Payment failed - insufficient amount.");
        }
    }

    /**
     * Helper method to get integer input with error handling
     */
    private static int getIntInput() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Using default value 1.");
            return 1;
        }
    }

    /**
     * Helper method to get double input with error handling
     */
    private static double getDoubleInput() {
        try {
            String input = scanner.nextLine().trim();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Using default value 0.0.");
            return 0.0;
        }
    }
}