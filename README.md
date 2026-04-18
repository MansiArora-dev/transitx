# 🚖 TransitX — Ride-Hailing Backend

A production-grade ride-hailing REST API inspired by Uber, built with Spring Boot and deployed on AWS.

Features JWT-based authentication, role-based access control, geolocation-aware ride matching using PostGIS, wallet-based payment system, email notifications via Gmail SMTP, and a fully automated CI/CD pipeline from GitHub to AWS Elastic Beanstalk.

---

## ☁️ AWS Architecture

| Component | Service |
|-----------|---------|
| App Hosting | AWS Elastic Beanstalk (EC2 + Load Balancer + Auto Scaling) |
| Database | AWS RDS (PostgreSQL) |
| CI/CD | AWS CodePipeline + CodeBuild |
| Source | GitHub (main branch) |


## 📸 Screenshots

### CI/CD Pipeline — All Stages Successful
<img src="https://github.com/user-attachments/assets/61475662-9e1a-4a48-9554-6672a0767b48" width="750"/>


### API in Action — Live on AWS
<img src="https://github.com/user-attachments/assets/8b324407-a6f8-458e-8a6e-38c593af290b" width="750"/>


---

## 🔑 Core Features

### 🔐 Authentication & Authorization
- JWT access + refresh token flow
- Role-based access control: `RIDER`, `DRIVER`, `ADMIN`
- Signup auto-creates Rider profile + Wallet

### 🚗 Ride Lifecycle
- Rider requests ride with pickup + drop coordinates
- Nearest available Driver matched using **PostGIS geospatial queries**
- Ride status transitions: `REQUESTED → CONFIRMED → ONGOING → ENDED`
- Driver can accept or cancel; Rider can cancel before confirmation

### 💰 Wallet & Payments
- Every user gets a Wallet on signup
- Fare auto-deducted from Rider wallet on ride end
- Driver wallet credited; full transaction history maintained
- Strategy pattern used for payment processing

### ⭐ Ratings
- Rider rates Driver after ride ends
- Driver rates Rider after ride ends
- Average ratings tracked on profiles

### 📧 Email Notifications
- Gmail SMTP integration for ride confirmations and updates

---

## 📡 API Endpoints

### Auth
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/auth/signup` | Register new user | Public |
| POST | `/auth/login` | Login, get JWT tokens | Public |
| POST | `/auth/refresh` | Refresh access token | Public |
| POST | `/auth/onBoardNewDriver/{userId}` | Onboard new driver | ADMIN |

### Rider
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/rider/requestRide` | Request a new ride | RIDER |
| POST | `/rider/cancelRide/{rideId}` | Cancel a ride | RIDER |
| POST | `/rider/rateDriver/{rideId}` | Rate driver after ride | RIDER |
| GET | `/rider/getMyRides` | Get all past rides | RIDER |
| GET | `/rider/getMyProfile` | Get rider profile | RIDER |

### Driver
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/driver/acceptRide/{rideRequestId}` | Accept a ride request | DRIVER |
| POST | `/driver/startRide/{rideRequestId}` | Start the ride | DRIVER |
| POST | `/driver/endRide/{rideId}` | End the ride | DRIVER |
| POST | `/driver/cancelRide/{rideId}` | Cancel a ride | DRIVER |
| POST | `/driver/rateRider/{rideId}` | Rate rider after ride | DRIVER |
| GET | `/driver/getMyRides` | Get all past rides | DRIVER |
| GET | `/driver/getMyProfile` | Get driver profile | DRIVER |

---

## 🗄️ Database

PostgreSQL with **PostGIS** extension for geospatial support.

Key entities:

```
User ──< Rider
     ──< Driver
     ──  Wallet ──< WalletTransaction

RideRequest ──> Ride ──> Payment
                     ──> Rating
