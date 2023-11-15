package com.foxminded.repository;

import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s.group FROM Student s GROUP BY s.group HAVING COUNT(*) <= :amount")
    List<Group> findGroupsByStudentAmount(@Param("amount") long amount);
    List<Student> findStudentsByCoursesContaining(Course course);

}
