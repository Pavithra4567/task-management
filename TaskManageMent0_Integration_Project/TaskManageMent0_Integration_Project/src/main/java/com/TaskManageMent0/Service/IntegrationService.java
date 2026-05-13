package com.TaskManageMent0.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TaskManageMent0.Client.IssueClient;
import com.TaskManageMent0.Enum.IssueStatus;

@Service
public class IntegrationService {

    @Autowired
    private IssueClient issueClient;

    public void handleCommitMessage(String msg, String author) {

        String regex = "([A-Z]+-\\d+)";
        Matcher matcher = Pattern.compile(regex).matcher(msg);

        if (matcher.find()) {

            String issueKey = matcher.group(1);
            Long issueId = extractId(issueKey);

            issueClient.updateStatus(issueId, IssueStatus.DONE, author);
            issueClient.addCommit(issueId, author, "Closed via commit: " + msg);
        }
    }

    public void handlePullRequest(String title, String author) {

        String regex = "([A-Z]+-\\d+)";
        Matcher matcher = Pattern.compile(regex).matcher(title);

        if (matcher.find()) {

            String issueKey = matcher.group(1);
            Long issueId = extractId(issueKey);

            issueClient.updateStatus(issueId, IssueStatus.IN_PROGRESS, author);
            issueClient.addCommit(issueId, author, "Pull Request Opened: " + title);
        }
    }

    public void processGithubEvent(String event, Map<String, Object> payload) {

        if ("PUSH".equalsIgnoreCase(event)) {
            handlePushCode(payload);
        }

        if ("PULL_REQUEST".equalsIgnoreCase(event)) {
            handlePullRequestEvent(payload);
        }
    }

    private void handlePushCode(Map<String, Object> payload) {

        Object commitObj = payload.get("commits");

        if (!(commitObj instanceof List)) {
            return;
        }

        List<?> commits = (List<?>) commitObj;

        for (Object obj : commits) {

            if (!(obj instanceof Map)) {
                continue;
            }

            Map<String, Object> commit = (Map<String, Object>) obj;

            String message = (String) commit.get("message");

            Map<String, Object> authorMap = (Map<String, Object>) commit.get("author");

            String author = authorMap != null ? (String) authorMap.get("name") : "Unknown";

            handleCommitMessage(message, author);
        }
    }

    private void handlePullRequestEvent(Map<String, Object> payload) {

        Map<String, Object> pr = (Map<String, Object>) payload.get("pull_request");

        if (pr == null) {
            return;
        }

        String title = (String) pr.get("title");

        Map<String, Object> user = (Map<String, Object>) pr.get("user");

        String author = user != null ? (String) user.get("login") : "Unknown";

        handlePullRequest(title, author);
    }

    public void processJenkinsEvents(Map<String, Object> body) {

        String jobName = (String) body.get("name");
        String result = (String) body.get("result");
        String log = (String) body.get("log");

        String issueKey = extractIssueKey(jobName);

        if (issueKey == null) {
            return;
        }

        Long issueId = extractId(issueKey);

        if ("FAILURE".equalsIgnoreCase(result)) {

            issueClient.addCommit(
                    issueId,
                    "Jenkins",
                    "Build Failed\n------ LOG START ------\n" + log + "\n------ LOG END ------");
        }

        if ("SUCCESS".equalsIgnoreCase(result)) {

            issueClient.addCommit(issueId, "Jenkins", "Build Successful");
        }
    }

    private String extractIssueKey(String text) {

        if (text == null) {
            return null;
        }

        Matcher matcher = Pattern.compile("([A-Z]+-\\d+)").matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private Long extractId(String issueKey) {

        return Long.parseLong(issueKey.split("-")[1]);
    }

    public void processDockerEvent(Map<String, Object> payload) {

        String status = (String) payload.get("status");
        String image = (String) payload.get("from");

        Map<String, Object> actor = (Map<String, Object>) payload.get("Actor");

        Map<String, Object> attributes =
                actor != null ? (Map<String, Object>) actor.get("Attributes") : null;

        String containerName =
                attributes != null ? (String) attributes.get("name") : "";

        String imageName =
                attributes != null ? (String) attributes.get("image") : image;

        String issueKey = extractIssueKey(containerName + " " + imageName);

        if (issueKey == null) {

            System.out.println("No issue key found in docker payload");
            return;
        }

        Long issueId = extractId(issueKey);

        switch (status.toLowerCase()) {

            case "start":

                issueClient.updateStatus(issueId, IssueStatus.DEPLOYMENT, "Docker");

                issueClient.addCommit(
                        issueId,
                        "Docker",
                        "Container Started | Image: " + imageName);

                break;

            case "die":

                issueClient.updateStatus(issueId, IssueStatus.BLOCKED, "Docker");

                issueClient.addCommit(
                        issueId,
                        "Docker",
                        "Container Crashed | Image: " + imageName);

                break;

            case "pull":

                issueClient.addCommit(
                        issueId,
                        "Docker",
                        "Image Pulled: " + imageName);

                break;

            case "build":

                issueClient.addCommit(
                        issueId,
                        "Docker",
                        "Docker Image Built: " + imageName);

                break;

            default:

                issueClient.addCommit(
                        issueId,
                        "Docker",
                        "Docker Event: " + status + " | Image: " + imageName);
        }
    }
}
