# Parking Lot Management System

A comprehensive Java-based parking lot management system implementing SOLID principles and multiple design patterns. The system supports efficient vehicle parking, allocation strategies with different time complexities, and complete payment processing.

## 🏗️ Architecture & Design Patterns

### SOLID Principles Implementation

- **Single Responsibility Principle (SRP)**: Each class has a single, well-defined responsibility
- **Open/Closed Principle (OCP)**: System is open for extension (new vehicle types, strategies) but closed for modification
- **Liskov Substitution Principle (LSP)**: Vehicle and ParkingSpot hierarchies are properly substitutable
- **Interface Segregation Principle (ISP)**: Interfaces are focused and client-specific
- **Dependency Inversion Principle (DIP)**: High-level modules depend on abstractions, not concrete implementations

### Design Patterns Used

#### 1. **Singleton Pattern**

- **Location**: `ParkingLot` class
- **Purpose**: Ensures only one parking lot instance exists
- **Implementation**: Thread-safe double-checked locking

#### 2. **Factory Pattern**

- **Location**: `VehicleFactory`, `ParkingSpotFactory`
- **Purpose**: Creates objects without exposing creation logic
- **Benefits**: Centralizes object creation and allows easy extension

#### 3. **Strategy Pattern**

- **Location**: Parking allocation strategies
- **Implementations**:
  - `BasicParkingAllocationStrategy` - O(N) linear search
  - `BSTBasedAllocationStrategy` - O(log N) tree-based search
- **Benefits**: Allows runtime strategy switching

#### 4. **Observer Pattern**

- **Location**: `ParkingLotObserver` interface and `ParkingLotLogger`
- **Purpose**: Notifies interested parties of parking events
- **Events**: Vehicle parked, vehicle exit, lot full/available

#### 5. **Builder Pattern**

- **Location**: `ParkingLotBuilder`
- **Purpose**: Creates complex parking lot configurations
- **Features**: Fluent API for constructing multi-floor parking lots

#### 6. **Facade Pattern**

- **Location**: `ParkingLot` class methods
- **Purpose**: Provides simplified interface to complex subsystems
- **Benefits**: Hides complexity of services interaction

## 🚗 System Features

### Vehicle Management

- **Supported Types**: Bike, Car, Truck
- **Factory Creation**: Consistent vehicle object creation
- **License Plate Validation**: Automatic formatting and validation

### Parking Spot Allocation

- **3D Coordinates**: Each spot has (x, y, z) coordinates where z = floor
- **Distance-Based Allocation**: Prefers spots closer to entry gates
- **Flexible Parking**: Smaller vehicles can use larger spots when needed
- **Two Allocation Strategies**:
  - **Basic Strategy**: O(N) time complexity - linear search
  - **BST Strategy**: O(log N) time complexity - binary search tree

### Smart Allocation Logic

1. **Priority Match**: Find exact spot type for vehicle
2. **Fallback Strategy**: Use larger spots if exact match unavailable
3. **Distance Optimization**: Prefer spots closer to entry gates
4. **Floor Preference**: Ground floor preferred over higher floors

### Payment System

- **Dynamic Pricing**: Different rates for different vehicle types
  - Bikes: $5.00/hour
  - Cars: $10.00/hour
  - Trucks: $20.00/hour
- **Minimum Charge**: 1 hour minimum
- **Auto Calculation**: Time-based fee calculation
- **Payment Processing**: Full payment validation and change calculation

### Real-time Monitoring

- **Live Statistics**: Occupancy rates and availability
- **Event Logging**: All parking operations logged
- **Status Tracking**: Real-time vehicle and spot status

## 📁 Project Structure

```
parkinglot/
│
├── Main.java                     # Main application with demo and interactive modes
│
├── models/                       # Core domain models
│   ├── Vehicle.java             # Abstract base vehicle class
│   ├── Bike.java               # Bike implementation
│   ├── Car.java                # Car implementation
│   ├── Truck.java              # Truck implementation
│   ├── ParkingSpot.java        # Abstract parking spot with 3D coordinates
│   ├── BikeSpot.java           # Bike parking spot
│   ├── CarSpot.java            # Car parking spot
│   ├── TruckSpot.java          # Truck parking spot
│   ├── Ticket.java             # Parking ticket with timing
│   ├── Gate.java               # Entry/exit gates with coordinates
│   └── ParkingLot.java         # Main parking lot (Singleton + Facade)
│
├── enums/                        # Enum definitions
│   ├── VehicleType.java        # Vehicle type enumeration
│   └── SpotType.java           # Parking spot type enumeration
│
├── services/                     # Business logic services
│   ├── ParkingService.java     # Core parking operations
│   └── PaymentService.java     # Payment processing logic
│
├── strategies/                   # Allocation strategy implementations
│   ├── ParkingAllocationStrategy.java  # Strategy interface
│   ├── BasicParkingAllocationStrategy.java  # O(N) linear search
│   └── BSTBasedAllocationStrategy.java     # O(log N) BST search
│
└── patterns/                     # Design pattern implementations
    ├── VehicleFactory.java     # Factory for vehicle creation
    ├── ParkingSpotFactory.java # Factory for spot creation
    ├── ParkingLotBuilder.java  # Builder for complex lot setup
    ├── ParkingLotObserver.java # Observer interface
    └── ParkingLotLogger.java   # Concrete observer implementation
```

