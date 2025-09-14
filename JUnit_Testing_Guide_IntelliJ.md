# JUnit Testing in IntelliJ IDEA - Complete Guide
## IoT Smart Home Dashboard Project

---

## Table of Contents
1. [Quick Start Guide](#quick-start)
2. [Project Setup](#project-setup)
3. [Running Tests - Multiple Methods](#running-tests)
4. [Test Configuration](#test-configuration)
5. [Understanding Test Results](#test-results)
6. [Troubleshooting](#troubleshooting)
7. [Advanced Testing Features](#advanced-features)
8. [Creating New Tests](#creating-tests)
9. [Best Practices](#best-practices)
10. [Keyboard Shortcuts](#shortcuts)

---

## Quick Start Guide {#quick-start}

### **Method 1: Run Individual Test Method**
1. **Open** your test file (`DevicePermissionTest.java` or `SmartHomeServiceTest.java`)
2. **Right-click** on any `@Test` method name
3. **Select** "Run 'methodName()'"
4. **Alternative**: Press `Ctrl+Shift+F10` (Windows/Linux) or `Cmd+Shift+R` (Mac)

### **Method 2: Run Entire Test Class**
1. **Right-click** on the class name in editor
2. **Select** "Run 'ClassName'"
3. **Alternative**: Click the green arrow ▶️ next to class name
4. **Alternative**: Press `Ctrl+Shift+F10` with cursor on class name

### **Method 3: Run All Tests in Project**
1. **Right-click** on `src/test/java` folder in Project Explorer
2. **Select** "Run 'All Tests'"
3. **Alternative**: Menu → `Run` → `Run All Tests`

---

## Project Setup {#project-setup}

### **Your IoT Project Structure with Exact File Paths**
```
iot-smart-home-dashboard/
├── src/
│   ├── main/java/com/smarthome/
│   │   ├── SmartHomeDashboard.java
│   │   ├── model/
│   │   └── service/
│   └── test/java/com/smarthome/        ← Test files location
│       ├── DevicePermissionTest.java   ← Your test files
│       └── SmartHomeServiceTest.java   ← Your test files
├── pom.xml                             ← Maven configuration
└── target/                             ← Build output
```

### **Exact File Paths for Your JUnit Test Cases**

#### **Test Files Location:**
- **Base Directory**: `./iot-smart-home-dashboard/src/test/java/com/smarthome/`

#### **Individual Test File Paths:**
1. **Device Permission Tests**
   - **Full Path**: `./iot-smart-home-dashboard/src/test/java/com/smarthome/DevicePermissionTest.java`
   - **Relative Path**: `src/test/java/com/smarthome/DevicePermissionTest.java`
   - **Package**: `com.smarthome`
   - **Class Name**: `DevicePermissionTest`

2. **Smart Home Service Tests**
   - **Full Path**: `./iot-smart-home-dashboard/src/test/java/com/smarthome/SmartHomeServiceTest.java`
   - **Relative Path**: `src/test/java/com/smarthome/SmartHomeServiceTest.java`
   - **Package**: `com.smarthome`
   - **Class Name**: `SmartHomeServiceTest`

#### **How to Navigate to Test Files in IntelliJ:**
1. **Using Project Explorer:**
   - Navigate: `iot-smart-home-dashboard` → `src` → `test` → `java` → `com` → `smarthome`
   - **Double-click** on `DevicePermissionTest.java` or `SmartHomeServiceTest.java`

2. **Using Quick Navigation:**
   - Press `Ctrl+N` (Windows/Linux) or `Cmd+O` (Mac)
   - Type: `DevicePermissionTest` or `SmartHomeServiceTest`
   - Press `Enter` to open

3. **Using File Search:**
   - Press `Ctrl+Shift+N` (Windows/Linux) or `Cmd+Shift+O` (Mac)
   - Type: `DevicePermissionTest.java` or `SmartHomeServiceTest.java`

### **Verify JUnit Dependencies in pom.xml**
```xml
<dependencies>
    <!-- JUnit 5 Engine -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit 5 API -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>

    <!-- Mockito for Mocking -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.6.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## Running Tests - Multiple Methods {#running-tests}

### **Method 1: Using Green Arrow Icons**

#### **Class Level (Run All Tests in Class)**
```java
public class SmartHomeServiceTest {  // ← Green ▶️ arrow appears here

    @Test
    public void testUserRegistration() { // ← Green ▶️ arrow appears here
        // Test implementation
    }

    @Test
    public void testUserLogin() {        // ← Green ▶️ arrow appears here
        // Test implementation
    }
}
```

**Steps:**
1. **Look for green arrow icons** in the left margin (gutter)
2. **Click the arrow** next to class name to run all tests
3. **Click the arrow** next to method name to run single test

#### **Package Level (Run All Tests in Package)**
1. **Navigate** to `src/test/java/com/smarthome` in Project Explorer
2. **Right-click** on the package folder
3. **Select** "Run 'Tests in com.smarthome'"

### **Method 2: Using Context Menu**

#### **For DevicePermissionTest.java**
1. **Open** `DevicePermissionTest.java`
2. **Right-click** anywhere in the editor
3. **Select** one of these options:
   - "Run 'DevicePermissionTest'" (runs all tests in class)
   - "Debug 'DevicePermissionTest'" (debug mode)
   - "Run 'DevicePermissionTest' with Coverage" (shows code coverage)

#### **For SmartHomeServiceTest.java**
1. **Open** `SmartHomeServiceTest.java`
2. **Right-click** on specific test method
3. **Choose** from menu:
   - "Run 'testMethodName()'"
   - "Debug 'testMethodName()'"

### **Method 3: Using Run Menu**
1. **Menu Bar** → `Run`
2. **Select** from dropdown:
   - "Run..." (shows run configuration dialog)
   - "Run All Tests"
   - "Run Tests in Current File"

### **Method 4: Using Maven Integration**

#### **Terminal Commands (Bottom panel in IntelliJ)**
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=DevicePermissionTest

# Run specific test method
mvn test -Dtest=DevicePermissionTest#testGrantPermission

# Run tests with specific pattern
mvn test -Dtest="*Test"

# Skip tests (if needed)
mvn install -DskipTests
```

#### **Maven Tool Window**
1. **View** → **Tool Windows** → **Maven**
2. **Expand** your project → **Lifecycle**
3. **Double-click** `test` to run all tests

---

## Test Configuration {#test-configuration}

### **1. IntelliJ Test Runner Settings**

#### **Access Settings:**
- **Windows/Linux**: `File` → `Settings` → `Build, Execution, Deployment` → `Build Tools` → `Maven` → `Runner`
- **Mac**: `IntelliJ IDEA` → `Preferences` → `Build, Execution, Deployment` → `Build Tools` → `Maven` → `Runner`

#### **Recommended Settings:**
- ✅ **Check**: "Delegate IDE build/run actions to Maven"
- ✅ **Check**: "Run tests using: Maven"
- **JVM Options**: `-Xmx1024m` (if needed for large tests)

### **2. JUnit Test Framework Configuration**

#### **Access Framework Settings:**
- **Path**: `Settings` → `Build, Execution, Deployment` → `Testing` → `Test Framework`

#### **Configuration:**
- **Default test framework**: JUnit 5
- **Test output**: Show all
- **Generate test classes in**: `src/test/java`

### **3. Project Structure Verification**

#### **Check Project Structure:**
1. **File** → **Project Structure** (or `Ctrl+Alt+Shift+S`)
2. **Modules** → Select your module → **Dependencies** tab
3. **Verify** these are present:
   - JUnit Jupiter API
   - JUnit Jupiter Engine
   - Mockito Core (if using mocks)

### **4. Run Configuration Templates**

#### **Create JUnit Run Configuration:**
1. **Run** → **Edit Configurations**
2. **Click** `+` → **JUnit**
3. **Configure**:
   - **Name**: "All IoT Tests"
   - **Test kind**: "All in package"
   - **Package**: `com.smarthome`
   - **Search for tests**: In whole project

---

## Understanding Test Results {#test-results}

### **Test Results Window Layout**

```
┌─ Test Results Panel ──────────────────────────────────┐
├─ [Progress Bar: Green/Red/Yellow]                     │
├─ Test Tree Structure:                                 │
│  ├─ ✅ SmartHomeServiceTest                          │
│  │   ├─ ✅ testUserRegistration                     │
│  │   ├─ ✅ testUserLogin                            │
│  │   └─ ❌ testDeviceConnection                     │
│  └─ ✅ DevicePermissionTest                          │
│      ├─ ✅ testGrantPermission                       │
│      └─ ✅ testRevokePermission                      │
├─ Console Output (bottom panel)                        │
└─ Test Statistics (summary)                           │
└────────────────────────────────────────────────────────┘
```

### **Status Indicators**
- **✅ Green**: Test passed successfully
- **❌ Red**: Test failed with error
- **⚠️ Yellow**: Test skipped/ignored
- **🔄 Blue**: Test running currently

### **Progress Bar Colors**
- **Green Bar**: All tests passed
- **Red Bar**: One or more tests failed
- **Yellow Bar**: Some tests were skipped

### **Reading Test Output**

#### **Successful Test:**
```
✅ testUserRegistration
   Duration: 0.45s
   Status: PASSED
```

#### **Failed Test:**
```
❌ testDeviceConnection
   Duration: 0.12s
   Status: FAILED

   java.lang.AssertionError: Expected device to be connected
   Expected: true
   Actual: false

   at org.junit.jupiter.api.AssertionFailureBuilder.build(...)
   at SmartHomeServiceTest.testDeviceConnection(SmartHomeServiceTest.java:45)
```

### **Test Statistics Panel**
```
Tests: 5 total, 4 passed, 1 failed, 0 skipped
Time: 2.3s
Coverage: 78% (if run with coverage)
```

---

## Troubleshooting {#troubleshooting}

### **Common Issue 1: "No tests found"**

#### **Symptoms:**
- IntelliJ shows "No tests found" message
- Tests exist but don't appear in results

#### **Solutions:**
1. **Verify Test Annotations:**
   ```java
   import org.junit.jupiter.api.Test;  // ← Correct import for JUnit 5

   @Test  // ← Must have this annotation
   public void testMethod() {
       // Test code
   }
   ```

2. **Check Test Location:**
   - Tests must be in `src/test/java` directory
   - Package structure must match main code

3. **Refresh Project:**
   - **File** → **Invalidate Caches and Restart**
   - **Build** → **Rebuild Project**

### **Common Issue 2: "ClassNotFoundException"**

#### **Symptoms:**
```
java.lang.ClassNotFoundException: com.smarthome.model.Customer
```

#### **Solutions:**
1. **Clean and Rebuild:**
   ```bash
   mvn clean compile test-compile
   ```

2. **Check Dependencies:**
   - **Maven Tool Window** → **Reload Maven Projects**
   - **File** → **Invalidate Caches and Restart**

3. **Verify Test Dependencies:**
   ```java
   // In test files, import from main source
   import com.smarthome.model.Customer;     // ✅ Correct
   import com.smarthome.service.CustomerService; // ✅ Correct
   ```

### **Common Issue 3: DynamoDB Connection Errors**

#### **Symptoms:**
```
Exception in thread "main" java.net.ConnectException: Connection refused
```

#### **Solutions:**
1. **Start Local DynamoDB First:**
   ```bash
   # In separate terminal/command prompt
   cd dynamodb-local
   java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -dbPath .
   ```

2. **Mock Database in Tests:**
   ```java
   @Mock
   private CustomerService customerService;

   @Test
   void testWithoutDatabase() {
       // Use mocks instead of real database
       when(customerService.findCustomer(anyString())).thenReturn(mockCustomer);
   }
   ```

### **Common Issue 4: Maven Surefire Plugin Issues**

#### **Symptoms:**
- Tests run in IntelliJ but fail with `mvn test`
- "No tests to run" from Maven

#### **Solution:**
Add to `pom.xml`:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.1.2</version>
            <configuration>
                <includes>
                    <include>**/*Test.java</include>
                    <include>**/*Tests.java</include>
                </includes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### **Common Issue 5: Test Method Not Recognized**

#### **Problem:**
```java
// ❌ Wrong - JUnit 4 annotation
@org.junit.Test
public void testMethod() { }

// ✅ Correct - JUnit 5 annotation
@org.junit.jupiter.api.Test
public void testMethod() { }
```

#### **Solution:**
```java
// Correct imports for JUnit 5
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
```

---

## Advanced Testing Features {#advanced-features}

### **1. Run Tests with Code Coverage**

#### **Steps:**
1. **Right-click** on test class/method
2. **Select** "Run 'TestName' with Coverage"
3. **View Results** in Coverage tab

#### **Coverage Report:**
```
Coverage Summary:
- Class Coverage: 85%
- Method Coverage: 78%
- Line Coverage: 82%

Files with Low Coverage:
- SmartHomeService.java: 65%
- EnergyManagementService.java: 70%
```

#### **Visual Coverage Indicators:**
- **Green highlight**: Code covered by tests
- **Red highlight**: Code NOT covered by tests
- **Yellow highlight**: Partially covered branches

### **2. Debug Tests**

#### **Setting Breakpoints:**
1. **Click** in left gutter next to line number
2. **Red dot** appears indicating breakpoint

#### **Running in Debug Mode:**
1. **Right-click** test method
2. **Select** "Debug 'testName'"
3. **Use Debug Controls**:
   - `F8`: Step Over
   - `F7`: Step Into
   - `F9`: Resume Program

#### **Debug Windows:**
- **Variables**: Shows current variable values
- **Watches**: Monitor specific expressions
- **Console**: Test output and print statements

### **3. Rerun Failed Tests**

#### **After Test Execution:**
1. **Look for** 🔄 "Rerun Failed Tests" button in test results
2. **Click** to run only the tests that failed
3. **Saves time** when fixing multiple test failures

### **4. Test History and Results**

#### **Access Test History:**
- **View** → **Tool Windows** → **Test Results**
- **Shows** previous test runs
- **Filter** by passed/failed/skipped

### **5. Parallel Test Execution**

#### **Configure in pom.xml:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <parallel>methods</parallel>
        <threadCount>4</threadCount>
    </configuration>
</plugin>
```

---

## Creating New Tests {#creating-tests}

### **Method 1: Generate Test from Class**

#### **Steps:**
1. **Open** the class you want to test (e.g., `Customer.java`)
2. **Right-click** in editor → **Go To** → **Test**
3. **Alternative**: Press `Ctrl+Shift+T` (Windows/Linux) or `Cmd+Shift+T` (Mac)
4. **Click** "Create New Test"
5. **Configure Test Class:**
   - **Testing library**: JUnit5
   - **Class name**: CustomerTest
   - **Destination package**: com.smarthome
   - **Select methods** to generate test stubs for

### **Method 2: Manual Test Creation**

#### **Create Test File:**
1. **Right-click** on `src/test/java/com/smarthome`
2. **New** → **Java Class**
3. **Name**: `CustomerTest`
4. **Add test methods**

#### **Test Template:**
```java
package com.smarthome;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer Management Tests")
class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        // Initialize test data before each test
        customer = new Customer("test@example.com", "Test User", "hashedPassword");
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        customer = null;
    }

    @Test
    @DisplayName("Should add gadget successfully")
    void shouldAddGadgetSuccessfully() {
        // Arrange
        Gadget gadget = new Gadget("TV", "Samsung", "Living Room");

        // Act
        customer.addGadget(gadget);

        // Assert
        assertEquals(1, customer.getGadgets().size());
        assertTrue(customer.getGadgets().contains(gadget));
    }

    @Test
    @DisplayName("Should not add duplicate gadget")
    void shouldNotAddDuplicateGadget() {
        // Arrange
        Gadget gadget1 = new Gadget("TV", "Samsung", "Living Room");
        Gadget gadget2 = new Gadget("TV", "LG", "Living Room"); // Same type + room

        // Act
        customer.addGadget(gadget1);
        customer.addGadget(gadget2); // This should not be added

        // Assert
        assertEquals(1, customer.getGadgets().size());
    }
}
```

### **Method 3: Test with Mockito**

#### **Mock Dependencies:**
```java
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmartHomeServiceTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private GadgetService gadgetService;

    private SmartHomeService smartHomeService;

    @BeforeEach
    void setUp() {
        smartHomeService = new SmartHomeService();
        // Inject mocks if needed
    }

    @Test
    void testConnectToGadgetWithMocks() {
        // Arrange
        Customer mockCustomer = new Customer();
        when(customerService.findCustomerByEmail("test@example.com"))
            .thenReturn(mockCustomer);

        // Act
        boolean result = smartHomeService.connectToGadget("TV", "Samsung", "Living Room");

        // Assert
        assertTrue(result);
        verify(customerService).updateCustomer(mockCustomer);
    }
}
```

---

## Best Practices {#best-practices}

### **1. Test Naming Conventions**

#### **Method Names:**
```java
// ✅ Good - Describes what should happen
@Test
void shouldReturnTrueWhenPasswordIsValid() { }

@Test
void shouldThrowExceptionWhenEmailIsNull() { }

@Test
void shouldAddGadgetWhenValidParametersProvided() { }

// ❌ Bad - Vague or unclear
@Test
void testPassword() { }

@Test
void test1() { }
```

#### **Class Names:**
```java
// ✅ Good
CustomerTest.java
SmartHomeServiceTest.java
DevicePermissionTest.java

// ❌ Bad
TestCustomer.java
Tests.java
```

### **2. Test Structure (AAA Pattern)**

```java
@Test
void shouldCalculateEnergyConsumptionCorrectly() {
    // Arrange - Set up test data
    Gadget gadget = new Gadget("AC", "LG", "Bedroom");
    gadget.setPowerRatingWatts(1500);
    gadget.turnOn();

    // Act - Execute the method being tested
    double energy = gadget.getCurrentTotalEnergyConsumedKWh();

    // Assert - Verify the results
    assertTrue(energy >= 0);
    assertNotNull(energy);
}
```

### **3. Test Data Management**

#### **Use @BeforeEach for Common Setup:**
```java
@BeforeEach
void setUp() {
    customer = new Customer("test@example.com", "Test User", "password123");
    gadget = new Gadget("TV", "Samsung", "Living Room");
}
```

#### **Use @TestInstance for Expensive Setup:**
```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseIntegrationTest {

    @BeforeAll
    void setUpDatabase() {
        // Start test database once for all tests
    }

    @AfterAll
    void tearDownDatabase() {
        // Clean up database after all tests
    }
}
```

### **4. Assertion Best Practices**

#### **Use Specific Assertions:**
```java
// ✅ Good - Specific and clear
assertEquals(5, customer.getGadgets().size());
assertTrue(customer.isGroupAdmin());
assertThrows(IllegalArgumentException.class, () -> {
    customer.addGadget(null);
});

// ❌ Bad - Generic and unclear
assertTrue(customer.getGadgets().size() > 0);
assertEquals(true, customer.isGroupAdmin());
```

#### **Custom Error Messages:**
```java
assertEquals(5, gadgets.size(),
    "Expected 5 gadgets but found " + gadgets.size());

assertNotNull(customer.getEmail(),
    "Customer email should not be null after registration");
```

### **5. Test Organization**

#### **Group Related Tests:**
```java
@Nested
@DisplayName("User Registration Tests")
class UserRegistrationTests {

    @Test
    @DisplayName("Should register with valid data")
    void shouldRegisterWithValidData() { }

    @Test
    @DisplayName("Should reject invalid email")
    void shouldRejectInvalidEmail() { }
}

@Nested
@DisplayName("Device Management Tests")
class DeviceManagementTests {

    @Test
    void shouldAddDevice() { }

    @Test
    void shouldRemoveDevice() { }
}
```

### **6. Parameterized Tests**

```java
@ParameterizedTest
@ValueSource(strings = {"", "  ", "invalid-email", "@domain.com"})
@DisplayName("Should reject invalid email formats")
void shouldRejectInvalidEmailFormats(String invalidEmail) {
    assertFalse(customerService.isValidEmail(invalidEmail));
}

@ParameterizedTest
@CsvSource({
    "TV, Samsung, Living Room, true",
    "AC, LG, Bedroom, true",
    "'', Samsung, Living Room, false",
    "TV, '', Living Room, false"
})
void testGadgetCreation(String type, String model, String room, boolean expected) {
    boolean result = gadgetService.isValidGadgetData(type, model, room);
    assertEquals(expected, result);
}
```

---

## Keyboard Shortcuts {#shortcuts}

### **Essential Shortcuts**

| Action | Windows/Linux | Mac | Description |
|--------|---------------|-----|-------------|
| Run test | `Ctrl+Shift+F10` | `Cmd+Shift+R` | Run test at cursor |
| Debug test | `Ctrl+Shift+F9` | `Cmd+Shift+D` | Debug test at cursor |
| Rerun last test | `Shift+F10` | `Shift+F10` | Rerun previous test |
| Go to test | `Ctrl+Shift+T` | `Cmd+Shift+T` | Navigate between class and test |
| Run with coverage | `Ctrl+Shift+F6` | `Cmd+Shift+F6` | Run with code coverage |

### **Debug Shortcuts**

| Action | Shortcut | Description |
|--------|----------|-------------|
| Toggle breakpoint | `Ctrl+F8` | Add/remove breakpoint |
| Step over | `F8` | Execute next line |
| Step into | `F7` | Enter method call |
| Step out | `Shift+F8` | Exit current method |
| Resume program | `F9` | Continue execution |
| Evaluate expression | `Alt+F8` | Check variable value |

### **Navigation Shortcuts**

| Action | Shortcut | Description |
|--------|----------|-------------|
| Find class | `Ctrl+N` | Quick find test class |
| Find file | `Ctrl+Shift+N` | Find test file |
| Recent files | `Ctrl+E` | Recently opened tests |
| Go to declaration | `Ctrl+B` | Jump to method being tested |

---

## Testing Your IoT Project - Practical Examples

### **Testing DevicePermissionTest.java**

#### **File Path**: `./iot-smart-home-dashboard/src/test/java/com/smarthome/DevicePermissionTest.java`

**Steps to Run:**
1. **Navigate to File**:
   - **Full Path**: `./iot-smart-home-dashboard/src/test/java/com/smarthome/DevicePermissionTest.java`
   - **In IntelliJ**: Project Explorer → `iot-smart-home-dashboard` → `src` → `test` → `java` → `com` → `smarthome` → `DevicePermissionTest.java`

2. **Open** the file by double-clicking
3. **Right-click** anywhere in the editor
4. **Select** "Run 'DevicePermissionTest'"
5. **Observe** results in Test Results panel

**Alternative Methods:**
- **Green Arrow**: Click ▶️ next to class name or method
- **Keyboard**: `Ctrl+Shift+F10` (Windows/Linux) or `Cmd+Shift+R` (Mac)
- **Maven Command**: `mvn test -Dtest=DevicePermissionTest`

### **Testing SmartHomeServiceTest.java**

#### **File Path**: `./iot-smart-home-dashboard/src/test/java/com/smarthome/SmartHomeServiceTest.java`

**Steps to Run:**
1. **Navigate to File**:
   - **Full Path**: `./iot-smart-home-dashboard/src/test/java/com/smarthome/SmartHomeServiceTest.java`
   - **Quick Navigation**: Press `Ctrl+N`, type `SmartHomeServiceTest`, press Enter

2. **Open** `SmartHomeServiceTest.java`
3. **Click** green arrow ▶️ next to specific test method
4. **Watch** console output for any errors
5. **Check** that DynamoDB local is running if tests fail

**Maven Commands for Specific Tests:**
```bash
# Run specific class
mvn test -Dtest=SmartHomeServiceTest

# Run specific method
mvn test -Dtest=SmartHomeServiceTest#testUserRegistration

# Run both test classes
mvn test -Dtest=DevicePermissionTest,SmartHomeServiceTest
```

### **Running All Tests**

#### **Test Directory Path**: `./iot-smart-home-dashboard/src/test/java/`

**Steps to Run All Tests:**
1. **Navigate to Test Directory**:
   - **Full Path**: `./iot-smart-home-dashboard/src/test/java/`
   - **In IntelliJ**: Project Explorer → `iot-smart-home-dashboard` → `src` → `test` → `java`

2. **Right-click** on `test/java` folder
3. **Select** "Run 'All Tests'"
4. **Monitor** progress bar
5. **Review** summary when complete

**Maven Commands for All Tests:**
```bash
# Run all tests in project
mvn test

# Run all tests in specific directory
mvn test -Dtest="com.smarthome.*Test"

# Run tests with specific pattern
mvn test -Dtest="**/*Test.java"
```

#### **File Paths Being Tested:**
When you run "All Tests", Maven will execute:
- `./iot-smart-home-dashboard/src/test/java/com/smarthome/DevicePermissionTest.java`
- `./iot-smart-home-dashboard/src/test/java/com/smarthome/SmartHomeServiceTest.java`
- Any other `*Test.java` files in the test directory

### **Debugging Failed Tests**

1. **Look for** red ❌ indicators in test results
2. **Click** on failed test to see error details
3. **Set breakpoint** in failing test method
4. **Right-click** → "Debug 'testName'"
5. **Step through** code to find issue

---

## 📁 **Quick Reference: All Test File Paths**

### **Complete File Path List:**

#### **Test Files:**
```
📂 Project Root: ./iot-smart-home-dashboard/

📁 Test Directory: ./iot-smart-home-dashboard/src/test/java/com/smarthome/

📄 Test Files:
├── ./iot-smart-home-dashboard/src/test/java/com/smarthome/DevicePermissionTest.java
└── ./iot-smart-home-dashboard/src/test/java/com/smarthome/SmartHomeServiceTest.java
```

#### **Configuration Files:**
```
📄 Maven Config: ./iot-smart-home-dashboard/pom.xml
📄 Main App: ./iot-smart-home-dashboard/src/main/java/com/smarthome/SmartHomeDashboard.java
📁 Build Output: ./iot-smart-home-dashboard/target/
```

### **Command Line Navigation:**
```bash
# Navigate to project root
cd ./iot-smart-home-dashboard

# Navigate to test directory
cd ./iot-smart-home-dashboard/src/test/java/com/smarthome

# Run specific test files
mvn test -Dtest=DevicePermissionTest
mvn test -Dtest=SmartHomeServiceTest

# Run tests by file path pattern
mvn test -Dtest="src/test/java/com/smarthome/*Test.java"
```

### **IntelliJ Navigation Paths:**
```
Right-click paths in Project Explorer:
├── iot-smart-home-dashboard/src/test/java/ → "Run All Tests"
├── iot-smart-home-dashboard/src/test/java/com/smarthome/ → "Run Tests in Package"
├── DevicePermissionTest.java → "Run DevicePermissionTest"
└── SmartHomeServiceTest.java → "Run SmartHomeServiceTest"
```

### **File Opening Shortcuts:**
- **Quick Open**: `Ctrl+N` → Type `DevicePermissionTest` or `SmartHomeServiceTest`
- **File Search**: `Ctrl+Shift+N` → Type `DevicePermissionTest.java` or `SmartHomeServiceTest.java`
- **Recent Files**: `Ctrl+E` → Select from recently opened test files

---

**Happy Testing!** 🚀

Your JUnit tests in IntelliJ IDEA are now ready to run efficiently. This guide covers everything from basic test execution to advanced debugging and best practices for your IoT Smart Home Dashboard project.