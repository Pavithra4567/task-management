package com.TaskManageMent0.Entity;

import com.TaskManageMent0.Enum.IssueType;
import jakarta.persistence.*;

@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Long projectId;
    private Long sprintId;
    private Long epicId;
    private Long parentIssueId;
    private Integer backLogPosition;

    @Enumerated(EnumType.STRING)
    private IssueType issueType;

    public Issue() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public Long getSprintId() { return sprintId; }
    public void setSprintId(Long sprintId) { this.sprintId = sprintId; }

    public Long getEpicId() { return epicId; }
    public void setEpicId(Long epicId) { this.epicId = epicId; }

    public Long getParentIssueId() { return parentIssueId; }
    public void setParentIssueId(Long parentIssueId) { this.parentIssueId = parentIssueId; }

    public Integer getBackLogPosition() { return backLogPosition; }
    public void setBackLogPosition(Integer backLogPosition) { this.backLogPosition = backLogPosition; }

    public IssueType getIssueType() { return issueType; }
    public void setIssueType(IssueType issueType) { this.issueType = issueType; }
}
