package com.lambdaschool.devdesk.queue.controllers;

import com.lambdaschool.devdesk.queue.exceptions.ResourceFoundException;
import com.lambdaschool.devdesk.queue.models.Answer;
import com.lambdaschool.devdesk.queue.services.AnswerServices;
import com.lambdaschool.devdesk.queue.services.IssueServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    AnswerServices answerServices;

    @Autowired
    IssueServices issueServices;

    @GetMapping(path = "/answers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllAnswers()
    {
        var answers = answerServices.findAll();
        return new ResponseEntity<>(answers, HttpStatus.OK);
    }

    @GetMapping(path = "/issueid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(path = "issueid/{issueid}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewAnswer(@PathVariable long issueid, @Valid @RequestBody Answer answer)
    {
        var ans = answerServices.save(issueid, answer);
        URI issueLocation = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/answers/answer/{id}")
                .buildAndExpand(ans.getId())
                .toUri();
        var headers = new HttpHeaders();
        headers.setLocation(issueLocation);
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }
}
