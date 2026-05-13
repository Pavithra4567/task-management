package com.TaskManageMent0.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.TaskManageMent0.Entity.Issue;
import com.TaskManageMent0.Service.BackLogService;

@RestController
@RequestMapping("/api/backlog")
public class BackLogController {

    @Autowired
    private BackLogService backLogService;

    @GetMapping("/{projectId}")
    public ResponseEntity<List<Issue>> getBackLog(@PathVariable Long projectId) {

        return ResponseEntity.ok(backLogService.getBackLog(projectId));
    }

    @PostMapping("/{projectId}/reorder")
    public ResponseEntity<String> reorder(
            @PathVariable Long projectId,
            @RequestBody List<Long> orderedIssueId) {

        backLogService.reorderBacklog(projectId, orderedIssueId);

        return ResponseEntity.ok("Backlog reordered");
    }

    @PutMapping("/add-to-sprint/{issueId}/{sprintId}")
    public ResponseEntity<Issue> addIssueToSprint(
            @PathVariable Long issueId,
            @PathVariable Long sprintId) {

        return ResponseEntity.ok(
                backLogService.addIssueToSprint(issueId, sprintId));
    }

    @GetMapping("/{projectId}/hierarchy")
    public ResponseEntity<Map<String, Object>> getHierarchy(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                backLogService.getBackLogHierarchy(projectId));
    }
}
