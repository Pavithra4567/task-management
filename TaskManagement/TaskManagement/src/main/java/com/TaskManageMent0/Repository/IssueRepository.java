package com.TaskManageMent0.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.TaskManageMent0.Entity.Issue;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByProjectIdAndSprintIdIsNullOrderByBackLogPosition(Long projectId);
}
