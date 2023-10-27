INSERT INTO groups (group_name) VALUES ('gr1'), ('gr2'), ('gr3');
INSERT INTO courses (course_name, course_description) VALUES ('ART', 'desc1'), ('MATH', 'desc2'), ('SCIENCE', 'desc3');
INSERT INTO students (group_id, first_name, last_name) VALUES (1, 'firstName1', 'lastName1'), (2, 'firstName2', 'lastName2'), (3, 'firstName3', 'lastName3');
INSERT INTO students_courses (student_id, course_id) VALUES (1, 2), (2, 2), (2, 3);