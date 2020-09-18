package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.models.Issue;

import java.util.List;

public interface IssueServices {
    List<Issue> getAllIssues();
    List<Issue> getIssueByPartialUsername(String name);
    List<Issue> getIssuesByCreatedUserId(long id);
    Issue getIssueById(long id);
    Issue save(Issue issue);
    void delete(Issue issue);
    void delete(long id);
}
