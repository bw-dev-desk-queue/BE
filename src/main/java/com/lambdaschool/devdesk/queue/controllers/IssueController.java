package com.lambdaschool.devdesk.queue.controllers;

import com.lambdaschool.devdesk.queue.models.Issue;
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
import java.util.List;

@RestController
@RequestMapping("/issues")
public class IssueController {
    @Autowired
    private IssueServices issueServices;

    @GetMapping(path = "/issues", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllIssues()
    {
        var issues = issueServices.getAllIssues();
        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @GetMapping(path = "/issue/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIssueById(@PathVariable long id)
    {
        var issue = issueServices.getIssueById(id);
        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @GetMapping(path = "/issues/username/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIssuesByUsernameLike(@PathVariable String name)
    {
        List<Issue> issues = issueServices.getIssueByPartialUsername(name);
        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @GetMapping(path = "/issues/userid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLissuesByUserid(@PathVariable long id)
    {
        List<Issue> issues = issueServices.getIssuesByCreatedUserId(id);
        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @PostMapping(path = "/issues", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewIssue(@RequestBody @Valid Issue issue)
    {
        var newIssue = issueServices.save(issue);
        URI issueLocation = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/issues/issue/{id}")
                .buildAndExpand(newIssue.getId())
                .toUri();
        var headers = new HttpHeaders();
        headers.setLocation(issueLocation);
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }
}
