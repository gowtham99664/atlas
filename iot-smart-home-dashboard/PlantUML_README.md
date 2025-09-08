# IoT Smart Home Dashboard - PlantUML Diagrams

This directory contains comprehensive PlantUML diagrams for the IoT Smart Home Dashboard project. These diagrams provide visual documentation of the system architecture, data models, and key processes.

## 📋 Diagram Files

### 1. `architecture.puml` - System Architecture & Class Diagram
**Description**: Complete class diagram showing all components, their relationships, and the layered architecture.

**Key Features**:
- **Main Application Layer**: `SmartHomeDashboard` console interface
- **Service Layer**: All 8 business services with their methods
- **Model Layer**: `Customer` and `Gadget` entities with full attributes
- **Utility Layer**: Session management, database config, password utilities
- **v2.1 Enhancements**: Auto-aligned table system with helper classes

**Use Case**: Understanding the overall system structure and class relationships.

### 2. `sequence-diagrams.puml` - Process Flow Sequences
**Description**: Detailed sequence diagrams for key user workflows and system processes.

**Included Sequences**:
- **User Registration Flow**: Complete registration process with validation
- **Device Management Flow**: Adding and managing smart devices
- **Auto-Aligned Table Display Flow** (v2.1): Enhanced table rendering process
- **Smart Scene Execution Flow**: One-click automation scenarios
- **Timer Management Flow**: Device scheduling with countdown
- **Health Monitoring Flow**: Device health analysis and reporting

**Use Case**: Understanding how different components interact during specific operations.

### 3. `component-diagram.puml` - Component Architecture
**Description**: High-level component view using C4 model style showing system boundaries and relationships.

**Key Components**:
- Console Interface with number-based selection
- Core Service Layer with auto-aligned table system (v2.1)
- 8 Specialized Services (Customer, Gadget, Timer, Health, Scenes, Energy, Calendar, Weather)
- Data Models with security features
- Utility Layer with session management
- External Dependencies (DynamoDB, BCrypt, AWS SDK)

**Use Case**: System overview for architects and stakeholders.

### 4. `use-case-diagram.puml` - Use Case Model
**Description**: Complete use case diagram showing all system functionality and actor interactions.

**Use Case Categories**:
- **User Management**: Registration, login, password reset
- **Device Management**: Add/view/control devices with auto-aligned display
- **Automation & Scheduling**: Timers, scenes, calendar events
- **Monitoring & Analytics**: Health monitoring, energy analytics
- **Smart Features**: Weather suggestions, energy optimization
- **System Operations**: Database management, demo mode, security

**Use Case**: Requirements documentation and user story mapping.

### 5. `data-model.puml` - Data Model & Entity Relationships
**Description**: Detailed entity-relationship diagram showing data structures and business rules.

**Key Entities**:
- **Customer**: User entity with security features (lockout, failed attempts)
- **Gadget**: Smart device with usage tracking and timer support
- **Service Objects**: Health reports, energy analytics, scene results
- **Helper Classes**: TableDimensions and TableFormatStrings (v2.1)
- **Database Schema**: DynamoDB table structure and validation rules

**Use Case**: Database design and data structure understanding.

## 🚀 How to Use These Diagrams

