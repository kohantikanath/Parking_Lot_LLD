package parkinglot.services;

import parkinglot.models.Ticket;
import parkinglot.enums.VehicleType;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Service for handling parking payments
 * Implements different pricing strategies for different vehicle types
 */
public class PaymentService {

    // Hourly rates for different vehicle types
    private static final double BIKE_HOURLY_RATE = 5.0;
    private static final double CAR_HOURLY_RATE = 10.0;
    private static final double TRUCK_HOURLY_RATE = 20.0;

    private static final double MINIMUM_CHARGE_HOURS = 1.0;

    /**
     * Calculate parking fee for a ticket
     * 
     * @param ticket the parking ticket
     * @return calculated fee
     */
    public double calculateFee(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        LocalDateTime exitTime = ticket.getExitTime() != null ? ticket.getExitTime() : LocalDateTime.now();

        Duration duration = Duration.between(ticket.getEntryTime(), exitTime);
        double hours = Math.max(duration.toMinutes() / 60.0, MINIMUM_CHARGE_HOURS);

        // Round up to the nearest hour
        hours = Math.ceil(hours);

        double hourlyRate = getHourlyRate(ticket.getVehicle().getVehicleType());
        return hours * hourlyRate;
    }

    /**
     * Process payment for a ticket
     * 
     * @param ticket     the ticket to pay for
     * @param amountPaid the amount paid
     * @return true if payment was successful
     */
    public boolean processPayment(Ticket ticket, double amountPaid) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        double fee = calculateFee(ticket);
        ticket.setAmount(fee);

        if (amountPaid >= fee) {
            ticket.setPaid(true);
            ticket.setExitTime(LocalDateTime.now());

            if (amountPaid > fee) {
                double change = amountPaid - fee;
                System.out.println(String.format("Payment successful! Change: $%.2f", change));
            } else {
                System.out.println("Payment successful!");
            }
            return true;
        } else {
            System.out.println(String.format("Insufficient payment. Required: $%.2f, Paid: $%.2f",
                    fee, amountPaid));
            return false;
        }
    }

    /**
     * Get hourly rate for a vehicle type
     * 
     * @param vehicleType the vehicle type
     * @return hourly rate
     */
    private double getHourlyRate(VehicleType vehicleType) {
        switch (vehicleType) {
            case BIKE:
                return BIKE_HOURLY_RATE;
            case CAR:
                return CAR_HOURLY_RATE;
            case TRUCK:
                return TRUCK_HOURLY_RATE;
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        }
    }

    /**
     * Get pricing information
     * 
     * @return string with pricing details
     */
    public String getPricingInfo() {
        return String.format(
                "Pricing (per hour): Bike: $%.2f, Car: $%.2f, Truck: $%.2f\nMinimum charge: %.0f hour(s)",
                BIKE_HOURLY_RATE, CAR_HOURLY_RATE, TRUCK_HOURLY_RATE, MINIMUM_CHARGE_HOURS);
    }
}