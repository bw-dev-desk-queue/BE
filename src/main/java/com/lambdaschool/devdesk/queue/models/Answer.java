package com.lambdaschool.devdesk.queue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "answers")
public class Answer extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer;
    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonIgnoreProperties(value = "answers", allowSetters = true)
    private User createduser;
    @ManyToOne()
    @JoinColumn(name = "answers", nullable = false)
    @JsonIgnoreProperties(value = {"answers", "createduser"}, allowSetters = true)
    private Issue issue;

    public Answer() {
    }

    public Answer(String answer, User createduser, Issue issue) {
        this.answer = answer;
        this.createduser = createduser;
        this.issue = issue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public User getCreateduser() {
        return createduser;
    }

    public void setCreateduser(User createduser) {
        this.createduser = createduser;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }
}
