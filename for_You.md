# Customer.java - Complete Code with Beginner Explanations

This document explains the `Customer.java` file step by step for beginners learning Java programming. The file is located at `src/main/java/com/smarthome/model/Customer.java`.

## What is this file?
This file creates a "Customer" class - think of it like a blueprint for creating customer objects in a smart home system. Each customer can have devices, be part of groups, and have security features.

## Package Declaration and Imports

```java
package com.smarthome.model;
```
**What this means:** This tells Java that this file belongs to the `com.smarthome.model` package. Packages are like folders that organize your code.

```java
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
```
**What this means:** These are import statements. They tell Java to bring in code from other places so we can use it:
- **DynamoDB imports:** These help us save customer data to a database
- **LocalDateTime:** This helps us work with dates and times
- **ArrayList and List:** These help us create lists of things (like a list of devices)

## Class Declaration and Annotations

```java
@DynamoDbBean
public class Customer {
```
**What this means:**
- `@DynamoDbBean` is an annotation (starts with @) that tells the database how to save this class
- `public class Customer` creates a new class called "Customer" that other parts of the program can use
- The `{` opens the class - everything inside belongs to this class

## Instance Variables (Fields)
These are like properties that every Customer object will have:

```java
private String email;
private String fullName;
private String password;
private List<Gadget> gadgets;
private List<String> groupMembers;
private String groupCreator;
private List<DeletedDeviceEnergyRecord> deletedDeviceEnergyRecords;
private List<DevicePermission> devicePermissions;

private int failedLoginAttempts;
private LocalDateTime accountLockedUntil;
private LocalDateTime lastFailedLoginTime;
```

**What each variable means:**
- `private` means only this class can directly access these variables (encapsulation)
- `String email;` - Stores the customer's email address (like "john@example.com")
- `String fullName;` - Stores the customer's full name (like "John Smith")
- `String password;` - Stores the encrypted password for security
- `List<Gadget> gadgets;` - A list of smart devices this customer owns
- `List<String> groupMembers;` - A list of family members who can access devices
- `String groupCreator;` - Who created the family group
- `List<DeletedDeviceEnergyRecord>` - Keeps track of energy used by deleted devices
- `List<DevicePermission>` - Controls who can use which devices
- `int failedLoginAttempts;` - Counts how many times login failed
- `LocalDateTime accountLockedUntil;` - When the account will be unlocked
- `LocalDateTime lastFailedLoginTime;` - When was the last failed login

## Constructors (How to Create a Customer Object)

### Default Constructor (Empty Constructor)
```java
public Customer() {
    this.gadgets = new ArrayList<>();
    this.groupMembers = new ArrayList<>();
    this.groupCreator = null;
    this.deletedDeviceEnergyRecords = new ArrayList<>();
    this.devicePermissions = new ArrayList<>();
    this.failedLoginAttempts = 0;
    this.accountLockedUntil = null;
    this.lastFailedLoginTime = null;
}
```
**What this does:**
- A constructor is a special method that runs when you create a new Customer
- `public Customer()` creates an empty customer
- `this.gadgets = new ArrayList<>();` creates an empty list for devices
- `this.groupMembers = new ArrayList<>();` creates an empty list for family members
- `this.groupCreator = null;` means not part of any group yet
- Sets all the lists to empty and numbers to 0 to avoid errors later

### Constructor with Information
```java
public Customer(String email, String fullName, String password) {
    this.email = email;
    this.fullName = fullName;
    this.password = password;
    this.gadgets = new ArrayList<>();
    this.groupMembers = new ArrayList<>();
    this.groupCreator = null;
    this.deletedDeviceEnergyRecords = new ArrayList<>();
    this.devicePermissions = new ArrayList<>();
    this.failedLoginAttempts = 0;
    this.accountLockedUntil = null;
    this.lastFailedLoginTime = null;
}
```
**What this does:**
- This constructor takes three pieces of information: email, name, and password
- `this.email = email;` saves the email you provided
- Then it sets up all the empty lists just like the first constructor
- Use this when creating a new customer account

## Getter and Setter Methods (How to Access and Change Information)

Getters and setters are methods that let you safely get and change the private variables.

