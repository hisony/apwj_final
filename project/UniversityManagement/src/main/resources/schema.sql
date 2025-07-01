-- CREATE INSERT USER & ROLES
-- NO NEED TO EXEC THIS PART
# CREATE TABLE users (
#                        username VARCHAR(50) NOT NULL PRIMARY KEY,
#                        password VARCHAR(100) NOT NULL,
#                        enabled BOOLEAN NOT NULL
# );
# INSERT INTO users (username, password, enabled) VALUES ('','',true);
#
# CREATE TABLE authorities (
#                              username VARCHAR(50) NOT NULL,
#                              authority VARCHAR(50) NOT NULL,
#                              CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users(username)
# );
# INSERT INTO authorities (username, authority) VALUES ('user1', 'ROLE_USER');








-- CREATE TABLES UNIVERSITY MANAGEMENT
CREATE TABLE user (
                      username VARCHAR(50) PRIMARY KEY,
                      password VARCHAR(100) NOT NULL,
                      enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE authority (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           username VARCHAR(50) NOT NULL,
                           authority VARCHAR(50) NOT NULL,
                           FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE,
                           UNIQUE KEY unique_auth (username, authority)
);

-- Teacher table
CREATE TABLE teacher (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         email VARCHAR(100) NOT NULL UNIQUE,
                         department VARCHAR(50) NOT NULL,
                         INDEX idx_teacher_department (department)
);

-- Student table
CREATE TABLE student (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         email VARCHAR(100) NOT NULL UNIQUE,
                         dob DATE NOT NULL,
                         department VARCHAR(50) NOT NULL,
                         INDEX idx_student_department (department)
);

-- Course table
CREATE TABLE course (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        code VARCHAR(20) NOT NULL UNIQUE,
                        department VARCHAR(50) NOT NULL,
                        teacher_id BIGINT,
                        FOREIGN KEY (teacher_id) REFERENCES teacher(id) ON DELETE SET NULL,
                        INDEX idx_course_department (department),
                        INDEX idx_course_teacher (teacher_id)
);

-- Class Schedule table
CREATE TABLE class_schedule (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                course_id BIGINT NOT NULL,
                                day_of_week VARCHAR(10) NOT NULL,
                                start_time DATETIME NOT NULL,
                                end_time DATETIME NOT NULL,
                                room VARCHAR(20) NOT NULL,
                                FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
                                INDEX idx_schedule_course (course_id),
                                CHECK (end_time > start_time)
);

-- Enrollment table
CREATE TABLE enrollment (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            student_id BIGINT NOT NULL,
                            course_id BIGINT NOT NULL,
                            enrollment_date DATE NOT NULL,
                            FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
                            FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
                            UNIQUE KEY unique_enrollment (student_id, course_id),
                            INDEX idx_enrollment_student (student_id),
                            INDEX idx_enrollment_course (course_id)
);

-- Grade table
CREATE TABLE grade (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       student_id BIGINT NOT NULL,
                       course_id BIGINT NOT NULL,
                       grade VARCHAR(2) NOT NULL,
                       FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
                       FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
                       UNIQUE KEY unique_grade (student_id, course_id),
                       INDEX idx_grade_student (student_id),
                       INDEX idx_grade_course (course_id),
                       CHECK (grade IN ('A+', 'A', 'A-', 'B+', 'B', 'B-', 'C+', 'C', 'C-', 'D+', 'D', 'F'))
);

-- Attendance table
CREATE TABLE attendance (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            student_id BIGINT NOT NULL,
                            course_id BIGINT NOT NULL,
                            date DATE NOT NULL,
                            present BOOLEAN NOT NULL DEFAULT FALSE,
                            FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
                            FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
                            UNIQUE KEY unique_attendance (student_id, course_id, date),
                            INDEX idx_attendance_student (student_id),
                            INDEX idx_attendance_course (course_id),
                            INDEX idx_attendance_date (date)
);

-- Notice table
CREATE TABLE notice (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(200) NOT NULL,
                        content TEXT NOT NULL,
                        published_at DATETIME NOT NULL,
                        published_by VARCHAR(50) NOT NULL,
                        FOREIGN KEY (published_by) REFERENCES user(username) ON DELETE CASCADE,
                        INDEX idx_notice_date (published_at)
);






-- POPULATE TABLES UNIVERSITY MANAGEMENT
-- Users
INSERT INTO user (username, password, enabled) VALUES
                                                   ('admin', '$2a$10$xn3LI/AjqicFYZFruSwve.277z3M1YBnC9ygktX7ML7lIbkD1Rt8.', true),  -- password: admin123
                                                   ('teacher1', '$2a$10$xn3LI/AjqicFYZFruSwve.277z3M1YBnC9ygktX7ML7lIbkD1Rt8.', true),
                                                   ('student1', '$2a$10$xn3LI/AjqicFYZFruSwve.277z3M1YBnC9ygktX7ML7lIbkD1Rt8.', true);

-- Authorities
INSERT INTO authority (username, authority) VALUES
                                                ('admin', 'ROLE_ADMIN'),
                                                ('teacher1', 'ROLE_TEACHER'),
                                                ('student1', 'ROLE_STUDENT');

-- Teachers
INSERT INTO teacher (name, email, department) VALUES
                                                  ('John Smith', 'john.smith@university.edu', 'Computer Science'),
                                                  ('Sarah Johnson', 'sarah.johnson@university.edu', 'Mathematics'),
                                                  ('Michael Brown', 'michael.brown@university.edu', 'Physics'),
                                                  ('Emma Davis', 'emma.davis@university.edu', 'Chemistry');

-- Students
INSERT INTO student (name, email, dob, department) VALUES
                                                       ('Alice Wilson', 'alice.wilson@university.edu', '2000-05-15', 'Computer Science'),
                                                       ('Bob Anderson', 'bob.anderson@university.edu', '2001-03-22', 'Mathematics'),
                                                       ('Charlie Thompson', 'charlie.thompson@university.edu', '2000-11-08', 'Physics'),
                                                       ('Diana Martin', 'diana.martin@university.edu', '2001-07-30', 'Chemistry');

-- Courses
INSERT INTO course (name, code, department, teacher_id) VALUES
                                                            ('Introduction to Programming', 'CS101', 'Computer Science', 1),
                                                            ('Calculus I', 'MATH101', 'Mathematics', 2),
                                                            ('Physics Mechanics', 'PHYS101', 'Physics', 3),
                                                            ('Organic Chemistry', 'CHEM101', 'Chemistry', 4);

-- Class Schedules
INSERT INTO class_schedule (course_id, day_of_week, start_time, end_time, room) VALUES
                                                                                    (1, 'Monday', '2025-09-01 09:00:00', '2025-09-01 10:30:00', 'Room 101'),
                                                                                    (1, 'Wednesday', '2025-09-03 09:00:00', '2025-09-03 10:30:00', 'Room 101'),
                                                                                    (2, 'Tuesday', '2025-09-02 11:00:00', '2025-09-02 12:30:00', 'Room 202'),
                                                                                    (2, 'Thursday', '2025-09-04 11:00:00', '2025-09-04 12:30:00', 'Room 202'),
                                                                                    (3, 'Monday', '2025-09-01 14:00:00', '2025-09-01 15:30:00', 'Room 303'),
                                                                                    (4, 'Tuesday', '2025-09-02 14:00:00', '2025-09-02 15:30:00', 'Room 404');

-- Enrollments
INSERT INTO enrollment (student_id, course_id, enrollment_date) VALUES
                                                                    (1, 1, '2025-08-15'), -- Alice in CS101
                                                                    (1, 2, '2025-08-15'), -- Alice in MATH101
                                                                    (2, 2, '2025-08-16'), -- Bob in MATH101
                                                                    (3, 3, '2025-08-16'), -- Charlie in PHYS101
                                                                    (4, 4, '2025-08-17'); -- Diana in CHEM101

-- Grades
INSERT INTO grade (student_id, course_id, grade) VALUES
                                                     (1, 1, 'A'), -- Alice in CS101
                                                     (1, 2, 'B+'), -- Alice in MATH101
                                                     (2, 2, 'A-'), -- Bob in MATH101
                                                     (3, 3, 'B'), -- Charlie in PHYS101
                                                     (4, 4, 'A-'); -- Diana in CHEM101

-- Attendance (sample for first week)
INSERT INTO attendance (student_id, course_id, date, present) VALUES
                                                                  (1, 1, '2025-09-01', true),  -- Alice in CS101
                                                                  (1, 1, '2025-09-03', true),  -- Alice in CS101
                                                                  (1, 2, '2025-09-02', true),  -- Alice in MATH101
                                                                  (2, 2, '2025-09-02', true),  -- Bob in MATH101
                                                                  (3, 3, '2025-09-01', false), -- Charlie in PHYS101
                                                                  (4, 4, '2025-09-02', true);  -- Diana in CHEM101

-- Notices
INSERT INTO notice (title, content, published_at, published_by) VALUES
                                                                    ('Welcome to Fall Semester 2025', 'Welcome back students! We hope you have a great semester.', '2025-08-30 09:00:00', 'admin'),
                                                                    ('Library Hours Update', 'The library will now be open until 11 PM on weekdays.', '2025-09-01 10:00:00', 'admin'),
                                                                    ('CS101 Assignment Posted', 'First programming assignment has been posted on the course portal.', '2025-09-02 14:00:00', 'teacher1');