package com.lambdaschool.devdesk.queue.repositories;

import com.lambdaschool.devdesk.queue.models.Issue;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IssuesRepository extends CrudRepository<Issue, Long> {
    List<Issue> findByCreateduser_IdOrderByTitleDesc(long id);
    List<Issue> findAllByCreateduser_UsernameContainingIgnoreCaseOrderByCreateduserDesc(String partialName);
}