### Email Methods
```java
@DynamoDbPartitionKey
public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}
```
**What this does:**
- `@DynamoDbPartitionKey` tells the database this is the main key for finding customers
- `getEmail()` returns the customer's email - you can read it but not change it directly
- `setEmail()` lets you change the email address
- `this.email = email;` saves the new email to this customer object

### Full Name and Password Methods
```java
public String getFullName() {
    return fullName;
}

public void setFullName(String fullName) {
    this.fullName = fullName;
}

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}
```
**What this does:** Simple getters and setters for name and password

## Device Management Methods

### Getting and Setting Device List
```java
public List<Gadget> getGadgets() {
    return gadgets;
}

public void setGadgets(List<Gadget> gadgets) {
    this.gadgets = gadgets != null ? gadgets : new ArrayList<>();
}
```
**What this does:**
- `getGadgets()` returns the list of devices
- `setGadgets()` sets a new list, but if it's null, creates an empty list instead

### Adding a Device
```java
public void addGadget(Gadget gadget) {
    if (this.gadgets == null) {
        this.gadgets = new ArrayList<>();
    }

    boolean exists = this.gadgets.stream()
            .anyMatch(g -> g.getType().equalsIgnoreCase(gadget.getType()) &&
                          g.getRoomName().equalsIgnoreCase(gadget.getRoomName()));

    if (!exists) {
        this.gadgets.add(gadget);
    }
}
```
**What this does:**
- First, check if the gadgets list exists, if not create it
- `boolean exists = ...` checks if this device already exists
- `stream()` goes through each device in the list
- `anyMatch()` returns true if any device matches
- `equalsIgnoreCase()` compares text ignoring CAPITAL/lowercase
- Only add the device if it doesn't already exist (no duplicates!)

### Finding a Specific Device
```java
public Gadget findGadget(String type, String roomName) {
    if (this.gadgets == null) {
        return null;
    }

    return this.gadgets.stream()
            .filter(g -> g.getType().equalsIgnoreCase(type) &&
                       g.getRoomName().equalsIgnoreCase(roomName))
            .findFirst()
            .orElse(null);
}
```
**What this does:**
- Return null if no devices exist
- `stream()` goes through the device list
- `filter()` keeps only devices that match the type and room
- `findFirst()` gets the first matching device
- `orElse(null)` returns null if no device was found

## Security Methods (Protecting Against Hackers)

These methods help protect customer accounts from people trying to guess passwords.

### Basic Security Getters and Setters
```java
public int getFailedLoginAttempts() {
    return failedLoginAttempts;
}

public void setFailedLoginAttempts(int failedLoginAttempts) {
    this.failedLoginAttempts = failedLoginAttempts;
}

public LocalDateTime getAccountLockedUntil() {
    return accountLockedUntil;
}

public void setAccountLockedUntil(LocalDateTime accountLockedUntil) {
    this.accountLockedUntil = accountLockedUntil;
}

public LocalDateTime getLastFailedLoginTime() {
    return lastFailedLoginTime;
}

public void setLastFailedLoginTime(LocalDateTime lastFailedLoginTime) {
    this.lastFailedLoginTime = lastFailedLoginTime;
}
```
**What this does:** Basic getters and setters for security information

### Check if Account is Locked
```java
public boolean isAccountLocked() {
    return accountLockedUntil != null && LocalDateTime.now().isBefore(accountLockedUntil);
}
```
**What this does:**
- Returns `true` if the account is currently locked, `false` if not
- `accountLockedUntil != null` checks if there's a lock time set
- `LocalDateTime.now().isBefore(accountLockedUntil)` checks if current time is before the unlock time

### When Someone Fails to Login
```java
public void incrementFailedAttempts() {
    this.failedLoginAttempts++;
    this.lastFailedLoginTime = LocalDateTime.now();
}
```
**What this does:**
- `this.failedLoginAttempts++;` adds 1 to the failed attempts counter
- `LocalDateTime.now()` records the exact time this happened

### When Someone Logs in Successfully
```java
public void resetFailedAttempts() {
    this.failedLoginAttempts = 0;
    this.accountLockedUntil = null;
    this.lastFailedLoginTime = null;
}
```
**What this does:**
- Resets everything back to normal when login is successful
- Sets failed attempts back to 0
- Clears the lock time and last failed time

