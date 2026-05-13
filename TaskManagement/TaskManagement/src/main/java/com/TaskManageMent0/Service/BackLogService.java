package com.TaskManageMent0.Service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TaskManageMent0.Entity.Issue;
import com.TaskManageMent0.Entity.Sprint;
import com.TaskManageMent0.Enum.IssueType;
import com.TaskManageMent0.Enum.SprintState;
import com.TaskManageMent0.Repository.IssueRepository;
import com.TaskManageMent0.Repository.SprintRepository;

@Service
public class BackLogService {

    @Autowired
    private SprintRepository sprintRepo;

    @Autowired
    private IssueRepository issueRepo;

    public List<Issue> getBackLog(Long projectId) {
        return issueRepo.findByProjectIdAndSprintIdIsNullOrderByBackLogPosition(projectId);
    }

    @Transactional
    public void reorderBacklog(Long projectId, List<Long> orderedIssueId) {

        int pos = 0;

        for (Long issueId : orderedIssueId) {

            Issue issue = issueRepo.findById(issueId)
                    .orElseThrow(() -> new RuntimeException("Issue not found"));

            issue.setBackLogPosition(pos++);
            issueRepo.save(issue);
        }
    }

    @Transactional
    public Issue addIssueToSprint(Long issueId, Long sprintId) {

        Issue issue = issueRepo.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        Sprint sprint = sprintRepo.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found"));

        SprintState state = sprint.getState();

        if (state != SprintState.PLANNED &&
                state != SprintState.ACTIVE) {

            throw new RuntimeException(
                    "Cannot add issue to sprint in state: " + state);
        }

        issue.setSprintId(sprintId);
        issue.setBackLogPosition(null);

        return issueRepo.save(issue);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getBackLogHierarchy(Long projectId) {

        List<Issue> backLog = getBackLog(projectId);

        Map<Long, Map<String, Object>> epicMap = new LinkedHashMap<>();
        Map<Long, Issue> storyMap = new HashMap<>();

        for (Issue issue : backLog) {

            if (issue.getIssueType() == IssueType.EPIC) {

                Map<String, Object> epicData = new LinkedHashMap<>();

                epicData.put("epic", issue);
                epicData.put("stories", new ArrayList<Issue>());
                epicData.put("subtasks", new HashMap<Long, List<Issue>>());

                epicMap.put(issue.getId(), epicData);
            }

            if (issue.getIssueType() == IssueType.STORY) {
                storyMap.put(issue.getId(), issue);
            }
        }

        return Collections.singletonMap("epics", epicMap);
    }
}
