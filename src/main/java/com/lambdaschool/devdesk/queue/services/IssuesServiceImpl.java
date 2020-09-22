package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.exceptions.ResourceFoundException;
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

    @Autowired
    HelperFunctions helperFunctions;

    @Override
    public List<Issue> getAllIssues(boolean includeResolved) {
        List<Issue> issues;
        if(!includeResolved)
        {
            issues = issuesRepository.findAllByIsresolvedFalse();
            if(issues == null || issues.size() == 0)
            {
                throw new ResourceNotFoundException(String.format("unable to find unresolved issues"));
            }
        }
        else
        {
            issues = new ArrayList<>();
            issuesRepository.findAll().iterator().forEachRemaining(issues::add);
        }
        return issues;
    }

    @Override
    public List<Issue> getIssueByPartialUsername(String name, boolean includeResolved) {
        List<Issue> issues;
        if(!includeResolved)
        {
            issues = issuesRepository.findAllByCreateduser_UsernameContainingIgnoreCaseOrderByCreateduserDesc(name);
            if(issues.size() == 0)
            {
                throw new ResourceNotFoundException(String.format("Unable to find unresolved issues with partial username %s", name));
            }
        }
        else
        {
            issues = issuesRepository.findAllByCreateduser_UsernameContainingIgnoreCaseAndIsresolvedFalseOrderByCreateduserDesc(name);
            if(issues.size() == 0)
            {
                throw new ResourceNotFoundException(String.format("Unable to find issues with partial username %s", name));
            }
        }
        return issues;
    }

    @Override
    public List<Issue> getIssuesByCreatedUserId(long id, boolean includeResolved) {
        var issues = issuesRepository.findByCreateduser_IdOrderByTitleDesc(id);
        if(issues.size() == 0)
        {
            throw new ResourceNotFoundException(String.format("Unable to find issues with user id %d", id));
        }
        return issues;
    }

    @Override
    public Issue getIssueById(long id, boolean includeResolved) {
        Issue issue;
        if(!includeResolved)
        {
            issue = issuesRepository.findByIdAndIsresolvedFalse(id);
            if(issue == null)
            {
                throw new ResourceNotFoundException(String.format("unable to find issues with id %d", id));
            }
        }
        else
        {
            issue = issuesRepository.findById(id).orElseThrow(() -> {
                return new ResourceNotFoundException(String.format("unable to find unresolved issues with id %d", id));
            });

        }
        return issue;
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
    public Issue update(long id, Issue issue) {
        var dataIssue = getIssueById(id, true);
        if(helperFunctions.isAuthorizedToMakeChange(dataIssue.getCreateduser().getUsername()))
        {
            if(dataIssue.isIsresolved())
            {
                throw new ResourceFoundException("Unable to update a resolved issue");
            }
            if(issue.getAnswers().size() > 0)
            {
                throw new ResourceFoundException("Unable to modify answers via this endpoint");
            }
            if(issue.getCategory() != null)
            {
                dataIssue.setCategory(issue.getCategory());
            }
            if(issue.getDescription() != null)
            {
                dataIssue.setDescription(issue.getDescription());
            }
            if(issue.getTitle() != null)
            {
                dataIssue.setTitle(issue.getTitle());
            }
            if(issue.getWhatitried() != null)
            {
                dataIssue.setWhatitried(issue.getWhatitried());
            }
            if(issue.getCreateduser() != null)
            {
                throw new ResourceFoundException("Unable to modify users via this endpoint");
            }
            return issuesRepository.save(dataIssue);
        }
        else
        {
            return dataIssue;
        }
    }

    @Transactional
    @Override
    public void delete(Issue issue) {
        var dataIssue = getIssueById(issue.getId(), true);
        issuesRepository.delete(dataIssue);
    }

    @Transactional
    @Override
    public void delete(long id) {
        var dataIssue = getIssueById(id, true);
        issuesRepository.delete(dataIssue);
    }
}