### Lock the Account
```java
public void lockAccount(int minutes) {
    this.accountLockedUntil = LocalDateTime.now().plusMinutes(minutes);
}
```
**What this does:**
- Locks the account for a certain number of minutes
- `LocalDateTime.now().plusMinutes(minutes)` calculates when to unlock it

## Group Management Methods (Sharing Devices with Family)

These methods let customers share their smart home devices with family members.

### Adding and Removing Family Members
```java
public List<String> getGroupMembers() {
    return groupMembers;
}

public void setGroupMembers(List<String> groupMembers) {
    this.groupMembers = groupMembers != null ? groupMembers : new ArrayList<>();
}

public void addGroupMember(String memberEmail) {
    if (this.groupMembers == null) {
        this.groupMembers = new ArrayList<>();
    }

    if (!this.groupMembers.contains(memberEmail.toLowerCase().trim())) {
        this.groupMembers.add(memberEmail.toLowerCase().trim());
    }
}

public void removeGroupMember(String memberEmail) {
    if (this.groupMembers != null) {
        this.groupMembers.remove(memberEmail.toLowerCase().trim());
    }
}
```
**What this does:**
- `addGroupMember()` adds a family member to the group
- `toLowerCase().trim()` makes sure emails are consistent (no extra spaces, all lowercase)
- Only adds if the member isn't already in the group (no duplicates)
- `removeGroupMember()` removes someone from the group

### Checking Group Status
```java
public boolean isPartOfGroup() {
    return this.groupMembers != null && !this.groupMembers.isEmpty();
}

public boolean isGroupMember(String memberEmail) {
    return this.groupMembers != null && this.groupMembers.contains(memberEmail.toLowerCase().trim());
}

public boolean isGroupAdmin() {
    return this.groupCreator != null && this.groupCreator.equalsIgnoreCase(this.email);
}

public int getGroupSize() {
    int size = 0;
    if (this.groupMembers != null) {
        size += this.groupMembers.size();
    }
    return size + 1; // +1 for the current user
}
```
**What this does:**
- `isPartOfGroup()` returns true if this customer has family members
- `isGroupMember()` checks if a specific person is in the group
- `isGroupAdmin()` checks if this customer is the group leader
- `getGroupSize()` counts how many people are in the group (including yourself)

## Energy Tracking for Deleted Devices

```java
@DynamoDbAttribute("deletedDeviceEnergyRecords")
public List<DeletedDeviceEnergyRecord> getDeletedDeviceEnergyRecords() {
    if (deletedDeviceEnergyRecords == null) {
        deletedDeviceEnergyRecords = new ArrayList<>();
    }
    return deletedDeviceEnergyRecords;
}

public void addDeletedDeviceRecord(DeletedDeviceEnergyRecord record) {
    if (deletedDeviceEnergyRecords == null) {
        deletedDeviceEnergyRecords = new ArrayList<>();
    }
    deletedDeviceEnergyRecords.add(record);
}

public double getTotalDeletedDeviceEnergyForCurrentMonth() {
    if (deletedDeviceEnergyRecords == null || deletedDeviceEnergyRecords.isEmpty()) {
        return 0.0;
    }

    LocalDateTime now = LocalDateTime.now();
    String currentMonth = now.getYear() + "-" + String.format("%02d", now.getMonthValue());

    return deletedDeviceEnergyRecords.stream()
            .filter(record -> currentMonth.equals(record.getDeletionMonth()))
            .mapToDouble(DeletedDeviceEnergyRecord::getTotalEnergyConsumedKWh)
            .sum();
}
```
**What this does:**
- Keeps track of how much energy deleted devices used (for billing)
- `getTotalDeletedDeviceEnergyForCurrentMonth()` calculates total energy for this month
- Uses streams to filter records for current month and add up the energy usage

## toString Method (For Debugging)
```java
@Override
public String toString() {
    return "Customer{" +
            "email='" + email + '\'' +
            ", fullName='" + fullName + '\'' +
            ", gadgetsCount=" + (gadgets != null ? gadgets.size() : 0) +
            ", failedAttempts=" + failedLoginAttempts +
            ", isLocked=" + isAccountLocked() +
            '}';
}
```
**What this does:**
- `@Override` means we're replacing Java's default toString method
- Returns a readable text description of the customer
- Useful for debugging - you can print a customer object and see its important information
- Shows email, name, device count, failed logins, and if account is locked

