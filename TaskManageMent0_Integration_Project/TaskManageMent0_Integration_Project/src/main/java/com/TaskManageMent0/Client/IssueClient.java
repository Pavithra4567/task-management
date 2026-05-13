package com.TaskManageMent0.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.TaskManageMent0.Enum.IssueStatus;

@FeignClient(name = "issue-service", url = "${issue-service.url}")
public interface IssueClient {

    @PutMapping("/api/issues/{id}/status")
    void updateStatus(
            @PathVariable Long id,
            @RequestParam IssueStatus issueStatus,
            @RequestParam String performedBy);

    @PostMapping("/api/issues/{id}/commit")
    void addCommit(
            @PathVariable Long id,
            @RequestParam String author,
            @RequestParam String body);
}
