# IoT Smart Home Dashboard - Detailed Architectural Diagram Guide
## For Technical Presentations & System Documentation

---

## 🎯 Diagram Purpose
This detailed architectural diagram serves multiple purposes:
- **Technical Documentation** for developers
- **System Overview** for stakeholders
- **Integration Guide** for third-party systems
- **Troubleshooting Reference** for support teams
- **Scalability Planning** for future enhancements

---

## 🏗️ Complete System Architecture Layout

### 1. PRESENTATION LAYER (Top - Light Blue Background)

**Main CLI Interface Container**
```
┌─────────────────────────────────────────────────────────────────────────┐
│                        PRESENTATION LAYER                               │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │              SmartHomeDashboard.java (Main Class)                   │ │
│ │  • 18 Menu Options                    • Navigation System           │ │
│ │  • Input Validation                   • Error Handling              │ │
│ │  • Session Management UI              • Help & Documentation        │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│                                                                         │
│ ┌──────────────┐  ┌─────────────────┐  ┌─────────────────────────────┐ │
│ │Scanner Input │  │Output Formatter │  │Navigation Helper Methods    │ │
│ │• Validation  │  │• Tables         │  │• Menu Flow Control         │ │
│ │• Sanitization│  │• Colors         │  │• State Management          │ │
│ └──────────────┘  └─────────────────┘  └─────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

**Key UI Components to Add:**
- **Menu Categories**: Device Management, Energy & Automation, Smart Features, System
- **Input Methods**: Keyboard input, navigation commands (0, Ctrl+C)
- **Output Formats**: Tables, reports, status messages, error alerts
- **User Flow**: Registration → Login → Feature Access → Logout

---

### 2. SERVICE LAYER (Middle-Upper - Light Green Background)

**Central Orchestration Hub**
```
┌─────────────────────────────────────────────────────────────────────────┐
│                          SERVICE LAYER                                  │
│                                                                         │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │                    SmartHomeService (CORE)                          │ │
│ │  • Session Management    • Device Orchestration                     │ │
│ │  • Service Coordination  • Business Logic Hub                       │ │
│ │  • Error Handling        • Security Enforcement                     │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│                          ↕ ↕ ↕ ↕ ↕ ↕ ↕ ↕                              │
│                                                                         │
│ ┌──────────────────┐     ┌──────────────────┐     ┌─────────────────┐  │
│ │  CustomerService │     │   GadgetService  │     │EnergyManagement │  │
│ │• Authentication  │────▶│• Device Control  │────▶│• Consumption    │  │
│ │• Registration    │     │• Status Monitor  │     │• Cost Analysis  │  │
│ │• Password Mgmt   │     │• CRUD Operations │     │• Reports        │  │
│ │• Profile Updates │     │• 15+ Categories  │     │• Projections    │  │
│ └──────────────────┘     └──────────────────┘     └─────────────────┘  │
│                                                                         │
│ ┌──────────────────┐     ┌──────────────────┐     ┌─────────────────┐  │
│ │   TimerService   │     │ CalendarService  │     │  WeatherService │  │
│ │• Scheduling      │     │• Event Mgmt      │     │• Data Fetching  │  │
│ │• Auto Execution  │     │• Automation      │     │• Recommendations│  │
│ │• Cron-like Jobs  │     │• Context Aware   │     │• Smart Rules    │  │
│ │• Persistence     │     │• Integration     │     │• Forecasting    │  │
│ └──────────────────┘     └──────────────────┘     └─────────────────┘  │
│                                                                         │
│ ┌──────────────────┐     ┌──────────────────┐     ┌─────────────────┐  │
│ │SmartScenesService│     │DeviceHealthServ  │     │  SessionManager │  │
│ │• 8 Pre-configs   │     │• Monitoring      │     │• Auth State     │  │
│ │• Custom Scenes   │     │• Maintenance     │     │• User Context   │  │
│ │• Multi-device    │     │• Performance     │     │• Security       │  │
│ │• One-click Exec  │     │• Analytics       │     │• Timeout Mgmt   │  │
│ └──────────────────┘     └──────────────────┘     └─────────────────┘  │
└─────────────────────────────────────────────────────────────────────────┘
```

**Service Interaction Details:**
- **SmartHomeService**: Central hub coordinating all operations
- **CustomerService**: Handles bcrypt encryption, email validation, profile management
- **GadgetService**: Manages 15+ device categories with real-time status tracking
- **EnergyManagementService**: Calculates kWh consumption, cost analysis in Rupees
- **TimerService**: Background scheduler with automatic device control
- **SmartScenesService**: Manages 8 pre-configured + custom scenes
- **DeviceHealthService**: Monitors performance and maintenance needs

---

### 3. MODEL LAYER (Middle-Lower - Light Orange Background)

**Data Entity Models**
```
┌─────────────────────────────────────────────────────────────────────────┐
│                           MODEL LAYER                                   │
│                                                                         │
│ ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────────┐   │
│ │    Customer     │  │     Gadget      │  │   DevicePermission      │   │
│ │• customerId     │  │• gadgetId       │  │• permissionId           │   │
│ │• fullName       │  │• type           │  │• ownerEmail             │   │
│ │• email          │  │• model          │  │• memberEmail            │   │
│ │• passwordHash   │  │• roomName       │  │• deviceType             │   │
│ │• groupId        │  │• status         │  │• roomName               │   │
│ │• isAdmin        │  │• powerWatts     │  │• accessGranted          │   │
│ │• devices[]      │  │• usageMinutes   │  │• grantedDate            │   │
│ │• createdAt      │  │• energyKWh      │  │• grantedBy              │   │
│ │• lastLogin      │  │• lastOnTime     │  │                         │   │
│ └─────────────────┘  └─────────────────┘  └─────────────────────────┘   │
│                                                                         │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │               DeletedDeviceEnergyRecord                             │ │
│ │• recordId           • deviceType        • deletionDate             │ │
│ │• originalOwner      • roomName          • energyConsumed           │ │
│ │• totalUsageHours    • powerRating       • costRupees              │ │
│ │• creationDate       • deletionReason    • preservedForBilling     │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