## Summary for Beginners

This `Customer` class is like a blueprint for creating customer accounts in a smart home system. Here's what it can do:

### 🏠 **Smart Home Features:**
- Store customer information (email, name, password)
- Manage a list of smart devices (TV, lights, etc.)
- Share devices with family members
- Control who can use which devices

### 🔒 **Security Features:**
- Track failed login attempts
- Lock accounts if someone tries to hack them
- Reset security when login is successful

### 👨‍👩‍👧‍👦 **Family Features:**
- Create groups with family members
- Add and remove people from groups
- Check who's the group admin

### 📊 **Energy Tracking:**
- Remember energy usage even after devices are deleted
- Calculate monthly energy consumption
- Help with electricity bills

### 🛠️ **Programming Concepts You Learned:**
- **Classes and Objects**: The Customer class is a template for creating customer objects
- **Encapsulation**: Private variables with public getters/setters
- **Constructors**: Special methods that create new objects
- **Methods**: Functions that do specific tasks
- **Lists**: Collections that store multiple items
- **Null Safety**: Checking if variables exist before using them
- **Streams**: Modern Java way to work with lists of data
- **Annotations**: Special markers that tell frameworks how to use your code

This code shows many real-world programming concepts that professional developers use every day!

---

# DeletedDeviceEnergyRecord.java - Complete Code with Beginner Explanations

This document explains the `DeletedDeviceEnergyRecord.java` file for beginners. The file is located at `src/main/java/com/smarthome/model/DeletedDeviceEnergyRecord.java`.

## What is this file?
When someone deletes a smart home device (like removing an old TV), we don't want to lose the energy usage data. This class saves all the important information about how much electricity the device used before it was deleted. This is important for electricity bills and tracking energy usage over time.

## Package Declaration and Imports

```java
package com.smarthome.model;

import java.time.LocalDateTime;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
```
**What this means:**
- `package com.smarthome.model;` - This file belongs to the model package (like Customer.java)
- `LocalDateTime` - Helps us work with dates and times
- `DynamoDbBean` and `DynamoDbAttribute` - Help save this data to the database

## Class Declaration

```java
@DynamoDbBean
public class DeletedDeviceEnergyRecord {
```
**What this means:**
- `@DynamoDbBean` tells the database this class can be saved as data
- This class is like a "receipt" that keeps track of deleted device information

## Instance Variables (What Information We Save)

```java
private String deviceType;
private String roomName;
private String deviceModel;
private double totalEnergyConsumedKWh;
private long totalUsageMinutes;
private LocalDateTime deletionTime;
private LocalDateTime deviceCreationTime;
private double powerRatingWatts;
private String deletionMonth; // Format: "YYYY-MM" for monthly grouping
```

**What each variable stores:**
- `String deviceType;` - What kind of device (like "TV" or "Light")
- `String roomName;` - Which room it was in (like "Living Room")
- `String deviceModel;` - The specific model (like "Samsung 55 inch")
- `double totalEnergyConsumedKWh;` - Total electricity used (in kilowatt-hours)
- `long totalUsageMinutes;` - How many minutes the device was used
- `LocalDateTime deletionTime;` - When the device was deleted
- `LocalDateTime deviceCreationTime;` - When the device was first added
- `double powerRatingWatts;` - How much power the device uses per hour
- `String deletionMonth;` - Which month it was deleted (for easy searching)

## Constructors (How to Create Records)

### Empty Constructor
```java
public DeletedDeviceEnergyRecord() {
}
```
**What this does:** Creates an empty record (needed by the database)

