# IoT Smart Home Dashboard - Draw.io Architectural Diagram Guide

## Draw.io Setup Instructions

1. Go to https://app.diagrams.net/
2. Create a new blank diagram
3. Use the following components and layout

## Architecture Overview

The IoT Smart Home Dashboard follows a **4-Layer Architecture**:
- **Presentation Layer** (CLI Interface)
- **Service Layer** (Business Logic)
- **Model Layer** (Data Entities)
- **Data Layer** (Database)

---

## Step-by-Step Draw.io Instructions

### Step 1: Create Main Layers (Use Rectangle Shapes)

**Layer 1 - Presentation Layer (Top)**
- Shape: Large Rectangle (Light Blue Background)
- Title: "PRESENTATION LAYER"
- Components inside:
  - Rectangle: "SmartHomeDashboard.java" (Main CLI Interface)
  - Rectangle: "Scanner Input/Output"
  - Rectangle: "Menu Navigation System"

**Layer 2 - Service Layer (Middle-Upper)**
- Shape: Large Rectangle (Light Green Background)
- Title: "SERVICE LAYER (Business Logic)"

**Layer 3 - Model Layer (Middle-Lower)**
- Shape: Large Rectangle (Light Orange Background)
- Title: "MODEL LAYER (Data Entities)"

**Layer 4 - Data Layer (Bottom)**
- Shape: Large Rectangle (Light Gray Background)
- Title: "DATA LAYER"

### Step 2: Add Service Layer Components

Inside the Service Layer rectangle, add these components (use smaller rectangles with rounded corners):

**Core Orchestrator:**
- "SmartHomeService" (Central, larger box - Purple)

**Specialized Services (arrange around SmartHomeService):**
- "CustomerService" (User Management)
- "GadgetService" (Device Operations)
- "EnergyManagementService" (Power & Cost Analysis)
- "TimerService" (Scheduling)
- "CalendarEventService" (Event Automation)
- "WeatherService" (Weather Integration)
- "SmartScenesService" (Scene Automation)
- "DeviceHealthService" (Monitoring)

**Utility Services:**
- "SessionManager" (Session Management)
- "DynamoDBConfig" (Database Configuration)

### Step 3: Add Model Layer Components

Inside the Model Layer rectangle, add these entities (use cylinder shapes or rectangles):
- "Customer" (User data)
- "Gadget" (Device information)
- "DevicePermission" (Access control)
- "DeletedDeviceEnergyRecord" (Historical data)

### Step 4: Add Data Layer Components

Inside the Data Layer rectangle:
- "DynamoDB Local" (Database - use cylinder shape)
- "Customer Table"
- "Gadget Table"
- "DevicePermission Table"
- "DeletedEnergyRecord Table"

### Step 5: Add External Systems (Right side)

Create separate rectangles for external integrations:
- "Weather API" (External service)
- "IoT Devices" (Physical devices)
- "User Interface" (CLI Terminal)

### Step 6: Add Connections (Use Arrows)

**Presentation to Service Layer:**
- SmartHomeDashboard → SmartHomeService (bidirectional arrow)

**Service Layer Internal Connections:**
- SmartHomeService → All other services (arrows pointing from center)
- TimerService → GadgetService
- SmartScenesService → GadgetService
- EnergyManagementService → GadgetService
- CustomerService ↔ SessionManager

**Service to Model Layer:**
- Each service → corresponding model entities
- CustomerService → Customer
- GadgetService → Gadget
- EnergyManagementService → DeletedDeviceEnergyRecord

**Model to Data Layer:**
- All model entities → DynamoDB Local (downward arrows)

**External Connections:**
- WeatherService ↔ Weather API
- GadgetService ↔ IoT Devices
- SmartHomeDashboard ↔ User Interface

### Step 7: Add Labels and Annotations

**Add text boxes for key features:**
- "18 Menu Options" (near Presentation)
- "Multi-User Support" (near CustomerService)
- "15+ Device Categories" (near GadgetService)
- "Real-time Energy Tracking" (near EnergyManagementService)
- "8 Smart Scenes" (near SmartScenesService)
- "Automated Scheduling" (near TimerService)

## Color Scheme Recommendations

**Layer Colors:**
- Presentation Layer: Light Blue (#E3F2FD)
- Service Layer: Light Green (#E8F5E8)
- Model Layer: Light Orange (#FFF3E0)
- Data Layer: Light Gray (#F5F5F5)

**Component Colors:**
- Core Services: Purple (#9C27B0)
- Specialized Services: Blue (#2196F3)
- Models: Orange (#FF9800)
- Database: Gray (#607D8B)
- External Systems: Red (#F44336)

## Component Details to Add

**SmartHomeService (Central Hub):**
- Methods: login, logout, device control, energy reports
- Coordinates all other services
- Session management integration

**Device Categories Box:**
List supported devices:
- Entertainment (TV, Speaker)
- Climate (AC, Fan, Thermostat)
- Lighting (Smart Lights, Switches)
- Security (Camera, Lock, Doorbell)
- Kitchen (Refrigerator, Microwave)
- Cleaning (Vacuum)

**Key Features Box:**
- User Authentication & Security
- Device Management & Control
- Energy Consumption Tracking
- Smart Automation (Scenes & Timers)
- Weather Integration
- Group/Multi-User Support
- Analytics & Insights
- Health Monitoring

## Data Flow Arrows

**User Interaction Flow:**
1. User Input → CLI Interface
2. CLI → SmartHomeService
3. SmartHomeService → Specific Services
4. Services → Models
5. Models → Database
6. Response flows back up the chain

**Device Control Flow:**
1. User Command → GadgetService
2. GadgetService → Device Models
3. Models → Database (state persistence)
4. Real device communication (if applicable)

**Energy Monitoring Flow:**
1. Device Usage → GadgetService
2. GadgetService → EnergyManagementService
3. Energy calculations → Database storage
4. Reports → User Interface

## Layout Tips for Draw.io

1. **Use Swimlanes**: Create vertical swimlanes for each layer
2. **Consistent Spacing**: Keep equal spacing between components
3. **Arrow Styles**: Use different arrow styles for different types of connections
4. **Grouping**: Group related components with subtle background colors
5. **Legend**: Add a legend explaining shapes and colors
6. **Title**: Add main title "IoT Smart Home Dashboard - System Architecture"

## Additional Elements to Include

**Security Box (Top Right):**
- Password Encryption (bcrypt)
- Session Management
- Local Data Storage
- No Cloud Transmission

**Technology Stack Box (Bottom Right):**
- Java 8+
- DynamoDB Local
- CLI Interface
- Multi-platform Support

**Performance Features Box (Left Side):**
- Real-time Processing
- Local Database
- Efficient Memory Usage
- Scalable Architecture

This architecture demonstrates a clean separation of concerns with proper layering and clear data flow patterns.