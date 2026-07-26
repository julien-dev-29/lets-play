# Spring Boot CRUD API Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a RESTful CRUD API with Spring Boot and MongoDB for User and Product management, featuring JWT authentication and role-based authorization.

**Architecture:** Layered architecture (Controller → Service → Repository) with Spring Security filter chain for JWT validation. Global exception handler for consistent error responses. Bean Validation for input sanitization.

**Tech Stack:** Java 17+, Spring Boot 3.x, Spring Data MongoDB, Spring Security, jjwt, Bucket4j, Maven

## Global Constraints

- Java 17 or higher
- Spring Boot 3.x
- MongoDB running locally on default port 27017
- No 5XX errors escape the application
- Passwords must be BCrypt-hashed
- JWT tokens in `Authorization: Bearer <token>` header
- GET `/api/products` accessible without authentication
- All other endpoints require authentication unless noted

---

## File Structure

```
pom.xml                                          # Maven dependencies
src/main/resources/application.properties        # App config (Mongo, JWT secret, etc.)
src/main/java/com/letsplay/LetsPlayApplication.java

src/main/java/com/letsplay/model/Role.java
src/main/java/com/letsplay/model/User.java
src/main/java/com/letsplay/model/Product.java

src/main/java/com/letsplay/repository/UserRepository.java
src/main/java/com/letsplay/repository/ProductRepository.java

src/main/java/com/letsplay/dto/UserRequest.java
src/main/java/com/letsplay/dto/UserResponse.java
src/main/java/com/letsplay/dto/ProductRequest.java
src/main/java/com/letsplay/dto/ProductResponse.java

src/main/java/com/letsplay/service/JwtService.java
src/main/java/com/letsplay/service/AuthService.java
src/main/java/com/letsplay/service/UserService.java
src/main/java/com/letsplay/service/ProductService.java

src/main/java/com/letsplay/config/JwtFilter.java
src/main/java/com/letsplay/config/SecurityConfig.java
src/main/java/com/letsplay/config/WebConfig.java

src/main/java/com/letsplay/controller/AuthController.java
src/main/java/com/letsplay/controller/UserController.java
src/main/java/com/letsplay/controller/ProductController.java

src/main/java/com/letsplay/exception/ResourceNotFoundException.java
src/main/java/com/letsplay/exception/UnauthorizedException.java
src/main/java/com/letsplay/exception/GlobalExceptionHandler.java

src/test/java/com/letsplay/LetsPlayApplicationTests.java
```

---

## Task 1: Project Setup

**Files:**
- Create: `pom.xml`
- Create: `src/main/resources/application.properties`
- Create: `src/main/java/com/letsplay/LetsPlayApplication.java`

**Interfaces:**
- Consumes: None
- Produces: Spring Boot application that starts and connects to MongoDB

- [ ] **Step 1: Create pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version>
        <relativePath/>
    </parent>

    <groupId>com.letsplay</groupId>
    <artifactId>lets-play</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>lets-play</name>
    <description>CRUD API with Spring Boot and MongoDB</description>

    <properties>
        <java.version>17</java.version>
        <jjwt.version>0.12.5</jjwt.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jjwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>com.bucket4j</groupId>
            <artifactId>bucket4j-core</artifactId>
            <version>8.10.1</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 2: Create application.properties**

```properties
# Server
server.port=8080

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/letsplay

# JWT
jwt.secret=mySecretKeyThatIsLongEnoughForHS256Algorithm1234567890
jwt.expiration=86400000

# Logging
logging.level.com.letsplay=DEBUG
```

- [ ] **Step 3: Create LetsPlayApplication.java**

```java
package com.letsplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LetsPlayApplication {
    public static void main(String[] args) {
        SpringApplication.run(LetsPlayApplication.class, args);
    }
}
```

- [ ] **Step 4: Create test class**

```java
package com.letsplay;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LetsPlayApplicationTests {
    @Test
    void contextLoads() {
    }
}
```

- [ ] **Step 5: Commit**

```bash
git add pom.xml src/
git commit -m "feat: project setup with Spring Boot, MongoDB, Security, JWT"
```

---

## Task 2: Models and Enums

**Files:**
- Create: `src/main/java/com/letsplay/model/Role.java`
- Create: `src/main/java/com/letsplay/model/User.java`
- Create: `src/main/java/com/letsplay/model/Product.java`

**Interfaces:**
- Consumes: None
- Produces: `Role` enum, `User` and `Product` document classes used by repositories

- [ ] **Step 1: Create Role enum**

```java
package com.letsplay.model;

public enum Role {
    ADMIN,
    USER
}
```

- [ ] **Step 2: Create User model**

