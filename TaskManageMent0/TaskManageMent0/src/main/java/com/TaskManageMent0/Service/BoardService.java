package com.TaskManageMent0.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TaskManageMent0.Entity.Board;
import com.TaskManageMent0.Entity.BoardCard;
import com.TaskManageMent0.Entity.BoardColumn;
import com.TaskManageMent0.Repository.BoardCardRepository;
import com.TaskManageMent0.Repository.BoardColumnRepository;
import com.TaskManageMent0.Repository.BoardRepository;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepo;

    @Autowired
    private BoardColumnRepository boardColumnRepo;

    @Autowired
    private BoardCardRepository boardCardRepo;

    public Board createBoard(Board board) {
        return boardRepo.save(board);
    }

    public Optional<Board> getByBoardId(Long id) {
        return boardRepo.findById(id);
    }

    public List<BoardColumn> getBoardColumns(Long boardId) {
        return boardColumnRepo.findByBoardIdOrderByPosition(boardId);
    }

    public List<BoardCard> getBoardByCards(Long boardId, Long columnId) {
        return boardCardRepo.findByBoardIdAndColumnIdOrderByPosition(boardId, columnId);
    }

    public BoardCard addIssueToBoard(Long boardId, Long columnId, Long issueId) {

        BoardColumn column = boardColumnRepo.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));

        List<BoardCard> existing = boardCardRepo
                .findByBoardIdAndColumnIdOrderByPosition(boardId, columnId);

        BoardCard card = new BoardCard();
        card.setBoardId(boardId);
        card.setIssueId(issueId);
        card.setColumn(column);
        card.setPosition(existing.size());

        return boardCardRepo.save(card);
    }
}
