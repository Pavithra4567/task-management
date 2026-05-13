package com.TaskManageMent0.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.TaskManageMent0.Entity.Sprint;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
}
