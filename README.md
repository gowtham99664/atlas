# IoT Smart Home Dashboard

## Introduction

The IoT Smart Home Dashboard is a comprehensive Java console application designed for managing and controlling smart home devices. This system provides real-time device control, energy monitoring, automation scheduling, and multi-user collaboration features. Built with modern Java technologies and AWS DynamoDB, it offers a scalable solution for home automation enthusiasts and professionals.

### Key Highlights

- **14+ Device Categories**: Control TVs, ACs, lights, security systems, kitchen appliances, and more
- **200+ Supported Brands**: Compatible with Samsung, LG, Sony, Philips, MI, Realme, and many others
- **Smart Automation**: Timer-based scheduling, smart scenes, and weather-responsive automation
- **Energy Analytics**: Real-time consumption tracking and cost analysis
- **Multi-User Support**: Group management with granular device permissions
- **Security Features**: BCrypt password encryption with progressive account lockout
- **Calendar Integration**: Event-driven device automation
- **Real-time Monitoring**: Live device status updates and health monitoring

## Technical Requirements

### System Requirements
- **Java**: Version 21 or higher
- **Memory**: Minimum 1GB RAM, Recommended 2GB RAM
- **Storage**: 500MB available disk space
- **Network**: Port 8002 for DynamoDB Local


### Dependencies
- AWS DynamoDB SDK 2.21.29
- BCrypt for Java 0.4
- JUnit 5.10.0 (for testing)
- SLF4J Simple 2.0.9 (for logging)

## How to Run

### Prerequisites
```bash
# Verify Java installation
java --version
# Should show Java 21 or higher
```

### Start the Application
```bash
# 1. Start DynamoDB Local Database
cd dynamodb-local
java -jar DynamoDBLocal.jar -sharedDb -port 8002

# 2. In a new terminal, build and run the application
cd iot-smart-home-dashboard
mvn clean package
java -jar target\iot-smart-home-dashboard-1.0.0.jar
```

## Application Menu

Once launched, the application presents an intuitive menu-driven interface:

```
=== IoT Smart Home Management Dashboard ===

[ACCOUNT & ACCESS MANAGEMENT]:
1. Create New Account          - User registration and authentication
2. Sign In                     - Secure login with BCrypt
3. Reset Password              - Account recovery functionality

[DEVICE MANAGEMENT]:
4. Add New Device              - Register new IoT devices
5. Device Status Monitor       - Real-time device overview
6. Device Control Panel        - Comprehensive control interface

[USER MANAGEMENT]:
7. Manage User Groups          - Multi-user collaboration

[AUTOMATION & SCHEDULING]:
8. Energy Management Reports   - Consumption analytics
9. Schedule Device Timers      - Automated scheduling
10. View Active Schedules      - Monitor scheduled operations
11. Calendar Integration       - Event-driven automation
12. Weather-Based Automation   - Adaptive responses

[SMART FEATURES]:
13. Smart Scene Configuration  - One-click automation
14. Device Health Monitoring   - Maintenance tracking
15. Usage Analytics Dashboard  - Advanced insights

[SYSTEM SETTINGS]:
16. User Preferences          - Personal settings
17. Clear Display             - Interface management
18. Sign Out                  - Secure logout
19. Exit Application          - Graceful shutdown

[HELP & INFORMATION]:
0. About & Developer Info     - Application information

Select option (0-19):
```

## Project Structure

```
atlas-final/
┌── README.md                              # Project documentation
├── iot-smart-home-dashboard.pdf           # Additional documentation
├── dynamodb-local/                        # Local database server
│   ├── DynamoDBLocal.jar                  # DynamoDB Local executable
│   ├── shared-local-instance.db           # Local database storage
│   └── DynamoDBLocal_lib/                 # Database dependencies
│       └── [50+ JAR files]                # AWS SDK & runtime libraries
└── iot-smart-home-dashboard/              # Main application directory
    ├── pom.xml                            # Maven build & dependency config
    └── src/
        ├── main/
        │   ├── java/com/smarthome/
        │   │   ├── SmartHomeDashboard.java        # Main CLI application (4,342 lines)
        │   │   │
        │   │   ├── model/                         # Domain entities & data models
        │   │   │   ├── Customer.java              # User account entity
        │   │   │   ├── Gadget.java                # IoT device entity
        │   │   │   ├── CalendarEvent.java         # Calendar event model
        │   │   │   ├── DevicePermission.java      # Access control model
        │   │   │   ├── WeatherInfo.java           # Weather data model
        │   │   │   └── SmartAlert.java            # Notification model
        │   │   │
        │   │   ├── service/                       # Business logic layer
        │   │   │   ├── SmartHomeService.java      # Core service implementation
        │   │   │   ├── TimerService.java          # Background automation service
        │   │   │   ├── CalendarService.java       # Event management service
        │   │   │   ├── AlertService.java          # Notification service
        │   │   │   └── EnergyService.java         # Energy analytics service
        │   │   │
        │   │   └── util/                          # Utility & configuration classes
        │   │       ├── DynamoDBConfig.java        # Database connection config
        │   │       ├── SessionManager.java        # User session management
        │   │       └── PasswordUtils.java         # Security & encryption utils
        │   │
        │   └── resources/
        │       └── application.properties         # Application configuration
        │
        └── test/                                  # Test suite & quality assurance
            └── java/com/smarthome/               # Test implementation classes
                ├── SmartHomeServiceCoreTest.java     # Core functionality tests
                ├── SmartHomeServiceDeviceTest.java   # Device management tests
                ├── SmartHomeServiceTimerTest.java    # Timer & automation tests
                ├── SmartHomeServiceCalendarTest.java # Calendar integration tests
                ├── SmartHomeServiceEnergyTest.java   # Energy analytics tests
                ├── SmartHomeServiceSecurityTest.java # Security & auth tests
                ├── SmartHomeServiceGroupTest.java    # Multi-user group tests
                ├── CustomerTest.java                 # Customer entity tests
                ├── GadgetTest.java                   # Device entity tests
                └── [Additional test files]          # Extended test coverage
```

