package com.foxminded.repository;

import com.foxminded.enums.CourseName;
import com.foxminded.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByName(CourseName name);

}
