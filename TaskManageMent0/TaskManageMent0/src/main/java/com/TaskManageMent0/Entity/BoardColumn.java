package com.TaskManageMent0.Entity;

import javax.persistence.*;

import com.TaskManageMent0.Enum.IssueStatus;

@Entity
@Table(name = "board_columns",
       indexes = {@Index(columnList = "board_id,position")})
public class BoardColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String name;

    @Enumerated(EnumType.STRING)
    private IssueStatus statusKey;

    private Integer position;

    private Integer wipLimit;

    public BoardColumn() {}

    public Long getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IssueStatus getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(IssueStatus statusKey) {
        this.statusKey = statusKey;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getWipLimit() {
        return wipLimit;
    }

    public void setWipLimit(Integer wipLimit) {
        this.wipLimit = wipLimit;
    }
}
