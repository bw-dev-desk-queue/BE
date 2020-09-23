package com.lambdaschool.devdesk.queue.repositories;

import com.lambdaschool.devdesk.queue.models.Issue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssuesRepository extends CrudRepository<Issue, Long> {
    List<Issue> findByCreateduser_IdOrderByTitleDesc(long id);
    List<Issue> findAllByCreateduser_UsernameContainingIgnoreCaseOrderByCreateduserDesc(String partialName);
    List<Issue> findAllByCreateduser_UsernameContainingIgnoreCaseAndIsresolvedFalseOrderByCreateduserDesc(String partialName);
    List<Issue> findAllByIsresolvedFalse();
    Issue findByIdAndIsresolvedFalse(long id);
}
