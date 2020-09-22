package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.models.Answer;

import java.util.List;

public interface AnswerServices {
    List<Answer> findAnswersByUserId(long id);
    List<Answer> findAnswersByUsernameContaining(String partialUsername);
    List<Answer> findByAnswerContaining(String partialAnswer);
    List<Answer> findByIssueId(long id);
    List<Answer> findAll();
    Answer findAnswerById(long id);
    Answer save(Answer answer);
    Answer save(long issueId, Answer answer);
    void delete(Answer answer);
    void delete(long id);
}
