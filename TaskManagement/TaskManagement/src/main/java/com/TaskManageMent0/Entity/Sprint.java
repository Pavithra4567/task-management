package com.TaskManageMent0.Entity;

import com.TaskManageMent0.Enum.SprintState;
import jakarta.persistence.*;

@Entity
@Table(name = "sprints")
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sprintName;

    @Enumerated(EnumType.STRING)
    private SprintState state;

    public Sprint() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSprintName() { return sprintName; }
    public void setSprintName(String sprintName) { this.sprintName = sprintName; }

    public SprintState getState() { return state; }
    public void setState(SprintState state) { this.state = state; }
}
