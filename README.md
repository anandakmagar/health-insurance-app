# Health Insurance Application Using Microservices Architecture
## Overview

This project is a microservices-based health insurance application system. It allows users to check insurance quotes, accept quotes, create policies, and send notifications. The application is built using Java, Spring Boot, and follows microservices architecture principles. It leverages technologies such as MySQL and Kafka for seamless communication between services.

## Microservices

1. **quote-service:**
    - Check insurance quotes
    - Accept insurance quotes
    - Find insurance quotes by ID

2. **api-gateway:**
    - Route incoming requests to specific microservices based on URLs

3. **service-registry:**
    - Register and discover microservices

4. **security-service:**
    - User authentication and authorization using JWT tokens
    - Method-level security for roles (ADMIN, CUSTOMER)
    - Password reset and change functionality

5. **policy-service:**
    - Create insurance policies
    - Acceptance of quotes restricted to ADMIN

6. **notification-service:**
    - Send notifications, such as email, when a quote is accepted

## Technology Stack

- Java
- Spring Boot
- Microservices Architecture
- MySQL Database
- Kafka for asynchronous communication
- Spring Security with JWT for authentication and authorization

## Key Features

- Role-based access control (ADMIN and CUSTOMER)
- Seamless communication between microservices using Kafka
- Secure user authentication and authorization with JWT tokens
- Automatic policy creation and user registration upon quote acceptance
- Password reset and change functionality
- Efficient routing of requests using API Gateway

## API Endpoints

- **Check the quote:** `POST` (Anyone) - `localhost:8092/quote-service/api/quote/check`
- **Accept the quote:** `POST` (ADMIN) - `localhost:8092/security-service/api/secured/accept/{id}`
- **Get quote by ID:** `GET` (ADMIN) - `localhost:8092/security-service/api/secured/{id}`
- **Add user:** `POST` (ADMIN) - `localhost:8092/security-service/api/auth/register`
- **Authenticate user:** `POST` (Anyone) - `localhost:8092/security-service/api/auth/authenticate`
- **Password reset code:** `POST` (Anyone) - `localhost:8092/security-service/api/auth/password-reset/{username}`
- **Change password:** `POST` (Anyone) - `localhost:8092/security-service/api/auth/password-change/514930644/{username}/{newPassword}`

## Docker File for Kafka
- Command to run: docker compose up -d
- Check whether Kafka broker and Zookeeper running: docker ps