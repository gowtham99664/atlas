# SmartHome Test Suite - Execution Guide

**Version:** 1.0
**Date:** January 2025
**Project:** IoT Smart Home Dashboard

---

## 📋 Table of Contents

1. [How to Execute Tests](#how-to-execute-tests)
2. [Test Suite Overview](#test-suite-overview)
3. [Test Classes Explanation](#test-classes-explanation)
4. [Test Results Interpretation](#test-results-interpretation)
5. [Troubleshooting](#troubleshooting)

---

## 🚀 How to Execute Tests

### Method 1: Using Maven (Recommended)

```bash
# Navigate to project directory
cd iot-smart-home-dashboard

# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=SmartHomeServiceCoreTest

# Run tests with detailed output
mvn test -Dtest=SmartHomeServiceCoreTest -Dtest.verbose=true
```

### Method 2: Using IDE (IntelliJ IDEA / Eclipse)

**IntelliJ IDEA:**
1. Right-click on `src/test/java` folder
2. Select "Run 'All in smarthome'"
3. Or right-click individual test file → "Run 'TestClassName'"

**Eclipse:**
1. Right-click on `src/test/java` folder
2. Select "Run As" → "JUnit Test"
3. Or right-click individual test file → "Run As" → "JUnit Test"

### Method 3: Using Gradle (if applicable)

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests SmartHomeServiceCoreTest
```

---

## 📊 Test Suite Overview

| **Test Class** | **Tests** | **Purpose** | **Execution Time** |
|----------------|-----------|-------------|-------------------|
| `SmartHomeServiceCoreTest` | 25 | Core functionality (login, registration, basic devices) | ~30 seconds |
| `SmartHomeServiceEnergyTest` | 17 | Energy tracking, billing, cost calculations | ~20 seconds |
| `SmartHomeServiceGroupTest` | 25 | Family sharing, permissions, group management | ~35 seconds |
| `SmartHomeServiceTimerTest` | 28 | Device scheduling, timers, calendar events | ~40 seconds |
| `SmartHomeServiceIntegrationTest` | 35 | Weather, scenes, health monitoring, device editing | ~50 seconds |
| **TOTAL** | **130** | **Complete system coverage** | **~3 minutes** |

---

## 🧪 Test Classes Explanation

### 1. **SmartHomeServiceCoreTest** (25 Tests)
**What it tests:** Essential user and device management functionality

#### **User Registration Tests (Tests 1-6)**
- ✅ Valid customer registration with proper credentials
- ❌ Registration with mismatched passwords
- ❌ Registration with invalid email formats
- ❌ Registration with weak passwords
- ❌ Registration with invalid names
- ❌ Duplicate email registration prevention

#### **Login/Logout Tests (Tests 7-10)**
- ✅ Valid customer login with correct credentials
- ❌ Login with wrong password
- ❌ Login with non-existent email
- ✅ Proper logout functionality

#### **Device Connection Tests (Tests 11-15)**
- ✅ Connect valid devices (TV, AC, lights, etc.)
- ❌ Connect device without login
- ❌ Connect duplicate device in same room
- ✅ Connect multiple different devices
- ❌ Connect invalid device models/rooms

#### **Device Control Tests (Tests 16-17)**
- ✅ Change device status (ON/OFF)
- ✅ Change specific device status by room

#### **User Profile Tests (Tests 18-22)**
- ✅ Show current user information
- ✅ Update user full name
- ✅ Update user email address
- ✅ Update user password
- ✅ Verify current password

#### **Service & Utilities Tests (Tests 23-25)**
- ✅ Service initialization verification
- ✅ Password reset workflow
- ✅ Email availability checking

---

### 2. **SmartHomeServiceEnergyTest** (17 Tests)
**What it tests:** Energy consumption tracking and billing calculations

#### **Energy Service Tests (Tests 1-5)**
- ✅ Energy service initialization
- ✅ Energy reports with no devices
- ✅ Energy reports with single device
- ✅ Energy reports with multiple devices
- ✅ Energy reports with running devices

#### **Device Power Tests (Tests 6-8)**
- ✅ Device power rating assignment (TV=150W, AC=1500W, etc.)
- ✅ Energy consumption tracking over time
- ✅ Multiple device energy tracking

#### **Cost Calculation Tests (Tests 9-10)**
- ✅ Slab-based cost calculation (Indian electricity rates)
- ✅ Energy efficiency tips generation

#### **Report Generation Tests (Tests 11-13)**
- ✅ Complete energy report generation
- ✅ Device energy usage display
- ✅ Energy slab breakdown display

#### **Advanced Features Tests (Tests 14-17)**
- ✅ Deleted device energy preservation
- ✅ Zero energy consumption handling
- ✅ Large energy consumption handling
- ✅ Mixed device usage patterns

---

### 3. **SmartHomeServiceGroupTest** (25 Tests)
**What it tests:** Family sharing and device permission system

#### **Group Creation Tests (Tests 1-5)**
- ✅ Add person to group successfully
- ✅ Add multiple people to group
- ❌ Add self to group (prevented)
- ❌ Add non-existent user to group
- ❌ Add duplicate person to group

#### **Group Removal Tests (Tests 6-8)**
- ✅ Remove person from group
- ❌ Remove non-existent person
- ❌ Remove self from group

#### **Group Information Tests (Tests 9-10)**
- ✅ Show group information
- ✅ Show group info without group

#### **Device Permission Tests (Tests 11-17)**
- ✅ Grant device permission to group member
- ❌ Grant permission for non-existent device
- ❌ Grant permission to non-group member
- ✅ Revoke device permission
- ❌ Revoke non-existent permission
- ✅ Multiple device permissions
- ✅ Show device permissions

#### **Security Tests (Tests 18-19)**
- ❌ Non-admin cannot grant permissions
- ❌ Non-admin cannot revoke permissions

#### **Access Control Tests (Tests 20-25)**
- ✅ Permission-based device viewing
- ✅ Permission-based device control
- ✅ Group member management
- ✅ Duplicate permission prevention
- ✅ Cross-group permission isolation
- ❌ Permission operations without login

---

### 4. **SmartHomeServiceTimerTest** (28 Tests)
**What it tests:** Device scheduling and automation features

#### **Timer Service Tests (Tests 1-2)**
- ✅ Timer service initialization
- ✅ Force timer check functionality

#### **Device Timer Scheduling Tests (Tests 3-9)**
- ✅ Schedule valid device timer
- ❌ Schedule timer for non-existent device
- ❌ Schedule timer for past time
- ❌ Schedule timer with invalid action
- ❌ Schedule timer with invalid date format
- ✅ Schedule multiple timers for same device
- ✅ Schedule timers for multiple devices

#### **Timer Display Tests (Tests 10-12)**
- ✅ Show scheduled timers with no timers
- ✅ Show scheduled timers with active timers
- ✅ Get scheduled timers with devices

#### **Timer Cancellation Tests (Tests 13-16)**
- ✅ Cancel valid device timer
- ❌ Cancel non-existent timer
- ❌ Cancel timer for non-existent device
- ❌ Cancel timer with invalid action

#### **Calendar Event Tests (Tests 17-22)**
- ✅ Create valid calendar event
- ❌ Create calendar event with past time
- ❌ Create calendar event with invalid date format
- ✅ Create multiple calendar events
- ✅ Show upcoming events
- ✅ Show event automation

#### **Security & Edge Cases (Tests 23-28)**
- ❌ Timer operations without login
- ✅ Schedule timer with minimal future time
- ❌ Schedule timer very close to current time
- ❌ Calendar event end time before start time
- ❌ Schedule timer for device in different room
- ✅ Overlapping timer schedules

---

### 5. **SmartHomeServiceIntegrationTest** (35 Tests)
**What it tests:** Advanced features and system integration

#### **Weather Service Tests (Tests 1-6)**
- ✅ Weather service initialization
- ✅ Show current weather
- ✅ Show weather forecast
- ✅ Update weather data
- ✅ Clear weather data
- ✅ Check user weather data

#### **Smart Scenes Tests (Tests 7-16)**
- ✅ Smart scenes service initialization
- ✅ Show available scenes
- ✅ Execute smart scene
- ✅ Show scene details
- ✅ Show editable scene details
- ✅ Add device to scene
- ✅ Remove device from scene
- ✅ Change device action in scene
- ✅ Reset scene to original
- ✅ Get scene actions

#### **Device Health Tests (Tests 17-20)**
- ✅ Device health service initialization
- ✅ Show device health report
- ✅ Show maintenance schedule
- ✅ Get system health summary

#### **Device Management Tests (Tests 21-30)**
- ✅ Edit device room
- ❌ Edit device room to invalid room
- ❌ Edit device room for non-existent device
- ✅ Edit device model
- ❌ Edit device model to invalid model
- ✅ Edit device power rating
- ❌ Edit device power rating with invalid value
- ✅ Delete device
- ❌ Delete non-existent device
- ✅ Delete device with energy history preservation

#### **Advanced Integration Tests (Tests 31-35)**
- ❌ Device management operations without login
- ✅ Multiple devices same type different rooms
- ✅ Integration services without devices
- ✅ Service integration consistency
- ✅ Complex device operations

---

## 📈 Test Results Interpretation

### **Successful Test Run Example:**
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.smarthome.SmartHomeServiceCoreTest
🔧 Setting up core test environment...
✅ Valid customer registration test passed
✅ Mismatched passwords test passed
[... more test outputs ...]
🧹 Cleaned up core test environment
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 130, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] BUILD SUCCESS
```

### **Understanding Test Results:**

| **Result** | **Meaning** | **Action Required** |
|------------|-------------|-------------------|
| ✅ **Tests run: 130, Failures: 0** | All tests passed | None - system working correctly |
| ❌ **Tests run: 130, Failures: 5** | 5 tests failed | Check failure details, fix issues |
| ⚠️ **Tests run: 125, Skipped: 5** | 5 tests were skipped | Investigate why tests were skipped |
| 🚫 **Errors: 3** | 3 tests had compilation/runtime errors | Fix code errors before running tests |

### **Common Test Failure Patterns:**

#### **Database Connection Issues:**
```
[ERROR] Failed to connect to DynamoDB Local
Solution: Start DynamoDB Local on port 8002
```

#### **Missing Dependencies:**
```
[ERROR] ClassNotFoundException: org.junit.jupiter.api.Test
Solution: Add JUnit 5 dependency to pom.xml
```

#### **Service Initialization Failures:**
```
[ERROR] NullPointerException in SmartHomeService constructor
Solution: Check service dependencies and configuration
```

---

## 🔧 Troubleshooting

### **Problem 1: Tests Not Running**
**Symptoms:** Tests don't execute or IDE doesn't recognize test files
**Solutions:**
1. Verify JUnit 5 dependencies in `pom.xml`
2. Check that test files are in `src/test/java` directory
3. Ensure test methods have `@Test` annotation
4. Refresh/reimport project in IDE

### **Problem 2: Database Connection Errors**
**Symptoms:** Tests fail with database connection errors
**Solutions:**
1. Start DynamoDB Local: `java -jar DynamoDBLocal.jar -port 8002`
2. Check `application.properties` configuration
3. Verify network connectivity to localhost:8002

### **Problem 3: Service Initialization Failures**
**Symptoms:** NullPointerException in service constructors
**Solutions:**
1. Check all service dependencies are available
2. Verify configuration files are in classpath
3. Ensure proper package structure

### **Problem 4: Test Environment Issues**
**Symptoms:** Tests fail due to leftover data or session issues
**Solutions:**
1. Each test class has proper `@BeforeEach` and `@AfterEach` methods
2. Tests are isolated and don't depend on each other
3. Clear any cached data between test runs

### **Problem 5: Slow Test Execution**
**Symptoms:** Tests take too long to run
**Solutions:**
1. Run tests in parallel: `mvn test -T 1C`
2. Run specific test classes instead of all tests
3. Check for infinite loops or long delays in code

---

## 📝 Quick Reference Commands

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=SmartHomeServiceCoreTest

# Run tests with pattern
mvn test -Dtest="*CoreTest"

# Run single test method
mvn test -Dtest=SmartHomeServiceCoreTest#testValidCustomerRegistration

# Generate test report
mvn test jacoco:report

# Clean and test
mvn clean test

# Skip tests (if needed)
mvn install -DskipTests
```

---

## ✅ Test Execution Checklist

Before running tests, ensure:

- [ ] Java 11+ is installed
- [ ] Maven is installed and configured
- [ ] DynamoDB Local is available (if using database)
- [ ] All dependencies are downloaded (`mvn dependency:resolve`)
- [ ] Project compiles without errors (`mvn compile`)
- [ ] Test directory structure is correct (`src/test/java/com/smarthome/`)
- [ ] JUnit 5 dependencies are in `pom.xml`

**Happy Testing! 🎉**

---

*This guide covers the complete test suite for the IoT Smart Home Dashboard. For questions or issues, refer to the troubleshooting section or check the individual test files for detailed comments.*