### Architecture Highlights

**Layered Architecture Design:**
- **Presentation Layer**: Console-based user interface with menu-driven navigation
- **Service Layer**: Business logic with dedicated services for core functionality
- **Domain Layer**: Entity models representing real-world smart home concepts
- **Infrastructure Layer**: Database persistence with AWS DynamoDB integration

**Key Implementation Features:**
- **Modular Design**: Separate packages for models, services, and utilities
- **Comprehensive Testing**: 100+ test cases across all layers and functionality
- **Security Integration**: BCrypt encryption and session management
- **Scalable Architecture**: Ready for cloud deployment and enterprise use


## Testing

The application includes comprehensive test coverage with 100+ test cases:

```bash
# Run all tests
mvn test
```

**Test Categories**: Core functionality, Device management, Timer automation, Calendar integration, Security authentication, Energy management, Group management

---

## Smart Scenes Available

- **MOVIE**: Dims lights, turns on TV and sound system
- **PARTY**: Activates ambient lighting and entertainment
- **SLEEP**: Turns off unnecessary devices, sets night mode
- **WORK**: Optimizes lighting and climate for productivity
- **DINNER**: Sets dining ambiance with appropriate lighting
- **ROMANTIC**: Creates mood lighting and atmosphere
- **GAMING**: Optimizes environment for gaming
- **READING**: Adjusts lighting for comfortable reading

---

## Features Overview

### Device Management
- **14 Device Categories**: Entertainment, Climate Control, Lighting, Security, Kitchen Appliances, and more
- **200+ Brand Support**: Samsung, LG, Sony, Philips, MI, Realme, and others
- **Real-time Control**: Instant ON/OFF operations with status monitoring
- **Room Organization**: 30+ predefined room types for organized management

### Automation & Scheduling
- **Timer System**: Precision scheduling with 10-second monitoring intervals
- **Smart Scenes**: 8 pre-configured scenarios (Movie, Party, Sleep, Work, etc.)
- **Calendar Integration**: Event-driven automation with flexible timing
- **Weather Automation**: Adaptive device control based on weather conditions

### Analytics & Monitoring
- **Energy Tracking**: Real-time kWh consumption and cost calculations
- **Usage Patterns**: Detailed analytics with efficiency recommendations
- **Device Health**: Maintenance scheduling and performance monitoring
- **Alert System**: Time-based and energy threshold notifications

### Security & Access Control
- **BCrypt Authentication**: Industry-standard password hashing
- **Progressive Lockout**: Escalating security measures for failed attempts
- **Group Management**: Multi-user support with role-based permissions
- **Session Management**: Secure login/logout with activity tracking



## Application Interface

---

## Architecture

### System Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│                  (Console Interface)                        │
├─────────────────────────────────────────────────────────────┤
│                    Service Layer                            │
│     ┌─────────────┬─────────────┬─────────────────────────┐ │
│     │SmartHome    │Timer        │Calendar                 │ │
│     │Service      │Service      │Service                  │ │
│     └─────────────┴─────────────┴─────────────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                    Domain Layer                             │
│     ┌─────────────┬─────────────┬─────────────────────────┐ │
│     │Customer     │Gadget       │DevicePermission         │ │
│     │Entity       │Entity       │Entity                   │ │
│     └─────────────┴─────────────┴─────────────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│                Infrastructure Layer                         │
│              (AWS DynamoDB Local)                           │
└─────────────────────────────────────────────────────────────┘
```

### Technology Stack
- **Runtime**: Java 21 (OpenJDK LTS)
- **Build System**: Apache Maven 3.8+
- **Database**: AWS DynamoDB (Local development, Cloud ready)
- **Security**: BCrypt password hashing
- **Concurrency**: ScheduledExecutorService with thread pooling
- **Testing**: JUnit 5 with Mockito
- **Architecture Pattern**: Layered Architecture with Service Separation

---

