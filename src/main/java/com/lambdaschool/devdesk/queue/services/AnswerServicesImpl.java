package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.exceptions.ResourceFoundException;
import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.Answer;
import com.lambdaschool.devdesk.queue.models.User;
import com.lambdaschool.devdesk.queue.repositories.AnswersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerServicesImpl implements AnswerServices{

    @Autowired
    AnswersRepository answersRepository;

    @Autowired
    UserServices userServices;

    @Autowired
    IssueServices issueServices;

    @Override
    public List<Answer> findAnswersByUserId(long id) {
        var answers = answersRepository.findByCreateduser_Id(id);
        if(answers.size() == 0)
        {
            throw new ResourceNotFoundException(String.format("Unable to find answers for user id %d", id));
        }
        return answers;
    }

    @Override
    public List<Answer> findAnswersByUsernameContaining(String partialUsername) {
        var answers = answersRepository.findByCreateduser_UsernameContainingIgnoreCase(partialUsername);
        if(answers.size() == 0)
        {
            throw new ResourceNotFoundException(String.format("Unable to find answers with partial name of %s", partialUsername));
        }
        return answers;
    }

    @Override
    public Answer findAnswerById(long id) {
        return answersRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Unable to find answer by id %d", id))
        );
    }

    @Override
    public List<Answer> findByAnswerContaining(String partialAnswer) {
        var answers = answersRepository.findByAnswerContainingIgnoreCase(partialAnswer);
        if(answers.size() == 0)
        {
            throw new ResourceNotFoundException(String.format("Unable to find answer with text containing %s", partialAnswer));
        }
        return answers;
    }

    @Override
    public List<Answer> findByIssueId(long id) {
        var answers = answersRepository.findByIssue_Id(id);
        if(answers.size() == 0)
        {
            throw new ResourceNotFoundException(String.format("Unable to find answers with issue id %d", id));
        }
        return answers;
    }

    @Override
    public List<Answer> findAll() {
        List<Answer> answers = new ArrayList<>();
        answersRepository.findAll().iterator().forEachRemaining(answers::add);
        if(answers.size() == 0)
        {
            throw new ResourceNotFoundException(String.format("No answers yet!"));
        }
        return answers;
    }

    @Transactional
    @Override
    public Answer save(Answer answer) {
        Answer newAnswer = new Answer();
        User created = userServices.getById(answer.getCreateduser().getId());
        newAnswer.setCreateduser(created);
        var issue = issueServices.getIssueById(answer.getIssue().getId());
        newAnswer.setIssue(issue);
        newAnswer.setAnswer(answer.getAnswer());
        return answersRepository.save(newAnswer);
    }

    @Transactional
    @Override
    public Answer save(long issueId, Answer answer) {
        var dataIssue = issueServices.getIssueById(issueId);
        if(dataIssue.isIsresolved())
        {
            throw new ResourceFoundException(String.format("issue with id %d is already marked as resolved", issueId));
        }
        var newAnswer = new Answer();
        var userName = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userServices.findByName(userName);
        newAnswer.setCreateduser(user);
        newAnswer.setAnswer(answer.getAnswer());
        newAnswer.setIssue(dataIssue);
        return answersRepository.save(newAnswer);
    }

    @Transactional
    @Override
    public void delete(Answer answer) {
        var toDelete = findAnswerById(answer.getId());
        answersRepository.delete(toDelete);
    }

    @Transactional
    @Override
    public void delete(long id) {
        var toDelete = findAnswerById(id);
        answersRepository.delete(toDelete);
    }
}
