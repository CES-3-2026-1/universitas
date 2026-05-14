-- Configuración inicial
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------
-- Tabla user
-- -----------------------------------------------------
DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS user (
    id VARCHAR(50) NOT NULL,
    document VARCHAR(50) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    age INT,
    email VARCHAR(100),
    phone_number VARCHAR(20),
    address VARCHAR(255),
    gpa DECIMAL(3, 2),
    department VARCHAR(100),
    user_type ENUM('STUDENT', 'TEACHER', 'ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Tabla classroom
-- -----------------------------------------------------
DROP TABLE IF EXISTS classroom;
CREATE TABLE IF NOT EXISTS classroom (
    id VARCHAR(50) NOT NULL,
    building_name VARCHAR(100),
    room_number VARCHAR(20),
    capacity INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Tabla subject
-- -----------------------------------------------------
DROP TABLE IF EXISTS subject;
CREATE TABLE IF NOT EXISTS subject (
    id VARCHAR(50) NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    credits INT DEFAULT 0,
    academic_area VARCHAR(100),
    is_elective BOOLEAN DEFAULT FALSE,
    prerequisite_id VARCHAR(50),
    language VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Tabla course
-- -----------------------------------------------------
DROP TABLE IF EXISTS course;
CREATE TABLE IF NOT EXISTS course (
    id VARCHAR(50) NOT NULL,
    subject_id VARCHAR(50),
    user_id VARCHAR(50), -- Profesor asignado
    classroom_id VARCHAR(50),
    schedule VARCHAR(100),
    start_date DATE,
    end_date DATE,
    semester INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Tabla enrollment
-- -----------------------------------------------------
DROP TABLE IF EXISTS enrollment;
CREATE TABLE IF NOT EXISTS enrollment (
    id INT AUTO_INCREMENT NOT NULL,
    user_id VARCHAR(50), -- Estudiante
    course_id VARCHAR(50),
    enrollment_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Tabla grade
-- -----------------------------------------------------
DROP TABLE IF EXISTS grade;
CREATE TABLE IF NOT EXISTS grade (
    id INT AUTO_INCREMENT NOT NULL,
    enrollment_id INT,
    user_id VARCHAR(50), -- Para referencia directa opcional
    subject_id VARCHAR(50),
    score DECIMAL(3, 2),
    description VARCHAR(255),
    grade_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Tabla attendance (Nuestra adición original)
-- -----------------------------------------------------
DROP TABLE IF EXISTS attendance;
CREATE TABLE IF NOT EXISTS attendance (
    id VARCHAR(50) NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- Relaciones (Foreign Keys)
-- -----------------------------------------------------
ALTER TABLE subject ADD CONSTRAINT fk_subject_prereq FOREIGN KEY (prerequisite_id) REFERENCES subject(id);

ALTER TABLE course ADD CONSTRAINT fk_course_subject FOREIGN KEY (subject_id) REFERENCES subject(id);
ALTER TABLE course ADD CONSTRAINT fk_course_user FOREIGN KEY (user_id) REFERENCES user(id);
ALTER TABLE course ADD CONSTRAINT fk_course_classroom FOREIGN KEY (classroom_id) REFERENCES classroom(id);

ALTER TABLE enrollment ADD CONSTRAINT fk_enrollment_user FOREIGN KEY (user_id) REFERENCES user(id);
ALTER TABLE enrollment ADD CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES course(id);

ALTER TABLE grade ADD CONSTRAINT fk_grade_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollment(id);
ALTER TABLE grade ADD CONSTRAINT fk_grade_user FOREIGN KEY (user_id) REFERENCES user(id);
ALTER TABLE grade ADD CONSTRAINT fk_grade_subject FOREIGN KEY (subject_id) REFERENCES subject(id);

ALTER TABLE attendance ADD CONSTRAINT fk_attendance_user FOREIGN KEY (user_id) REFERENCES user(id);

-- Restaurar verificación de llaves foráneas
SET FOREIGN_KEY_CHECKS = 1;

-- Consulta informativa de zona horaria
SELECT @@global.time_zone, @@session.time_zone;
