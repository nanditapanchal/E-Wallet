E-Wallet 
📌 Project Overview

This project is a Spring Boot Microservices Application that simulates a digital wallet system. It includes services for user management, wallet handling, transactions, and notifications, all connected through a centralized API Gateway and Eureka Naming Server. A Spring Cloud Config Server is used for centralized configuration management.

📂 Folder Structure
Major Project/
│── 1-naming-server                # Eureka Service Discovery
│── 2-user-service                 # Manages user registration, login, profile
│── 3-wallet-service               # Handles wallet creation, balance, add money
│── 4-transaction-service          # Manages fund transfers between wallets
│── 5-gate-way                     # API Gateway (Zuul/Spring Cloud Gateway)
│── 6-spring-cloud-config-server   # Centralized configuration server
│── 7-wallet-notification          # Sends wallet transaction notifications

⚙️ Technologies Used

Spring Boot (Microservices)

Spring Cloud (Eureka, Config Server, Gateway)

Spring Data JPA + Hibernate

MySQL (Database per service)

REST APIs (JSON communication)

Maven (Build tool)

🔌 Service Ports (Typical Setup)
Service	Port	Description
Naming Server (Eureka)	8761	Service Registry
User Service	8081	User management APIs
Wallet Service	8082	Wallet APIs
Transaction Service	8083	Transaction APIs
Gateway	8080	Entry point for all requests
Config Server	8888	Centralized configuration
Notification Service	8084	Notification APIs

(Ports may differ based on your application.properties.)

🗄️ Database Details

Each service has its own MySQL database (per microservice database design).

User Service DB – stores user details (id, name, email, password, etc.)

Wallet Service DB – stores wallet details (walletId, userId, balance)

Transaction Service DB – stores transactions (txnId, senderWalletId, receiverWalletId, amount, timestamp)

Notification Service DB – stores notification logs

🔗 API Endpoints (Generic)
1. User Service (port 8081)

POST /users/register – Register new user

POST /users/login – User login

GET /users/{id} – Get user by ID

2. Wallet Service (port 8082)

POST /wallets/create – Create wallet for user

POST /wallets/addMoney – Add money to wallet

GET /wallets/{userId} – Get wallet balance

3. Transaction Service (port 8083)

POST /transactions/transfer – Transfer money from one wallet to another

GET /transactions/{walletId} – Get all transactions for a wallet

4. Notification Service (port 8084)

POST /notifications/send – Send notification to user

GET /notifications/{userId} – Get notification history

🚀 Startup Order

To run the project correctly, start services in this order:

Config Server (port 8888)

Eureka Naming Server (port 8761)

User Service (port 8081)

Wallet Service (port 8082)

Transaction Service (port 8083)

Notification Service (port 8084)

API Gateway (port 8080)

🛠️ How to Run

Clone the repository

Import all services into an IDE (IntelliJ/Eclipse)

Configure MySQL DB for each service (update application.properties)

Start services in order (Config → Eureka → Microservices → Gateway)

Access APIs via http://localhost:8080/
 (Gateway routes to services)
