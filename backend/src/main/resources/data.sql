-- Insert sample courses
INSERT IGNORE INTO courses (id, code, title, description, credits, department, max_enrollment, created_at, updated_at) VALUES
(1, 'CS101', 'Introduction to Computer Science', 'Basic concepts of programming and computer science', 3, 'Computer Science', 50, NOW(), NOW()),
(2, 'CS201', 'Data Structures and Algorithms', 'Advanced programming concepts and algorithm analysis', 4, 'Computer Science', 40, NOW(), NOW()),
(3, 'CS301', 'Database Systems', 'Relational database design and SQL', 3, 'Computer Science', 35, NOW(), NOW()),
(4, 'MATH101', 'Calculus I', 'Differential calculus and applications', 4, 'Mathematics', 60, NOW(), NOW()),
(5, 'MATH201', 'Linear Algebra', 'Vector spaces, matrices, and linear transformations', 3, 'Mathematics', 45, NOW(), NOW()),
(6, 'ENG101', 'Technical Writing', 'Writing skills for technical professionals', 2, 'English', 30, NOW(), NOW()),
(7, 'CS401', 'Software Engineering', 'Software development methodologies and project management', 4, 'Computer Science', 30, NOW(), NOW()),
(8, 'CS501', 'Machine Learning', 'Introduction to machine learning algorithms and applications', 3, 'Computer Science', 25, NOW(), NOW());

-- Insert sample students
INSERT IGNORE INTO students (id, student_id, first_name, last_name, email, phone_number, date_of_birth, department, enrollment_year, created_at, updated_at) VALUES
(1, 'ST2021001', 'John', 'Doe', 'john.doe@university.edu', '+1234567890', '2000-05-15', 'Computer Science', 2021, NOW(), NOW()),
(2, 'ST2021002', 'Jane', 'Smith', 'jane.smith@university.edu', '+1234567891', '2001-03-22', 'Computer Science', 2021, NOW(), NOW()),
(3, 'ST2022001', 'Bob', 'Johnson', 'bob.johnson@university.edu', '+1234567892', '2002-07-08', 'Mathematics', 2022, NOW(), NOW()),
(4, 'ST2022002', 'Alice', 'Brown', 'alice.brown@university.edu', '+1234567893', '2001-11-30', 'Computer Science', 2022, NOW(), NOW()),
(5, 'ST2023001', 'Charlie', 'Davis', 'charlie.davis@university.edu', '+1234567894', '2003-01-17', 'English', 2023, NOW(), NOW());

-- -- Insert sample users (password is 'password123' encrypted with BCrypt)
-- INSERT IGNORE INTO users (id, username, password, email, role, student_id, enabled, created_at, updated_at) VALUES
-- (1, 'admin', '$2a$10$N.1T3YaYwp1.BmE8T5uFLuWKYJwM3H6hcNLNBkNQc0nhFEvxoBXMG', 'admin@university.edu', 'ADMIN', NULL, true, NOW(), NOW()),
-- (2, 'john.doe', '$2a$10$N.1T3YaYwp1.BmE8T5uFLuWKYJwM3H6hcNLNBkNQc0nhFEvxoBXMG', 'john.doe@university.edu', 'STUDENT', 1, true, NOW(), NOW()),
-- (3, 'jane.smith', '$2a$10$N.1T3YaYwp1.BmE8T5uFLuWKYJwM3H6hcNLNBkNQc0nhFEvxoBXMG', 'jane.smith@university.edu', 'STUDENT', 2, true, NOW(), NOW()),
-- (4, 'bob.johnson', '$2a$10$N.1T3YaYwp1.BmE8T5uFLuWKYJwM3H6hcNLNBkNQc0nhFEvxoBXMG', 'bob.johnson@university.edu', 'STUDENT', 3, true, NOW(), NOW()),
-- (5, 'alice.brown', '$2a$10$N.1T3YaYwp1.BmE8T5uFLuWKYJwM3H6hcNLNBkNQc0nhFEvxoBXMG', 'alice.brown@university.edu', 'STUDENT', 4, true, NOW(), NOW());

-- Insert sample registrations
INSERT IGNORE INTO registrations (id, student_id, course_id, registration_date, status, remarks) VALUES
(1, 1, 1, '2023-08-15 10:00:00', 'ENROLLED', 'Regular enrollment'),
(2, 1, 2, '2023-08-15 10:15:00', 'ENROLLED', 'Regular enrollment'),
(3, 1, 4, '2023-08-15 10:30:00', 'ENROLLED', 'Regular enrollment'),
(4, 2, 1, '2023-08-16 09:00:00', 'ENROLLED', 'Regular enrollment'),
(5, 2, 3, '2023-08-16 09:15:00', 'ENROLLED', 'Regular enrollment'),
(6, 3, 4, '2023-08-17 11:00:00', 'ENROLLED', 'Regular enrollment'),
(7, 3, 5, '2023-08-17 11:15:00', 'ENROLLED', 'Regular enrollment'),
(8, 4, 1, '2023-08-18 14:00:00', 'ENROLLED', 'Regular enrollment'),
(9, 4, 7, '2023-08-18 14:15:00', 'ENROLLED', 'Regular enrollment'),
(10, 5, 6, '2023-08-19 16:00:00', 'ENROLLED', 'Regular enrollment');

-- Insert sample results
INSERT IGNORE INTO results (id, registration_id, marks, grade, gpa_points, feedback, result_date, created_at, updated_at) VALUES
(1, 1, 85.50, 'A', 3.70, 'Excellent performance in programming assignments', '2023-12-15 10:00:00', NOW(), NOW()),
(2, 2, 92.00, 'A+', 4.00, 'Outstanding understanding of data structures', '2023-12-15 11:00:00', NOW(), NOW()),
(3, 3, 78.25, 'B+', 3.00, 'Good grasp of calculus concepts', '2023-12-16 09:00:00', NOW(), NOW()),
(4, 4, 88.75, 'A', 3.70, 'Strong programming fundamentals', '2023-12-16 10:00:00', NOW(), NOW()),
(5, 5, 91.50, 'A+', 4.00, 'Exceptional database design skills', '2023-12-17 11:00:00', NOW(), NOW());