**Model Relationships:**
- **Customer 1:N Gadget** (One customer owns multiple devices)
- **Customer 1:N DevicePermission** (Admin can grant permissions to multiple members)
- **Customer 1:N DeletedDeviceEnergyRecord** (Maintains billing history)
- **Gadget M:N Customer** (Through DevicePermission for sharing)

---

### 4. DATA LAYER (Bottom - Light Gray Background)

**Database Architecture**
```
┌─────────────────────────────────────────────────────────────────────────┐
│                           DATA LAYER                                    │
│                                                                         │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │                      DynamoDB Local                                 │ │
│ │  • NoSQL Document Database     • Local Storage                     │ │
│ │  • JSON Document Format        • No Cloud Dependencies             │ │
│ │  • ACID Transactions           • High Performance                   │ │
│ │  • Automatic Scaling           • Cross-platform                    │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│                                                                         │
│ ┌────────────────┐ ┌────────────────┐ ┌─────────────────────────────┐   │
│ │Customer Table  │ │ Gadget Table   │ │  DevicePermission Table     │   │
│ │PK: email       │ │PK: customerId  │ │  PK: permissionId           │   │
│ │SK: customerId  │ │SK: deviceType+ │ │  GSI: ownerEmail            │   │
│ │Indexes:        │ │   roomName     │ │  GSI: memberEmail           │   │
│ │• groupId-index │ │Indexes:        │ │  Indexes:                   │   │
│ │• createdAt     │ │• status-index  │ │  • device-access-index      │   │
│ └────────────────┘ └────────────────┘ └─────────────────────────────┘   │
│                                                                         │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │                  DeletedDeviceEnergyRecord Table                    │ │
│ │  PK: originalOwner    SK: deletionDate+recordId                    │ │
│ │  Indexes: monthly-billing-index, device-type-index                 │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

---

### 5. EXTERNAL INTEGRATIONS (Right Side - Light Red Background)

**Third-Party Systems**
```
┌─────────────────────────────────────────────────────────────────────────┐
│                      EXTERNAL INTEGRATIONS                              │
│                                                                         │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │                        Weather API                                  │ │
│ │  • Real-time Weather Data      • 5-Day Forecasting                 │ │
│ │  • Temperature & Humidity      • Air Quality Index                 │ │
│ │  • Smart Recommendations       • Location-based Data               │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│                                                                         │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │                    IoT Device Categories                            │ │
│ │                                                                     │ │
│ │  🎬 Entertainment        🌡️ Climate Control      💡 Lighting        │ │
│ │  • Smart TVs            • Air Conditioners      • Smart Bulbs      │ │
│ │  • Speakers             • Fans & Thermostats    • Smart Switches   │ │
│ │                                                                     │ │
│ │  🔒 Security            🍳 Kitchen Appliances   🧹 Cleaning         │ │
│ │  • Cameras              • Refrigerators         • Robot Vacuums    │ │
│ │  • Smart Locks          • Microwaves            • Smart Cleaners   │ │
│ │  • Doorbells            • Washing Machines                         │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
│                                                                         │
│ ┌─────────────────────────────────────────────────────────────────────┐ │
│ │                    User Interface Systems                           │ │
│ │  • Command Line Interface     • Terminal Applications              │ │
│ │  • Cross-platform Support     • Keyboard Input/Output              │ │
│ │  • Windows/macOS/Linux        • Real-time Feedback                 │ │
│ └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 🔄 DATA FLOW DIAGRAMS

