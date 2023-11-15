package com.foxminded.repository;

import com.foxminded.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//@Transactional
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);

}