### Constructor from Existing Device
```java
public DeletedDeviceEnergyRecord(Gadget device) {
    this.deviceType = device.getType();
    this.roomName = device.getRoomName();
    this.deviceModel = device.getModel();
    this.totalEnergyConsumedKWh = device.getTotalEnergyConsumedKWh();
    this.totalUsageMinutes = device.getTotalUsageMinutes();
    this.deletionTime = LocalDateTime.now();
    this.powerRatingWatts = device.getPowerRatingWatts();

    // Calculate current session energy if device is currently on
    if (device.isOn() && device.getLastOnTime() != null) {
        double currentSessionHours = device.getCurrentSessionUsageHours();
        double currentSessionEnergy = (device.getPowerRatingWatts() / 1000.0) * currentSessionHours;
        this.totalEnergyConsumedKWh += currentSessionEnergy;
        this.totalUsageMinutes += (long)(currentSessionHours * 60);
    }

    // Set deletion month for easy querying
    this.deletionMonth = deletionTime.getYear() + "-" +
                       String.format("%02d", deletionTime.getMonthValue());
}
```
**What this does:**
- Takes a `Gadget` (device) and copies all its information
- `this.deviceType = device.getType();` - Copies the device type
- `this.deletionTime = LocalDateTime.now();` - Records when we're deleting it (right now)
- **Special calculation:** If the device is currently ON when being deleted:
  - `device.getCurrentSessionUsageHours()` - How long it's been on
  - Calculate extra energy used in this session
  - Add it to the total energy and usage time
- `String.format("%02d", ...)` - Makes sure months have 2 digits (like "03" instead of "3")

## Getter and Setter Methods with Database Annotations

### Device Type Methods
```java
@DynamoDbAttribute("deviceType")
public String getDeviceType() {
    return deviceType;
}

public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
}
```

### Room Name Methods
```java
@DynamoDbAttribute("roomName")
public String getRoomName() {
    return roomName;
}

public void setRoomName(String roomName) {
    this.roomName = roomName;
}
```

### Device Model Methods
```java
@DynamoDbAttribute("deviceModel")
public String getDeviceModel() {
    return deviceModel;
}

public void setDeviceModel(String deviceModel) {
    this.deviceModel = deviceModel;
}
```

### Energy Consumption Methods
```java
@DynamoDbAttribute("totalEnergyConsumedKWh")
public double getTotalEnergyConsumedKWh() {
    return totalEnergyConsumedKWh;
}

public void setTotalEnergyConsumedKWh(double totalEnergyConsumedKWh) {
    this.totalEnergyConsumedKWh = totalEnergyConsumedKWh;
}
```

### Usage Time Methods
```java
@DynamoDbAttribute("totalUsageMinutes")
public long getTotalUsageMinutes() {
    return totalUsageMinutes;
}

public void setTotalUsageMinutes(long totalUsageMinutes) {
    this.totalUsageMinutes = totalUsageMinutes;
}
```

### Time-Related Methods
```java
@DynamoDbAttribute("deletionTime")
public LocalDateTime getDeletionTime() {
    return deletionTime;
}

public void setDeletionTime(LocalDateTime deletionTime) {
    this.deletionTime = deletionTime;
}

@DynamoDbAttribute("deviceCreationTime")
public LocalDateTime getDeviceCreationTime() {
    return deviceCreationTime;
}

public void setDeviceCreationTime(LocalDateTime deviceCreationTime) {
    this.deviceCreationTime = deviceCreationTime;
}
```

### Power Rating Methods
```java
@DynamoDbAttribute("powerRatingWatts")
public double getPowerRatingWatts() {
    return powerRatingWatts;
}

public void setPowerRatingWatts(double powerRatingWatts) {
    this.powerRatingWatts = powerRatingWatts;
}
```

### Deletion Month Methods
```java
@DynamoDbAttribute("deletionMonth")
public String getDeletionMonth() {
    return deletionMonth;
}

public void setDeletionMonth(String deletionMonth) {
    this.deletionMonth = deletionMonth;
}
```

**What these methods do:**
- `@DynamoDbAttribute("fieldName")` tells the database what to call each field
- Each getter returns the value, each setter changes the value
- These are standard patterns you'll see in many Java classes

## Utility Methods

### Format Usage Time for Humans
```java
public String getFormattedUsageTime() {
    long hours = totalUsageMinutes / 60;
    long minutes = totalUsageMinutes % 60;

    if (hours > 0) {
        return String.format("%dh %dm", hours, minutes);
    } else {
        return String.format("%dm", minutes);
    }
}
```
**What this does:**
- Converts minutes into a readable format
- `totalUsageMinutes / 60` calculates hours (like 150 minutes = 2 hours)
- `totalUsageMinutes % 60` calculates remaining minutes (like 150 minutes = 30 remaining minutes)
- If there are hours, shows "2h 30m"
- If less than an hour, just shows "30m"

