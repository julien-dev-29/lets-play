# Spring Boot CRUD API Design

## Overview

A RESTful CRUD API built with Spring Boot and MongoDB for User and Product management, featuring JWT-based authentication and role-based authorization.

## Data Models

### User
| Field | Type | Constraints |
|-------|------|-------------|
| id | String | Auto-generated MongoID |
| username | String | Unique |
| email | String | Unique |
| password | String | BCrypt-hashed |
| roles | List\<Role\> | Enum: ADMIN, USER |

### Product
| Field | Type | Constraints |
|-------|------|-------------|
| id | String | Auto-generated MongoID |
| name | String | Required |
| description | String | Optional |
| price | Double | Required, positive |
| owner | String | References User id |

### Role (Enum)
- `ADMIN`
- `USER`

## API Endpoints

### Authentication
| Method | Endpoint | Auth Required | Description |
|--------|----------|---------------|-------------|
| POST | `/api/auth/register` | No | Register a new user |
| POST | `/api/auth/login` | No | Login and receive JWT |

### Users (admin only)
| Method | Endpoint | Auth Required | Roles | Description |
|--------|----------|---------------|-------|-------------|
| GET | `/api/users` | Yes | ADMIN | List all users |
| GET | `/api/users/{id}` | Yes | ADMIN | Get user by ID |
| PUT | `/api/users/{id}` | Yes | ADMIN | Update user |
| DELETE | `/api/users/{id}` | Yes | ADMIN | Delete user |

### Products
| Method | Endpoint | Auth Required | Roles | Description |
|--------|----------|---------------|-------|-------------|
| GET | `/api/products` | No | Public | List all products |
| GET | `/api/products/{id}` | No | Public | Get product by ID |
| POST | `/api/products` | Yes | ADMIN, USER | Create product |
| PUT | `/api/products/{id}` | Yes | ADMIN, or owner USER | Update product |
| DELETE | `/api/products/{id}` | Yes | ADMIN, or owner USER | Delete product |

## Authentication & Security

### JWT Authentication
- Login returns a JWT access token
- Clients send token in `Authorization: Bearer <token>` header
- Spring Security filter chain validates JWT on every request
- Token contains user ID, username, and roles

### Password Security
- Passwords hashed with BCrypt (+ salt)
- `@JsonIgnore` on password field prevents leakage in API responses
- Passwords excluded from all response DTOs

### Authorization
- `@PreAuthorize` annotations on service methods for role/ownership checks
- Admin users can manage all resources
- Regular users can only manage their own products

### Input Validation
- Bean Validation annotations (`@NotBlank`, `@Email`, `@Positive`, etc.)
- Prevents MongoDB injection through proper input sanitization
- Custom validation error responses

## Error Handling

Global `@RestControllerAdvice` with `@ExceptionHandler` methods:

| HTTP Code | Scenario |
|-----------|----------|
| 400 | Bad request / validation errors |
| 401 | Unauthorized / invalid token |
| 403 | Forbidden / insufficient permissions |
| 404 | Resource not found |
| 409 | Conflict (e.g., duplicate username/email) |
| 422 | Unprocessable entity |

No 5XX errors escape the application. All exceptions are caught and return appropriate HTTP responses with JSON error messages.

## Project Structure

```
src/main/java/com/letsplay/
├── LetsPlayApplication.java
├── config/
│   ├── SecurityConfig.java
│   ├── JwtFilter.java
│   └── WebConfig.java
├── controller/
│   ├── AuthController.java
│   ├── UserController.java
│   └── ProductController.java
├── dto/
│   ├── UserRequest.java
│   ├── UserResponse.java
│   ├── ProductRequest.java
│   └── ProductResponse.java
├── model/
│   ├── User.java
│   ├── Product.java
│   └── Role.java
├── repository/
│   ├── UserRepository.java
│   └── ProductRepository.java
├── service/
│   ├── AuthService.java
│   ├── UserService.java
│   ├── ProductService.java
│   └── JwtService.java
└── exception/
    ├── GlobalExceptionHandler.java
    ├── ResourceNotFoundException.java
    └── UnauthorizedException.java
```

## Bonus Features

### CORS Configuration
- Configurable via Spring's `WebMvcConfigurer`
- Allow specific origins, methods, and headers

### Rate Limiting
- Implemented with Bucket4j or a custom filter
- Prevents brute force attacks on authentication endpoints

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data MongoDB
- Spring Boot Starter Security
- Spring Boot Starter Validation
- jjwt (io.jsonwebtoken) for JWT
- Lombok (optional, for reducing boilerplate)
- Bucket4j (for rate limiting, if implemented)
