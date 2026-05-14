-- Poblado de datos iniciales (DML)
-- Desactivar llaves foráneas para facilitar la inserción masiva
SET FOREIGN_KEY_CHECKS = 0;

-- Limpiar datos existentes
TRUNCATE TABLE grade;
TRUNCATE TABLE enrollment;
TRUNCATE TABLE course;
TRUNCATE TABLE subject;
TRUNCATE TABLE classroom;
TRUNCATE TABLE user;
TRUNCATE TABLE attendance;

-- 1. Insertar Usuarios (Mezcla de Profesores y Estudiantes)
INSERT INTO user (id, document, first_name, last_name, age, email, phone_number, address, gpa, department, user_type) VALUES
('U001', '1001', 'John', 'Doe', 35, 'john.doe@poli.edu.co', '555-0101', 'Street 1 #20', NULL, 'Engineering', 'TEACHER'),
('U002', '1002', 'Jane', 'Smith', 42, 'jane.smith@poli.edu.co', '555-0102', 'Street 2 #30', NULL, 'Mathematics', 'TEACHER'),
('U003', '1003', 'Robert', 'Brown', 38, 'robert.brown@poli.edu.co', '555-0103', 'Street 3 #40', NULL, 'Physics', 'TEACHER'),
('U004', '1004', 'Emily', 'Davis', 45, 'emily.davis@poli.edu.co', '555-0104', 'Street 4 #50', NULL, 'Social Sciences', 'TEACHER'),
('U005', '1005', 'Michael', 'Wilson', 30, 'michael.wilson@poli.edu.co', '555-0105', 'Street 5 #60', NULL, 'Engineering', 'TEACHER'),
('U006', '2001', 'Alice', 'Johnson', 20, 'alice.j@poli.edu.co', '555-2001', 'Av 10', 4.5, 'Engineering', 'STUDENT'),
('U007', '2002', 'Bob', 'Miller', 21, 'bob.m@poli.edu.co', '555-2002', 'Av 11', 3.8, 'Engineering', 'STUDENT'),
('U008', '2003', 'Charlie', 'Garcia', 22, 'charlie.g@poli.edu.co', '555-2003', 'Av 12', 4.2, 'Mathematics', 'STUDENT'),
('U009', '2004', 'David', 'Martinez', 19, 'david.m@poli.edu.co', '555-2004', 'Av 13', 3.5, 'Physics', 'STUDENT'),
('U010', '2005', 'Eve', 'Rodriguez', 23, 'eve.r@poli.edu.co', '555-2005', 'Av 14', 4.8, 'Social Sciences', 'STUDENT'),
('U011', '2006', 'Frank', 'Lee', 20, 'frank.l@poli.edu.co', '555-2006', 'Av 15', 3.0, 'Engineering', 'STUDENT'),
('U012', '2007', 'Grace', 'Walker', 21, 'grace.w@poli.edu.co', '555-2007', 'Av 16', 4.1, 'Engineering', 'STUDENT'),
('U013', '2008', 'Hank', 'Hall', 22, 'hank.h@poli.edu.co', '555-2008', 'Av 17', 3.9, 'Mathematics', 'STUDENT'),
('U014', '2009', 'Ivy', 'Young', 19, 'ivy.y@poli.edu.co', '555-2009', 'Av 18', 4.6, 'Physics', 'STUDENT'),
('U015', '2010', 'Jack', 'King', 24, 'jack.k@poli.edu.co', '555-2010', 'Av 19', 3.2, 'Social Sciences', 'STUDENT');

-- Insertar más estudiantes para llegar a un buen número
INSERT INTO user (id, document, first_name, last_name, age, email, phone_number, address, gpa, department, user_type)
SELECT 
    CONCAT('U0', id_num),
    CONCAT('300', id_num),
    CONCAT('Student_', id_num),
    'LastName',
    20 + (id_num % 5),
    CONCAT('student_', id_num, '@poli.edu.co'),
    CONCAT('555-', id_num),
    'Main St',
    3.0 + (id_num % 20) / 10,
    CASE WHEN id_num % 3 = 0 THEN 'Engineering' WHEN id_num % 3 = 1 THEN 'Mathematics' ELSE 'Physics' END,
    'STUDENT'
FROM (
    SELECT 16 as id_num UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20 UNION
    SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25 UNION
    SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29 UNION SELECT 30
) as nums;

-- 2. Insertar Salones (Classrooms)
INSERT INTO classroom (id, building_name, room_number, capacity) VALUES
('C01', 'Block A', '101', 30),
('C02', 'Block A', '102', 30),
('C03', 'Block B', '201', 25),
('C04', 'Block B', '202', 25),
('C05', 'Block C', '301', 40),
('C06', 'Block C', '302', 40),
('C07', 'Library', 'L01', 15),
('C08', 'Lab 1', 'LB01', 20),
('C09', 'Lab 2', 'LB02', 20),
('C10', 'Auditorium', 'AUD', 100);

