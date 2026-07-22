# Real-Time Messaging System with Spring Boot & WebSockets

A real-time messaging system focused on high concurrency, dynamic room management, and efficient session handling using WebSockets (STOMP).

## Key Features

* **Real-time messaging:** Bidirectional communication using SockJS and STOMP over WebSockets.
* **Dynamic room management:** Real-time room creation and joining.
* **Defensive Concurrency (Thread-Safe):** Active user and session tracking using ConcurrentHashMap with atomic operations (putIfAbsent, computeIfPresent) to prevent race conditions.
* **Solid RESTful API:** Structured endpoints with semantic HTTP responses.
* **Interactive Interface:** Responsive and dynamic UI built with HTML5, CSS3, and vanilla JavaScript.

---

## Tech Stack

* **Backend:** Java 25, Spring Boot (WebSocket, STOMP, REST API), Lombok, SLF4J / Logback.
* **Frontend:** JavaScript (SockJS-client, StompJS), HTML5, CSS3.
* **Tools:** Git, Maven.

---

## Architecture & Concurrency Decisions

To ensure server stability under concurrent requests when multiple users join or leave rooms simultaneously:

1. **Atomic Operations:** Multi-step read/write patterns (containsKey + put) were replaced with native ConcurrentHashMap methods like putIfAbsent and computeIfPresent.
2. **Automatic Room Cleanup:** Room user counts decrease atomically. Upon reaching 0, the entry is automatically removed from memory without leaving null pointers or throwing unhandled exceptions during unexpected disconnections.

---

## How to Run Locally

### Prerequisites
* Java 25 installed.
* Maven 3.9 + intalled.

### Steps

1. Clone the repository:
   git clone https://github.com/IrvingVC1521/Spring-Boot-Messaging-System.git

2. Navigate to the project directory:
   cd Spring-Boot-Messaging-System

3. Run the application with Maven:
   mvn spring-boot:run

4. Open your browser and go to:
   http://localhost:8080