### ToString Method for Debugging
```java
@Override
public String toString() {
    return String.format("DeletedDevice{%s %s in %s, Energy: %.3f kWh, Usage: %s, Deleted: %s}",
            deviceType, deviceModel, roomName, totalEnergyConsumedKWh,
            getFormattedUsageTime(), deletionTime.toString());
}
```
**What this does:**
- Creates a readable description of the deleted device
- Example output: "DeletedDevice{TV Samsung 55inch in Living Room, Energy: 45.230 kWh, Usage: 120h 45m, Deleted: 2024-01-15T14:30:00}"
- `%.3f` shows energy with 3 decimal places
- Uses the `getFormattedUsageTime()` method we created above

## Summary for Beginners

This `DeletedDeviceEnergyRecord` class is like a **digital receipt** that keeps important information when devices are removed. Here's what it does:

### 💡 **Why This Class Exists:**
- When you remove a device, you don't want to lose energy usage data
- Important for electricity bills and energy tracking
- Helps with monthly/yearly energy reports

### 📊 **What Information It Saves:**
- **Device Details**: Type, model, room location
- **Energy Data**: How much electricity it used (kWh)
- **Time Data**: How long it was used, when it was deleted
- **Power Info**: How much power the device consumes

### 🔧 **Special Features:**
- **Smart Energy Calculation**: If device is ON when deleted, it calculates the current session energy
- **Month Grouping**: Saves deletion month for easy monthly reports
- **Human-Readable Time**: Converts minutes to "2h 30m" format
- **Database Ready**: All the annotations needed to save to database

### 📚 **Programming Concepts You Learned:**
- **Data Transfer Objects**: Classes that mainly store and transfer data
- **Constructor Overloading**: Multiple ways to create objects
- **Mathematical Operations**: Division (`/`), modulo (`%`) for time calculations
- **String Formatting**: Making numbers look nice for humans
- **Conditional Logic**: Different behavior based on conditions (if device is on)
- **Database Annotations**: Telling the database how to save data

### 🏠 **Real-World Example:**
```
You have a TV in your living room that used 45.5 kWh of electricity over 120 hours.
When you delete it from the app, this class creates a record:
- Device: "TV Samsung 55inch in Living Room"
- Energy: 45.500 kWh
- Usage: 120h 0m
- Deleted: January 15, 2024 at 2:30 PM
- Month: "2024-01"

Now you can still see this energy usage in your January electricity bill!
```

This class shows how real applications handle data that needs to be preserved even when the original item is removed!

---

# DevicePermission.java - Complete Code with Beginner Explanations

This document explains the `DevicePermission.java` file for beginners. The file is located at `src/main/java/com/smarthome/model/DevicePermission.java`.

## What is this file?
Imagine you have a smart TV in your living room, and you want to let your family members control it, but maybe you don't want guests to control it. This class manages **permissions** - it decides who can do what with each device. It's like giving someone a key to your house, but only for specific rooms!

## Package Declaration and Imports

```java
package com.smarthome.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import java.time.LocalDateTime;
```
**What this means:**
- `package com.smarthome.model;` - This file belongs to the model package (same as other model classes)
- Database annotations to save permission data
- `LocalDateTime` - To record when permission was given

## Class Declaration

```java
@DynamoDbBean
public class DevicePermission {
```
**What this means:**
- `@DynamoDbBean` tells the database this class can be saved
- This class is like a "permission slip" that says who can use which device

## Instance Variables (What Information We Store)

```java
private String memberEmail;
private String deviceType;
private String roomName;
private String deviceOwnerEmail;
private boolean canControl;
private boolean canView;
private LocalDateTime grantedAt;
private String grantedBy;
```

