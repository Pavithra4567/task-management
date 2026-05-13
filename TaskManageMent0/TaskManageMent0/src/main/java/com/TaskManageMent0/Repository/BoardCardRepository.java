package com.TaskManageMent0.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.TaskManageMent0.Entity.BoardCard;

public interface BoardCardRepository extends JpaRepository<BoardCard, Long> {

    List<BoardCard> findByBoardIdAndColumnIdOrderByPosition(Long boardId, Long columnId);

    Optional<BoardCard> findByIssueId(Long issueId);

    long countByBoardIdAndColumnId(Long boardId, Long columnId);
}