## 🚀 Getting Started

### Prerequisites

- Java 8 or higher
- No external dependencies required

### Compilation

```bash
# Navigate to the project directory
cd Parking_Lot

# Compile all Java files
javac parkinglot/*.java parkinglot/*/*.java
```

### Running the Application

```bash
# Run the main application
java parkinglot.Main
```

## 💡 Usage Examples

### Basic Usage with Builder Pattern

```java
// Create a parking lot using builder pattern
ParkingLot parkingLot = ParkingLotBuilder.createMediumLot()
    .withName("Downtown Parking")
    .withStrategy(new BSTBasedAllocationStrategy())
    .withObservers(new ParkingLotLogger())
    .withGates(new Gate("MAIN-ENTRY", 0, 0, 0, Gate.GateType.ENTRY))
    .build();

// Create and park vehicles
Vehicle car = VehicleFactory.createVehicle(VehicleType.CAR, "ABC123");
Ticket ticket = parkingLot.parkVehicle(car);

// Process payment
double fee = parkingLot.calculateFee(ticket);
parkingLot.processPayment(ticket, fee);

// Exit vehicle
parkingLot.exitVehicle("ABC123");
```

### Strategy Switching

```java
// Use different allocation strategies
BasicParkingAllocationStrategy basicStrategy = new BasicParkingAllocationStrategy();
BSTBasedAllocationStrategy bstStrategy = new BSTBasedAllocationStrategy();

// Create parking lots with different strategies
ParkingLot basicLot = ParkingLot.getInstance("Basic Lot", basicStrategy);
ParkingLot bstLot = ParkingLot.getInstance("BST Lot", bstStrategy);
```

## 🔧 System Operations

### Interactive Mode Features

1. **Park Vehicle**: Choose vehicle type and enter license plate
2. **Exit Vehicle**: Process vehicle exit and fee calculation
3. **Check Status**: View current vehicle parking status
4. **View Statistics**: Real-time lot occupancy and performance metrics
5. **Process Payment**: Handle payment processing with validation
6. **Pricing Information**: View current pricing structure

### Demo Mode Features

- **Strategy Comparison**: Side-by-side comparison of O(N) vs O(log N) strategies
- **Automatic Testing**: Predefined scenarios demonstrating all features
- **Edge Case Handling**: Full lot scenarios and fallback allocation
- **Performance Metrics**: Time complexity demonstration

## 🏢 Coordinate System

The system uses a 3D coordinate system:

- **X**: Horizontal distance from entry gate
- **Y**: Vertical distance from entry gate
- **Z**: Floor number (0 = ground floor, 1 = first floor, etc.)

**Distance Calculation**: Uses weighted Euclidean distance with floor preference

```java
distance = √(x² + y² + (z * 10)²)  // Z weighted 10x for floor preference
```

## 🎯 Key Benefits

### Performance

- **O(log N)** allocation strategy for large parking lots
- **Thread-safe** operations for concurrent access
- **Memory efficient** with optimized data structures

### Scalability

- **Extensible design** - easy to add new vehicle types or spot types
- **Strategy pattern** allows runtime algorithm changes
- **Builder pattern** enables complex lot configurations

### Maintainability

- **SOLID principles** ensure clean, maintainable code
- **Design patterns** provide proven architectural solutions
- **Comprehensive logging** for debugging and monitoring

### Flexibility

- **Multiple allocation strategies** for different scenarios
- **Configurable pricing** for different vehicle types
- **Real-time monitoring** and event handling

## 🔮 Future Enhancements

Potential areas for expansion:

- **Database integration** for persistent storage
- **Web interface** for remote management
- **Mobile app integration** for user convenience
- **Advanced analytics** and reporting
- **Dynamic pricing** based on demand
- **Reservation system** for advance booking
- **Electric vehicle** charging spot support

---

_This parking lot management system demonstrates enterprise-level Java development practices with comprehensive design pattern implementation and SOLID principle adherence._
