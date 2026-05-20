# InfraLink API

InfraLink is a Help Desk system built with Java and Spring Boot that focuses on ticket management and real-time communication between clients and attendants.

The main goal of the project is to simulate a real support environment where users can create support tickets, enter service queues, and communicate with attendants through a real-time chat using WebSocket.

## Project Idea

The system works with different ticket categories:

- Doubt
- Instability
- No Service

When a client creates a ticket, the system places it into a queue based on the selected category.

Attendants can call the next ticket according to queue priority. Once a ticket is attended:

- The ticket status changes from `PENDING` to `IN_PROGRESS`
- A real-time chat session is opened between client and attendant
- Messages are exchanged using WebSocket

When the support session ends:

- The attendant finishes the ticket
- The ticket status changes to `FINISHED`
- The chat session is closed

---

# Technologies Used

- Java
- Spring Boot
- Spring Security
- WebSocket
- REST API
- Docker
- MySQL
- JWT Authentication

---

# Running the Project

## Start Docker

Run the following command to start the MySQL container:

```bash
docker compose up -d
```

---

# Application Properties Configuration

Configure your `application.properties` file:

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/infraLink_dataBase
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret=your_secret_password_jwt
```

---

# Authentication

The project uses JWT authentication with Spring Security.

Some routes are public, while others require authentication and specific roles.

Roles used in the system:

- ADMIN
- ATTEND
- CLIENT

---

# REST Endpoints

## User Routes

### Register User

```http
POST /register
```

Public route used to create a new account.

---

### Login

```http
POST /login
```

Public route used to authenticate the user and receive a JWT token.

---

# Ticket Routes

### Get All Tickets

```http
GET /ticket
```

Access:
- ATTEND
- ADMIN

Returns all active tickets.

---

### Attend Next Ticket

```http
POST /ticket/attend
```

Access:
- ATTEND
- ADMIN

Calls the next ticket from the queue based on priority.

---

### Create Ticket

```http
POST /ticket/create
```

Requires authentication.

Creates a new support ticket.

---

### Finish Ticket

```http
PATCH /ticket/finish/{ticketId}
```

Access:
- ATTEND
- ADMIN

Finishes a ticket and closes the support session.

---

# WebSocket

## Send Chat Message

```http
@MessageMapping("/chat.send")
```

Public WebSocket route used to send real-time messages between client and attendant.

---

# Main Features

- JWT Authentication
- Role-based authorization
- Ticket queue management
- Real-time chat with WebSocket
- Ticket status flow
- Help Desk support simulation

---

# Ticket Status Flow

```text
PENDING -> IN_PROGRESS -> FINISHED
```

---

# Thanks for everyone ❤