-- 3. Insertar Materias (Subjects)
INSERT INTO subject (id, code, name, description, credits, academic_area, is_elective, prerequisite_id, language) VALUES
('S01', 'PROG01', 'Introduction to Programming', 'Basic concepts of logic and programming', 3, 'Software Engineering', FALSE, NULL, 'English'),
('S02', 'DATA01', 'Data Structures', 'Organization and management of data', 4, 'Software Engineering', FALSE, 'S01', 'English'),
('S03', 'CALC01', 'Calculus I', 'Differential and integral calculus', 4, 'Mathematics', FALSE, NULL, 'English'),
('S04', 'CALC02', 'Calculus II', 'Advanced calculus topics', 4, 'Mathematics', FALSE, 'S03', 'English'),
('S05', 'PHYS01', 'General Physics I', 'Mechanics and motion', 4, 'Physics', FALSE, 'S03', 'English'),
('S06', 'DB01', 'Database Systems', 'Relational models and SQL', 3, 'Software Engineering', FALSE, 'S02', 'English'),
('S07', 'WEB01', 'Web Development', 'Frontend and backend technologies', 3, 'Software Engineering', TRUE, 'S01', 'English'),
('S08', 'ETH01', 'Ethics', 'Professional and social ethics', 2, 'Humanities', FALSE, NULL, 'Spanish'),
('S09', 'ALG01', 'Linear Algebra', 'Vector spaces and matrices', 3, 'Mathematics', FALSE, NULL, 'English'),
('S10', 'OOP01', 'Object Oriented Programming', 'Design patterns and OOP principles', 4, 'Software Engineering', FALSE, 'S01', 'English');

-- 4. Insertar Cursos (Courses)
INSERT INTO course (id, subject_id, user_id, classroom_id, schedule) VALUES
('CRS01', 'S01', 'U001', 'C01', 'Mon-Wed 08:00-10:00'),
('CRS02', 'S01', 'U005', 'C02', 'Tue-Thu 08:00-10:00'),
('CRS03', 'S02', 'U001', 'C08', 'Mon-Wed 10:00-12:00'),
('CRS04', 'S03', 'U002', 'C03', 'Tue-Thu 10:00-12:00'),
('CRS05', 'S04', 'U002', 'C04', 'Fri 08:00-12:00'),
('CRS06', 'S05', 'U003', 'C09', 'Mon-Wed 14:00-16:00'),
('CRS07', 'S06', 'U005', 'C05', 'Tue-Thu 14:00-16:00'),
('CRS08', 'S07', 'U001', 'C06', 'Fri 14:00-18:00'),
('CRS09', 'S10', 'U005', 'C08', 'Sat 08:00-12:00'),
('CRS10', 'S09', 'U002', 'C03', 'Mon-Wed 16:00-18:00');

-- 5. Insertar Inscripciones (Enrollments)
INSERT INTO enrollment (user_id, course_id, enrollment_date) VALUES
('U006', 'CRS01', '2026-01-15'), ('U006', 'CRS03', '2026-01-15'), ('U006', 'CRS04', '2026-01-15'),
('U007', 'CRS01', '2026-01-16'), ('U007', 'CRS02', '2026-01-16'), ('U007', 'CRS07', '2026-01-16'),
('U008', 'CRS04', '2026-01-15'), ('U008', 'CRS10', '2026-01-15'),
('U009', 'CRS06', '2026-01-17'), ('U009', 'CRS04', '2026-01-17'),
('U010', 'CRS08', '2026-01-18'), ('U010', 'CRS01', '2026-01-18'),
('U011', 'CRS01', '2026-01-15'), ('U011', 'CRS02', '2026-01-15'),
('U012', 'CRS03', '2026-01-15'), ('U012', 'CRS07', '2026-01-15'),
('U013', 'CRS10', '2026-01-15'), ('U013', 'CRS04', '2026-01-15'),
('U014', 'CRS06', '2026-01-15'), ('U014', 'CRS05', '2026-01-15'),
('U015', 'CRS08', '2026-01-15'), ('U015', 'CRS09', '2026-01-15');

-- Insertar más inscripciones masivas
INSERT INTO enrollment (user_id, course_id, enrollment_date)
SELECT 
    CONCAT('U0', student_id),
    CONCAT('CRS', LPAD(course_id, 2, '0')),
    '2026-01-20'
FROM (
    SELECT 16 as student_id UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20 UNION
    SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25 UNION
    SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29 UNION SELECT 30
) as s
CROSS JOIN (
    SELECT 1 as course_id UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
) as c
WHERE (s.student_id + c.course_id) % 3 = 0;

-- 6. Insertar Calificaciones (Grades)
INSERT INTO grade (enrollment_id, user_id, subject_id, score, description, grade_date)
SELECT 
    e.id,
    e.user_id,
    c.subject_id,
    ROUND(3.0 + (RAND() * 2.0), 1),
    'Partial Exam 1',
    '2026-02-15'
FROM enrollment e 
JOIN course c ON e.course_id = c.id
WHERE e.id <= 60;

INSERT INTO grade (enrollment_id, user_id, subject_id, score, description, grade_date)
SELECT 
    e.id,
    e.user_id,
    c.subject_id,
    ROUND(2.5 + (RAND() * 2.5), 1),
    'Partial Exam 2',
    '2026-03-20'
FROM enrollment e 
JOIN course c ON e.course_id = c.id
WHERE e.id <= 60;

INSERT INTO grade (enrollment_id, user_id, subject_id, score, description, grade_date)
SELECT 
    e.id,
    e.user_id,
    c.subject_id,
    ROUND(3.5 + (RAND() * 1.5), 1),
    'Final Project',
    '2026-05-10'
FROM enrollment e 
JOIN course c ON e.course_id = c.id
WHERE e.id <= 60;

-- Restaurar verificación de llaves foráneas
SET FOREIGN_KEY_CHECKS = 1;