**What each variable stores:**
- `String memberEmail;` - The email of the person getting permission (like "john@family.com")
- `String deviceType;` - What kind of device (like "TV" or "Light")
- `String roomName;` - Which room the device is in (like "Living Room")
- `String deviceOwnerEmail;` - Who owns the device (like "mom@family.com")
- `boolean canControl;` - Can they turn it on/off? (true = yes, false = no)
- `boolean canView;` - Can they see the device status? (true = yes, false = no)
- `LocalDateTime grantedAt;` - When was this permission given?
- `String grantedBy;` - Who gave this permission?

## Constructors (How to Create Permissions)

### Empty Constructor
```java
public DevicePermission() {
    this.canControl = true;
    this.canView = true;
    this.grantedAt = LocalDateTime.now();
}
```
**What this does:**
- Creates an empty permission
- `this.canControl = true;` - By default, people can control devices
- `this.canView = true;` - By default, people can see device status
- `this.grantedAt = LocalDateTime.now();` - Records the current time

### Constructor with All Information
```java
public DevicePermission(String memberEmail, String deviceType, String roomName,
                       String deviceOwnerEmail, String grantedBy) {
    this.memberEmail = memberEmail;
    this.deviceType = deviceType;
    this.roomName = roomName;
    this.deviceOwnerEmail = deviceOwnerEmail;
    this.grantedBy = grantedBy;
    this.canControl = true;
    this.canView = true;
    this.grantedAt = LocalDateTime.now();
}
```
**What this does:**
- Creates a complete permission with all details
- Sets who gets permission, for which device, and who gave it
- By default gives full permission (control = true, view = true)
- Records the current time when permission is created

**Real-world example:**
```java
// Mom gives John permission to control the living room TV
DevicePermission johnTvPermission = new DevicePermission(
    "john@family.com",     // John gets permission
    "TV",                  // For the TV
    "Living Room",         // In the living room
    "mom@family.com",      // Mom owns the TV
    "mom@family.com"       // Mom is giving the permission
);
```

## Getter and Setter Methods with Database Annotations

### Member Email Methods
```java
@DynamoDbAttribute("memberEmail")
public String getMemberEmail() {
    return memberEmail;
}

public void setMemberEmail(String memberEmail) {
    this.memberEmail = memberEmail;
}
```

### Device Type Methods
```java
@DynamoDbAttribute("deviceType")
public String getDeviceType() {
    return deviceType;
}

public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
}
```

### Room Name Methods
```java
@DynamoDbAttribute("roomName")
public String getRoomName() {
    return roomName;
}

public void setRoomName(String roomName) {
    this.roomName = roomName;
}
```

### Device Owner Methods
```java
@DynamoDbAttribute("deviceOwnerEmail")
public String getDeviceOwnerEmail() {
    return deviceOwnerEmail;
}

public void setDeviceOwnerEmail(String deviceOwnerEmail) {
    this.deviceOwnerEmail = deviceOwnerEmail;
}
```

### Permission Level Methods
```java
@DynamoDbAttribute("canControl")
public boolean isCanControl() {
    return canControl;
}

public void setCanControl(boolean canControl) {
    this.canControl = canControl;
}

@DynamoDbAttribute("canView")
public boolean isCanView() {
    return canView;
}

public void setCanView(boolean canView) {
    this.canView = canView;
}
```
**What these do:**
- `isCanControl()` - Returns true if person can control the device (turn on/off, change settings)
- `isCanView()` - Returns true if person can see the device (view status, energy usage)
- Notice: Boolean getters often start with "is" instead of "get"

### Time and Grant Information Methods
```java
@DynamoDbAttribute("grantedAt")
public LocalDateTime getGrantedAt() {
    return grantedAt;
}

public void setGrantedAt(LocalDateTime grantedAt) {
    this.grantedAt = grantedAt;
}

@DynamoDbAttribute("grantedBy")
public String getGrantedBy() {
    return grantedBy;
}

public void setGrantedBy(String grantedBy) {
    this.grantedBy = grantedBy;
}
```

**What these methods do:**
- Standard getters and setters for all the permission information
- `@DynamoDbAttribute("fieldName")` tells the database how to save each field
- These let you safely read and change permission details

## Special Utility Methods

