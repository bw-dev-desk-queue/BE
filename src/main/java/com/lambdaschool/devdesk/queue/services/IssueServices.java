package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.models.Issue;

import java.util.List;

public interface IssueServices {
    List<Issue> getAllIssues(boolean includeResolved);
    List<Issue> getIssuesByPartialUsername(String name, boolean includeResolved);
    List<Issue> getIssuesByCreatedUserId(long id, boolean includeResolved);
    Issue getIssueById(long id, boolean includeResolved);
    void markResolved(long id, boolean isResolved);
    Issue update(long id, Issue issue);
    Issue save(Issue issue);
    void delete(Issue issue);
    void delete(long id);
}
