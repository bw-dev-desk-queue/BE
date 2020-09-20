package com.lambdaschool.devdesk.queue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "issues")
public class Issue extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotNull
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String description;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonIgnoreProperties(value = "issues", allowSetters = true)
    private User createduser;

    @OneToMany(mappedBy = "issue", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"issue", "createduser"}, allowSetters = true)
    private Set<Answer> answers = new HashSet<>();

    public Issue() {
    }

    public Issue(String title, String description, User createduser) {
        this.title = title;
        this.description = description;
        this.createduser = createduser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreateduser() {
        return createduser;
    }

    public void setCreateduser(User createduser) {
        this.createduser = createduser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
}