### Primary User Journey Flow
```
User Input → Input Validation → Authentication Check → Service Router →
Business Logic → Data Access → Database Operation → Response Formatting →
User Feedback
```

### Device Control Flow
```
Device Command → Permission Check → Device Validation → State Change →
Energy Calculation → Usage Tracking → Database Update → Status Response
```

### Energy Monitoring Flow
```
Device ON/OFF → Time Tracking → Power Calculation → Usage Accumulation →
Cost Analysis → Report Generation → Historical Storage
```

---

## 🔗 DETAILED COMPONENT CONNECTIONS

### Connection Types Legend:
- **Solid Arrow (→)**: Direct method calls
- **Dashed Arrow (⇢)**: Event notifications
- **Double Arrow (↔)**: Bidirectional communication
- **Dotted Line (⋯)**: Data dependency

### Service Layer Connections:
```
SmartHomeService ──┬── CustomerService (Authentication)
                   ├── GadgetService (Device Operations)
                   ├── EnergyManagementService (Analytics)
                   ├── TimerService (Scheduling)
                   ├── SmartScenesService (Automation)
                   ├── WeatherService (Integration)
                   ├── CalendarEventService (Events)
                   └── DeviceHealthService (Monitoring)

GadgetService ⇢ EnergyManagementService (Usage Events)
TimerService ⇢ GadgetService (Scheduled Actions)
SmartScenesService ⇢ GadgetService (Scene Execution)
WeatherService ⇢ SmartScenesService (Weather Automation)
```

---

## 📊 TECHNICAL SPECIFICATIONS

### Performance Characteristics
```
┌─────────────────────────────────────────────────────────────────────┐
│                    PERFORMANCE METRICS                              │
│                                                                     │
│  Response Time: < 100ms for device operations                      │
│  Database Operations: < 50ms for CRUD operations                   │
│  Memory Usage: ~50MB base, +2MB per 100 devices                    │
│  Concurrent Users: Up to 10 users per group                        │
│  Device Limit: 1000+ devices per user                              │
│  Energy Calculations: Real-time with <1s latency                   │
│  Timer Accuracy: ±5 seconds for scheduled operations               │
└─────────────────────────────────────────────────────────────────────┘
```