### Check if Permission Matches a Device
```java
public boolean matchesDevice(String deviceType, String roomName, String ownerEmail) {
    return this.deviceType.equalsIgnoreCase(deviceType) &&
           this.roomName.equalsIgnoreCase(roomName) &&
           this.deviceOwnerEmail.equalsIgnoreCase(ownerEmail);
}
```
**What this does:**
- Checks if this permission applies to a specific device
- `equalsIgnoreCase()` compares text ignoring CAPITAL/lowercase differences
- Returns `true` if all three things match:
  1. Device type matches (like "TV")
  2. Room name matches (like "Living Room")
  3. Owner email matches (like "mom@family.com")

**Example usage:**
```java
// Check if John's permission applies to Mom's living room TV
boolean applies = johnTvPermission.matchesDevice("TV", "Living Room", "mom@family.com");
// This would return true if the permission is for that exact device
```

### Get Unique Device Identifier
```java
public String getDeviceId() {
    return deviceOwnerEmail + ":" + deviceType + ":" + roomName;
}
```
**What this does:**
- Creates a unique ID for the device this permission is about
- Combines owner email + device type + room name with ":" separators
- Example result: "mom@family.com:TV:Living Room"
- This helps identify exactly which device, even if there are multiple TVs in the house

### ToString Method for Debugging
```java
@Override
public String toString() {
    return "DevicePermission{" +
            "memberEmail='" + memberEmail + '\'' +
            ", deviceType='" + deviceType + '\'' +
            ", roomName='" + roomName + '\'' +
            ", deviceOwnerEmail='" + deviceOwnerEmail + '\'' +
            ", canControl=" + canControl +
            ", canView=" + canView +
            ", grantedBy='" + grantedBy + '\'' +
            '}';
}
```
**What this does:**
- Creates a readable description of the permission
- Example output: "DevicePermission{memberEmail='john@family.com', deviceType='TV', roomName='Living Room', deviceOwnerEmail='mom@family.com', canControl=true, canView=true, grantedBy='mom@family.com'}"
- Useful for debugging and logging

## Summary for Beginners

This `DevicePermission` class is like a **digital key system** for smart home devices. Here's what it does:

### 🔑 **Why This Class Exists:**
- Controls who can use which devices in a smart home
- Like giving house keys, but only for specific rooms/devices
- Prevents unauthorized access to your smart devices
- Allows family sharing while maintaining security

### 🏠 **Permission Levels:**
- **View Permission**: Can see if device is on/off, energy usage, status
- **Control Permission**: Can turn device on/off, change settings
- You can give someone view-only access (they can see but not control)

### 👨‍👩‍👧‍👦 **Real-World Examples:**

**Example 1: Family TV**
```
Mom gives John permission to control the living room TV:
- memberEmail: "john@family.com"
- deviceType: "TV"
- roomName: "Living Room"
- deviceOwnerEmail: "mom@family.com"
- canControl: true (John can turn it on/off)
- canView: true (John can see if it's on)
- grantedBy: "mom@family.com"
```

**Example 2: Guest Access**
```
Dad gives a guest limited access to the kitchen light:
- memberEmail: "guest@email.com"
- deviceType: "Light"
- roomName: "Kitchen"
- deviceOwnerEmail: "dad@family.com"
- canControl: false (Guest can't control it)
- canView: true (Guest can see if it's on)
- grantedBy: "dad@family.com"
```

### 🔧 **How It Works:**
1. **Device owner** creates a permission for a **family member**
2. **Permission specifies** exactly which device (type + room + owner)
3. **Permission levels** control what they can do (view/control)
4. **System checks** permissions before allowing any action

### 📚 **Programming Concepts You Learned:**
- **Access Control**: Limiting what users can do based on permissions
- **Boolean Logic**: Using true/false values for permissions
- **String Manipulation**: Creating unique IDs by combining strings
- **Method Chaining**: Using && (AND) operator for multiple conditions
- **Case-Insensitive Comparison**: Using `equalsIgnoreCase()` for user-friendly matching
- **Composite Keys**: Combining multiple fields to create unique identifiers

### 🛡️ **Security Benefits:**
- **Granular Control**: Permission per device, not all-or-nothing
- **Audit Trail**: Records who gave permission and when
- **Revocable**: Permissions can be removed anytime
- **Specific**: Targets exact device (not just "all TVs")

This class demonstrates how professional applications implement **role-based access control** - a fundamental security concept used in everything from banking apps to corporate systems!