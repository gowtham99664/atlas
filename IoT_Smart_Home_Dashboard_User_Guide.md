# IoT Smart Home Dashboard - User Guide

## Table of Contents
1. [Getting Started](#getting-started)
2. [Account Management](#account-management)
3. [Device Management](#device-management)
4. [Energy Management](#energy-management)
5. [Smart Automation](#smart-automation)
6. [Weather Integration](#weather-integration)
7. [Group Management](#group-management)
8. [Analytics & Monitoring](#analytics--monitoring)
9. [Settings](#settings)
10. [Troubleshooting](#troubleshooting)

---

## Getting Started

### System Requirements
- Java Runtime Environment (JRE) 8 or higher
- DynamoDB Local (included with application)
- Windows, macOS, or Linux operating system

### First Launch
1. Launch the application from command line or terminal
2. You'll see the main IoT Smart Home Enterprise Dashboard menu
3. Navigate using number keys (1-18) to select options
4. Use '0' at any prompt to return to the main menu

### Navigation Tips
- **Quick Exit**: Press Ctrl+C, then enter '0' to return to main menu
- **Input Validation**: All inputs are validated - follow the prompts for correct format
- **Help**: Look for `[TIP]` messages throughout the interface

---

## Account Management

### Creating Your First Account

1. **Registration Process**:
   - Select option `1` from main menu: "Register New Account"
   - Enter your full name (minimum 2 characters, letters and spaces only)
   - Provide a valid email address
   - Create a secure password meeting these requirements:
     - 8-128 characters long
     - At least one uppercase letter (A-Z)
     - At least one lowercase letter (a-z)
     - At least one number (0-9)
     - At least one special character (!@#$%^&*)
     - Cannot be a common password
     - Cannot have more than 2 repeating characters

2. **Login**:
   - Select option `2`: "Login"
   - Enter your registered email and password
   - System will authenticate and start your session

3. **Forgot Password**:
   - Select option `3`: "Forgot Password"
   - Enter your registered email
   - Follow prompts to create a new secure password

### Account Security Features
- Password encryption using bcrypt hashing
- Failed login attempt monitoring
- Account lockout protection
- Local data storage (no cloud transmission)

---

## Device Management

The system supports a comprehensive range of smart home devices across multiple categories:

### Supported Device Categories

#### 🎬 Entertainment Devices
- **Smart TVs**: Samsung, Sony, LG, TCL, Hisense, Panasonic, Philips, MI, OnePlus, Xiaomi
- **Smart Speakers**: Amazon Echo, Google Home, MI, Realme, JBL, Sony, Boat, Bose

#### 🌡️ Climate Control
- **Air Conditioners**: LG, Voltas, Blue Star, Samsung, Daikin, Hitachi, Panasonic, Carrier
- **Fans**: Atomberg, Crompton, Havells, Bajaj, Usha, Orient, Luminous
- **Air Purifiers**: MI, Realme, Honeywell, Philips, Kent, Eureka Forbes
- **Smart Thermostats**: Honeywell, Johnson Controls, Schneider Electric, Nest, Ecobee

#### 💡 Lighting & Switches
- **Smart Lights**: Philips Hue, MI, Syska, Havells, Wipro, Bajaj
- **Smart Switches**: Anchor, Havells, Legrand, Schneider Electric, Wipro

#### 🔒 Security & Safety
- **Security Cameras**: MI, Realme, TP-Link, D-Link, Hikvision, CP Plus
- **Smart Door Locks**: Godrej, Yale, Samsung, Philips, MI, Ultraloq
- **Smart Doorbells**: MI, Realme, Godrej, Yale, Ring, Honeywell

#### 🍳 Kitchen & Appliances
- **Refrigerators**: LG, Samsung, Whirlpool, Godrej, Haier, Panasonic, Bosch
- **Microwaves**: LG, Samsung, IFB, Panasonic, Whirlpool, Godrej
- **Washing Machines**: LG, Samsung, Whirlpool, IFB, Bosch, Godrej
- **Water Heaters/Geysers**: Bajaj, Havells, Crompton, V-Guard, Racold, AO Smith
- **Water Purifiers**: Kent, Aquaguard, Pureit, LivPure, Blue Star, Havells

#### 🧹 Cleaning Devices
- **Robotic Vacuums**: MI Robot, Realme TechLife, Eureka Forbes, Kent, iRobot Roomba

### Adding Devices

1. **Access Device Management**:
   - Select option `4`: "Add/Manage Devices" from main menu
   - Choose device category (Entertainment, Climate Control, etc.)

2. **Device Setup Process**:
   - Select specific device type (e.g., TV, AC, Camera)
   - Enter device model/brand from supported list
   - Specify room location (Living Room, Bedroom, Kitchen, etc.)
   - System will automatically detect and configure the device

3. **Room Names**: Choose from predefined rooms or create custom ones:
   - Living Room, Master Bedroom, Kitchen, Study Room
   - Guest Room, Dining Room, Children's Room, Home Office
   - Balcony, Terrace, Garage, Basement, etc.

### Managing Existing Devices

#### Viewing Device Status
- Select option `5`: "View Device Status & Usage"
- Browse all registered devices with current status
- Check individual device power state and usage statistics

#### Controlling Devices
- Select option `6`: "Control Device Operations"
- Choose device from list to toggle ON/OFF
- Real-time status updates displayed

#### Editing Device Information
1. Go to Device Management menu
2. Select "Edit Existing Device"
3. Choose device to modify
4. Available edit options:
   - Change room location
   - Update device model
   - Modify power rating (for energy calculations)

#### Deleting Devices
1. Access "Delete Device" from Device Management menu
2. Select device to remove
3. Review deletion confirmation (includes energy consumption data)
4. Confirm deletion (action cannot be undone)

---

## Energy Management

### Understanding Energy Consumption

The system tracks comprehensive energy usage data for all your devices:

#### Energy Metrics
- **Real-time Power Consumption**: Live tracking when devices are ON
- **Historical Usage**: Total runtime for each device
- **Energy Consumption**: Calculated in kilowatt-hours (kWh)
- **Cost Analysis**: Electricity cost calculations in Rupees
- **Deleted Device History**: Maintains records of removed devices for accurate billing

### Energy Reports

#### Accessing Energy Reports
- Select option `8`: "Energy Management Report" from main menu

#### Report Features
- **Device-wise Breakdown**: Energy consumption per device
- **Power Ratings**: Individual device power consumption (Watts)
- **Usage Time**: Total runtime formatted as days, hours, minutes
- **Cost Calculations**: Based on standard electricity rates
- **Current Session Tracking**: Live monitoring of running devices

#### Energy Efficiency Tips
The system provides automatic recommendations:
- High consumption device identification
- Off-peak usage scheduling suggestions
- Maintenance reminders for efficiency optimization
- Cost-saving opportunities based on usage patterns

---

## Smart Automation

### Smart Scenes

Smart Scenes allow you to control multiple devices with a single command, creating customized automation for different activities or times of day.

#### Pre-configured Scenes
The system includes 8 ready-to-use scenes:

1. **MORNING**: Optimal settings for starting your day
2. **WORK**: Focus-oriented environment setup
3. **EVENING**: Relaxing atmosphere configuration
4. **NIGHT**: Preparing for sleep mode
5. **PARTY**: Entertainment-focused setup
6. **MOVIE**: Cinema-like experience
7. **VACATION**: Energy-saving settings when away
8. **EMERGENCY**: Safety-oriented emergency configuration

#### Using Smart Scenes
1. **Execute Scene**:
   - Select option `13`: "Smart Scenes (One-Click Automation)"
   - Choose "Execute Scene"
   - Select desired scene from list
   - All configured devices automatically adjust to scene settings

2. **View Scene Details**:
   - See which devices are controlled by each scene
   - Review specific actions (ON/OFF states)
   - Understand scene purposes and benefits

#### Customizing Scenes

**Adding Devices to Scenes**:
1. Select "Edit Scene Devices"
2. Choose scene to modify
3. Select "Add Device to Scene"
4. Pick device from your registered devices
5. Choose action (ON or OFF)

**Removing Devices**:
1. Access scene editing menu
2. Select "Remove Device from Scene"
3. Choose device to remove from scene
4. Confirm removal

**Changing Device Actions**:
1. Select "Change Device Action (ON ↔ OFF)"
2. Choose device in scene
3. Action automatically toggles between ON and OFF

**Resetting Scenes**:
- Use "Reset Scene to Original" to restore default configurations
- All custom modifications will be lost

### Device Timers

Schedule automatic device operations for specific dates and times.

#### Setting Up Timers
1. **Access Timer Menu**:
   - Select option `9`: "Schedule Device Timers"

2. **Timer Configuration**:
   - Choose device from your registered list
   - System shows current device status
   - Timer will toggle device to opposite state (if ON, timer sets to OFF)
   - Enter date and time in format: DD-MM-YYYY HH:MM
   - Example: 25-12-2024 18:30

#### Managing Scheduled Timers
1. **View Timers**:
   - Select option `10`: "View Scheduled Timers"
   - See all active timers with device and scheduled time

2. **Cancel Timers**:
   - Choose specific timer from list
   - Confirm cancellation

3. **Force Execute**:
   - Manually trigger due timers immediately
   - Useful for testing or immediate execution

#### Timer Features
- **Automatic Execution**: Timers run automatically at scheduled time
- **Status Updates**: Device status changes reflected immediately
- **Conflict Resolution**: System handles multiple timers intelligently
- **Time Validation**: Prevents scheduling in the past

---

## Weather Integration

### Weather-Based Automation

The system provides intelligent device control suggestions based on weather conditions.

#### Weather Features
1. **Current Weather Display**:
   - Select option `12`: "Weather-Based Suggestions"
   - Choose "Current Weather & Suggestions"
   - View temperature, humidity, conditions
   - Get automated device control recommendations

2. **Weather Data Management**:
   - "Update/Enter Weather Data": Manual weather input
   - "5-Day Weather Forecast": Extended weather predictions
   - "Clear Weather Data": Reset weather information

#### Automated Suggestions
Based on weather conditions, the system suggests:
- **Hot Weather**: AC activation, fan usage optimization
- **Cold Weather**: Heater recommendations, energy saving tips
- **Rainy Conditions**: Humidity control suggestions
- **Air Quality**: Air purifier usage recommendations

#### Weather Automation Rules
The system includes intelligent automation rules:
- Temperature-based AC/heating control
- Humidity-based air purifier activation
- Weather condition alerts and suggestions
- Energy optimization based on external conditions

---

## Group Management

### Multi-User Features

The IoT Smart Home Dashboard supports multiple users with role-based access control.

#### Group Structure
- **Admin Users**: Full control over devices and group management
- **Group Members**: Access to shared devices based on permissions
- **Guest Access**: Limited functionality for temporary users

#### Managing Group Members

1. **Adding Members**:
   - Select option `7`: "Group Management"
   - Choose "Add Person to Group"
   - Enter email address of person to invite
   - They can join using existing account or create new one

2. **Removing Members** (Admin Only):
   - Access "Remove Person from Group"
   - Select member from list
   - Confirm removal (action cannot be undone)

3. **Viewing Group Information**:
   - See all group members
   - View member roles and status
   - Check group statistics

#### Device Permissions

**Granting Access** (Admin Only):
1. Select "Grant Device Access to Member"
2. Choose device from your owned devices
3. Select group member to grant access
4. Confirm permission grant

**Revoking Access** (Admin Only):
1. Select "Revoke Device Access from Member"
2. Enter member email, device type, and room
3. Confirm access revocation

**Viewing Permissions**:
- "View All Device Permissions" shows complete access matrix
- See which members can access which devices
- Monitor permission changes and history

---

## Analytics & Monitoring

### Usage Analytics & Insights

Comprehensive analysis tools help you understand and optimize your smart home usage.

#### Energy Consumption Analysis
- **Device-wise breakdown**: See which devices consume the most energy
- **Historical trends**: Track consumption over time
- **Cost projections**: Estimate monthly and yearly electricity costs
- **Efficiency recommendations**: Get suggestions for reducing consumption

#### Device Usage Patterns
- **Usage categorization**: Heavy, Moderate, Light, or Minimal use classification
- **Runtime statistics**: Total hours of operation per device
- **Peak usage identification**: Discover when devices are used most
- **Session tracking**: Monitor current active sessions

#### Cost Analysis & Projections
- **Monthly estimates**: Based on current usage patterns
- **Yearly projections**: Long-term cost forecasting
- **Savings opportunities**: Identify potential cost reductions
- **Efficiency recommendations**: Actionable suggestions for cost savings

#### Peak Usage Times
- **Morning peak analysis** (6:00-10:00 AM): Kitchen appliances, lighting
- **Evening peak analysis** (6:00-10:00 PM): Entertainment, climate control
- **Night period analysis** (10:00 PM-6:00 AM): Security, essential devices
- **Load balancing tips**: Optimize usage timing for cost savings

### Device Health Monitoring

Monitor the health and performance of your smart home devices.

#### Health Features
1. **System Health Report**:
   - Overall system status assessment
   - Device performance analysis
   - Maintenance recommendations
   - Issue identification and resolution suggestions

2. **Device Maintenance Schedule**:
   - Preventive maintenance reminders
   - Service interval tracking
   - Performance optimization tips
   - Longevity enhancement recommendations

3. **Health Summary**:
   - Quick overview of system status
   - Critical issue alerts
   - Performance metrics
   - Immediate action recommendations

---

## Calendar Events & Automation

### Event-Based Automation

Integrate calendar events with smart home automation for context-aware device control.

#### Creating Calendar Events
1. **Event Setup**:
   - Select option `11`: "Calendar Events & Automation"
   - Choose "Create New Event"
   - Enter event details:
     - Title and description
     - Start and end date/time (DD-MM-YYYY HH:MM format)
     - Event type (Meeting, Party, Sleep, Work, etc.)

2. **Event Types**:
   - **Meeting**: Professional focus environment
   - **Party**: Entertainment and social settings
   - **Sleep**: Quiet, minimal lighting
   - **Work**: Productivity-oriented setup
   - **Vacation**: Energy-saving mode
   - **Exercise**: Ventilation and lighting optimization

#### Event Automation Features
- **Automatic Device Control**: Events can trigger scene execution
- **Context-Aware Adjustments**: Device settings adapt to event type
- **Schedule Integration**: Events coordinate with timer schedules
- **Smart Notifications**: Reminders and automation alerts

#### Managing Events
- **View Upcoming Events**: See scheduled events and their automation
- **Event Automation Details**: Review specific device actions for events
- **Event Types Help**: Learn about automation possibilities for each event type

---

## Settings

### Account Settings

Manage your user profile and security settings.

#### Profile Management
1. **Update User Profile**:
   - Change full name
   - Update email address (availability checked)
   - Modify password (with strength validation)
   - Security confirmation required for changes

2. **Password Management**:
   - Change existing password
   - Password strength validation
   - Security best practices enforcement
   - Current password verification required

#### Account Information
- **View Account Details**: See profile information and account statistics
- **Privacy & Security Info**: Understand data protection measures
- **Security Features**: Learn about encryption and protection systems

#### System Settings
- **Local Data Storage**: All data stored securely on your device
- **Encryption**: Bcrypt password hashing and data encryption
- **No Cloud Transmission**: Complete local control over your data
- **Backup Recommendations**: Guidelines for data protection

---

## Troubleshooting

### Common Issues and Solutions

#### Login Problems
**Issue**: Cannot login with correct credentials
**Solution**:
- Verify email address format and spelling
- Check password for correct case and special characters
- Use "Forgot Password" feature to reset credentials
- Ensure account exists (try registration if unsure)

#### Device Connection Issues
**Issue**: Device not responding or showing incorrect status
**Solution**:
- Verify device is powered on and connected to network
- Check device model spelling matches supported brands
- Ensure room name is correctly specified
- Try deleting and re-adding device if persistent issues

#### Timer Not Executing
**Issue**: Scheduled timer doesn't activate at specified time
**Solution**:
- Verify date/time format (DD-MM-YYYY HH:MM)
- Check system clock is accurate
- Ensure application remains running for timer execution
- Use "Force Execute Due Timers" to manually trigger

#### Energy Consumption Inaccuracies
**Issue**: Energy usage seems incorrect
**Solution**:
- Verify device power ratings are accurate
- Check if device was actually running during reported time
- Review usage session data for accuracy
- Edit device power rating if manufacturer specs differ

#### Group Permission Problems
**Issue**: Cannot access shared devices
**Solution**:
- Verify you're added to the group by admin
- Check specific device permissions have been granted
- Confirm device owner hasn't revoked access
- Contact group admin to verify permission status

### Technical Support

#### Performance Optimization
- **Regular Maintenance**: Restart application periodically for optimal performance
- **Data Cleanup**: Remove unused devices and expired timers
- **System Resources**: Ensure adequate memory and storage space
- **Network Stability**: Maintain stable network connection for device communication

#### Data Management
- **Backup Strategy**: Regularly backup important settings and configurations
- **Storage Space**: Monitor disk space usage, especially for logging data
- **Database Maintenance**: Periodic cleanup of old data and logs
- **Performance Monitoring**: Watch for slowdowns and address promptly

#### Getting Help
- **Built-in Help**: Use help options within each feature section
- **Documentation**: Refer to this user guide for detailed instructions
- **Error Messages**: Pay attention to system messages and follow suggestions
- **Feature Discovery**: Explore different menu options to discover capabilities

### Best Practices

#### Security
- Use strong, unique passwords
- Regularly update passwords
- Monitor group member access
- Review device permissions periodically
- Keep system software updated

#### Energy Efficiency
- Set up smart scenes for common activities
- Use timers to prevent devices running unnecessarily
- Monitor energy reports regularly
- Follow system recommendations for optimization
- Schedule high-power devices during off-peak hours

#### Device Management
- Organize devices by rooms logically
- Use descriptive names for easy identification
- Regularly review and clean up unused devices
- Monitor device health and maintenance needs
- Update device information when hardware changes

---

## Quick Reference

### Main Menu Options Quick Guide
1. **Register New Account** - Create user account
2. **Login** - Access your smart home dashboard
3. **Forgot Password** - Reset password security
4. **Add/Manage Devices** - Device registration and management
5. **View Device Status & Usage** - Monitor device states
6. **Control Device Operations** - Manual device control
7. **Group Management** - Multi-user access control
8. **Energy Management Report** - Consumption and cost analysis
9. **Schedule Device Timers** - Automated device scheduling
10. **View Scheduled Timers** - Timer management and monitoring
11. **Calendar Events & Automation** - Event-based automation
12. **Weather-Based Suggestions** - Weather-driven recommendations
13. **Smart Scenes** - One-click multi-device automation
14. **Device Health Monitoring** - Performance and maintenance tracking
15. **Usage Analytics & Insights** - Comprehensive usage analysis
16. **Settings** - Account and system configuration
17. **Logout** - End current session
18. **Exit** - Close application safely

### Keyboard Shortcuts
- **Ctrl+C then 0**: Quick return to main menu
- **0**: Return to previous menu (context-dependent)
- **etc**: View complete lists (device brands, room names)

### Important Notes
- Application uses local database (DynamoDB Local)
- All data stored locally - no cloud transmission
- Session management for security
- Comprehensive input validation
- Real-time energy consumption tracking
- Multi-user support with role-based permissions

---

*IoT Smart Home Dashboard - Making your home smarter, more efficient, and more secure.*