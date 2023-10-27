CREATE TABLE groups
(
    group_id   SERIAL PRIMARY KEY,
    group_name VARCHAR(5) NOT NULL UNIQUE
);

CREATE TABLE students
(
    student_id SERIAL PRIMARY KEY,
    group_id   INT,
    first_name VARCHAR(40) NOT NULL,
    last_name  VARCHAR(40) NOT NULL,
    FOREIGN KEY (group_id)
        REFERENCES groups (group_id)
);

CREATE TABLE courses
(
    course_id          SERIAL PRIMARY KEY,
    course_name        VARCHAR(40) NOT NULL UNIQUE,
    course_description VARCHAR(40) NOT NULL
);

CREATE TABLE students_courses
(
    student_id INT NOT NULL,
    course_id  INT NOT NULL,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id)
        REFERENCES students (student_id),
    FOREIGN KEY (course_id)
        REFERENCES courses (course_id)
);