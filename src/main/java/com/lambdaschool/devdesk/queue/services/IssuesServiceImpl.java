package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.Answer;
import com.lambdaschool.devdesk.queue.models.Issue;
import com.lambdaschool.devdesk.queue.repositories.IssuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class IssuesServiceImpl implements IssueServices{
    @Autowired
    IssuesRepository issuesRepository;

    @Autowired
    UserServices userServices;

    @Autowired
    AnswerServices answerServices;

    @Override
    public List<Issue> getAllIssues() {
        List<Issue> issues = new ArrayList<>();
        issuesRepository.findAll().iterator().forEachRemaining(issues::add);
        return issues;
    }

    @Override
    public List<Issue> getIssueByPartialUsername(String name) {
        var issues = issuesRepository.findAllByCreateduser_UsernameContainingIgnoreCaseOrderByCreateduserDesc(name);
        if(issues.size() == 0)
        {
            throw new ResourceNotFoundException(String.format("Unable to find issues with partial username %s", name));
        }
        return issues;
    }

    @Override
    public List<Issue> getIssuesByCreatedUserId(long id) {
        var issues = issuesRepository.findByCreateduser_IdOrderByTitleDesc(id);
        if(issues.size() == 0)
        {
            throw new ResourceNotFoundException(String.format("Unable to find issues with user id %d", id));
        }
        return issues;
    }

    @Override
    public Issue getIssueById(long id) {
        return issuesRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Unable to find issue with id %d", id))
                );
    }

    @Transactional
    @Override
    public Issue save(Issue issue){
        var i = new Issue();
        var user = userServices.getById(issue.getCreateduser().getId());
        i.setCreateduser(user);
        i.setDescription(issue.getDescription());
        i.setWhatitried(issue.getWhatitried());
        i.setTitle(issue.getTitle());
        i.setCategory(issue.getCategory());
        for(Answer a : issue.getAnswers())
        {
            if(a.getId() > 0)
            {
                var dataAnswer = answerServices.findAnswerById(a.getId());
                i.getAnswers().add(dataAnswer);
            }
            else
            {
                i.getAnswers().add(a);
            }
        }
        return issuesRepository.save(i);
    }

    @Transactional
    @Override
    public void delete(Issue issue) {
        var dataIssue = getIssueById(issue.getId());
        issuesRepository.delete(dataIssue);
    }

    @Transactional
    @Override
    public void delete(long id) {
        var dataIssue = getIssueById(id);
        issuesRepository.delete(dataIssue);
    }
}
