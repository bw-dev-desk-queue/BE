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

    @Column(nullable = false)
    @NotNull
    private String category;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String whatitried;

    @Column(nullable = false)
    @NotNull
    private boolean isresolved = false;

    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonIgnoreProperties(value = "issues", allowSetters = true)
    private User createduser;

    @OneToMany(mappedBy = "issue", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"issue", "createduser"}, allowSetters = true)
    private Set<Answer> answers = new HashSet<>();

    public Issue() {
    }

    public Issue(String title, String description, String whatitried, String category, User createduser) {
        this.title = title;
        this.description = description;
        this.createduser = createduser;
        this.whatitried = whatitried;
        this.category = category;
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

    public String getWhatitried() {
        return whatitried;
    }

    public void setWhatitried(String whatitried) {
        this.whatitried = whatitried;
    }

    public boolean isIsresolved() {
        return isresolved;
    }

    public void setIsresolved(boolean isresolved) {
        this.isresolved = isresolved;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
