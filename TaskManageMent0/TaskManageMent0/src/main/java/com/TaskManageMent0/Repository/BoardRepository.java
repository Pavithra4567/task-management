package com.TaskManageMent0.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TaskManageMent0.Entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByProjectKey(String projectKey);
}
