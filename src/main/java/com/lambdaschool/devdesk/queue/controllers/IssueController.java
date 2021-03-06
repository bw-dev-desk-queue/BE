package com.lambdaschool.devdesk.queue.controllers;

import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.Issue;
import com.lambdaschool.devdesk.queue.services.HelperFunctions;
import com.lambdaschool.devdesk.queue.services.IssueServices;
import com.lambdaschool.devdesk.queue.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/issues")
public class IssueController {
    @Autowired
    private IssueServices issueServices;

    @Autowired
    private UserServices userServices;

    @GetMapping(path = "/issues", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllIssues(@PathParam("includeresolved") boolean includeresolved)
    {
        var issues = issueServices.getAllIssues(includeresolved);
        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @GetMapping(path = "/issue/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIssueById(@PathVariable long id, @PathParam("includeresolved") boolean includeresolved)
    {
        var issue = issueServices.getIssueById(id, includeresolved);
        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @GetMapping(path = "/username/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIssuesByUsernameLike(@PathVariable String name, @PathParam("includeresolved") boolean includeresolved)
    {
        List<Issue> issues = issueServices.getIssuesByPartialUsername(name, includeresolved);
        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @GetMapping(path = "/userid/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLissuesByUserid(@PathVariable long id, @PathParam("includeresolved") boolean includeresolved)
    {
        List<Issue> issues = issueServices.getIssuesByCreatedUserId(id, includeresolved);
        return new ResponseEntity<>(issues, HttpStatus.OK);
    }

    @GetMapping(path = "/resolve/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> markIssueAsResolvedById(@PathParam("resolved") boolean resolved, @PathVariable long id)
    {
        issueServices.markResolved(id, resolved);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/issues", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewIssue(@RequestBody @Valid Issue issue)
    {
        var authName = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userServices.findByName(authName);
        issue.setCreateduser(user);
        var newIssue = issueServices.save(issue);
        URI issueLocation = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/issues/issue/{id}")
                .buildAndExpand(newIssue.getId())
                .toUri();
        var headers = new HttpHeaders();
        headers.setLocation(issueLocation);
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

    @PutMapping(path = "/issue/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateIssueById(@PathVariable long id, @RequestBody Issue issue)
    {
        var updated = issueServices.update(id, issue);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping(path = "/issue/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteIssueById(@PathVariable long id)
    {
        issueServices.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
