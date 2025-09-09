# IoT Smart Home Dashboard - User Guide

## Table of Contents
1. [Application Overview](#application-overview)
2. [System Requirements](#system-requirements)
3. [Installation and Setup](#installation-and-setup)
4. [User Guide](#user-guide)
5. [Technical Architecture](#technical-architecture)
6. [Architecture Diagrams](#architecture-diagrams)
7. [Troubleshooting](#troubleshooting)
8. [Developer Information](#developer-information)
9. [Support and Maintenance](#support-and-maintenance)

---

## Application Overview

### About IoT Smart Home Dashboard
The IoT Smart Home Dashboard is a comprehensive Java-based application designed to provide centralized control and management of smart home devices. This console-based application enables users to register, authenticate, and manage various IoT gadgets across different rooms in their home environment.

### Key Features
- **Multi-User Support**: Secure user registration and authentication system with interactive flows
- **Group Management**: Share devices across users, admin controls, member management
- **Comprehensive Device Management**: Control 18+ categories of smart devices across Indian home environments
- **Advanced Timer System**: Schedule devices with automatic cleanup of overdue timers and status verification
- **Smart Automation**: One-click scene execution for daily routines (Morning, Evening, Night, Movie, Workout, etc.)
- **Health Monitoring**: Real-time device health analysis with diagnostic insights and maintenance recommendations
- **Energy Management**: Track power consumption, usage patterns, and efficiency metrics with live session updates
- **Calendar Integration**: Event-based automation with smart scheduling and reminder system
- **Weather Intelligence**: Weather-based device control suggestions and automation rules
- **Room-Based Organization**: Manage devices across 38+ room types including traditional Indian spaces
- **Real-Time Analytics**: Live usage tracking with session-based calculations and comprehensive reporting
- **Enhanced User Experience**: Interactive authentication, graceful exit handling, and improved error recovery
- **Data Persistence**: Integration with AWS DynamoDB for reliable data storage
- **Production Ready**: Automatic session cleanup, resource management, and graceful shutdown handling

### Comprehensive Indian Smart Home Devices
#### Entertainment & Media Devices
| Device Type | Indian Market Brands | Functionality |
|-------------|---------------------|---------------|
| **TV** | MI, Realme, OnePlus, TCL, Samsung, LG, Sony, Panasonic, Haier, Thomson, Kodak, Motorola, Nokia, Videocon, BPL, Micromax, Intex, Vu, Shinco, Daiwa, Akai, Sanyo, Lloyd, Hitachi, Toshiba, Sharp, Philips, Iffalcon, Coocaa, MarQ | Power ON/OFF control |
| **Smart Speaker** | Amazon Echo, Google Home, MI, Realme, JBL, Sony, Philips, Boat, Zebronics, Portronics, Motorola, Samsung, LG | Power ON/OFF control |

#### Climate Control Systems
| Device Type | Indian Market Brands | Functionality |
|-------------|---------------------|---------------|
| **Air Conditioner** | Voltas, Blue Star, Godrej, Lloyd, Carrier, Daikin, Hitachi, LG, Samsung, Panasonic, Whirlpool, O General, Mitsubishi, Haier, IFB, Sansui, Koryo, MarQ, Onida, Kelvinator, Bajaj, Usha, Orient, Maharaja Whiteline, Hindware, Havells, Videocon, BPL, Electrolux, Bosch, Siemens, Midea, Gree | Power ON/OFF control |
| **Fan** | Havells, Bajaj, Usha, Orient, Crompton, Luminous, Atomberg, Polycab, Anchor, Syska, V-Guard, Khaitan, Surya, Almonard, Maharaja Whiteline, Hindware, Rico, GM, Standard, Activa, Singer, Orpat, Seema, Citron, Lazer, Finolex, Replay | Power ON/OFF control |
| **Air Purifier** | MI, Realme, Honeywell, Philips, Kent, Eureka Forbes, Sharp, LG, Samsung, Panasonic, Godrej, Havells, Bajaj, Blue Star, Crompton, Orient, Coway, Dyson, IQAir | Power ON/OFF control |
| **Smart Thermostat** | Honeywell, Johnson Controls, Schneider Electric, Siemens, Nest, Ecobee, MI, Realme, Godrej, Blue Star, Carrier | Temperature control |

#### Lighting & Electrical Systems
| Device Type | Indian Market Brands | Functionality |
|-------------|---------------------|---------------|
| **Smart Light** | Philips Hue, MI, Syska, Havells, Wipro, Bajaj, Orient, Crompton, Polycab, V-Guard, Anchor, Luminous, Eveready, Osram, Godrej, Schneider Electric, Legrand, Panasonic | Power ON/OFF control |
| **Smart Switch** | Anchor, Havells, Legrand, Schneider Electric, Wipro, Polycab, V-Guard, Orient, Syska, Bajaj, Godrej, MI, Realme, Panasonic, Philips, Simon, Roma, Crabtree, MK Electric | Power ON/OFF control |

#### Security & Safety Systems
| Device Type | Indian Market Brands | Functionality |
|-------------|---------------------|---------------|
| **Security Camera** | MI, Realme, TP-Link, D-Link, Hikvision, CP Plus, Godrej, Honeywell, Dahua, Panasonic, Sony, Samsung, LG, Zebronics, Imou, Qubo, Alert Eyes, Digisol, Reolink, Kent Cam | Power ON/OFF control |
| **Smart Door Lock** | Godrej, Yale, Samsung, Philips, MI, Realme, Ultraloq, Kaadas, Harfo, Dorset, IPSA, Atom, Ozone, Plantex, Hafele, Hettich, Cleveland, Ebco, Dorma, Honeywell | Lock/Unlock control |
| **Smart Doorbell** | MI, Realme, Godrej, Yale, Ring, Honeywell, CP Plus, Hikvision, D-Link, TP-Link, Qubo, Alert Eyes, Zebronics | Power ON/OFF control |

#### Kitchen & Home Appliances
| Device Type | Indian Market Brands | Functionality |
|-------------|---------------------|---------------|
| **Refrigerator** | LG, Samsung, Whirlpool, Godrej, Haier, Panasonic, Bosch, IFB, Videocon, BPL, Onida, Kelvinator, Lloyd, Hitachi | Power ON/OFF control |
| **Microwave** | LG, Samsung, IFB, Panasonic, Whirlpool, Godrej, Bajaj, Morphy Richards, Haier, Bosch, Onida, BPL, Videocon | Power ON/OFF control |
| **Washing Machine** | LG, Samsung, Whirlpool, IFB, Bosch, Godrej, Haier, Panasonic, Videocon, BPL, Onida, Kelvinator, Lloyd | Power ON/OFF control |
| **Water Heater/Geyser** | Bajaj, Havells, Crompton, V-Guard, Racold, AO Smith, Haier, Whirlpool, LG, Samsung, Godrej, Orient, Usha, Maharaja Whiteline, Hindware, Venus, Singer, Morphy Richards | Power ON/OFF control |
| **Water Purifier** | Kent, Aquaguard, Pureit, LivPure, Blue Star, Havells, Eureka Forbes, AO Smith, Faber, V-Guard, Godrej, HUL | Power ON/OFF control |

#### Cleaning Systems
| Device Type | Indian Market Brands | Functionality |
|-------------|---------------------|---------------|
| **Robotic Vacuum** | MI Robot, Realme TechLife, Eureka Forbes, Kent, Black+Decker, Karcher, Philips, LG, Samsung, Inalsa, American Micronic, Prestige, Panasonic, Bosch, Dyson, Shark, Bissell | Power ON/OFF control |

### Comprehensive Indian House Room Coverage
The application now supports all possible areas in a modern Indian home:

#### Living & Common Areas
- Living Room, Hall, Drawing Room, Family Room, Sitting Room

#### Sleeping Areas  
- Master Bedroom, Bedroom 1, Bedroom 2, Bedroom 3, Guest Room, Kids Room, Parents Room, Childrens Room

#### Kitchen & Dining Areas
- Kitchen, Dining Room, Breakfast Area, Pantry, Store Room

#### Bathroom & Wash Areas
- Master Bathroom, Common Bathroom, Guest Bathroom, Powder Room

#### Outdoor & Utility Spaces
- Balcony, Terrace, Garden, Courtyard, Entrance, Porch, Garage, Parking, Utility Room, Laundry Room

#### Study & Work Spaces
- Study Room, Office Room, Library, Home Office

#### Entertainment & Special Areas
- Home Theater, Entertainment Room, Prayer Room, Pooja Room

#### Service Areas
- Servant Room, Driver Room, Security Room, Storage Room

#### Traditional Indian Spaces
- Verandah, Chowk, Angan, Baithak, Otla

**Total Support**: 38+ room types covering every area of a traditional and modern Indian home

---

## System Requirements

### Minimum Requirements
- **Operating System**: Windows 10/11, macOS 10.14+, Linux (Ubuntu 18.04+)
- **Java Version**: Java 11 or higher
- **Memory**: 512 MB RAM minimum, 1 GB recommended
- **Storage**: 100 MB free disk space
- **Network**: Internet connection (for DynamoDB cloud access)

### Dependencies
- Apache Maven 3.6+
- AWS SDK for Java v2
- DynamoDB Local (included in project)
- BCrypt library for password encryption

---

## Installation and Setup

### Quick Start (Automated)
1. Navigate to the project directory
2. Run the automated setup script:
   ```batch
   QUICK_START.bat
   ```
   This script will:
   - Start DynamoDB Local
   - Build the application
   - Launch the dashboard

### Manual Setup

#### Step 1: Start DynamoDB Local
```batch
start-dynamodb.bat
```
**Or manually:**
```bash
cd dynamodb-local
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8000
```

#### Step 2: Build the Application
```bash
cd iot-smart-home-dashboard
mvn clean compile package
```

#### Step 3: Run the Application
**Option A: Using Maven**
```bash
mvn exec:java -Dexec.mainClass="com.smarthome.SmartHomeDashboard"
```

**Option B: Using JAR file**
```bash
java -jar target/iot-smart-home-dashboard-1.0.0.jar
```

---

## User Guide

### User Journey Flow
```
┌─────────────────┐
│   Start App     │
└─────────┬───────┘
          │
          ▼
┌─────────────────┐
│   Main Menu     │
└─┬─────┬─────┬───┘
  │     │     │
  ▼     ▼     ▼
┌────┐ ┌────┐ ┌──────────────┐    ┌──────────────┐
│Reg │ │Login│ │ Forgot Pass  │───▶│Device Control│
└────┘ └────┘ └──────────────┘    │  Features    │
  │     │            │             └──────────────┘
  │     │            ▼                    │
  │     │     ┌──────────────┐            ▼
  │     │     │Simple Password│    ┌─────────────┐
  │     │     │    Reset      │    │View Gadgets │
  │     │     └──────────────┘    └─────────────┘
  │     │            │                    │
  │     ▼            ▼                    ▼
  │  ┌─────────────────┐           ┌─────────────┐
  │  │ Authenticated   │◄──────────│Change Status│
  │  │   Session       │           └─────────────┘
  │  └─────────────────┘                  │
  │         │                             │
  ▼         ▼                             │
┌───────────────┐                         │
│Success/Failure│                         │
└───────┬───────┘                         │
        │                                 │
        └─────────────────────────────────┘
                        │
                        ▼
                ┌─────────────┐
                │   Logout    │
                │  (App Exit) │
                └─────────────┘
```

### Password Security Flow
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         PASSWORD SECURITY WORKFLOW                         │
└─────────────────────────────────────────────────────────────────────────────┘

                           ┌─────────────────┐
                           │ User Password   │
                           │    Input        │
                           └─────────┬───────┘
                                     │
                                     ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                        PASSWORD VALIDATION                                 │
    │                                                                             │
    │ ┌─────────────┐ ┌─────────────┐ ┌──────────────┐ ┌─────────────────────┐   │
    │ │Length Check │ │Case & Number│ │Special Chars │ │Common Password Block│   │
    │ │(8-128 chars)│ │Validation   │ │  Required    │ │   & Pattern Check   │   │
    │ └─────────────┘ └─────────────┘ └──────────────┘ └─────────────────────┘   │
    └─────────────────────────┬───────────────────────────────────────────────────┘
                              │ Valid Password
                              ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                         BCrypt HASHING                                     │
    │                                                                             │
    │  ┌─────────────────────────────────────────────────────────────────────┐   │
    │  │ Salt Generation → Hash Creation → Secure Storage                   │   │
    │  └─────────────────────────────────────────────────────────────────────┘   │
    └─────────────────────────┬───────────────────────────────────────────────────┘
                              │ Encrypted Password
                              ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                    LOGIN ATTEMPT TRACKING                                  │
    │                                                                             │
    │ ┌──────────────────┐ ┌─────────────────┐ ┌──────────────────────────────┐ │
    │ │ Password Match   │ │ Failed Attempts │ │    Progressive Lockout       │ │
    │ │   Verification   │ │   Counting      │ │   3-4: 5min, 5-6: 15min     │ │
    │ │                  │ │                 │ │      7+: 60min               │ │
    │ └──────────────────┘ └─────────────────┘ └──────────────────────────────┘ │
    └─────────────────────────┬───────────────────────────────────────────────────┘
                              │ Authentication Result
                              ▼
                    ┌─────────────────────────┐
                    │ Success: Session Created│
                    │ Failure: Error Message  │
                    │ Locked: Timeout Display │
                    └─────────────────────────┘
```

### Getting Started

#### 1. Customer Registration
- Select option `1. Customer Register` from the main menu
- Provide the following information:
  - **Full Name**: Minimum 2 characters, letters and spaces only
  - **Email**: Valid email format (e.g., user@example.com)
  - **Password**: Minimum 6 characters
  - **Confirm Password**: Must match the original password

#### 2. Customer Login
- Select option `2. Customer Login` from the main menu
- Enter your registered email and password
- Upon successful login, you'll have access to device management features

#### 3. Forgot Password (Simple Reset)
- Select option `3. Forgot Password` from the main menu
- Enter your registered email address
- System will verify the email exists in the database
- Enter your new password following the password requirements
- Confirm your new password
- Upon successful reset, you can login with your new password immediately

### Device Management

#### 3. Control Gadgets - Comprehensive Indian Smart Home
The application now provides 18 categories of smart devices organized by functionality:

```
Smart Home Control Categories:
┌─────────────────────────────────────────────────────────────────────────────┐
│                     ENTERTAINMENT DEVICES                                  │
│  1. TV Control        │  2. Smart Speaker Control                         │
├─────────────────────────────────────────────────────────────────────────────┤
│                      CLIMATE CONTROL                                       │
│  3. AC Control        │  4. Fan Control      │  5. Air Purifier Control   │
│  6. Smart Thermostat Control                                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                     LIGHTING & SWITCHES                                    │
│  7. Smart Light Control │  8. Smart Switch Control                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                      SECURITY & SAFETY                                     │
│  9. Security Camera   │ 10. Smart Door Lock  │ 11. Smart Doorbell         │
├─────────────────────────────────────────────────────────────────────────────┤
│                     KITCHEN & APPLIANCES                                   │
│ 12. Refrigerator      │ 13. Microwave        │ 14. Washing Machine        │
│ 15. Water Heater      │ 16. Water Purifier                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                        CLEANING SYSTEMS                                    │
│ 17. Robotic Vacuum Control                                                 │
└─────────────────────────────────────────────────────────────────────────────┘
```

#### Device Connection Process:
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ Select Category │───▶│ Choose Indian   │───▶│ Select Indian   │
│ from 18 Options │    │ Market Brand    │    │ Room Location   │
│                 │    │                 │    │                 │
│ • Entertainment │    │ TV: MI, Realme, │    │ • Living Room   │
│ • Climate       │    │ OnePlus, etc.   │    │ • Master Bedroom│
│ • Lighting      │    │ AC: Voltas,     │    │ • Kitchen       │
│ • Security      │    │ Blue Star, etc. │    │ • Pooja Room    │
│ • Kitchen       │    │ Fan: Havells,   │    │ • Balcony       │
│ • Cleaning      │    │ Bajaj, etc.     │    │ • 33+ more      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

- **Indian Brand Focus**: All device models prioritize brands available in India
- **Traditional Spaces**: Includes Indian-specific rooms like Pooja Room, Chowk, Angan
- **Complete Coverage**: 16 device categories across all home automation needs

**Important Notes:**
- Only one device of each type per room is allowed
- Devices are automatically set to "OFF" status when first connected
- Each user's devices are completely isolated from other users

#### 4. View Gadgets
- Select option `5. View Device Status & Usage` to see all your connected devices
- **Enhanced Auto-Aligned Table Display** (v2.1):
  - **Smart Column Sizing**: Tables automatically adjust column widths based on your device names
  - **No Truncation**: Full device names are always visible - no more "..." cutoffs
  - **Professional Formatting**: Clean, well-aligned table with proper borders
  - **Dynamic Layout**: Accommodates short and long device names intelligently
- The system displays:
  - Device number for easy reference
  - Complete device type and model (no truncation)
  - Room location
  - Power rating (Watts)
  - Current status (RUNNING/OFF)
  - Usage time (hours and minutes format)
  - Energy consumption (kWh with 3 decimal precision)
  - Current session information for running devices
  - Timer information when scheduled
- Example display:
```
+----+------------------------------------------------------+----------+-----------+-------------+-------------+
| #  | Device                                               | Power    | Status    | Usage Time  | Energy(kWh) |
+----+------------------------------------------------------+----------+-----------+-------------+-------------+
| 1  | TV Samsung 65" QLED Smart TV 4K Ultra HD (Living Room) |     150W | RUNNING   |      1h 30m |       0.112 |
|    |   Current Session:                                   |          |           | 0.8h        |             |
| 2  | AC LG Dual Inverter 1.5 Ton (Master Bedroom)        |    1500W | OFF       |      2h 00m |       0.000 |
+----+------------------------------------------------------+----------+-----------+-------------+-------------+
```
- You can select a specific gadget number to check detailed status

#### 5. Change Gadget Status
- Select option `5. Change Gadget Status` to toggle device power
- Enter the gadget type (TV/AC/FAN/ROBO_VAC_MOP)
- The system will toggle the device between ON and OFF states
- Updated status for all devices will be displayed after the change

### Password Security & Management

#### Password Requirements
All passwords must meet the following security criteria:
- **Length**: 8-128 characters
- **Uppercase**: At least one uppercase letter (A-Z)
- **Lowercase**: At least one lowercase letter (a-z)
- **Numbers**: At least one digit (0-9)
- **Special Characters**: At least one special character (!@#$%^&*()_+-=[]{}|;':",./<>?)
- **Common Password Check**: Cannot be a commonly used password
- **Repetition Check**: Cannot have more than 2 consecutive repeating characters

#### Account Security Features

##### Failed Login Protection
The system implements a progressive lockout mechanism:
- **3-4 failed attempts**: Account locked for 5 minutes
- **5-6 failed attempts**: Account locked for 15 minutes
- **7+ failed attempts**: Account locked for 60 minutes

**Lockout Behavior:**
- Failed attempts are tracked per user account
- Lockout timer starts from the last failed attempt
- Successful login resets the failed attempt counter
- Account automatically unlocks after the lockout period expires

##### Password Reset Security
- **Email Verification**: Only registered email addresses can reset passwords
- **Immediate Reset**: No security questions required - simplified process
- **Password Validation**: New passwords must meet all security requirements
- **Failed Attempts Reset**: Successful password reset clears any existing account locks
- **Secure Hashing**: All passwords are encrypted using BCrypt with salt

#### Common Password Blacklist
The system prevents use of common weak passwords including:
- Standard weak passwords: password, 123456, admin, qwerty
- Indian context passwords: india123, mumbai, delhi, bangalore, cricket, bollywood
- Generic patterns: password1, admin123, welcome, letmein

### Advanced Smart Home Features

#### 7-10. Timer Management System
**Schedule Device Timer (Option 7)**: Set future ON/OFF times for devices
**Set Device Timer (Option 8)**: Create immediate countdown timers  
**Cancel Device Timer (Option 9)**: Remove scheduled timers
**View Scheduled Timers (Option 10)**: Display all timers with countdown

**Timer Display Format:**
```
=== Scheduled Timers ===
1. TV LG in Living Room
   - Turn OFF at: 08-09-2025 16:37 [2h 15m remaining]
2. AC Samsung in Living Room  
   - Turn ON at: 09-09-2025 07:00 [14h 38m remaining]
```

**Timer Features:**
- Real-time countdown display (hours and minutes)
- Automatic execution at scheduled time
- User-friendly number selection for cancellation
- [OVERDUE] indicators for missed timers

#### 11. Calendar Events & Automation
Create smart events that automatically control devices:

**Event Types:**
1. MEETING/CONFERENCE - Study room automation
2. MOVIE/ENTERTAINMENT - Living room cinema setup  
3. SLEEP/BEDTIME - Bedroom nighttime routine
4. COOKING/MEAL - Kitchen preparation
5. WORKOUT/EXERCISE - Exercise environment
6. ARRIVAL/HOME - Welcome home setup
7. DEPARTURE/LEAVING - Energy saving mode

**Example Automation:**
- Movie event: Dims lights, turns on TV, adjusts AC 15 minutes before
- Sleep event: Turns off all lights, activates security, sets bedroom AC

#### 12. Weather-Based Suggestions
Intelligent device control recommendations based on weather:

**Weather Triggers:**
- **Hot Weather (>30°C)**: Suggests AC ON, fan speed increase
- **Cold Weather (<18°C)**: Recommends heater activation
- **High Humidity (>70%)**: Air purifier and dehumidifier suggestions
- **Poor Air Quality (AQI >150)**: Air purification recommendations
- **Rainy Weather**: Indoor climate optimization
- **Stormy Conditions**: Safety device activation

#### 13. Smart Scenes (One-Click Automation)
Pre-configured device combinations for common activities:

**Available Scenes:**
1. **MORNING** - Start your day right
2. **EVENING** - Relax after work  
3. **NIGHT** - Prepare for sleep
4. **AWAY** - Secure and save energy
5. **ENERGY_SAVING** - Minimize consumption
6. **MOVIE** - Perfect cinema experience
7. **WORKOUT** - Exercise environment
8. **COOKING** - Kitchen preparation

**Scene Selection:** Choose by number (1-8) instead of typing scene names

#### 14. Device Health Monitoring
Comprehensive health analysis for all connected devices:

**Health Metrics:**
- **Health Score**: 0-100% with status (EXCELLENT/GOOD/WARNING/CRITICAL)
- **Energy Efficiency**: Actual vs expected power consumption
- **Usage Analysis**: Runtime hours and patterns
- **Performance Indicators**: Device-specific diagnostics

**Low Health Diagnostics:**
- **Below 40%**: Critical failures, immediate inspection needed
- **40-50%**: Significant degradation, professional maintenance required
- **50-60%**: Performance issues detected, maintenance recommended

**Device-Specific Insights:**
- **AC**: Filter status, refrigerant leak detection
- **Refrigerator**: Seal integrity, coil efficiency
- **Geyser**: Sediment buildup indicators
- **TV**: Overheating prevention, ventilation checks

#### 15. Usage Analytics & Insights
Comprehensive energy and usage tracking:

**Analytics Features:**
1. **Energy Consumption Analysis** - Track power usage patterns
2. **Device Usage Patterns** - Monitor runtime statistics  
3. **Cost Analysis & Projections** - Electricity cost calculations
4. **Efficiency Recommendations** - Optimization suggestions
5. **Peak Usage Times** - Identify high-consumption periods

**Real-Time Tracking:**
- Live session usage updates
- Current power consumption display
- Energy efficiency percentages (≤100%)
- Cumulative usage across all sessions

**Usage Display Format:**
```
1. AC Samsung (Living Room) [ON]
   [Power] 1500.0W | [Usage] 47h 23m | [Energy] 71.345 kWh
   [Current session: 2.3 hours]
   [Timer: OFF at 08-09 22:30 (4h 28m)]
```

### Session Management
- **Logout**: The system automatically logs out when you exit the application
- **Session Persistence**: Login session remains active during the application runtime
- **Security**: Passwords are encrypted using BCrypt for maximum security
- **User Isolation**: Each user's data is completely separated and secure

---

## Technical Architecture

### System Architecture Overview
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          PRESENTATION LAYER                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐  ┌────────────────┐  ┌─────────────────────────────────┐│
│  │  Console UI     │  │  Input         │  │  Output Formatting &            ││
│  │  - Main Menu    │  │  Validation    │  │  Error Messages                 ││
│  │  - User Forms   │  │  - Sanitization│  │  - Success Notifications        ││
│  │  - Navigation   │  │  - Type Checks │  │  - Status Reports               ││
│  └─────────────────┘  └────────────────┘  └─────────────────────────────────┘│
└─────────────────────────┬───────────────────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────────────────┐
│                          BUSINESS LOGIC LAYER                               │
├─────────────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────────────────────┐│
│  │                     SmartHomeService                                    ││
│  │  - User Registration & Authentication                                   ││
│  │  - Device Connection & Management                                       ││
│  │  - Status Control & Monitoring                                          ││
│  │  - Business Rules Enforcement                                           ││
│  └─────┬─────────────────────────────┬─────────────────────────────────────┘│
│        │                             │                                      │
│  ┌─────▼─────────────┐         ┌─────▼──────────────────────┐             │
│  │  CustomerService  │         │      GadgetService         │             │
│  │  - User CRUD      │         │  - Device Creation         │             │
│  │  - Authentication │         │  - Model Validation        │             │
│  │  - Profile Mgmt   │         │  - Type Management         │             │
│  │  - Validation     │         │  - Status Operations       │             │
│  └───────────────────┘         └────────────────────────────┘             │
└─────────────────────────┬───────────────────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────────────────┐
│                          DATA ACCESS LAYER                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│  ┌─────────────────────┐  ┌──────────────────┐  ┌─────────────────────────┐ │
│  │  SessionManager     │  │  DynamoDBConfig  │  │  Security Components    │ │
│  │  - Login State      │  │  - DB Connection │  │  - BCrypt Hashing       │ │
│  │  - User Context     │  │  - Table Setup   │  │  - Input Sanitization   │ │
│  │  - Session Cleanup  │  │  - Demo Mode     │  │  - Validation Rules     │ │
│  └─────────────────────┘  └──────────────────┘  └─────────────────────────┘ │
└─────────────────────────┬───────────────────────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────────────────────┐
│                          PERSISTENCE LAYER                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│  ┌────────────────────────┐              ┌─────────────────────────────────┐ │
│  │    DynamoDB Local      │              │         Demo Mode               │ │
│  │    (Development)       │     OR       │      (In-Memory Storage)        │ │
│  │  - Port 8000           │              │  - HashMap<String, Customer>    │ │
│  │  - Local Files         │              │  - No Persistence               │ │
│  │  - customers Table     │              │  - Session Only                 │ │
│  └────────────────────────┘              └─────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Enhanced Class Relationship Diagram
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                             Class Architecture                              │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────┐         ┌──────────────────────┐
│   SmartHomeDashboard │ ◆────── │   SmartHomeService   │
│                      │         │                      │
│ + main()             │         │ + registerCustomer() │
│ + showMainMenu()     │         │ + loginCustomer()    │
│ + registerCustomer() │         │ + connectToGadget()  │
│ + loginCustomer()    │         │ + viewGadgets()      │
│ + controlGadgets()   │         │ + changeGadgetStatus│
│ + viewGadgets()      │         │ + getCurrentUser()   │
│ + changeStatus()     │         │ + isLoggedIn()       │
└──────────────────────┘         └─────────┬────────────┘
                                           │
                    ┌──────────────────────┼──────────────────────┐
                    │                      │                      │
                    ▼                      ▼                      ▼
      ┌─────────────────────┐  ┌──────────────────┐  ┌─────────────────────┐
      │   CustomerService   │  │   GadgetService  │  │   SessionManager    │
      │                     │  │                  │  │                     │
      │ + registerCustomer()│  │ + createGadget() │  │ + login()           │
      │ + authenticateUser()│  │ + validateModel()│  │ + logout()          │
      │ + findByEmail()     │  │ + validateType() │  │ + isLoggedIn()      │
      │ + updateCustomer()  │  │ + validateRoom() │  │ + getCurrentUser()  │
      │ + isValidEmail()    │  └──────────────────┘  │ + updateCurrentUser│
      │ + isValidPassword() │                        └─────────────────────┘
      │ + isValidName()     │                                    │
      └─────────┬───────────┘                                    │
                │                                                │
                ▼                                                ▼
      ┌─────────────────────┐                        ┌─────────────────────┐
      │     Customer        │ ◇─────┐               │   DynamoDBConfig    │
      │                     │       │               │                     │
      │ - email: String     │       │               │ + getEnhancedClient│
      │ - fullName: String  │       │               │ + shutdown()        │
      │ - password: String  │       │               └─────────────────────┘
      │ - gadgets: List     │       │
      │                     │       │
      │ + addGadget()       │       │
      │ + findGadget()      │       │ 1..*
      │ + removeGadget()    │       │               ┌─────────────────────┐
      └─────────────────────┘       └──────────────▶│      Gadget         │
                                                    │                     │
                                                    │ - id: String        │
                                                    │ - type: String      │
                                                    │ - model: String     │
                                                    │ - roomName: String  │
                                                    │ - status: String    │
                                                    │                     │
                                                    │ + toggleStatus()    │
                                                    │ + toString()        │
                                                    └─────────────────────┘
```

### Data Models

#### Customer Entity
- **Primary Key**: Email (String)
- **Attributes**: Full Name, Encrypted Password, List of Gadgets
- **Validation**: Email format, password strength, name format

#### Gadget Entity
- **Attributes**: Type, Model, Room Name, Status, Unique ID
- **Constraints**: One device per type per room per user
- **Status Values**: "ON" or "OFF"

### Security Features

#### Password Security
- **BCrypt Encryption**: All passwords are hashed using BCrypt with salt for maximum security
- **Strength Validation**: Comprehensive password policy enforcement
- **Common Password Prevention**: Blacklist of 30+ common weak passwords
- **Pattern Detection**: Prevents simple patterns and repetitive characters

#### Account Protection
- **Progressive Lockout**: Escalating lockout periods for failed login attempts
- **Automatic Recovery**: Accounts unlock automatically after timeout period
- **Reset Protection**: Simplified password reset with email verification
- **Attempt Tracking**: Failed login attempts are tracked and displayed to users

#### Data Security
- **User Isolation**: Complete data separation between users
- **Input Validation**: Comprehensive sanitization and validation
- **Session Management**: Secure session handling with automatic cleanup
- **Email-based Keys**: User data accessed only through verified email addresses

---

## Architecture Diagrams

### Data Flow Architecture
```
┌──────────────────────────────────────────────────────────────────────────────┐
│                              DATA FLOW DIAGRAM                              │
└──────────────────────────────────────────────────────────────────────────────┘

User Input ──────┐
                 │
                 ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    INPUT VALIDATION LAYER                                  │
│  ┌────────────────┐ ┌────────────────┐ ┌─────────────────┐                │
│  │ Null Checking  │ │ Format Validation│ │ Length Validation│               │
│  │ Empty Checking │ │ Regex Patterns  │ │ Business Rules  │               │
│  └────────────────┘ └────────────────┘ └─────────────────┘                │
└─────────────────────────┬───────────────────────────────────────────────────┘
                          │ Valid Data
                          ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         BUSINESS LOGIC                                     │
│                                                                             │
│  Registration Flow:              Authentication Flow:                       │
│  ┌─────────────────┐             ┌──────────────────┐                      │
│  │ 1. Check Email  │             │ 1. Find User     │                      │
│  │    Uniqueness   │             │ 2. Verify BCrypt │                      │
│  │ 2. Hash Password│             │ 3. Create Session│                      │
│  │ 3. Create User  │             └──────────────────┘                      │
│  └─────────────────┘                                                       │
│                                                                             │
│  Device Management Flow:                                                    │
│  ┌─────────────────────────────────────────────────────────────────────────┤
│  │ 1. Check Authentication → 2. Validate Device → 3. Check Duplicates     │
│  │ 4. Create/Update Device → 5. Update User Data → 6. Update Session      │
│  └─────────────────────────────────────────────────────────────────────────┤
└─────────────────────────┬───────────────────────────────────────────────────┘
                          │ Processed Data
                          ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                        DATA PERSISTENCE                                    │
│                                                                             │
│  ┌─────────────────────────────┐    ┌──────────────────────────────────┐   │
│  │      DynamoDB Mode          │    │          Demo Mode               │   │
│  │                             │    │                                  │   │
│  │ ┌─────────────────────────┐ │    │ ┌──────────────────────────────┐ │   │
│  │ │ Create/Update/Query     │ │    │ │ HashMap Operations           │ │   │
│  │ │ Enhanced Client         │ │    │ │ In-Memory Storage            │ │   │
│  │ │ Table Schema Mapping    │ │    │ │ Session-Only Data            │ │   │
│  │ └─────────────────────────┘ │    │ └──────────────────────────────┘ │   │
│  └─────────────────────────────┘    └──────────────────────────────────┘   │
└─────────────────────────┬───────────────────────────────────────────────────┘
                          │ Result Data
                          ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         RESPONSE LAYER                                     │
│  ┌────────────────┐ ┌────────────────┐ ┌─────────────────┐                │
│  │ Success Messages│ │ Error Messages │ │ Status Updates  │                │
│  │ Confirmation    │ │ Validation Errs│ │ Device Lists    │                │
│  └────────────────┘ └────────────────┘ └─────────────────┘                │
└─────────────────────────┬───────────────────────────────────────────────────┘
                          │
                          ▼
                    Console Output
```

### Security Architecture
```
┌──────────────────────────────────────────────────────────────────────────────┐
│                            SECURITY ARCHITECTURE                            │
└──────────────────────────────────────────────────────────────────────────────┘

                           ┌─────────────────────┐
                           │   USER INPUT        │
                           │   (Untrusted)       │
                           └─────────┬───────────┘
                                     │
                                     ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                      INPUT SANITIZATION                                     │
    │  ┌──────────────────┐ ┌──────────────────┐ ┌──────────────────────────────┐│
    │  │ SQL Injection    │ │ XSS Prevention   │ │ Input Length Limits          ││
    │  │ Prevention       │ │ (Console Based)  │ │ & Type Validation            ││
    │  └──────────────────┘ └──────────────────┘ └──────────────────────────────┘│
    └─────────────────────────┬───────────────────────────────────────────────────┘
                              │ Sanitized Input
                              ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                       AUTHENTICATION                                        │
    │                                                                             │
    │  Registration:                      Login:                                  │
    │  ┌────────────────────────────────┐ ┌─────────────────────────────────────┐│
    │  │ 1. Email Uniqueness Check      │ │ 1. Email Lookup                     ││
    │  │ 2. Password Policy Enforcement │ │ 2. BCrypt Password Verification     ││
    │  │ 3. BCrypt Hashing (Salt+Hash)  │ │ 3. Session Token Creation           ││
    │  │ 4. Secure Storage              │ │ 4. Login State Management           ││
    │  └────────────────────────────────┘ └─────────────────────────────────────┘│
    └─────────────────────────┬───────────────────────────────────────────────────┘
                              │ Authenticated User
                              ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                       AUTHORIZATION                                         │
    │                                                                             │
    │  ┌──────────────────────────────────────────────────────────────────────┐  │
    │  │               Session-Based Access Control                           │  │
    │  │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────┐  │  │
    │  │  │ Login Required  │  │ User Isolation  │  │ Data Segregation    │  │  │
    │  │  │ for All Device  │  │ by Email Key    │  │ Per User Account    │  │  │
    │  │  │ Operations      │  └─────────────────┘  └─────────────────────┘  │  │
    │  │  └─────────────────┘                                                  │  │
    │  └──────────────────────────────────────────────────────────────────────┘  │
    └─────────────────────────┬───────────────────────────────────────────────────┘
                              │ Authorized Operations
                              ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                         DATA PROTECTION                                     │
    │                                                                             │
    │  ┌───────────────────┐ ┌────────────────────┐ ┌─────────────────────────┐ │
    │  │ Password Hashing  │ │ User Data Isolation│ │ Secure Session Mgmt     │ │
    │  │ • BCrypt + Salt   │ │ • Email-based Keys │ │ • Auto-logout on Exit   │ │
    │  │ • Never Plaintext │ │ • No Cross-User    │ │ • In-Memory Sessions    │ │
    │  │ • Strength Policy │ │   Data Access      │ │ • No Session Persistence│ │
    │  └───────────────────┘ └────────────────────┘ └─────────────────────────┘ │
    └─────────────────────────────────────────────────────────────────────────────┘
```

### Smart Home Layout & Device Management
```
┌──────────────────────────────────────────────────────────────────────────────┐
│                        SMART HOME LAYOUT ARCHITECTURE                       │
└──────────────────────────────────────────────────────────────────────────────┘

                              ┌─────────────────┐
                              │  USER ACCOUNT   │
                              │  (Authenticated)│
                              └─────────┬───────┘
                                        │
                                        ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                           ROOM MANAGEMENT                                   │
    │                                                                             │
    │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐         │
    │  │   HallWay   │ │  BedRoom1   │ │  BedRoom2   │ │  BedRoom3   │         │
    │  │             │ │             │ │             │ │             │         │
    │  │ Max Devices:│ │ Max Devices:│ │ Max Devices:│ │ Max Devices:│         │
    │  │ • 1 TV      │ │ • 1 TV      │ │ • 1 TV      │ │ • 1 TV      │         │
    │  │ • 1 AC      │ │ • 1 AC      │ │ • 1 AC      │ │ • 1 AC      │         │
    │  │ • 1 Fan     │ │ • 1 Fan     │ │ • 1 Fan     │ │ • 1 Fan     │         │
    │  │ • 1 RoboVac │ │ • 1 RoboVac │ │ • 1 RoboVac │ │ • 1 RoboVac │         │
    │  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘         │
    └─────────────────────────┬───────────────────────────────────────────────────┘
                              │ Device Assignments
                              ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                        DEVICE TYPE MANAGEMENT                              │
    │                                                                             │
    │  ┌──────────────────┐  ┌──────────────────┐  ┌───────────────────────────┐ │
    │  │       TV         │  │       AC         │  │         FAN               │ │
    │  │                  │  │                  │  │                           │ │
    │  │ Models:          │  │ Models:          │  │ Models:                   │ │
    │  │ • Samsung, Sony  │  │ • LG, Voltas     │  │ • Atomberg, Crompton     │ │
    │  │ • LG, TCL        │  │ • Daikin, Hitachi│  │ • Havells, Bajaj         │ │
    │  │ • Hisense, MI    │  │ • Carrier, Godrej│  │ • Usha, Orient           │ │
    │  │ • + 20 more      │  │ • + 25 more      │  │ • + 25 more              │ │
    │  │ Status: ON/OFF   │  │ Status: ON/OFF   │  │ Status: ON/OFF            │ │
    │  │ Default: OFF     │  │ Default: OFF     │  │ Default: OFF              │ │
    │  └──────────────────┘  └──────────────────┘  └───────────────────────────┘ │
    │                                                                             │
    │  ┌───────────────────────────────────────────────────────────────────────┐ │
    │  │                    ROBO VAC & MOP                                     │ │
    │  │                                                                       │ │
    │  │ Models: • Robo Vac & Mop, iRobot Roomba, Xiaomi Mi Robot            │ │
    │  │         • Eufy RoboVac, Shark IQ Robot, Dyson V15 + 18 more         │ │
    │  │ Status: ON/OFF                                                        │ │
    │  │ Default: OFF                                                          │ │
    │  └───────────────────────────────────────────────────────────────────────┘ │
    └─────────────────────────┬───────────────────────────────────────────────────┘
                              │ Device Control
                              ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                        BUSINESS RULES ENGINE                                │
    │                                                                             │
    │  Constraints:                        Operations:                            │
    │  ┌───────────────────────────────┐   ┌─────────────────────────────────────┐│
    │  │ • One device type per room    │   │ • Add Device (with validation)      ││
    │  │ • User isolation enforced     │   │ • Remove Device                     ││
    │  │ • Status toggle validation    │   │ • Toggle Status (ON ↔ OFF)          ││
    │  │ • Model validation required   │   │ • View All Devices                  ││
    │  │ • Room name validation        │   │ • Check Individual Device Status    ││
    │  └───────────────────────────────┘   └─────────────────────────────────────┘│
    └─────────────────────────────────────────────────────────────────────────────┘
```

### Deployment Architecture
```
┌──────────────────────────────────────────────────────────────────────────────┐
│                            DEPLOYMENT ARCHITECTURE                          │
└──────────────────────────────────────────────────────────────────────────────┘

                           ┌─────────────────────┐
                           │   DEVELOPER         │
                           │   WORKSTATION       │
                           └─────────┬───────────┘
                                     │ Build & Package
                                     ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                           BUILD ENVIRONMENT                                 │
    │                                                                             │
    │  ┌─────────────────────┐  ┌─────────────────────┐  ┌─────────────────────┐ │
    │  │   Apache Maven      │  │   Java 11+ JDK      │  │   Dependencies      │ │
    │  │                     │  │                     │  │                     │ │
    │  │ • mvn clean         │  │ • Compilation       │  │ • AWS SDK v2        │ │
    │  │ • mvn compile       │  │ • JAR Creation      │  │ • BCrypt            │ │
    │  │ • mvn package       │  │ • Resource Bundling │  │ • SLF4J Logging    │ │
    │  └─────────────────────┘  └─────────────────────┘  └─────────────────────┘ │
    └─────────────────────────┬───────────────────────────────────────────────────┘
                              │ Executable JAR
                              ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                          RUNTIME ENVIRONMENT                                │
    │                                                                             │
    │  ┌─────────────────────────────────────────────────────────────────────────┐│
    │  │                    APPLICATION RUNTIME                                  ││
    │  │                                                                         ││
    │  │  ┌─────────────────────────┐    ┌─────────────────────────────────────┐ ││
    │  │  │ Java Application        │    │        Console Interface           │ ││
    │  │  │                         │    │                                     │ ││
    │  │  │ • JAR Execution         │◄──►│ • User Input/Output                 │ ││
    │  │  │ • Memory Management     │    │ • Menu Navigation                   │ ││
    │  │  │ • Session Handling      │    │ • Error Display                     │ ││
    │  │  └─────────────────────────┘    └─────────────────────────────────────┘ ││
    │  └─────────────────────────────────────────────────────────────────────────┘│
    └─────────────────────────┬───────────────────────────────────────────────────┘
                              │ Data Operations
                              ▼
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │                         DATA LAYER                                          │
    │                                                                             │
    │  ┌───────────────────────────────┐    ┌──────────────────────────────────┐  │
    │  │      PRODUCTION MODE          │    │          DEMO MODE               │  │
    │  │                               │    │                                  │  │
    │  │ ┌─────────────────────────────┐│    │┌─────────────────────────────────┐│  │
    │  │ │   DynamoDB Local            ││    ││   In-Memory HashMap             ││  │
    │  │ │                             ││    ││                                 ││  │
    │  │ │ • Port 8000                 ││    ││ • No external dependencies      ││  │
    │  │ │ • Local file storage        ││    ││ • Session-only persistence      ││  │
    │  │ │ • Table auto-creation       ││    ││ • Instant startup              ││  │
    │  │ │ • AWS SDK integration       ││    ││ • Zero configuration           ││  │
    │  │ └─────────────────────────────┘│    │└─────────────────────────────────┘│  │
    │  └───────────────────────────────┘    └──────────────────────────────────┘  │
    └─────────────────────────────────────────────────────────────────────────────┘

              Execution Scripts:
              ┌─────────────────────────────────────────────────────┐
              │ • QUICK_START.bat (Windows - Full Automation)      │
              │ • start-dynamodb.bat (Database Only)               │
              │ • mvn exec:java (Development Mode)                 │
              │ • java -jar target/iot-*.jar (Production Mode)     │
              └─────────────────────────────────────────────────────┘
```

### Application State Flow
```
┌──────────────────────────────────────────────────────────────────────────────┐
│                          APPLICATION STATE FLOW                             │
└──────────────────────────────────────────────────────────────────────────────┘

┌─────────────┐
│ Application │
│   Startup   │
└──────┬──────┘
       │
       ▼
┌─────────────┐      ┌─────────────┐      ┌─────────────────┐
│ Initialize  │ ───▶ │ Initialize  │ ───▶ │   Show Main     │
│ DynamoDB    │      │ Services    │      │     Menu        │
│ Connection  │      │ & Session   │      │  (Logged Out)   │
└─────────────┘      └─────────────┘      └─────────┬───────┘
       │                    │                       │
       │ (Connection Failed) │                       │
       ▼                    │                       │
┌─────────────┐             │                       │
│   Switch    │             │                       │
│  to Demo    │             │                       │
│    Mode     │             │                       │
└─────────────┘             │                       │
                            │                       │
                            ▼                       ▼
                    ┌─────────────────┐     ┌─────────────────┐
                    │   Services      │     │ User Selection: │
                    │  Initialized    │     │ 1. Register     │
                    │   Successfully  │     │ 2. Login        │
                    └─────────────────┘     │ 3. Control      │
                                           │ 4. View         │
                                           │ 5. Change Status│
                                           │ 6. Exit         │
                                           └─────┬───────────┘
                                                 │
                    ┌────────────────────────────┼────────────────────────────┐
                    │                            │                            │
                    ▼                            ▼                            ▼
         ┌──────────────────┐         ┌─────────────────┐         ┌─────────────────┐
         │   Registration   │         │      Login      │         │   Requires      │
         │     Process      │         │     Process     │         │ Authentication  │
         └─────────┬────────┘         └─────────┬───────┘         └─────────┬───────┘
                   │                           │                           │
                   ▼                           ▼                           │
         ┌──────────────────┐         ┌─────────────────┐                  │
         │ Success/Failure  │         │   Successful    │                  │
         │   Message &      │         │     Login       │                  │
         │ Return to Menu   │         └─────────┬───────┘                  │
         └──────────────────┘                   │                          │
                                               ▼                          │
                                    ┌─────────────────┐                   │
                                    │   Authenticated │◄──────────────────┘
                                    │   Main Menu     │
                                    │  (Logged In)    │
                                    └─────────┬───────┘
                                              │
                    ┌─────────────────────────┼─────────────────────────┐
                    │                         │                         │
                    ▼                         ▼                         ▼
         ┌──────────────────┐      ┌─────────────────┐      ┌─────────────────┐
         │ Device Control   │      │  View Gadgets   │      │ Change Gadget   │
         │    (Connect)     │      │   & Status      │      │    Status       │
         └─────────┬────────┘      └─────────┬───────┘      └─────────┬───────┘
                   │                        │                        │
                   ▼                        ▼                        ▼
         ┌──────────────────┐      ┌─────────────────┐      ┌─────────────────┐
         │ Select Device    │      │   Display       │      │   Toggle        │
         │ Type & Room      │      │ Device List     │      │ Device Status   │
         └─────────┬────────┘      │ with Status     │      └─────────┬───────┘
                   │               └─────────────────┘                │
                   ▼                        │                        │
         ┌──────────────────┐              │                        │
         │  Validation &    │              │                        │
         │  Add/Update      │              │                        │
         │   Customer       │              │                        │
         └──────────────────┘              │                        │
                   │                        │                        │
                   └────────────────────────┼────────────────────────┘
                                           │
                                           ▼
                                 ┌─────────────────┐
                                 │    Return to    │
                                 │  Authenticated  │
                                 │   Main Menu     │
                                 └─────────────────┘
```

---

## Troubleshooting

### Common Issues and Solutions

#### Issue: "Please login first!" message
**Cause**: User session has expired or user not logged in
**Solution**: Login again using option 2 from the main menu

#### Issue: Database connection errors
**Cause**: DynamoDB Local not running or connection issues
**Solution**: 
1. Ensure DynamoDB Local is running: `start-dynamodb.bat`
2. Check if port 8000 is available
3. Application will automatically switch to demo mode if database is unavailable

#### Issue: "A [device] already exists in [room]" error
**Cause**: Attempting to add duplicate device type in the same room
**Solution**: You can only have one device of each type per room. Choose a different room or device type.

#### Issue: Maven build failures
**Cause**: Missing dependencies or Java version issues
**Solution**:
1. Ensure Java 11+ is installed and configured
2. Run `mvn clean compile` to refresh dependencies
3. Check internet connection for dependency downloads

#### Issue: Invalid input errors
**Cause**: Incorrect data format or empty inputs
**Solution**: Follow the input format guidelines:
- **Email**: Must be valid format (user@domain.com)
- **Password**: Must meet all security requirements (see Password Requirements section)
- **Name**: Only letters and spaces, minimum 2 characters

#### Issue: "Account is locked" message
**Cause**: Too many failed login attempts
**Solution**: 
1. Wait for the lockout period to expire:
   - 5 minutes after 3-4 failed attempts
   - 15 minutes after 5-6 failed attempts  
   - 60 minutes after 7+ failed attempts
2. Ensure you're using the correct password
3. Use "Forgot Password" option if you've forgotten your password

#### Issue: "Invalid password" during registration
**Cause**: Password doesn't meet security requirements
**Solution**: Ensure your password has:
- 8-128 characters length
- At least one uppercase letter (A-Z)
- At least one lowercase letter (a-z)
- At least one number (0-9)
- At least one special character (!@#$%^&*)
- Not a common password
- No more than 2 repeating characters

#### Issue: "Password does not meet security requirements" during reset
**Cause**: New password doesn't meet validation criteria
**Solution**: Follow the same password requirements as registration

#### Issue: "No account found with this email address"
**Cause**: Email not registered or mistyped
**Solution**:
1. Verify the email address spelling
2. Ensure you're using the same email used during registration
3. Register a new account if you haven't already

### Demo Mode
If DynamoDB is unavailable, the application automatically switches to demo mode:
- **Indication**: "🎮 Running in DEMO MODE - data won't persist between sessions"
- **Functionality**: All features work normally but data is not saved
- **Limitation**: Data is lost when application is closed
- **Password Security**: All password validation and security features remain active
- **Account Lockout**: Lockout mechanism works but resets when application restarts

### Password Reset Process
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      SIMPLIFIED PASSWORD RESET FLOW                        │
└─────────────────────────────────────────────────────────────────────────────┘

        ┌─────────────────┐
        │ User Selects    │
        │"Forgot Password"│
        └─────────┬───────┘
                  │
                  ▼
        ┌─────────────────┐
        │ Enter Email     │
        │   Address       │
        └─────────┬───────┘
                  │
                  ▼
        ┌─────────────────┐       ┌─────────────────┐
        │Email Validation │──NO──▶│ Error: Account  │
        │ & User Lookup   │       │   Not Found     │
        └─────────┬───────┘       └─────────────────┘
                  │ YES
                  ▼
        ┌─────────────────┐
        │ Display Password│
        │  Requirements   │
        └─────────┬───────┘
                  │
                  ▼
        ┌─────────────────┐
        │ Enter New       │
        │   Password      │
        └─────────┬───────┘
                  │
                  ▼
        ┌─────────────────┐
        │ Confirm New     │
        │   Password      │
        └─────────┬───────┘
                  │
                  ▼
        ┌─────────────────┐       ┌─────────────────┐
        │Password Match & │──NO──▶│ Error: Passwords│
        │  Validation     │       │  Don't Match    │
        └─────────┬───────┘       └─────────────────┘
                  │ YES
                  ▼
        ┌─────────────────┐
        │ BCrypt Hash &   │
        │ Update Database │
        └─────────┬───────┘
                  │
                  ▼
        ┌─────────────────┐
        │ Clear Failed    │
        │ Login Attempts  │
        └─────────┬───────┘
                  │
                  ▼
        ┌─────────────────┐
        │ Success Message │
        │ Ready to Login  │
        └─────────────────┘
```

---

## Developer Information

### Development Team
**Project Lead**: [To be filled by developer]

**Development Team**:
- [Developer Name 1] - [Role/Responsibility]
- [Developer Name 2] - [Role/Responsibility]
- [Developer Name 3] - [Role/Responsibility]

### Project Timeline
**Project Start Date**: [To be filled]
**Current Version**: 1.0.0
**Last Updated**: [To be filled]

### Development Environment
**IDE Used**: [To be filled]
**Version Control**: [To be filled]
**Build Tool**: Apache Maven
**Testing Framework**: [To be filled]

### Code Quality Metrics
- **Lines of Code**: [To be filled]
- **Test Coverage**: [To be filled]
- **Code Complexity**: [To be filled]

### Future Enhancements
[To be filled by developer - list planned features and improvements]

### Known Issues

#### Password Security Limitations
- **No Email Verification**: Password reset doesn't send email verification (simplified implementation)
- **No Password History**: System doesn't prevent reuse of recent passwords
- **No Two-Factor Authentication**: Single-factor authentication only

#### Account Management
- **No Account Recovery**: If email is forgotten, account cannot be recovered
- **No Password Expiry**: Passwords don't expire automatically
- **Manual Lockout Reset**: No admin interface to manually unlock accounts

### API Documentation
[To be filled by developer - detailed API documentation if applicable]

---

## Support and Maintenance

### Version History
| Version | Release Date | Key Features | Developer Notes |
|---------|--------------|--------------|-----------------|
| 2.1     | September 2025 | **Auto-Aligned Tables**, Code Optimization, Input Handling Fixes | Enhanced table display with intelligent column sizing, performance improvements, resolved infinite loop issues |
| 2.0     | September 2025 | Timer System, Smart Scenes, Health Monitoring, Energy Analytics, Calendar Events, Weather Intelligence | Major feature release with comprehensive automation |
| 1.0.0   | Initial      | Core functionality with device management and authentication | Initial release with full functionality |

### Maintenance Schedule
**Regular Updates**: [To be filled]
**Security Patches**: [To be filled]
**Feature Updates**: [To be filled]

### Support Contact Information
**Technical Support**: [To be filled]
**Email**: [To be filled]
**Documentation**: This document and BUILD_AND_RUN_GUIDE.md

### License Information
[To be filled by developer]

### Acknowledgments
- AWS SDK for Java team for DynamoDB integration
- BCrypt library for secure password hashing
- Apache Maven for build management

---

**Document Version**: 2.1
**Last Updated**: September 2025
**Latest Update**: Auto-Aligned Table Display & Performance Optimization 
**Prepared by**: Sushma Mainampati

---

## Quick Reference Card

### 🚀 **Getting Started (First Time Users)**
```
1. Start DynamoDB: run start-dynamodb.bat
2. Start Application: java -jar iot-smart-home-dashboard-1.0.0.jar
3. Register Account: Choose option 1, create strong password
4. Login: Choose option 2 with your credentials
5. Connect Devices: Choose option 4, select device type and room
```

### 📱 **Main Menu Options**

#### 🏠 **Device Management**
| Option | Action | Requires Login | Description |
|--------|--------|----------------|-------------|
| 1 | Register New Account | ❌ No | Create new user account with interactive flow |
| 2 | Login | ❌ No | Login to existing account |
| 3 | Forgot Password | ❌ No | Password recovery assistance |
| 4 | Add/Manage Devices | ✅ Yes | Add, edit, or remove smart devices |
| 5 | View Device Status & Usage | ✅ Yes | Display all devices with real-time status |
| 6 | Control Device Operations | ✅ Yes | Turn devices ON/OFF with status verification |
| 7 | Group Management | ✅ Yes | **NEW**: Manage device sharing groups |

#### ⚡ **Energy & Automation**  
| Option | Action | Requires Login | Description |
|--------|--------|----------------|-------------|
| 8 | Energy Management Report | ✅ Yes | View consumption and cost analysis |
| 9 | Schedule Device Timers | ✅ Yes | Set future device automation |
| 10 | View Scheduled Timers | ✅ Yes | Display active timers with countdown |
| 11 | Calendar Events & Automation | ✅ Yes | Event-based device automation |
| 12 | Weather-Based Suggestions | ✅ Yes | Smart device recommendations |

#### 🔮 **Smart IoT Features**
| Option | Action | Requires Login | Description |
|--------|--------|----------------|-------------|
| 13 | Smart Scenes (One-Click Automation) | ✅ Yes | Execute predefined automation scenes |
| 14 | Device Health Monitoring | ✅ Yes | System health and maintenance insights |
| 15 | Usage Analytics & Insights | ✅ Yes | Comprehensive usage patterns analysis |

#### 🔧 **System**
| Option | Action | Requires Login | Description |
|--------|--------|----------------|-------------|
| 16 | Logout | ✅ Yes | Secure logout with session cleanup |
| 17 | Exit | ❌ No | **ENHANCED**: Graceful shutdown with auto-logout |

---

## 👥 **Group Management Feature Guide**

### Overview
The Group Management feature allows users to share their smart devices with family members, friends, or roommates. This enables collaborative control of home automation while maintaining security and administrative controls.

### How Group Management Works
- **Group Creator**: The first person to add someone to their group becomes the **Group Admin**
- **Shared Device Control**: All group members can view and control each other's devices
- **Admin Privileges**: Only the group admin can remove members from the group
- **Real-Time Synchronization**: Device status changes are immediately visible to all group members

### Group Management Menu (Option 7)
```
=== Group Management ===
1. View Group Information      - See group size, members, and admin status
2. Add Person to Group         - Add new members by email address  
3. Remove Person from Group    - Admin-only: Remove members with confirmation
4. Return to Main Menu         - Go back to main dashboard
```

### Step-by-Step Group Setup

#### Creating Your First Group
1. **Login** to your account
2. **Add devices** to your account (if not already done)
3. Select **Option 7** (Group Management)
4. Select **Option 2** (Add Person to Group)
5. Enter the **email address** of the person you want to add
6. **You automatically become the Group Admin**

#### Adding Additional Members  
1. From Group Management menu, select **Option 2**
2. Enter **new member's email address**
3. System validates the user exists
4. Member is added and can immediately access shared devices

#### Viewing Group Information
Select **Option 1** in Group Management to see:
- **Group Size**: Total number of members
- **Group Admin**: Email of the admin user
- **Your Role**: Admin or Member status
- **Member List**: All group participants with names
- **Device Summary**: Device counts from all members

#### Removing Group Members (Admin Only)
1. Select **Option 3** (Remove Person from Group)
2. System displays current group information
3. Enter **email address** of member to remove
4. **Confirmation prompt** appears for security
5. Member loses access to shared devices immediately

### Group Management Rules
- ✅ **One Admin Per Group**: Only the group creator is admin
- ✅ **Admin Cannot Remove Self**: Prevents accidental group destruction
- ✅ **Mutual Device Access**: All members see all devices
- ✅ **Real-Time Updates**: Status changes sync across all members
- ✅ **Automatic Cleanup**: Broken group states are auto-repaired

### Group Device Control
- **View All Devices**: Personal + group members' devices in one list
- **Control Any Device**: Turn ON/OFF devices owned by any group member  
- **Status Persistence**: Changes are saved to the actual device owner's account
- **Owner Identification**: Device listings show which member owns each device

### Security & Privacy
- **Email Validation**: Only existing users can be added to groups
- **Admin Controls**: Only group admin can manage membership
- **Secure Sessions**: Proper logout and session management
- **Data Integrity**: Device ownership remains with original user

---

## 🕐 **Enhanced Timer Management**

### Automatic Timer Cleanup
- **Execution Window**: Timers execute within 2-minute grace period
- **Automatic Deletion**: Overdue timers (5+ minutes old) are automatically removed  
- **Status Verification**: Timer execution logs show before/after device status
- **Clean Interface**: No more confusing "[OVERDUE]" timer entries

### Timer Status Verification
```
[TIMER EXECUTED] TV in Living Room turned ON automatically
  Status changed from OFF to ON
```

---

### 🔐 **Password Requirements Checklist**
- ☑️ 8-128 characters long
- ☑️ At least one UPPERCASE letter (A-Z)
- ☑️ At least one lowercase letter (a-z) 
- ☑️ At least one number (0-9)
- ☑️ At least one special character (!@#$%^&*)
- ☑️ Not a common password (password, 123456, etc.)
- ☑️ No more than 2 repeating characters

### ⚡ **Quick Actions**
- **Forgot Password**: Option 3 → Email → New Password → Confirm
- **Add Device**: Option 4 → Select category number → Select brand number → Select room number
- **Toggle Device**: Option 6 → Select device number from list
- **Check Status**: Option 5 → Select device number for details
- **Set Timer**: Option 8 → Select device → Enter duration → Confirm
- **View Timers**: Option 10 → See all scheduled timers with countdown
- **Execute Scene**: Option 13 → Choose scene number (1-8) → Automatic execution
- **Health Check**: Option 14 → Select device → View diagnostic report
- **Energy Report**: Option 15 → Choose analysis type → View consumption data

### 🏠 **Popular Room + Device Combinations**
- **Living Room**: TV (Samsung/LG), AC (Voltas/Daikin), Smart Light (Philips)
- **Master Bedroom**: TV (Sony), AC (Blue Star), Fan (Havells)
- **Kitchen**: Microwave (LG), Refrigerator (Samsung), Water Purifier (Kent)
- **Security**: Camera (MI), Door Lock (Godrej), Doorbell (Yale)

---

## Frequently Asked Questions (FAQ)

### 🔐 **Account & Security**

**Q: I forgot my password. How do I reset it?**
A: Use option 3 "Forgot Password" from main menu. Enter your email, then set a new password that meets all requirements.

**Q: My account is locked. How long do I wait?**
A: Lockout times are progressive:
- 3-4 failed attempts: 5 minutes
- 5-6 failed attempts: 15 minutes  
- 7+ failed attempts: 60 minutes

**Q: Can I use the same password for password reset?**
A: Yes, but it must still meet all password requirements. Consider using a new, strong password for better security.

**Q: What if I can't remember my email?**
A: Unfortunately, email recovery is not available. You'll need to register a new account.

### 📱 **Device Management**

**Q: Can I have multiple TVs in the same room?**
A: No, only one device of each type is allowed per room. You can have the same device type in different rooms.

**Q: I made a typo in the device model. Can I change it?**
A: Currently, you need to remove the device and add it again. Contact support if you need help with this.

**Q: What happens if I add too many devices?**
A: There's no limit on total devices, but you can only have one of each type per room (max 18 devices per room).

**Q: Can I rename rooms?**
A: Room names are predefined. Choose from the 38+ available options when adding devices.

### 🖥️ **Technical Issues**

**Q: The app says "Demo Mode" - is this normal?**
A: Demo Mode activates when DynamoDB Local isn't running. Your data won't be saved. Start DynamoDB using start-dynamodb.bat to enable full functionality.

**Q: I see "No line found" errors. What's wrong?**
A: This usually happens with automated input. Run the application interactively from a proper console/terminal.

**Q: Can I run this on Mac/Linux?**
A: Yes! Use the same Java commands. Replace .bat files with equivalent shell scripts.

**Q: How do I know if DynamoDB is running?**
A: The app will show "Connected to local DynamoDB" on startup. If you see "Demo Mode", DynamoDB isn't running.

### 💡 **Best Practices**

**Q: What's the recommended way to organize devices?**
A: 
- Group by functionality (all entertainment in living room)
- Use descriptive room names (Master Bedroom vs Bedroom1)
- Start with essential devices (TV, AC) before adding others

**Q: How often should I change my password?**
A: Change it if you suspect it's compromised. The app doesn't enforce password expiry, but good security practice suggests periodic updates.

**Q: Can multiple people use the same account?**
A: Each person should have their own account for security and personalization. Device lists are user-specific.

### 🆕 **New Features FAQ**

**Q: How do I set up a timer for my devices?**
A: Use Option 8 "Set Device Timer" from main menu. Select your device, enter duration, and confirm. You'll see a countdown showing time remaining.

**Q: What are Smart Scenes and how do I use them?**
A: Smart Scenes are pre-configured automation setups. Use Option 13, then choose a scene number (1-8) like Morning, Evening, or Movie. All associated devices activate automatically.

**Q: My device health shows 45% - what does this mean?**
A: Health scores below 60% indicate performance issues. Check Option 14 for detailed diagnostics and maintenance recommendations specific to your device type.

**Q: How do I cancel a scheduled timer?**
A: Use Option 9 "Cancel Device Timer", then select the timer number from the displayed list. Much easier than typing device names!

**Q: What's the difference between Calendar Events and Smart Scenes?**
A: Calendar Events (Option 11) are scheduled for specific dates/times with automatic device control. Smart Scenes (Option 13) are manual one-click automation you trigger yourself.

**Q: Why does my energy efficiency show over 100%?**
A: Energy efficiency is now capped at 100% maximum. If you see this, your device is performing at optimal efficiency levels.

**Q: How do I see real-time power consumption?**
A: Option 5 "View Gadgets" shows live power usage, including current session runtime and total energy consumption that updates in real-time.

**Q: Can I get weather-based device recommendations?**
A: Yes! Option 12 "Weather-Based Suggestions" provides intelligent recommendations like turning on AC during hot weather or air purifiers during poor air quality.

---

*This document is maintained by the development team. For technical questions or documentation updates, please contact the project maintainers.*