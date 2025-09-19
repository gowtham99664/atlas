# DevicePermissionTest.java - Part 1: Setup and Teardown Methods

## Setup and Teardown Methods

### Test Environment Setup (@BeforeEach)

```java
@BeforeEach
@DisplayName("Setup device permission test environment")
void setUp() {
    smartHomeService = new SmartHomeService();
    smartHomeService.registerCustomer(ADMIN_NAME, ADMIN_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
    smartHomeService.registerCustomer(MEMBER_NAME, MEMBER_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
    System.out.println("🔧 Setting up device permission test environment...");
}
```

#### Detailed Setup Analysis

**Step-by-Step Breakdown:**

1. **Service Initialization**
   ```java
   smartHomeService = new SmartHomeService();
   ```
   - **Fresh instance** - Creates new service instance for each test
   - **Clean state** - No leftover data from previous tests
   - **Isolation** - Each test starts with identical conditions

2. **Admin User Registration**
   ```java
   smartHomeService.registerCustomer(ADMIN_NAME, ADMIN_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
   ```
   - **Admin role** - First user typically becomes group admin
   - **Consistent data** - Same admin details across all tests
   - **Password confirmation** - Tests password validation logic

3. **Member User Registration**
   ```java
   smartHomeService.registerCustomer(MEMBER_NAME, MEMBER_EMAIL, TEST_PASSWORD, TEST_PASSWORD);
   ```
   - **Member role** - Second user for testing permissions
   - **Permission target** - User to grant/revoke permissions to
   - **Group dynamics** - Tests multi-user scenarios

4. **Setup Logging**
   ```java
   System.out.println("🔧 Setting up device permission test environment...");
   ```
   - **Visual feedback** - Shows test progress in console
   - **Debugging aid** - Helps track test execution flow
   - **Emoji indicator** - 🔧 shows setup phase

**Setup Benefits:**

| Benefit | Description | Impact |
|---------|-------------|---------|
| **Test Isolation** | Each test gets fresh environment | No interference between tests |
| **Consistent State** | Same starting conditions | Predictable test outcomes |
| **Repeatable** | Setup runs before every test | Reliable test execution |
| **Clean Data** | No leftover test data | Accurate test results |

### Test Environment Cleanup (@AfterEach)

```java
@AfterEach
@DisplayName("Cleanup permission tests")
void tearDown() {
    if (smartHomeService.isLoggedIn()) {
        smartHomeService.logout();
    }
    System.out.println("🧹 Cleaned up permission test environment");
}
```

#### Detailed Cleanup Analysis

**Step-by-Step Breakdown:**

1. **Login State Check**
   ```java
   if (smartHomeService.isLoggedIn()) {
   ```
   - **Conditional cleanup** - Only logout if someone is logged in
   - **Safety check** - Prevents errors from double logout
   - **State awareness** - Checks current session state

2. **Session Cleanup**
   ```java
   smartHomeService.logout();
   ```
   - **Clear session** - Removes logged-in user from memory
   - **Reset state** - Prepares for next test
   - **Security practice** - Always logout after operations

3. **Cleanup Logging**
   ```java
   System.out.println("🧹 Cleaned up permission test environment");
   ```
   - **Completion indicator** - Shows cleanup finished
   - **Debugging aid** - Tracks test lifecycle
   - **Emoji indicator** - 🧹 shows cleanup phase

**Why Cleanup is Important:**

```java
// Without cleanup - potential issues
@Test
void test1() {
    smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
    // Test code...
    // No logout - user stays logged in!
}

@Test
void test2() {
    // This test thinks no one is logged in
    // But admin is still logged in from test1!
    // This could cause unexpected behavior
}
```

```java
// With proper cleanup - clean slate
@Test
void test1() {
    smartHomeService.loginCustomer(ADMIN_EMAIL, TEST_PASSWORD);
    // Test code...
    // @AfterEach automatically logs out
}

@Test
void test2() {
    // Clean state - no one logged in
    // Test behaves predictably
}
```

### Test Lifecycle Visualization

