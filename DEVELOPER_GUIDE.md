# IoT Smart Home Dashboard - Developer Guide

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [File Structure](#file-structure)
3. [Architecture Components](#architecture-components)
4. [Setup & Execution Guide](#setup--execution-guide)
5. [Development Guidelines](#development-guidelines)
6. [Database Configuration](#database-configuration)
7. [Common Issues & Solutions](#common-issues--solutions)

---

## 🏠 Project Overview

The **IoT Smart Home Dashboard** is a Java-based console application that provides centralized control and management of smart home devices. It features user authentication, device management, and secure password handling with DynamoDB integration.

### Key Features
- 🔐 **Secure Authentication**: BCrypt password hashing with progressive account lockout
- 📱 **Advanced Device Management**: Control 18+ categories of smart devices with real-time monitoring
- ⏰ **Smart Timer System**: Schedule devices with countdown timers and automatic execution
- 🎨 **Scene Automation**: One-click automation for daily routines (Morning, Evening, Movie, etc.)
- 📊 **Health Monitoring**: Real-time device diagnostics with maintenance recommendations  
- ⚡ **Energy Analytics**: Live power consumption tracking and efficiency analysis
- 📅 **Calendar Integration**: Event-based automation with intelligent scheduling
- 🌦️ **Weather Intelligence**: Weather-based device control suggestions
- 🏡 **Room Organization**: Support for 38+ room types covering Indian homes
- 🔄 **Simple Password Reset**: Streamlined password recovery without security questions
- 💾 **Dual Storage**: DynamoDB Local with automatic fallback to demo mode
- 🔢 **User-Friendly Interface**: Number-based selection system (no more typing device names)

---

## 📁 File Structure

```
iot-smart-home-dashboard/
├── pom.xml                           # Maven configuration & dependencies
├── src/main/java/com/smarthome/
│   ├── model/                        # Data Models
│   │   ├── Customer.java            # User entity with authentication fields
│   │   └── Gadget.java              # Smart device entity with usage tracking
│   ├── service/                      # Business Logic Layer
│   │   ├── CustomerService.java     # User management & authentication
│   │   ├── GadgetService.java       # Device operations & validation  
│   │   ├── SmartHomeService.java    # Main business orchestration
│   │   ├── TimerService.java        # Device scheduling & countdown timers
│   │   ├── DeviceHealthService.java # Health monitoring & diagnostics
│   │   ├── SmartScenesService.java  # Scene automation & execution
│   │   ├── EnergyManagementService.java # Power consumption & analytics
│   │   ├── CalendarEventService.java # Event-based automation
│   │   └── WeatherService.java      # Weather-based suggestions
│   ├── util/                         # Utility Classes
│   │   ├── DynamoDBConfig.java      # Database connection management
│   │   ├── SessionManager.java      # User session handling
│   │   └── PasswordInputUtil.java   # Secure input handling
│   └── SmartHomeDashboard.java      # Main application & console UI
└── target/                           # Build output directory
    └── iot-smart-home-dashboard-1.0.0.jar
```

---

## 🏗️ Architecture Components

### 1. **Main Application** (`SmartHomeDashboard.java`)
**Purpose**: Entry point and user interface controller

**What's Implemented**:
- Console-based menu system with 17 main options
- Advanced feature navigation (timers, scenes, health monitoring, analytics)
- User input validation and sanitization with number-based selection
- Navigation between registration, login, device management, and smart features
- Error handling for all user interactions
- Graceful application shutdown with resource cleanup

**Key Methods**:
- `main()`: Application entry point
- `showMainMenu()`: Main navigation controller with 17 options
- `registerCustomer()`: User registration flow
- `loginCustomer()`: Authentication flow
- `forgotPassword()`: Simplified password reset
- `controlGadgets()`: Device management interface
- `setDeviceTimer()`: Timer scheduling system
- `showScheduledTimers()`: Timer management with countdown display
- `executeSmartScene()`: Scene automation (number selection)
- `showDeviceHealthMenu()`: Health monitoring and diagnostics
- `showUsageAnalytics()`: Energy consumption analysis
- `showCalendarEventsMenu()`: Event-based automation
- `showWeatherSuggestions()`: Weather-based recommendations

---

### 2. **Data Models**

#### **Customer.java**
**Purpose**: User entity with authentication, security, and group management features

**What's Implemented**:
- User profile data (email, name, password hash)
- Device ownership tracking (list of gadgets)
- **Group Management System**: Collaborative device sharing functionality
- Security features for account protection:
  - Failed login attempt counter
  - Account lockout timestamp
  - Last failed login time
- Helper methods for account security management
- Group administration and member management

**Key Fields**:
```java
private String email;                    // Primary key & username
private String fullName;                 // User display name
private String password;                 // BCrypt hashed password
private List<Gadget> gadgets;           // User's connected devices
private int failedLoginAttempts;        // Security counter
private LocalDateTime accountLockedUntil; // Lockout expiry

// Group Management Fields
private Set<String> groupMembers;       // Email addresses of group members
private String groupCreator;            // Email of group admin (who created the group)
```

**Group Management Methods**:
```java
public boolean isGroupAdmin() {
    return this.groupCreator != null && this.groupCreator.equalsIgnoreCase(this.email);
}

public boolean isPartOfGroup() {
    return groupMembers != null && !groupMembers.isEmpty();
}

public int getGroupSize() {
    return groupMembers != null ? groupMembers.size() : 0;
}

public void addGroupMember(String memberEmail) {
    // Adds member and auto-assigns admin if first member
}

public boolean removeGroupMember(String memberEmail) {
    // Admin-only removal with validation
}
```

#### **Gadget.java**
**Purpose**: Smart device representation with advanced tracking

**What's Implemented**:
- Device identification (ID, type, model, room)
- Device status management (ON/OFF) with usage tracking
- Timer scheduling (ON/OFF times with countdown)
- Energy consumption tracking (real-time and cumulative)
- Power rating management with efficiency calculations
- Session-based usage monitoring
- String representation for console display

**Key Fields**:
```java
private String id;                          // Unique device identifier
private String type;                        // Device category (TV, AC, FAN, etc.)
private String model;                       // Brand/model name
private String roomName;                    // Location in house
private String status;                      // Current state (ON/OFF)
private double powerRatingWatts;           // Device power consumption
private LocalDateTime lastOnTime;          // Last activation time
private LocalDateTime lastOffTime;         // Last deactivation time  
private long totalUsageMinutes;            // Cumulative runtime
private double totalEnergyConsumedKWh;     // Total energy consumption
private LocalDateTime scheduledOnTime;     // Timer: scheduled activation
private LocalDateTime scheduledOffTime;    // Timer: scheduled deactivation
private boolean timerEnabled;              // Timer status flag
```

**New Methods**:
- `getCurrentTotalEnergyConsumedKWh()`: Real-time energy including current session
- `getCurrentUsageTimeFormatted()`: Live usage display with current session
- `getCurrentSessionUsageHours()`: Current running session duration

---

### 3. **Service Layer**

#### **CustomerService.java**
**Purpose**: User management and authentication logic

**What's Implemented**:
- **User Registration**: Email validation, password strength checking
- **Authentication**: BCrypt password verification with security tracking
- **Account Security**: Progressive lockout system (5min → 15min → 60min)
- **Password Reset**: Simplified email-based password recovery
- **Validation Rules**: Email format, password complexity, name validation

**Key Features**:
- Password requirements: 8-128 chars, mixed case, numbers, special chars
- Common password blacklist (30+ weak passwords)
- Failed login protection with automatic account lockout
- BCrypt hashing with salt for password security

#### **GadgetService.java**
**Purpose**: Device operations and validation

**What's Implemented**:
- **Device Creation**: Generates unique devices with proper validation
- **Type Validation**: Supports 18 device categories
- **Model Validation**: 350+ Indian market device models
- **Room Validation**: 38+ room types for Indian homes
- **Business Rules**: One device type per room enforcement

**Supported Categories**:
- Entertainment: TV, Smart Speaker
- Climate: AC, Fan, Air Purifier, Thermostat
- Lighting: Smart Light, Smart Switch
- Security: Camera, Door Lock, Doorbell
- Kitchen: Refrigerator, Microwave, Washing Machine, Geyser, Water Purifier
- Cleaning: Robotic Vacuum

#### **SmartHomeService.java**
**Purpose**: Main business logic orchestration and group management

**What's Implemented**:
- **User Operations**: Registration and login coordination
- **Device Management**: Connect, view, and control devices with real-time updates
- **Group Management**: Collaborative device sharing and administration
- **Auto-Aligned Tables**: Dynamic table formatting with intelligent column sizing
- **Session Integration**: User state management
- **Password Recovery**: Simplified reset workflow
- **Data Validation**: Input sanitization and business rule enforcement
- **Service Coordination**: Integration with all specialized services

**Group Management Features**:
```java
// Core group management methods
public void addPersonToGroup(String memberEmail) {
    // Validates email exists, adds to group, auto-assigns admin
}

public boolean removePersonFromGroup(String memberEmail) {
    // Admin-only removal with security validation
}

public void showGroupInfo() {
    // Displays group size, admin status, member list
}

// Device control with group awareness
public void changeSpecificGadgetStatus(String deviceType, String roomName) {
    // Updates device for actual owner, not current user
}

// Group-aware device listing
public List<Gadget> viewGadgets() {
    // Returns devices from user + all group members
}
```

**Group Security & Validation**:
- **Email Verification**: Only existing registered users can be added to groups
- **Admin Controls**: Only group creators can remove members
- **Auto-Repair**: Broken group states are automatically fixed
- **Device Ownership**: Status changes update the actual device owner's record

**Recent Optimizations (v2.1)**:
- **Refactored Table Display**: `displayAutoAlignedTable()` method now uses helper classes for better organization
- **Performance Improvements**: Eliminated duplicate calculations and optimized string formatting
- **Code Modularity**: Split large methods into focused, single-responsibility functions
- **Memory Efficiency**: Reduced object creation in table rendering loops

#### **TimerService.java**
**Purpose**: Device scheduling and countdown management

**What's Implemented**:
- **Timer Scheduling**: Set future ON/OFF times for devices
- **Countdown Display**: Real-time time remaining in hours and minutes format
- **Timer Execution**: Automatic device control at scheduled times
- **Timer Management**: Create, view, and cancel scheduled tasks
- **Validation**: Date/time format validation and conflict checking

**Key Features**:
```java
// Timer display with countdown
"Turn OFF at: 08-09-2025 16:37 [2h 15m remaining]"
"Turn ON at: 09-09-2025 07:00 [14h 38m remaining]"
"[OVERDUE]" // For past-due timers
```

#### **DeviceHealthService.java**
**Purpose**: Device health monitoring and diagnostics

**What's Implemented**:
- **Health Scoring**: 0-100% health calculation with status classification
- **Energy Efficiency**: Power consumption analysis (capped at ≤100%)
- **Diagnostic Analysis**: Device-specific health insights and recommendations
- **Performance Monitoring**: Usage patterns and efficiency tracking
- **Maintenance Alerts**: Specific recommendations for low health scores

**Health Categories**:
- **EXCELLENT** (80-100%): Optimal performance
- **GOOD** (60-79%): Normal operation
- **WARNING** (40-59%): Maintenance recommended
- **CRITICAL** (<40%): Immediate attention required

**Device-Specific Diagnostics**:
```java
// AC diagnostics: Filter status, refrigerant leak detection
// Refrigerator: Seal integrity, coil efficiency  
// Geyser: Sediment buildup indicators
// TV: Overheating prevention, ventilation checks
```

#### **SmartScenesService.java**
**Purpose**: Scene automation and execution

**What's Implemented**:
- **Scene Management**: 8 pre-configured automation scenarios
- **Device Coordination**: Multi-device control with single command
- **Execution Logic**: Intelligent device state management
- **Scene Validation**: Ensure devices are available before execution
- **Result Reporting**: Success/failure feedback with detailed logs

**Available Scenes**:
1. **MORNING** - Start your day (lights + TV + geyser)
2. **EVENING** - Relax after work (ambient lighting + entertainment)
3. **NIGHT** - Sleep preparation (security + bedroom climate)
4. **AWAY** - Energy saving + security activation
5. **MOVIE** - Cinema experience (lighting + entertainment + climate)
6. **WORKOUT** - Exercise environment (ventilation + music)
7. **COOKING** - Kitchen optimization (lighting + ventilation + appliances)
8. **ENERGY_SAVING** - Minimize consumption

#### **EnergyManagementService.java**
**Purpose**: Power consumption and efficiency analysis

**What's Implemented**:
- **Real-Time Tracking**: Live power consumption with session updates
- **Usage Analytics**: Historical consumption patterns and trends
- **Efficiency Monitoring**: Device performance vs expected ratings
- **Cost Analysis**: Electricity cost calculations and projections
- **Peak Usage Detection**: High consumption period identification
- **Recommendations**: Energy saving suggestions based on usage patterns

**Analytics Features**:
```java
// Real-time display format
"[Power] 1500.0W | [Usage] 47h 23m | [Energy] 71.345 kWh"
"[Current session: 2.3 hours]"
"Energy efficiency: 85.2% (≤100%)"
```

#### **CalendarEventService.java**
**Purpose**: Event-based device automation

**What's Implemented**:
- **Event Scheduling**: Create automation events with specific date/time
- **Smart Triggers**: Automatic device control based on event types
- **Event Types**: 7 predefined automation scenarios
- **Timeline Management**: Pre-event and post-event device control
- **Automation Rules**: Intelligent device coordination for each event type

**Event Automation Examples**:
```java
// MOVIE event: TV ON (2min before), Lights OFF (at start), AC ON (15min before)
// SLEEP event: All lights OFF, TV OFF, AC ON (5min before)
// MEETING event: Study room lights ON (5min before), AC ON (10min before)
```

#### **WeatherService.java**
**Purpose**: Weather-based device control suggestions

**What's Implemented**:
- **Weather Simulation**: Current conditions and forecasting
- **Smart Recommendations**: Device control suggestions based on weather
- **Automation Rules**: Configurable weather-triggered actions
- **Environmental Monitoring**: Temperature, humidity, air quality tracking
- **Seasonal Adaptation**: Context-aware suggestions for Indian climate

**Weather Triggers**:
```java
// Hot Weather (>30°C): AC activation, fan speed increase
// Cold Weather (<18°C): Heater recommendations  
// High Humidity (>70%): Dehumidifier and air purifier suggestions
// Poor Air Quality (AQI >150): Air purification activation
// Storm Conditions: Safety device activation
```

---

### 4. **Utility Classes**

#### **DynamoDBConfig.java**
**Purpose**: Database connection and configuration management

**What's Implemented**:
- DynamoDB Local connection (localhost:8000)
- Enhanced client configuration with proper error handling
- Automatic fallback to demo mode if database unavailable
- Connection lifecycle management with proper shutdown

#### **SessionManager.java**
**Purpose**: User session state management

**What's Implemented**:
- Singleton pattern for global session access
- Current user tracking during application runtime
- Login/logout state management
- Session data updates (when user data changes)
- Memory-based session storage (resets on app restart)

---

## 👥 Group Management System (v2.1)

### Overview
The Group Management System enables collaborative smart home control by allowing users to share their devices with family members, friends, or roommates. This feature maintains security and administrative controls while enabling seamless device sharing.

### Architecture Components

#### **Group Roles & Permissions**
- **Group Admin**: The first person to add someone to their group becomes the admin
- **Group Members**: All users in the group can view and control each other's devices
- **Admin Privileges**: Only the group admin can remove members from the group

#### **Key Features**
- **Shared Device Control**: All group members can view and control each other's devices
- **Real-Time Synchronization**: Device status changes are immediately visible to all group members  
- **Admin Controls**: Only group admin can manage membership
- **Auto-Repair**: Broken group states are automatically detected and repaired
- **Device Ownership Preservation**: Devices remain owned by their original user

### Implementation Details

#### **Customer.java Enhancements**
```java
// New fields for group management
private Set<String> groupMembers = new HashSet<>();
private String groupCreator; // Email of admin user

// Core group methods
public boolean isGroupAdmin() {
    return this.groupCreator != null && this.groupCreator.equalsIgnoreCase(this.email);
}

public void addGroupMember(String memberEmail) {
    if (groupMembers == null) {
        groupMembers = new HashSet<>();
    }
    
    // Auto-assign admin if first member being added
    if (groupMembers.isEmpty()) {
        this.groupCreator = this.email;
    }
    
    groupMembers.add(memberEmail.toLowerCase());
}

public boolean removeGroupMember(String memberEmail) {
    if (groupMembers != null) {
        boolean removed = groupMembers.remove(memberEmail.toLowerCase());
        
        // Reset group if no members left
        if (groupMembers.isEmpty()) {
            this.groupCreator = null;
        }
        
        return removed;
    }
    return false;
}
```

#### **SmartHomeService.java Group Logic**
```java
public void addPersonToGroup(String memberEmail) {
    // 1. Validate email exists in system
    Customer member = customerService.findCustomerByEmail(memberEmail);
    if (member == null) {
        System.out.println("[ERROR] No account found with email: " + memberEmail);
        return;
    }
    
    // 2. Prevent self-addition
    if (currentUser.getEmail().equalsIgnoreCase(memberEmail)) {
        System.out.println("[ERROR] Cannot add yourself to your own group!");
        return;
    }
    
    // 3. Add member and update both users' records
    currentUser.addGroupMember(memberEmail);
    member.addGroupMember(currentUser.getEmail());
    
    // 4. Update database for both users
    customerService.updateCustomer(currentUser);
    customerService.updateCustomer(member);
}

public List<Gadget> viewGadgets() {
    List<Gadget> allGadgets = new ArrayList<>();
    
    // Add current user's devices
    allGadgets.addAll(currentUser.getGadgets());
    
    // Add group members' devices if part of group
    if (currentUser.isPartOfGroup()) {
        for (String memberEmail : currentUser.getGroupMembers()) {
            Customer member = customerService.findCustomerByEmail(memberEmail);
            if (member != null) {
                allGadgets.addAll(member.getGadgets());
            }
        }
    }
    
    return allGadgets;
}
```

#### **UI Integration (SmartHomeDashboard.java)**
```java
// New menu option 7: Group Management
case 7:
    if (checkLoginStatus()) {
        showGroupManagementMenu();
    }
    break;

private static void showGroupManagementMenu() {
    System.out.println("=== Group Management ===");
    System.out.println("1. View Group Information");
    System.out.println("2. Add Person to Group");
    System.out.println("3. Remove Person from Group (Admin Only)");
    System.out.println("4. Return to Main Menu");
}
```

### Security & Data Integrity

#### **Validation Rules**
- Email addresses must exist in the system before being added to groups
- Only group admins can remove members (enforced at service level)
- Group admins cannot remove themselves (prevents group destruction)
- All group operations validate user authentication status

#### **Auto-Repair Functionality**
```java
// Detects and repairs inconsistent group states
private void repairBrokenGroupState(Customer currentUser, Customer member) {
    if (!member.getGroupMembers().contains(currentUser.getEmail())) {
        member.addGroupMember(currentUser.getEmail());
        customerService.updateCustomer(member);
        System.out.println("[AUTO-REPAIR] Fixed broken group relationship");
    }
}
```

#### **Device Control Security**
- Device status changes update the actual device owner's database record
- Group members can control devices but cannot modify device ownership
- All device operations maintain audit trail of who made changes

### Usage Examples

#### **Adding Members to Group**
```java
// User A adds User B to group
smartHomeService.addPersonToGroup("userB@example.com");
// Result: User A becomes admin, both users can see each other's devices
```

#### **Group Device Control**
```java
// Any group member can control any device in the group
smartHomeService.changeSpecificGadgetStatus("TV", "Living Room");
// Updates device owner's record, visible to all group members
```

#### **Admin Member Management**
```java
// Only admin can remove members
if (currentUser.isGroupAdmin()) {
    smartHomeService.removePersonFromGroup("member@example.com");
}
```

---

## 📊 Auto-Aligned Table System (v2.1)

### Overview
The application features an intelligent table formatting system that automatically adjusts column widths based on content, providing optimal readability without truncation.

### Implementation Details

#### **TableDimensions Helper Class**
```java
private static class TableDimensions {
    final int numWidth, deviceWidth, powerWidth, statusWidth, usageWidth, energyWidth;
    // Immutable container for calculated column widths
}
```

#### **TableFormatStrings Helper Class**
```java
private static class TableFormatStrings {
    final String borderFormat, headerFormat, rowFormat, emptyRowFormat;
    // Pre-calculated format strings for consistent table rendering
}
```

#### **Key Methods**

**`calculateTableDimensions(List<Gadget> allGadgets)`**:
- Analyzes all device names, power ratings, status values, and usage data
- Calculates optimal width for each column based on actual content
- Considers session info and timer data for accurate sizing
- Applies minimum width constraints for readability

**`createTableFormatStrings(TableDimensions dim)`**:
- Generates dynamic format strings based on calculated dimensions
- Creates border patterns using calculated widths
- Configures alignment (left-aligned text, right-aligned numbers)

**`displayTableRows(List<Gadget> allGadgets, TableFormatStrings formats)`**:
- Renders device rows with proper formatting
- Handles current session information display
- Shows timer information when applicable

### Benefits
- **No Truncation**: Full device names always visible
- **Dynamic Sizing**: Columns adapt to content automatically
- **Professional Appearance**: Consistent alignment and spacing
- **Performance Optimized**: Minimal string operations and object creation

### Before vs After Optimization

**Before (v2.0)**:
```java
// Single large method with repetitive calculations
private void displayAutoAlignedTable(List<Gadget> allGadgets) {
    // 120+ lines of mixed responsibility code
    // Duplicate calculations for dimensions
    // Inline string formatting
}
```

**After (v2.1)**:
```java
// Modular design with focused responsibilities
private void displayAutoAlignedTable(List<Gadget> allGadgets) {
    TableDimensions dimensions = calculateTableDimensions(allGadgets);
    TableFormatStrings formats = createTableFormatStrings(dimensions);
    displayTableRows(allGadgets, formats);
}
```

---

## 🚀 Setup & Execution Guide

### Prerequisites
- **Java 11+** (required for compilation and runtime)
- **Apache Maven 3.6+** (for dependency management and building)
- **DynamoDB Local** (included in project)

### Quick Start (Automated)
```bash
# From project root directory
QUICK_START.bat
```

### Manual Setup

#### Step 1: Start DynamoDB Local
```bash
# Option 1: Use provided script
start-dynamodb.bat

# Option 2: Manual startup
cd dynamodb-local
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8000
```

#### Step 2: Build Application
```bash
cd iot-smart-home-dashboard
mvn clean compile package
```

#### Step 3: Run Application
```bash
# Option 1: Using Maven (development)
mvn exec:java -Dexec.mainClass="com.smarthome.SmartHomeDashboard"

# Option 2: Using JAR file (production)
java -jar target/iot-smart-home-dashboard-1.0.0.jar
```

### Demo Mode
If DynamoDB is unavailable, the application automatically switches to demo mode:
- All functionality works normally
- Data is stored in memory only
- Data is lost when application closes
- Console shows: "🎮 Running in DEMO MODE"

---

## 💡 Development Guidelines

### Code Organization
- **Layered Architecture**: UI → Service → Model → Utility
- **Single Responsibility**: Each class has one main purpose
- **Dependency Injection**: Services are injected into higher layers
- **Error Handling**: Comprehensive try-catch with user-friendly messages

### Adding New Device Types
1. **Update GadgetService.java**: Add new type to `VALID_GADGET_TYPES`
2. **Add Models**: Include brand names in appropriate model validation
3. **Update UI**: Add control method in `SmartHomeDashboard.java`
4. **Test**: Verify device creation, connection, and status changes

### Adding New Rooms
1. **Update GadgetService.java**: Add room name to `VALID_ROOMS`
2. **Test**: Verify room validation works in device connection

### Security Features
- **Password Hashing**: Always use BCrypt, never store plain text
- **Input Validation**: Sanitize all user inputs
- **Account Lockout**: Configurable in `calculateLockoutMinutes()`
- **Session Management**: Automatic cleanup on application exit

---

## 🗄️ Database Configuration

### DynamoDB Local Setup
```java
// Connection configuration in DynamoDBConfig.java
private static final String ENDPOINT = "http://localhost:8000";
private static final Region REGION = Region.US_EAST_1;

// Table configuration
Table Name: "customers"
Partition Key: email (String)
```

### Table Schema
```json
{
  "email": "string (partition key)",
  "fullName": "string",
  "password": "string (BCrypt hash)",
  "gadgets": "list of gadget objects",
  "failedLoginAttempts": "number",
  "accountLockedUntil": "datetime",
  "lastFailedLoginTime": "datetime"
}
```

### Demo Mode Configuration
When DynamoDB is unavailable:
- Uses `HashMap<String, Customer>` for storage
- All validation and business logic remains active
- Data persists only during application runtime

---

## 🔧 Common Issues & Solutions

### Build Issues

#### Problem: Maven dependency resolution fails
**Solution**:
```bash
mvn clean
mvn dependency:resolve
mvn compile package
```

#### Problem: Java version compatibility
**Solution**:
```bash
# Check Java version
java -version

# Ensure Java 11+ is installed and JAVA_HOME is set correctly
echo $JAVA_HOME
```

### Runtime Issues

#### Problem: DynamoDB connection fails
**Symptoms**: Application shows connection errors
**Solution**:
1. Ensure DynamoDB Local is running: `start-dynamodb.bat`
2. Check port 8000 is available: `netstat -an | findstr 8000`
3. Application will automatically fall back to demo mode

#### Problem: Scanner input stream errors
**Symptoms**: "No line found" exceptions
**Solution**:
- Run application interactively (not with input redirection)
- Ensure console supports interactive input

#### Problem: Account lockout
**Symptoms**: "Account is locked" messages
**Solution**:
- Wait for lockout period to expire (5min/15min/60min)
- Use "Forgot Password" option to reset
- Lockout times are in `CustomerService.calculateLockoutMinutes()`

### Development Issues

#### Problem: Adding new features
**Best Practice**:
1. Follow existing code patterns
2. Add validation in service layer
3. Update UI in SmartHomeDashboard
4. Test both database and demo modes
5. Update user guide documentation

#### Problem: Debugging
**Useful Debug Points**:
- Check `DynamoDBConfig.getEnhancedClient()` for database issues
- Monitor `SessionManager.isLoggedIn()` for authentication flow
- Verify input validation in service classes

---

## 📚 Additional Resources

### Key Dependencies (pom.xml)
- **AWS SDK v2**: DynamoDB integration
- **BCrypt**: Password hashing
- **SLF4J**: Logging framework
- **Maven Shade**: Executable JAR creation

### Development Tools
- **IDE**: Any Java IDE (IntelliJ IDEA, Eclipse, VS Code)
- **Build Tool**: Apache Maven
- **Database**: DynamoDB Local
- **Version Control**: Git (recommended)

### Testing
- Application includes comprehensive validation
- Manual testing through console interface
- Demo mode for testing without database setup

---

## 💻 Code Examples

### Adding a New Device Type

**Step 1: Update GadgetService.java**
```java
// Add to VALID_GADGET_TYPES array
private static final String[] VALID_GADGET_TYPES = {
    "TV", "AC", "FAN", "LIGHT", "SWITCH", "CAMERA", "DOOR_LOCK", 
    "GEYSER", "DOORBELL", "VACUUM", "AIR_PURIFIER", "SPEAKER", 
    "WATER_PURIFIER", "THERMOSTAT", "WASHING_MACHINE", 
    "REFRIGERATOR", "MICROWAVE",
    "DISHWASHER" // New device type
};

// Add model validation
case "DISHWASHER":
    validModels = Arrays.asList(
        "Bosch", "IFB", "Siemens", "Whirlpool", "LG", "Kaff", "Faber"
    );
    break;
```

**Step 2: Update SmartHomeDashboard.java**
```java
// Add menu option in showGadgetControlMenu()
System.out.println("18. Control Dishwasher");
System.out.println("19. Exit"); // Update exit number

// Add case in switch statement
case 18: controlDishwasher(); break;

// Add control method
private static void controlDishwasher() {
    System.out.println("\n=== Control Dishwasher ===");
    
    try {
        String model = getValidatedGadgetInput("Dishwasher Model", 
                "Bosch, IFB, Siemens, Whirlpool, LG, Kaff, Faber, etc.");
        if (model == null) return;
        
        String roomName = getValidatedGadgetInput("Room Name", 
                "Kitchen, Dining Room, Utility Room, etc.");
        if (roomName == null) return;
        
        smartHomeService.connectToGadget("DISHWASHER", model, roomName);
    } catch (Exception e) {
        System.out.println("Failed to control Dishwasher. Please try again.");
    }
}
```

### Adding Custom Validation

**Email Domain Restriction Example**:
```java
// In CustomerService.java
public boolean isValidEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
        return false;
    }
    email = email.trim().toLowerCase();
    
    // Basic format validation
    boolean formatValid = email.matches("^[a-z0-9+_.-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
    
    // Custom domain validation (example)
    String[] allowedDomains = {"gmail.com", "yahoo.com", "outlook.com", "company.com"};
    boolean domainValid = Arrays.stream(allowedDomains)
        .anyMatch(domain -> email.endsWith("@" + domain));
    
    return formatValid && domainValid;
}
```

### Session Data Extension

**Adding User Preferences**:
```java
// In SessionManager.java
private Map<String, Object> userPreferences = new HashMap<>();

public void setUserPreference(String key, Object value) {
    if (isLoggedIn()) {
        userPreferences.put(currentUser.getEmail() + "_" + key, value);
    }
}

public Object getUserPreference(String key) {
    if (isLoggedIn()) {
        return userPreferences.get(currentUser.getEmail() + "_" + key);
    }
    return null;
}
```

---

## 🧪 Testing Guidelines

### Manual Testing Checklist

**Authentication Flow**:
- [ ] Register new user with valid data
- [ ] Register with invalid email format
- [ ] Register with weak password
- [ ] Register with existing email
- [ ] Login with correct credentials
- [ ] Login with wrong password (test lockout)
- [ ] Password reset flow

**Device Management Flow**:
- [ ] Connect new device with number selection
- [ ] Try duplicate device in same room
- [ ] Connect same device type in different room
- [ ] View all devices with real-time usage data
- [ ] Change device status using device number selection
- [ ] Test with invalid device types
- [ ] Verify power consumption updates in real-time
- [ ] Check energy efficiency calculations (≤100%)

**Security Testing**:
- [ ] Test account lockout (3, 5, 7 failed attempts)
- [ ] Test lockout expiry
- [ ] Test password strength validation
- [ ] Test session management

**Timer System Testing**:
- [ ] Schedule device timer with valid date/time
- [ ] Test countdown display accuracy (hours and minutes)
- [ ] Cancel scheduled timer using number selection
- [ ] Test [OVERDUE] display for past-due timers
- [ ] Verify automatic device execution at scheduled time

**Smart Scenes Testing**:
- [ ] Execute scenes using number selection (1-8)
- [ ] Test scene automation with multiple devices
- [ ] Verify scene execution results and feedback
- [ ] Test scene execution with missing devices
- [ ] Check device state coordination in scenes

**Health Monitoring Testing**:
- [ ] Generate health reports for different device types
- [ ] Test health score calculations (0-100%)
- [ ] Verify energy efficiency calculations (≤100%)
- [ ] Test device-specific diagnostic messages
- [ ] Check maintenance recommendations for low health scores

**Energy Analytics Testing**:
- [ ] Verify real-time power consumption updates
- [ ] Test usage time tracking (current session + total)
- [ ] Check energy consumption calculations (kWh)
- [ ] Test analytics report generation
- [ ] Verify cost analysis and projections

**Calendar Events Testing**:
- [ ] Create events using number selection for types
- [ ] Test event automation triggers
- [ ] Verify pre-event and post-event device control
- [ ] Test event scheduling and timeline management

**Weather Integration Testing**:
- [ ] Test weather-based device recommendations
- [ ] Verify weather condition simulations
- [ ] Check automation rule suggestions
- [ ] Test environmental monitoring features

### Automated Testing Setup

For future automated testing, consider this structure:

```java
// Example test class structure
public class CustomerServiceTest {
    private CustomerService customerService;
    
    @BeforeEach
    void setUp() {
        customerService = new CustomerService();
    }
    
    @Test
    void testValidPasswordAccepted() {
        assertTrue(customerService.isValidPassword("StrongPass123!"));
    }
    
    @Test
    void testWeakPasswordRejected() {
        assertFalse(customerService.isValidPassword("weak"));
    }
    
    // Add more test methods...
}
```

---

## 🚀 Production Deployment

### Pre-Deployment Checklist

**Environment Setup**:
- [ ] Java 11+ installed on target system
- [ ] DynamoDB Local configured and tested
- [ ] Network ports available (8000 for DynamoDB)
- [ ] Sufficient disk space for logs and data
- [ ] Backup strategy for DynamoDB data

**Security Hardening**:
- [ ] Review password policy settings
- [ ] Adjust lockout timing for production use
- [ ] Configure proper logging levels
- [ ] Secure DynamoDB access
- [ ] Review error messages (remove debug info)

**Performance Optimization**:
- [ ] Test with expected user load
- [ ] Monitor memory usage
- [ ] Configure JVM parameters if needed
- [ ] Set up log rotation

### Production Configuration

**JVM Tuning** (for larger deployments):
```bash
java -Xms512m -Xmx1024m -XX:+UseG1GC \
     -jar iot-smart-home-dashboard-1.0.0.jar
```

**DynamoDB Production Setup**:
```bash
# For production, consider AWS DynamoDB instead of Local
# Update DynamoDBConfig.java with production endpoints
private static final String ENDPOINT = "https://dynamodb.region.amazonaws.com";
```

### Monitoring & Maintenance

**Log Files to Monitor**:
- Application logs (console output)
- DynamoDB Local logs
- System resource usage (CPU, memory)
- Failed login attempts (security monitoring)

**Regular Maintenance Tasks**:
- Monitor disk space (DynamoDB data files)
- Review security logs for suspicious activity
- Update dependencies periodically
- Backup user data regularly

---

## ⚡ Performance Optimization

### Memory Management

**Current Memory Usage**:
- Base application: ~50MB
- Per user session: ~1-2MB
- Per device: ~0.1MB
- DynamoDB Local: ~100-200MB

**Optimization Tips**:
```java
// In SmartHomeService.java - Connection pooling example
private static final int MAX_CONNECTIONS = 10;
private static final ConnectionPool connectionPool = 
    new ConnectionPool(MAX_CONNECTIONS);

// Lazy loading for large device lists
public List<Gadget> getDevicesLazily() {
    return devices.stream()
        .limit(100) // Paginate for large lists
        .collect(Collectors.toList());
}
```

### Database Optimization

**Query Optimization**:
```java
// Use batch operations for multiple devices
public void updateMultipleDevices(List<Gadget> gadgets) {
    // Batch update implementation
    customerTable.batchWriteItem(
        gadgets.stream()
            .map(this::createWriteRequest)
            .collect(Collectors.toList())
    );
}
```

**Connection Management**:
```java
// Proper connection lifecycle
public void shutdown() {
    try {
        if (dynamoDbClient != null) {
            dynamoDbClient.close();
        }
    } catch (Exception e) {
        System.err.println("Error closing DynamoDB connection: " + e.getMessage());
    }
}
```

---

## 🔄 Version Control & Collaboration

### Git Workflow

**Branch Strategy**:
```
master/main     - Production ready code
develop         - Integration branch
feature/*       - New features
hotfix/*        - Critical fixes
```

**Commit Message Format**:
```
type(scope): description

feat(auth): add simplified password reset
fix(device): resolve duplicate device validation
docs(guide): update developer documentation
```

### Code Review Checklist

**Security Review**:
- [ ] No hardcoded passwords or keys
- [ ] Input validation implemented
- [ ] Error messages don't expose sensitive info
- [ ] Session management properly handled

**Code Quality**:
- [ ] Follows existing patterns
- [ ] Proper error handling
- [ ] Adequate comments for complex logic
- [ ] No code duplication

---

**📝 Note**: This guide covers the current implementation. For future enhancements, follow the established patterns and update this documentation accordingly.

**📞 Support**: Refer to troubleshooting sections in the main User Guide for additional help.

**🔧 Contributing**: Before making changes, review the code examples and testing guidelines above.

---

## 🔄 Version History

### v2.1 (September 2025) - Performance & Code Quality Update
**Optimizations**:
- ✅ **Code Refactoring**: Broke down large methods into focused, single-responsibility functions
- ✅ **Table System Enhancement**: Implemented helper classes for better organization
- ✅ **Performance Improvements**: Reduced duplicate calculations and memory usage
- ✅ **Input Handling Fix**: Resolved infinite loop issue in main menu
- ✅ **Project Cleanup**: Removed temporary test files and optimized codebase

**Technical Improvements**:
- Refactored `displayAutoAlignedTable()` into modular components
- Added `TableDimensions` and `TableFormatStrings` helper classes
- Optimized string formatting operations
- Enhanced code readability and maintainability
- Fixed scanner input handling for better stability

### v2.0 (September 2025) - Major Feature Release  
**Features Added**:
- Voice Command Removal, Timer System, Smart Scenes
- Health Monitoring, Energy Analytics, Calendar Events, Weather Integration

---

*Developer Guide v2.1 - Last Updated: September 2025*
*Latest Update: Performance Optimization & Code Quality Enhancement*
*Prepared by: Sushma Mainampati*