```java
package com.letsplay.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank(message = "Username is required")
    @Indexed(unique = true)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private List<Role> roles;

    public User() {}

    public User(String username, String email, String password, List<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Role> getRoles() { return roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }
}
```

- [ ] **Step 3: Create Product model**

```java
package com.letsplay.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @Positive(message = "Price must be positive")
    private double price;

    private String owner;

    public Product() {}

    public Product(String name, String description, double price, String owner) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.owner = owner;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}
```

- [ ] **Step 4: Commit**

```bash
git add src/main/java/com/letsplay/model/
git commit -m "feat: add User, Product models and Role enum"
```

---

## Task 3: Repositories

**Files:**
- Create: `src/main/java/com/letsplay/repository/UserRepository.java`
- Create: `src/main/java/com/letsplay/repository/ProductRepository.java`

**Interfaces:**
- Consumes: `User`, `Product` models
- Produces: `UserRepository.findByUsername()`, `UserRepository.findByEmail()`, `ProductRepository.findByOwner()`

- [ ] **Step 1: Create UserRepository**

```java
package com.letsplay.repository;

import com.letsplay.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
```

- [ ] **Step 2: Create ProductRepository**

```java
package com.letsplay.repository;

import com.letsplay.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByOwner(String ownerId);
}
```

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/letsplay/repository/
git commit -m "feat: add UserRepository and ProductRepository"
```

---

## Task 4: DTOs

**Files:**
- Create: `src/main/java/com/letsplay/dto/UserRequest.java`
- Create: `src/main/java/com/letsplay/dto/UserResponse.java`
- Create: `src/main/java/com/letsplay/dto/ProductRequest.java`
- Create: `src/main/java/com/letsplay/dto/ProductResponse.java`

**Interfaces:**
- Consumes: `User`, `Product`, `Role`
- Produces: Request/Response DTOs used by controllers and services

- [ ] **Step 1: Create UserRequest**

```java
package com.letsplay.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class UserRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private List<String> roles;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
```

- [ ] **Step 2: Create UserResponse**

```java
package com.letsplay.dto;

import com.letsplay.model.Role;
import java.util.List;

public class UserResponse {

    private String id;
    private String username;
    private String email;
    private List<Role> roles;

    public UserResponse() {}

    public UserResponse(String id, String username, String email, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Role> getRoles() { return roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }
}
```

- [ ] **Step 3: Create ProductRequest**

```java
package com.letsplay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ProductRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @Positive(message = "Price must be positive")
    private double price;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
```

- [ ] **Step 4: Create ProductResponse**

```java
package com.letsplay.dto;

public class ProductResponse {

    private String id;
    private String name;
    private String description;
    private double price;
    private String owner;

    public ProductResponse() {}

