package com.lambdaschool.devdesk.queue.controllers;

import com.lambdaschool.devdesk.queue.services.AnswerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    AnswerServices answerServices;

    @GetMapping(path = "/answers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAnswers()
    {
        var answers = answerServices.findAll();
        return new ResponseEntity<>(answers, HttpStatus.OK);
    }

    @GetMapping(path = "/answers/issueid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAnswersByIssueId(@PathVariable long id)
    {
        var answers = answerServices.findByIssueId(id);
        return new ResponseEntity<>(answers, HttpStatus.OK);
    }

    @GetMapping(path = "/answer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAnswerById(@PathVariable long id)
    {
        var answer = answerServices.findAnswerById(id);
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

}
