# WaySphere - Indoor Navigation Backend Service

A Spring Boot backend service for indoor navigation and floor plan management with geospatial features.

## Overview

WaySphere provides REST APIs for managing indoor floor plans, navigation nodes, and pathfinding across multiple floors. It supports multi-tenant organizations with email-based OTP authentication.

## Tech Stack

- **Framework**: Spring Boot 3.4.3
- **Database**: PostgreSQL with PostGIS extension
- **Java**: 17
- **Build Tool**: Maven
- **Key Libraries**:
  - JTS (Java Topology Suite) for geospatial operations
  - Spring Data JPA
  - Spring Security
  - Spring Mail

## Features

- 📍 **Geospatial Floor Features**: Store and retrieve GeoJSON floor plan features
- 🗺️ **Navigation Graph**: Create navigation nodes (POI and PATH points) with connections
- 🧭 **Pathfinding**: BFS-based pathfinding across single and multiple floors
- 🏢 **Multi-tenant**: Organization-based data isolation
- 🔐 **OTP Authentication**: Email-based verification for organization access
- 📧 **Email Service**: Automated OTP delivery via SMTP

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL 12+ with PostGIS extension
- Maven 3.6+

### Installation

1. Clone the repository
2. Configure PostgreSQL database with PostGIS:
   ```sql
   CREATE EXTENSION postgis;
   ```

3. Update database credentials in `application.properties`

4. Build the project:
   ```bash
   ./mvnw clean install
   ```

5. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## API Endpoints

### Organizations
- `POST /organizations` - Create organization
- `GET /organizations` - List organizations

### Floor Maps
- `POST /floors` - Create floor map
- `GET /floors/{orgId}` - Get floors by organization

### Floor Features (GeoJSON)
- `POST /floor-features/{orgId}/{floorId}` - Upload GeoJSON features
- `GET /floor-features/{orgId}/{floorId}` - Retrieve features as GeoJSON
- `PUT /floor-features/{orgId}/{floorId}/{featureId}` - Update feature
- `DELETE /floor-features/{orgId}/{floorId}` - Delete all floor features
- `DELETE /floor-features/{orgId}/{floorId}/{featureId}` - Delete specific feature

### Navigation Nodes
- `POST /navigation-nodes` - Create navigation node
- `GET /navigation-nodes` - Get all nodes
- `GET /navigation-nodes/{id}` - Get node by ID
- `GET /navigation-nodes/floor/poi?floor={floorId}` - Get POI nodes for floor
- `GET /navigation-nodes/org/poi?orgId={orgId}` - Get all POI nodes for organization
- `POST /navigation-nodes/connect` - Connect two nodes

### Pathfinding
- `GET /navigation-nodes/path?from={nodeId}&to={nodeId}` - Find path between nodes
- `GET /navigation-nodes/pathByLocation?lat={lat}&lng={lng}&level={level}&to={nodeId}` - Find path from GPS location

### Authentication
- `POST /api/send-otp?email={email}` - Request OTP
- `POST /api/verify-otp?email={email}&otp={code}` - Verify OTP and get organization

### Admin
- `POST /addPerson/{orgId}` - Add admin user
- `POST /admin/login` - Admin login

## Data Models

### Organization
Multi-tenant organizations with unique domain names

### FloorMap
Links to organization and stores floor level information

### DigitalFloorFeature
GeoJSON features stored as PostGIS geometry with JSONB properties

### NavigationNode
Two types: `POI` (Points of Interest) and `PATH` (pathfinding waypoints)
- Support for same-floor and cross-floor connections
- Exit nodes for inter-floor navigation

## Project Structure

```
src/main/java/com/waysphere/odata/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/             # Data transfer objects
├── model/           # JPA entities
├── repository/      # Spring Data repositories
├── service/         # Business logic
└── utils/           # Utility classes
```

## License

This project is licensed under the Apache License 2.0.