    public ProductResponse(String id, String name, String description, double price, String owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.owner = owner;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}
```

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/letsplay/dto/
git commit -m "feat: add User and Product DTOs"
```

---

## Task 5: JWT Service

**Files:**
- Create: `src/main/java/com/letsplay/service/JwtService.java`

**Interfaces:**
- Consumes: `User` model, `Role` enum, `application.properties` (jwt.secret, jwt.expiration)
- Produces: `JwtService.generateToken(String username, List<Role> roles)`, `JwtService.extractUsername(String token)`, `JwtService.isTokenValid(String token)`

- [ ] **Step 1: Create JwtService**

```java
package com.letsplay.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add src/main/java/com/letsplay/service/JwtService.java
git commit -m "feat: add JWT service for token generation and validation"
```

---

## Task 6: Auth Service

**Files:**
- Create: `src/main/java/com/letsplay/service/AuthService.java`

**Interfaces:**
- Consumes: `UserRepository`, `JwtService`, `PasswordEncoder`
- Produces: `AuthService.register(UserRequest)`, `AuthService.login(String username, String password)`

- [ ] **Step 1: Create AuthService**

```java
package com.letsplay.service;

import com.letsplay.dto.UserRequest;
import com.letsplay.dto.UserResponse;
import com.letsplay.model.Role;
import com.letsplay.model.User;
import com.letsplay.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        List<Role> roles = List.of(Role.USER);

        User user = new User(
            request.getUsername(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            roles
        );

        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getRoles());
    }

    public Map<String, String> login(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername(), 
            user.getRoles().stream().map(Enum::name).toList());

        return Map.of("token", token);
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add src/main/java/com/letsplay/service/AuthService.java
git commit -m "feat: add AuthService for registration and login"
```

---

## Task 7: User Service

**Files:**
- Create: `src/main/java/com/letsplay/service/UserService.java`

**Interfaces:**
- Consumes: `UserRepository`
- Produces: `UserService.getAll()`, `UserService.getById(String)`, `UserService.update(String, UserRequest)`, `UserService.delete(String)`

- [ ] **Step 1: Create UserService**

```java
package com.letsplay.service;

import com.letsplay.dto.UserRequest;
import com.letsplay.dto.UserResponse;
import com.letsplay.model.Role;
import com.letsplay.model.User;
import com.letsplay.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
            .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getRoles()))
            .toList();
    }

    public UserResponse getById(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles());
    }

    public UserResponse update(String id, UserRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getEmail() != null) user.setEmail(request.getEmail());

        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getRoles());
    }

    public void delete(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add src/main/java/com/letsplay/service/UserService.java
git commit -m "feat: add UserService for admin user management"
```

---

## Task 8: Product Service

**Files:**
- Create: `src/main/java/com/letsplay/service/ProductService.java`

**Interfaces:**
- Consumes: `ProductRepository`
- Produces: `ProductService.getAll()`, `ProductService.getById(String)`, `ProductService.create(ProductRequest, String)`, `ProductService.update(String, ProductRequest)`, `ProductService.delete(String)`, `ProductService.getByOwnerId(String)`

- [ ] **Step 1: Create ProductService**

```java
package com.letsplay.service;

import com.letsplay.dto.ProductRequest;
import com.letsplay.dto.ProductResponse;
import com.letsplay.model.Product;
import com.letsplay.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
            .map(p -> new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getOwner()))
            .toList();
    }

    public ProductResponse getById(String id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getOwner());
    }

    public ProductResponse create(ProductRequest request, String ownerId) {
        Product product = new Product(request.getName(), request.getDescription(), request.getPrice(), ownerId);
        Product saved = productRepository.save(product);
        return new ProductResponse(saved.getId(), saved.getName(), saved.getDescription(), saved.getPrice(), saved.getOwner());
    }

    public ProductResponse update(String id, ProductRequest request, String ownerId) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getOwner().equals(ownerId)) {
            throw new RuntimeException("Not authorized to update this product");
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        Product saved = productRepository.save(product);
        return new ProductResponse(saved.getId(), saved.getName(), saved.getDescription(), saved.getPrice(), saved.getOwner());
    }

    public void delete(String id, String ownerId) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getOwner().equals(ownerId)) {
            throw new RuntimeException("Not authorized to delete this product");
        }

        productRepository.deleteById(id);
    }

    public List<ProductResponse> getByOwnerId(String ownerId) {
        return productRepository.findByOwner(ownerId).stream()
            .map(p -> new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getOwner()))
            .toList();
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add src/main/java/com/letsplay/service/ProductService.java
git commit -m "feat: add ProductService with ownership validation"
```

---

## Task 9: Exception Handling

**Files:**
- Create: `src/main/java/com/letsplay/exception/ResourceNotFoundException.java`
- Create: `src/main/java/com/letsplay/exception/UnauthorizedException.java`
- Create: `src/main/java/com/letsplay/exception/GlobalExceptionHandler.java`

**Interfaces:**
- Consumes: All services (for exception types)
- Produces: `GlobalExceptionHandler` that catches exceptions and returns proper HTTP responses

- [ ] **Step 1: Create ResourceNotFoundException**

```java
package com.letsplay.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

- [ ] **Step 2: Create UnauthorizedException**

```java
package com.letsplay.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
```

- [ ] **Step 3: Create GlobalExceptionHandler**

```java
package com.letsplay.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Access denied");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
```

- [ ] **Step 4: Update services to use custom exceptions**

Update `AuthService.java`:
```java
// Change RuntimeException to UnauthorizedException for invalid credentials
// Change RuntimeException to RuntimeException with specific messages for duplicates
```

Update `UserService.java`:
```java
// Change RuntimeException("User not found") to ResourceNotFoundException("User not found")
```

Update `ProductService.java`:
```java
// Change RuntimeException("Product not found") to ResourceNotFoundException("Product not found")
// Change RuntimeException("Not authorized...") to UnauthorizedException("Not authorized...")
```

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/letsplay/exception/ src/main/java/com/letsplay/service/
git commit -m "feat: add global exception handler and custom exceptions"
```

---

## Task 10: JWT Filter

**Files:**
- Create: `src/main/java/com/letsplay/config/JwtFilter.java`

**Interfaces:**
- Consumes: `JwtService`, `UserRepository`
- Produces: `JwtFilter` (OncePerRequestFilter) that validates JWT and sets SecurityContext

- [ ] **Step 1: Create JwtFilter**

```java
package com.letsplay.config;

import com.letsplay.model.User;
import com.letsplay.repository.UserRepository;
import com.letsplay.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();

            var auth = new UsernamePasswordAuthenticationToken(user.getId(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add src/main/java/com/letsplay/config/JwtFilter.java
git commit -m "feat: add JWT filter for request authentication"
```

---

## Task 11: Security Config

**Files:**
- Create: `src/main/java/com/letsplay/config/SecurityConfig.java`

**Interfaces:**
- Consumes: `JwtFilter`
- Produces: `SecurityFilterChain` that configures endpoint access rules

- [ ] **Step 1: Create SecurityConfig**

```java
package com.letsplay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/products").permitAll()
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add src/main/java/com/letsplay/config/SecurityConfig.java
git commit -m "feat: add Spring Security config with JWT and role-based access"
```

---

## Task 12: Controllers

**Files:**
- Create: `src/main/java/com/letsplay/controller/AuthController.java`
- Create: `src/main/java/com/letsplay/controller/UserController.java`
- Create: `src/main/java/com/letsplay/controller/ProductController.java`

**Interfaces:**
- Consumes: `AuthService`, `UserService`, `ProductService`
- Produces: REST API endpoints matching the spec

- [ ] **Step 1: Create AuthController**

```java
package com.letsplay.controller;

import com.letsplay.dto.UserRequest;
import com.letsplay.dto.UserResponse;
import com.letsplay.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        UserResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        Map<String, String> response = authService.login(username, password);
        return ResponseEntity.ok(response);
    }
}
```

- [ ] **Step 2: Create UserController**

```java
package com.letsplay.controller;

import com.letsplay.dto.UserRequest;
import com.letsplay.dto.UserResponse;
import com.letsplay.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable String id, @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

- [ ] **Step 3: Create ProductController**

```java
package com.letsplay.controller;

import com.letsplay.dto.ProductRequest;
import com.letsplay.dto.ProductResponse;
import com.letsplay.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request, Authentication authentication) {
        String userId = authentication.getName();
        ProductResponse response = productService.create(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable String id, @Valid @RequestBody ProductRequest request, Authentication authentication) {
        String userId = authentication.getName();
        ProductResponse response = productService.update(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication authentication) {
        String userId = authentication.getName();
        productService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}
```

- [ ] **Step 4: Commit**

```bash
git add src/main/java/com/letsplay/controller/
git commit -m "feat: add Auth, User, and Product controllers"
```

---

## Task 13: CORS Configuration

**Files:**
- Create: `src/main/java/com/letsplay/config/WebConfig.java`

**Interfaces:**
- Consumes: None
- Produces: CORS configuration allowing cross-origin requests

- [ ] **Step 1: Create WebConfig**

```java
package com.letsplay.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add src/main/java/com/letsplay/config/WebConfig.java
git commit -m "feat: add CORS configuration"
```

---

## Task 14: Rate Limiting

**Files:**
- Create: `src/main/java/com/letsplay/config/RateLimitFilter.java`

**Interfaces:**
- Consumes: Bucket4j library
- Produces: `RateLimitFilter` that limits requests per IP

- [ ] **Step 1: Create RateLimitFilter**

```java
package com.letsplay.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, k -> createNewBucket());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        Bucket bucket = resolveBucket(ip);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
        } else {
            long waitTimeSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("Retry-After", String.valueOf(waitTimeSeconds));
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("{\"message\":\"Too many requests. Try again later.\"}");
        }
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add src/main/java/com/letsplay/config/RateLimitFilter.java
git commit -m "feat: add rate limiting filter with Bucket4j"
```

---

## Task 15: Final Verification

**Files:**
- Modify: None (verify existing code)

**Interfaces:**
- Consumes: All tasks above
- Produces: Working application

- [ ] **Step 1: Verify application starts**

```bash
cd "C:\Users\Julien\Desktop\DEV\01edu\java\lets-play"
mvn spring-boot:run
```

Expected: Application starts on port 8080, connects to MongoDB

- [ ] **Step 2: Test registration**

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","email":"admin@test.com","password":"password123","roles":["ADMIN"]}'
```

Expected: 201 Created with user response (no password)

- [ ] **Step 3: Test login**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'
```

Expected: 200 OK with JWT token

- [ ] **Step 4: Test product creation (with token)**

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"name":"Test Product","description":"A test","price":9.99}'
```

Expected: 201 Created with product response

- [ ] **Step 5: Test GET products (no auth)**

```bash
curl http://localhost:8080/api/products
```

Expected: 200 OK with product list

- [ ] **Step 6: Test access denied (user trying to access /api/users)**

```bash
curl -H "Authorization: Bearer <user_token>" http://localhost:8080/api/users
```

Expected: 403 Forbidden

- [ ] **Step 7: Final commit**

```bash
git add -A
git commit -m "chore: final verification and cleanup"
```
