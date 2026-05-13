package com.TaskManageMent0.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.TaskManageMent0.Service.IntegrationService;

@RestController
@RequestMapping("/api/integration")
public class IntegrationController {

    @Autowired
    private IntegrationService integrationService;

    @PostMapping("/github")
    public ResponseEntity<?> processGithubEvent(
            @RequestHeader("X-GitHub-Event") String event,
            @RequestBody Map<String, Object> payload) {

        System.out.println("GitHub Event Received: " + event);

        integrationService.processGithubEvent(event.toUpperCase(), payload);

        return ResponseEntity.ok("GitHub Event Processed Successfully");
    }

    @PostMapping("/jenkins")
    public ResponseEntity<?> processJenkinsEvent(
            @RequestBody Map<String, Object> payload) {

        System.out.println("Jenkins Event Received");

        integrationService.processJenkinsEvents(payload);

        return ResponseEntity.ok("Jenkins Event Processed Successfully");
    }

    @PostMapping("/docker")
    public ResponseEntity<?> processDockerEvent(
            @RequestBody Map<String, Object> payload) {

        System.out.println("Docker Event Received");

        integrationService.processDockerEvent(payload);

        return ResponseEntity.ok("Docker Event Processed Successfully");
    }

    @PostMapping("/commit")
    public ResponseEntity<?> handleCommit(
            @RequestParam String message,
            @RequestParam String author) {

        integrationService.handleCommitMessage(message, author);

        return ResponseEntity.ok("Commit Processed");
    }

    @PostMapping("/pullRequest")
    public ResponseEntity<?> handlePR(
            @RequestParam String title,
            @RequestParam String author) {

        integrationService.handlePullRequest(title, author);

        return ResponseEntity.ok("Pull Request Processed");
    }
}
