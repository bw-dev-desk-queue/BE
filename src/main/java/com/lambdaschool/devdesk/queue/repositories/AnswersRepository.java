package com.lambdaschool.devdesk.queue.repositories;

import com.lambdaschool.devdesk.queue.models.Answer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnswersRepository extends CrudRepository<Answer, Long> {
    List<Answer> findByAnswerContainingIgnoreCase(String answer);
    List<Answer> findByCreateduser_UsernameContainingIgnoreCase(String partialUsername);
    List<Answer> findByCreateduser_Id(long id);
    List<Answer> findByIssue_Id(long id);
}