### Security Architecture
```
┌─────────────────────────────────────────────────────────────────────┐
│                      SECURITY LAYERS                                │
│                                                                     │
│  🔐 Authentication Layer                                            │
│  • bcrypt Password Hashing (Cost Factor: 12)                       │
│  • Email-based Identity                                             │
│  • Session Token Management                                         │
│  • Failed Login Attempt Tracking                                    │
│                                                                     │
│  🛡️ Authorization Layer                                            │
│  • Role-based Access Control (Admin/Member)                        │
│  • Device-level Permissions                                         │
│  • Group-based Resource Sharing                                     │
│  • Action-level Security Checks                                     │
│                                                                     │
│  🔒 Data Protection Layer                                          │
│  • Local Database Encryption                                        │
│  • No Cloud Data Transmission                                       │
│  • Input Sanitization & Validation                                  │
│  • SQL Injection Prevention                                         │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🎛️ FEATURE IMPLEMENTATION DETAILS

### Smart Scenes Configuration
```
┌─────────────────────────────────────────────────────────────────────┐
│                      SMART SCENES MATRIX                            │
│                                                                     │
│  Scene Name    │ Typical Devices        │ Actions               │   │
│  ─────────────────────────────────────────────────────────────────  │
│  MORNING       │ Lights, Coffee, News   │ ON, Warm Light        │   │
│  WORK          │ Lights, AC, Electronics│ Focus Mode Settings   │   │
│  EVENING       │ TV, Lights, Music      │ Relaxation Setup     │   │
│  NIGHT         │ Security, Minimal Light│ Sleep Preparation     │   │
│  PARTY         │ Audio, Colorful Lights │ Entertainment Mode    │   │
│  MOVIE         │ TV, Dim Lights, Sound  │ Cinema Experience     │   │
│  VACATION      │ Security, Energy Save  │ Away Mode             │   │
│  EMERGENCY     │ All Lights, Alerts     │ Safety Protocol       │   │
└─────────────────────────────────────────────────────────────────────┘
```

### Energy Management Calculations
```
┌─────────────────────────────────────────────────────────────────────┐
│                   ENERGY CALCULATION ENGINE                         │
│                                                                     │
│  Real-time Energy = (Power Rating in Watts ÷ 1000) × Hours Used    │
│  Total Consumption = Device Energy + Deleted Device History         │
│  Cost Calculation = Energy (kWh) × Rate (₹/kWh)                    │
│  Efficiency Rating = Actual vs Expected Consumption                │
│                                                                     │
│  Tracking Frequency: Every minute for active devices               │
│  Historical Retention: Unlimited (local storage)                   │
│  Cost Projections: Weekly, Monthly, Yearly                         │
│  Peak Usage Detection: Automatic time-based analysis               │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🔧 SCALABILITY & EXTENSIBILITY

### Future Enhancement Points
```
┌─────────────────────────────────────────────────────────────────────┐
│                    EXTENSIBILITY DESIGN                             │
│                                                                     │
│  🔌 Plugin Architecture Ready                                      │
│  • Service Interface Contracts                                      │
│  • Device Driver Framework                                          │
│  • Custom Scene Development                                         │
│  • Third-party Integration APIs                                     │
│                                                                     │
│  📡 Communication Protocols                                        │
│  • HTTP/REST API Framework                                          │
│  • WebSocket Support (Future)                                       │
│  • MQTT Integration Ready                                            │
│  • Bluetooth/WiFi Device Support                                    │
│                                                                     │
│  🔄 Data Migration Support                                         │
│  • Database Schema Versioning                                       │
│  • Backward Compatibility                                            │
│  • Export/Import Functionality                                      │
│  • Cloud Migration Path                                             │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 📋 DRAW.IO IMPLEMENTATION CHECKLIST

### Essential Elements to Include:
- [ ] All 4 architectural layers with clear separation
- [ ] Service interconnections with labeled arrows
- [ ] Database tables with primary/foreign keys
- [ ] External system integrations
- [ ] Security layer annotations
- [ ] Performance metrics boxes
- [ ] Feature capability matrices
- [ ] Data flow direction indicators
- [ ] Error handling pathways
- [ ] Scalability extension points

### Visual Enhancement Tips:
- **Use Icons**: Add device icons, security shields, database symbols
- **Color Coding**: Consistent colors for similar components
- **Grouping**: Logical component grouping with backgrounds
- **Annotations**: Technical specifications and notes
- **Legend**: Explain symbols, colors, and connection types

This comprehensive diagram will serve as your complete technical reference for explaining the IoT Smart Home Dashboard architecture to any technical or business audience.