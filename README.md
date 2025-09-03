# University Course Management System

A modern full-stack web application for managing university courses, students, registrations, and academic results with role-based access control.

## 🚀 Features

### Authentication & Authorization
- **JWT-based authentication** with secure token management
- **Role-based access control** (Admin and Student roles)
- **Protected routes** based on user roles
- **Automatic session management** with token expiration

### Admin Features
- **Complete CRUD operations** for:
  - Students management
  - Courses management
  - Registration management
  - Results management
- **Dashboard with statistics** showing total students, courses, registrations, and results
- **Search and filter capabilities** across all entities
- **Bulk data management** with pagination support

### Student Features
- **Personal dashboard** with enrolled courses overview
- **Course enrollment/unenrollment** functionality
- **View available courses** not yet enrolled in
- **Access academic results** and grades
- **GPA calculation** based on results

## 🛠️ Technology Stack

### Backend
- **Spring Boot 3.5.5** - Java web framework
- **Spring Security** - Authentication and authorization
- **JWT (JSON Web Tokens)** - Stateless authentication
- **Spring Data JPA** - Database ORM
- **MySQL** - Relational database
- **Lombok** - Reduce boilerplate code
- **Maven** - Dependency management

### Frontend
- **React 18** - UI library
- **TypeScript** - Type-safe JavaScript
- **React Router v6** - Client-side routing
- **Tailwind CSS** - Utility-first CSS framework
- **Lucide React** - Icon library
- **Context API** - State management for authentication

## 📁 Project Structure

```
university-course-management/
├── backend/
│   ├── src/main/java/com/university/courses/
│   │   ├── config/         # Security and CORS configuration
│   │   ├── controller/     # REST API endpoints
│   │   ├── dto/           # Data Transfer Objects
│   │   ├── entity/        # JPA entities
│   │   ├── exception/     # Custom exceptions
│   │   ├── repository/    # Data access layer
│   │   ├── security/      # JWT and authentication
│   │   └── service/       # Business logic
│   └── src/main/resources/
│       ├── application.properties
│       └── data.sql       # Initial data
│
└── frontend/
    ├── public/
    └── src/
        ├── components/    # Reusable UI components
        ├── contexts/      # React contexts (Auth)
        ├── pages/         # Page components
        ├── services/      # API services
        ├── types/         # TypeScript definitions
        └── utils/         # Utility functions
```

## 🔧 Installation & Setup

### Prerequisites
- Java 21
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/university-course-management.git
   cd university-course-management/backend
   ```

2. **Configure MySQL Database**
   - Create a database named `course_management`
   - Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/course_management
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

3. **Install dependencies and run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The backend will start on http://localhost:8080

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
   The frontend will start on http://localhost:3000

## 👥 Default User Credentials

The application comes with pre-seeded user accounts:

### Admin Account
- **Username:** admin
- **Password:** password123
- **Role:** ADMIN

### Student Accounts
| Username | Password | Name |
|----------|----------|------|
| john.doe | password123 | John Doe |
| jane.smith | password123 | Jane Smith |
| bob.johnson | password123 | Bob Johnson |
| alice.brown | password123 | Alice Brown |

## 📝 API Endpoints

### Authentication Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/login | User login |
| POST | /api/auth/register | User registration |
| GET | /api/auth/me | Get current user |
| POST | /api/auth/validate | Validate token |

### Admin Endpoints (Requires ADMIN role)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/students | Get all students |
| POST | /api/students | Create student |
| PUT | /api/students/{id} | Update student |
| DELETE | /api/students/{id} | Delete student |
| GET | /api/courses | Get all courses |
| POST | /api/courses | Create course |
| PUT | /api/courses/{id} | Update course |
| DELETE | /api/courses/{id} | Delete course |
| GET | /api/registrations | Get all registrations |
| POST | /api/registrations | Create registration |
| PUT | /api/registrations/{id} | Update registration |
| DELETE | /api/registrations/{id} | Delete registration |
| GET | /api/results | Get all results |
| POST | /api/results | Create result |
| PUT | /api/results/{id} | Update result |
| DELETE | /api/results/{id} | Delete result |

### Student Endpoints (Requires STUDENT role)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/student/courses/available | Get available courses |
| GET | /api/student/courses/enrolled | Get enrolled courses |
| GET | /api/student/registrations | Get student's registrations |
| GET | /api/student/results | Get student's results |
| POST | /api/student/courses/{id}/enroll | Enroll in course |
| POST | /api/student/courses/{id}/unenroll | Unenroll from course |

## 🎯 Usage Guide

### For Administrators
1. Login with admin credentials
2. Navigate through tabs: Students, Courses, Registrations, Results
3. Add new records using the "Add" button in each section
4. Edit records by clicking the edit icon
5. Delete records by clicking the delete icon
6. Search using the search bar to filter records
7. View statistics in the summary cards at the bottom

### For Students
1. Login with student credentials or register a new account
2. View enrolled courses in the "My Courses" tab
3. Browse available courses in the "Available Courses" tab
4. Enroll in courses by clicking the "Enroll" button
5. Unenroll from courses by clicking the "Unenroll" button
6. View academic results in the "My Results" tab
7. Check GPA displayed in the student info card

## 🔒 Security Features
- Password encryption using BCrypt
- JWT token expiration (24 hours by default)
- Role-based endpoint protection
- CORS configuration for cross-origin requests
- Input validation on both frontend and backend
- SQL injection prevention through JPA parameterized queries

## 📊 Database Schema

### Users Table
- id (Primary Key)
- username (Unique)
- password (Encrypted)
- email (Unique)
- role (ADMIN/STUDENT)
- student_id (Foreign Key, optional)

### Students Table
- id (Primary Key)
- student_id (Unique)
- first_name
- last_name
- email
- phone_number
- date_of_birth
- department
- enrollment_year

### Courses Table
- id (Primary Key)
- code (Unique)
- title
- description
- credits
- department
- max_enrollment

### Registrations Table
- id (Primary Key)
- student_id (Foreign Key)
- course_id (Foreign Key)
- registration_date
- status (ENROLLED/DROPPED/COMPLETED)
- remarks

### Results Table
- id (Primary Key)
- registration_id (Foreign Key)
- marks
- grade
- gpa_points
- feedback
- result_date

## 🐛 Troubleshooting

### Backend Issues

**Database connection error**
- Ensure MySQL is running
- Check database credentials in application.properties
- Verify database exists

**Port already in use**
- Change port in application.properties: `server.port=8081`

**JWT token issues**
- Clear browser localStorage
- Check token expiration settings

### Frontend Issues

**Cannot connect to backend**
- Ensure backend is running on port 8080
- Check CORS configuration
- Verify API base URL in services

**Login not working**
- Check network tab for errors
- Verify credentials
- Clear browser cache and localStorage

**Routes not working**
- Ensure react-router-dom is installed
- Check protected route configuration

## 📈 Future Enhancements

- [ ] Email notifications for course enrollment
- [ ] Export functionality (PDF/Excel)
- [ ] Advanced search filters
- [ ] Bulk import of student data
- [ ] Course prerequisites management
- [ ] Attendance tracking
- [ ] Assignment submission system
- [ ] Real-time notifications
- [ ] Mobile responsive improvements
- [ ] Dark mode support