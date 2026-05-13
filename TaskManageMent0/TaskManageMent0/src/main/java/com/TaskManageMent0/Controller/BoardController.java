package com.TaskManageMent0.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.TaskManageMent0.Entity.Board;
import com.TaskManageMent0.Entity.BoardCard;
import com.TaskManageMent0.Entity.BoardColumn;
import com.TaskManageMent0.Service.BoardService;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @PostMapping
    public ResponseEntity<Board> create(@RequestBody Board board) {
        return ResponseEntity.ok(boardService.createBoard(board));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
        return ResponseEntity.ok(
                boardService.getByBoardId(id)
                        .orElseThrow(() -> new RuntimeException("Board not found"))
        );
    }

    @GetMapping("/{id}/columns")
    public ResponseEntity<List<BoardColumn>> getBoardColumns(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getBoardColumns(id));
    }

    @GetMapping("/{boardId}/cards/{columnId}")
    public ResponseEntity<List<BoardCard>> getBoardCards(
            @PathVariable Long boardId,
            @PathVariable Long columnId) {

        return ResponseEntity.ok(
                boardService.getBoardByCards(boardId, columnId)
        );
    }

    @PostMapping("/{id}/card")
    public ResponseEntity<BoardCard> addCard(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        Long columnId = Long.valueOf(body.get("columnId").toString());
        Long issueId = Long.valueOf(body.get("issueId").toString());

        return ResponseEntity.ok(
                boardService.addIssueToBoard(id, columnId, issueId)
        );
    }
}