```

- `app_user` — core user with roles
- `rider` / `driver` — profile tables linked to user
- `ride_request` — pending requests with pickup/drop Point coordinates
- `ride` — active and completed rides
- `wallet` / `wallet_transaction` — full payment ledger
- `rating` — post-ride ratings for both parties

Initial seed data loaded via `data.sql` on startup (dev profile).

---

## 🧪 Testing

- **JUnit 5** + **Mockito** — Unit tests with fully mocked dependencies, no DB required
- **Testcontainers** — Integration tests spin up a real PostgreSQL Docker container
- Tests cover: Auth Controllers, Auth Service, Repository layer
- Docker Hub credentials configured in CodeBuild to pull PostgreSQL image for Testcontainers

```bash
# Run all tests
mvn test
```

`TestContainerConfiguration.java` uses `@ServiceConnection` — Spring Boot auto-configures datasource from the container, no manual URL setup needed.

---

## 🛠️ AWS Setup (How I Built This)

### 1. RDS (PostgreSQL) Setup
1. AWS Console → RDS → **Create database**
2. Engine: **PostgreSQL**
3. Template: **Free tier**
4. DB instance identifier: `transitx-db`
5. Master username & password set
6. Connectivity: **VPC default** (accessible from EB environment)
7. Create database

### 2. Elastic Beanstalk Setup
1. AWS Console → Elastic Beanstalk → **Create application**
2. Application name: `springboot-transitx`
3. Platform: **Corretto 21 (Java)**
4. Environment type: **Load balanced**
5. Configure environment variables:
    - `SPRING_PROFILES_ACTIVE` = `prod`
    - `DB_URL` = RDS endpoint
    - `DB_USERNAME` = RDS username
    - `DB_PASSWORD` = RDS password
6. Create environment

### 3. CodeBuild Setup
1. AWS Console → CodeBuild → **Create build project**
2. Source: **GitHub** → repository connect
3. Buildspec: **Use buildspec.yml from repo**
4. Artifacts: **Amazon S3**
5. Configure environment variables:
    - `DOCKER_USERNAME` = Docker Hub username
    - `DOCKER_PASSWORD` = Docker Hub password
6. Create build project

### 4. CodePipeline Setup
1. AWS Console → CodePipeline → **Create pipeline**
2. Source stage: **GitHub → main branch**
3. Build stage: **CodeBuild project select**
4. Deploy stage: **Elastic Beanstalk → environment select**
5. Create pipeline

### 5. IAM Permissions
1. IAM → Roles → `AWSCodePipelineServiceRole-ap-south-1-transitx-pipeline`
2. **Add permissions → Attach policies**
3. Attach: `AdministratorAccess-AWSElasticBeanstalk`

### 6. GitHub Connection
1. CodePipeline → Settings → **Connections**
2. **Create connection → GitHub**
3. Authorize AWS to access GitHub repo

---

## ⚙️ CI/CD Pipeline Flow

Push to `main` → CodePipeline triggers → Docker Hub login → Maven build & test → JAR artifact → Elastic Beanstalk deploys

```yaml
version: 0.2
phases:
  install:
    runtime-versions:
      java: corretto21
    commands:
      - echo Installing Maven...
  pre_build:
    commands:
      - echo Logging in to Docker Hub...
      - echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
  build:
    commands:
      - echo Building, Testing, and Packaging the application...
      - mvn package
  post_build:
    commands:
      - echo Build, Testing, and Packaging completed.
artifacts:
  files:
    - target/*.jar
  discard-paths: yes
cache:
  paths:
    - '/root/.m2/**/*'
```

---

## 🌱 Spring Profiles

| Profile | Usage |
|---------|-------|
| `dev` | Local development with RDS config (credentials in `application-dev.properties`) |
| `local` | Local development with local PostgreSQL |
| `prod` | AWS production — RDS PostgreSQL, port 5000 |

```bash
# Run with dev profile (uses RDS, real DB)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Run with local profile
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Run with prod profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

> ⚠️ `application-dev.properties` contains real credentials and is git-ignored.
> Use `application-dev-example.properties` as a reference to create your own.

---

## 📂 Project Structure

```
transitx/
├── src/
│   ├── main/
│   │   ├── java/com/springboot/transitx/
│   │   │   ├── controllers/        # REST Controllers (Auth, Rider, Driver)
│   │   │   ├── services/           # Service interfaces
│   │   │   │   └── impl/           # Service implementations
│   │   │   ├── entities/           # JPA Entities
│   │   │   │   └── enums/          # Role, RideStatus, PaymentStatus...
│   │   │   ├── repositories/       # Spring Data JPA Repositories
│   │   │   ├── dto/                # Request/Response DTOs
│   │   │   ├── security/           # JWT filter, JWTService, SecurityConfig
│   │   │   ├── strategies/         # Strategy pattern (Payment, RideFare, DriverMatching)
│   │   │   ├── utilis/             # GeometryUtil (PostGIS helpers)
│   │   │   └── exceptions/         # Global exception handling
│   │   └── resources/
│   │       ├── application.properties                 # Base config
│   │       ├── application-dev.properties             # Dev profile — git-ignored
│   │       ├── application-dev-example.properties     # Dev config template
│   │       ├── application-local.properties           # Local DB config
│   │       ├── application-prod.properties            # Prod profile config
│   │       └── data.sql                               # Seed data (dev profile)
│   └── test/
│       └── java/com/springboot/transitx/
│           ├── TestContainerConfiguration.java        # Testcontainers setup
│           ├── TransitXApplicationTests.java          # Context load test
│           ├── controllers/                           # Controller integration tests
│           └── services/impl/                         # Service unit tests
├── buildspec.yml                   # CodeBuild configuration
├── pom.xml                         # Maven dependencies
└── README.md
```

---

## 🚀 Quick Start (Local)

**Prerequisites:** Java 21+, Maven, PostgreSQL, Docker

```bash
git clone https://github.com/MansiArora-dev/transitx.git
cd transitx

# Copy example config and fill in your values
cp src/main/resources/application-dev-example.properties \
   src/main/resources/application-dev.properties
```

Configure `application-dev.properties` with your DB, SMTP, and JWT credentials
using `application-dev-example.properties` as reference.

```bash
# Run with dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## 💻 Technologies

- **Java 21** | **Spring Boot 3.5** | **Maven**
- **AWS** — Elastic Beanstalk, RDS, CodePipeline, CodeBuild
- **PostgreSQL** | **PostGIS** | **Spring Security** | **JWT**
- **Gmail SMTP** | **Docker** | **Testcontainers** | **JUnit 5** | **Mockito**
- **ModelMapper** | **Lombok** | **Strategy Design Pattern**

---

## 👩‍💻 Developer

**Mansi Arora** — Software Engineer
[GitHub](https://github.com/MansiArora-dev)