### Prerequisites
1. **PlantUML**: Install PlantUML or use online viewer
   - Online: [http://www.plantuml.com/plantuml/](http://www.plantuml.com/plantuml/)
   - Local: Download PlantUML JAR file
   - IDE Plugins: Available for IntelliJ, VSCode, Eclipse

2. **Java**: Required for local PlantUML execution
   - Java 8+ recommended
   - Graphviz optional (for better layout)

### Viewing Diagrams Online
1. Copy the content of any `.puml` file
2. Go to [PlantUML Online Server](http://www.plantuml.com/plantuml/)
3. Paste the content and click "Submit"
4. View the generated diagram

### Generating Diagrams Locally
```bash
# Using PlantUML JAR
java -jar plantuml.jar architecture.puml

# Generate all diagrams
java -jar plantuml.jar *.puml

# Generate specific format (PNG, SVG, PDF)
java -jar plantuml.jar -tpng architecture.puml
java -jar plantuml.jar -tsvg sequence-diagrams.puml
```

### IDE Integration
- **IntelliJ IDEA**: Install "PlantUML integration" plugin
- **VSCode**: Install "PlantUML" extension by jebbs
- **Eclipse**: Install PlantUML plugin from marketplace

## 📊 Diagram Content Overview

### Architecture Highlights (v2.1)
- **Auto-Aligned Table System**: Enhanced with `TableDimensions` and `TableFormatStrings` helper classes
- **Modular Service Layer**: 8 specialized services for different functionality areas
- **Security Features**: BCrypt hashing, progressive lockout, input validation
- **Indian Market Focus**: 350+ device models, 38+ room types
- **Real-time Features**: Usage tracking, health monitoring, energy analytics

### Key Relationships
- **Customer → Gadget**: One-to-many ownership
- **Services → Models**: Business logic operates on entities
- **Session Manager**: Singleton pattern for user state
- **Database Config**: Connection management with demo mode fallback

### Design Patterns Illustrated
- **Singleton**: SessionManager for global state
- **Service Layer**: Business logic separation
- **Factory**: GadgetService for device creation
- **Observer**: Timer execution and health monitoring
- **Strategy**: Different scene execution strategies

## 🔄 Version History

### v2.1 (September 2025) - Current
- Added auto-aligned table system components
- Enhanced sequence diagrams with v2.1 table flow
- Updated component relationships
- Added helper class documentation

### v2.0 (September 2025)
- Added comprehensive service layer diagrams
- Included all 8 specialized services
- Timer, health, energy, and weather features
- Smart scenes and calendar integration

### v1.0 (Initial)
- Basic architecture and core components
- Customer and device management
- Authentication and security features

## 🎯 Diagram Usage Scenarios

### For Developers
- **architecture.puml**: Understand class structure before coding
- **sequence-diagrams.puml**: Follow process flows during implementation
- **data-model.puml**: Design database interactions

### For Architects
- **component-diagram.puml**: System overview and technology decisions
- **architecture.puml**: Layer separation and dependency management
- **use-case-diagram.puml**: Feature scope and requirements validation

### For Stakeholders
- **use-case-diagram.puml**: Feature overview and user capabilities
- **component-diagram.puml**: High-level system understanding
- **sequence-diagrams.puml**: Key process flows and user journeys

### For Documentation
- All diagrams provide visual documentation
- Include in technical specifications
- Use for onboarding new team members
- Reference during code reviews

## 🔧 Customizing Diagrams

### Themes and Styling
```plantuml
!theme plain
!theme bluegray
!theme amiga
```

### C4 Model Integration
```plantuml
!include <C4/C4_Component>
!include <C4/C4_Container>
```

### Color Customization
```plantuml
skinparam class {
    BackgroundColor LightBlue
    BorderColor DarkBlue
}
```

## 📞 Support

- **PlantUML Documentation**: [https://plantuml.com/](https://plantuml.com/)
- **PlantUML Syntax**: [https://plantuml.com/class-diagram](https://plantuml.com/class-diagram)
- **Project Documentation**: See `DEVELOPER_GUIDE.md` and `IoT_Smart_Home_Dashboard_User_Guide.md`

## 📝 Notes

- Diagrams are updated to reflect v2.1 optimizations
- Auto-aligned table system (v2.1) is highlighted in relevant diagrams
- All diagrams are consistent with actual codebase structure
- Business rules and validation constraints are documented
- Security features and Indian market focus are emphasized

---

**Last Updated**: September 2025 (v2.1)
**Generated for**: IoT Smart Home Dashboard Project
**Prepared by**: Sushma Mainampati
**Compatibility**: PlantUML 1.2020.0+