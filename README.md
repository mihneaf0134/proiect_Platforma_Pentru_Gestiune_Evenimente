# Events Management Platform

> A microservices-based event ticketing system built with **Spring Boot** and **gRPC**.

---

## Tools used

- **Backend Framework:** Spring Boot  
- **Inter-Service Communication:** gRPC  
- **External APIs:** REST  
- **Databases:**  
  - SQL (Events, Users)  
  - NoSQL (Clients)  
- **API Documentation:** OpenAPI / Swagger  

---

## Architecture

The system follows a **service-oriented architecture**, where each core domain is isolated into its own service.

### Authentication & Authorization Service
**Spring Boot + gRPC**

- Handles user authentication
- Implements role-based access control (Admin, Manager, Client)
- Stores user data in a SQL database
- Secures communication between services

---

### Event Service
**Spring Boot**

- Manages events
- Manages event packages (collections of events)
- Controls ticket availability
- Enforces ticket business rules
- Uses a SQL database
- Exposes REST endpoints
- Communicates with other services via gRPC

---

### Client Service
**Spring Boot**

- Manages client personal data
- Stores purchased tickets
- Uses a NoSQL database
- Communicates with Event Service via gRPC
- Validates ticket availability before purchase

---

## Core Functionalities

### Event & Package Management
- Create and configure artistic events
- Define ticket limits
- Create event packages
- Assign event owners at creation time

### Ticket Purchasing
- Clients can purchase multiple tickets
- Prevents overselling
- Blocks ticket limit modification after first sale
- Ensures packages respect event capacity constraints

### Role-Based Access Control
- Accounts created by administrator
- Managers manage only their own events
- Clients can purchase tickets

---

## Business Rules

- Tickets sold **cannot exceed** available capacity
- A package’s ticket limit cannot exceed the minimum availability of its events
- Tickets cannot be purchased if no seat limit is defined
- Ticket availability cannot be modified after at least one ticket is sold
- Event ownership is enforced at creation

---

## Project Objectives

This project demonstrates:

- Microservices architecture with **Spring Boot**
- Secure inter-service communication using **gRPC**
- REST + gRPC integration
- Hybrid persistence (SQL + NoSQL)
- Role-based authorization
- Strict business constraint enforcement

---

## Running the Project

### Prerequisites

- Docker
- Docker Compose

---

### Start the Environment

To start the entire system (all microservices + databases), run:

```bash
docker compose up --build
```

This will:

- Build all Spring Boot services
- Start the SQL and NoSQL databases
- Launch the gRPC-based authentication service
- Start all backend microservices

---

### Access the APIs (Swagger)

Once the containers are running, you can access the REST APIs using Swagger UI.

Open your browser and navigate to:

```
http://localhost:<service-port>/swagger-ui.html
```

(or)

```
http://localhost:<service-port>/swagger-ui/index.html
```


