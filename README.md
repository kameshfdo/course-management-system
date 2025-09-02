# Course Management System

A comprehensive course management system built with Spring Boot backend and React frontend, designed to manage students, courses, registrations, and academic results.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Usage](#usage)
- [Contributing](#contributing)

## Features

### Core Functionality
- **Student Management**: Create, update, view, and delete student records
- **Course Management**: Manage course catalog with departments, credits, and enrollment limits
- **Registration System**: Handle student course enrollments with status tracking
- **Results Management**: Record and calculate grades, GPA, and academic performance
- **Search & Filtering**: Advanced search capabilities across all entities
- **Pagination**: Efficient data handling with pagination support

### Key Capabilities
- Automatic GPA calculation based on marks
- Course enrollment capacity management
- Department-wise organization
- Comprehensive validation and error handling
- RESTful API design
- Cross-origin resource sharing (CORS) enabled

## Technology Stack

### Backend
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Hibernate**
- **MySQL Database**
- **Maven** for dependency management
- **Lombok** for code generation
- **SLF4J** for logging

### Frontend
- **React.js**
- **TypeScript**
- **Tailwind CSS**
- **Component-based architecture**

### Database
- **MySQL 8.0+**

## Project Structure

```
course-management-system/
├── backend/
│   ├── src/main/java/com/university/courses/
│   │   ├── config/           # Configuration classes
│   │   ├── controller/       # REST controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA entities
│   │   ├── exception/       # Exception handling
│   │   ├── repository/      # Data access layer
│   │   └── service/         # Business logic layer
│   └── src/main/resources/
│       ├── application.properties
│       └── data.sql         # Sample data
├── frontend/
│   ├── src/
│   │   ├── components/      # React components
│   │   ├── services/        # API service layer
│   │   ├── types/          # TypeScript type definitions
│   │   └── utils/          # Utility functions
│   ├── public/
│   └── package.json
└── README.md
```

## Prerequisites

- Java 17 or higher
- Node.js 16+ and npm
- MySQL 8.0+
- Maven 3.6+

## Installation

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd course-management-system/backend
   ```

2. **Configure MySQL Database**
   ```sql
   CREATE DATABASE course_management;
   CREATE USER 'your_username'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON course_management.* TO 'your_username'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Update application.properties**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/course_management?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Run the backend**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd ../frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   ```

The frontend will start on `http://localhost:3000`

## Configuration

### Database Configuration

The system uses MySQL with the following default configuration:
- **Host**: localhost:3306
- **Database**: course_management
- **Username**: root
- **Password**: 1234

Update `application.properties` to match your MySQL setup.

### CORS Configuration

CORS is configured to allow all origins during development. For production, update the `CorsConfig.java` to specify allowed origins.

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Students API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/students` | Get all students (paginated) |
| GET | `/students/{id}` | Get student by ID |
| GET | `/students/studentId/{studentId}` | Get student by student ID |
| POST | `/students` | Create new student |
| PUT | `/students/{id}` | Update student |
| DELETE | `/students/{id}` | Delete student |
| GET | `/students/search` | Search students with filters |
| GET | `/students/departments` | Get all departments |

### Courses API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/courses` | Get all courses (paginated) |
| GET | `/courses/{id}` | Get course by ID |
| GET | `/courses/code/{code}` | Get course by code |
| POST | `/courses` | Create new course |
| PUT | `/courses/{id}` | Update course |
| DELETE | `/courses/{id}` | Delete course |
| GET | `/courses/search` | Search courses with filters |
| GET | `/courses/departments` | Get all departments |

### Registrations API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/registrations` | Get all registrations (paginated) |
| GET | `/registrations/{id}` | Get registration by ID |
| GET | `/registrations/student/{studentId}` | Get registrations by student |
| GET | `/registrations/course/{courseId}` | Get registrations by course |
| POST | `/registrations` | Create new registration |
| PUT | `/registrations/{id}` | Update registration |
| PUT | `/registrations/{id}/status` | Update registration status |
| DELETE | `/registrations/{id}` | Delete registration |

### Results API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/results` | Get all results (paginated) |
| GET | `/results/{id}` | Get result by ID |
| GET | `/results/registration/{registrationId}` | Get result by registration |
| GET | `/results/student/{studentId}` | Get results by student |
| GET | `/results/course/{courseId}` | Get results by course |
| POST | `/results` | Create new result |
| PUT | `/results/{id}` | Update result |
| DELETE | `/results/{id}` | Delete result |
| GET | `/results/student/{studentId}/gpa` | Get student GPA |
| GET | `/results/course/{courseId}/average` | Get course average marks |

## Database Schema

### Core Entities

**Students**
- Personal information (name, email, phone)
- Academic details (student ID, department, enrollment year)
- Unique constraints on student ID and email

**Courses**
- Course details (code, title, description)
- Academic information (credits, department)
- Enrollment management (max enrollment capacity)
- Unique constraint on course code

**Registrations**
- Links students to courses
- Tracks enrollment status (ENROLLED, DROPPED, COMPLETED)
- Prevents duplicate registrations
- Records registration date and remarks

**Results**
- Academic performance tracking
- Automatic grade calculation (A+ to F)
- GPA point calculation (0.0 to 4.0)
- Supports feedback and result dates

### Relationships
- One-to-Many: Student → Registrations
- One-to-Many: Course → Registrations
- One-to-One: Registration → Result

## Usage

### Sample Data

The system includes sample data with:
- 5 students across different departments
- 8 courses in Computer Science, Mathematics, and English
- 10 sample registrations
- 5 sample results with calculated grades

### Grading System

The system uses a standard 4.0 GPA scale:

| Grade | Marks Range | GPA Points |
|-------|-------------|-----------|
| A+ | 90-100 | 4.0 |
| A | 85-89 | 3.7 |
| A- | 80-84 | 3.3 |
| B+ | 75-79 | 3.0 |
| B | 70-74 | 2.7 |
| B- | 65-69 | 2.3 |
| C+ | 60-64 | 2.0 |
| C | 55-59 | 1.7 |
| C- | 50-54 | 1.3 |
| F | Below 50 | 0.0 |

### Key Business Rules

1. **Unique Constraints**:
   - Student ID and email must be unique
   - Course codes must be unique
   - Students cannot register for the same course twice

2. **Enrollment Management**:
   - Courses can have maximum enrollment limits
   - Registration is prevented when capacity is reached

3. **Academic Integrity**:
   - Only one result per registration
   - Automatic grade and GPA calculation
   - Marks must be between 0 and 100

## API Usage Examples

### Create a Student
```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "ST2024001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@university.edu",
    "department": "Computer Science",
    "enrollmentYear": 2024
  }'
```

### Register Student for Course
```bash
curl -X POST http://localhost:8080/api/registrations \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": 1,
    "courseId": 1,
    "remarks": "Regular enrollment"
  }'
```

### Record Result
```bash
curl -X POST http://localhost:8080/api/results \
  -H "Content-Type: application/json" \
  -d '{
    "registrationId": 1,
    "marks": 85.5,
    "feedback": "Excellent performance"
  }'
```

## Development

### Running Tests
```bash
cd backend
mvn test
```

### Building for Production
```bash
# Backend
cd backend
mvn clean package

# Frontend
cd frontend
npm run build
```

## Error Handling

The system includes comprehensive error handling:
- **Validation Errors**: Detailed field-level validation messages
- **Resource Not Found**: Clear error messages for missing entities
- **Business Logic Violations**: Meaningful error responses for rule violations
- **Global Exception Handler**: Centralized error response formatting

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For questions or issues, please create an issue in the GitHub repository or contact the development team.