```
Test Class Execution Flow:

1. 🏗️  Test Class Created
   ↓
2. 🔧  @BeforeEach setUp()
   - Create SmartHomeService
   - Register admin user
   - Register member user
   ↓
3. 🧪 @Test testMethod1()
   - Execute test logic
   - Perform assertions
   ↓
4. 🧹 @AfterEach tearDown()
   - Logout if needed
   - Clean up resources
   ↓
5. 🔧 @BeforeEach setUp() (for next test)
   - Fresh environment
   - Register users again
   ↓
6. 🧪 @Test testMethod2()
   - Execute test logic
   - Perform assertions
   ↓
7. 🧹 @AfterEach tearDown()
   - Final cleanup
   ↓
8. ✅ All Tests Complete
```

### Real-World Testing Analogies

#### Restaurant Quality Control

Think of the setup and teardown like a restaurant's quality control:

**@BeforeEach (Setup) = Preparing Kitchen:**
- Clean all utensils and surfaces
- Prepare fresh ingredients
- Set up cooking equipment
- Each dish starts with a clean kitchen

**@Test = Cooking and Testing:**
- Cook the dish according to recipe
- Taste and check quality
- Verify presentation

**@AfterEach (Teardown) = Cleaning Up:**
- Wash all dishes and utensils
- Clean work surfaces
- Reset for next dish
- Kitchen ready for next chef

#### Laboratory Testing

Similar to a scientific laboratory:

**@BeforeEach = Lab Preparation:**
- Sterilize equipment
- Prepare chemicals and samples
- Calibrate instruments
- Clean workspace

**@Test = Experiment Execution:**
- Follow experimental procedure
- Collect data and measurements
- Record observations

**@AfterEach = Lab Cleanup:**
- Dispose of waste materials
- Clean and sterilize equipment
- Reset instruments
- Prepare for next experiment

### Common Setup and Teardown Patterns

#### Pattern 1: Database Testing
```java
@BeforeEach
void setUp() {
    // Start database transaction
    database.beginTransaction();

    // Insert test data
    insertTestData();
}

@AfterEach
void tearDown() {
    // Rollback all changes
    database.rollback();
}
```

#### Pattern 2: File System Testing
```java
@BeforeEach
void setUp() {
    // Create temporary directory
    testDirectory = Files.createTempDirectory("test");

    // Create test files
    createTestFiles();
}

@AfterEach
void tearDown() {
    // Delete all test files
    deleteDirectory(testDirectory);
}
```

#### Pattern 3: Network Service Testing
```java
@BeforeEach
void setUp() {
    // Start mock server
    mockServer.start();

    // Configure test endpoints
    setupMockEndpoints();
}

@AfterEach
void tearDown() {
    // Stop mock server
    mockServer.stop();
}
```

### Best Practices for Setup and Teardown

#### ✅ **Do's**

1. **Keep setup focused**
   ```java
   // Good - minimal, relevant setup
   @BeforeEach
   void setUp() {
       service = new SmartHomeService();
       registerTestUsers();
   }
   ```

2. **Always clean up resources**
   ```java
   // Good - proper cleanup
   @AfterEach
   void tearDown() {
       if (service.isLoggedIn()) {
           service.logout();
       }
       closeConnections();
   }
   ```

3. **Use meaningful names and descriptions**
   ```java
   // Good - clear purpose
   @BeforeEach
   @DisplayName("Setup device permission test environment")
   void setUp() {
   ```

#### ❌ **Don'ts**

1. **Don't create complex setup**
   ```java
   // Bad - too much complexity
   @BeforeEach
   void setUp() {
       // 50 lines of complex setup code
       // Multiple services, databases, files
       // Hard to understand and maintain
   }
   ```

2. **Don't ignore cleanup**
   ```java
   // Bad - no cleanup
   @AfterEach
   void tearDown() {
       // Empty or missing - resources leak!
   }
   ```

3. **Don't rely on test order**
   ```java
   // Bad - assumes test order
   @Test
   void test1() {
       // Creates data for test2
   }

   @Test
   void test2() {
       // Relies on data from test1
   }
   ```

The setup and teardown methods are the **foundation of reliable testing**, ensuring each test runs in a **clean, predictable environment** and properly **cleans up** afterward. This creates **isolated, repeatable tests** that produce **consistent results** every time they run.