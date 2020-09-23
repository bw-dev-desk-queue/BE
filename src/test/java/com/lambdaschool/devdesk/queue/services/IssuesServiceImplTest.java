package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.DevDeskQueueApplication;
import com.lambdaschool.devdesk.queue.exceptions.ResourceFoundException;
import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.Answer;
import com.lambdaschool.devdesk.queue.models.Issue;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevDeskQueueApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IssuesServiceImplTest {

    @Autowired
    IssueServices issueServices;

    @Autowired
    UserServices userServices;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void a_getAllIssues() {
        var issues = issueServices.getAllIssues(true);
        assertEquals(42, issues.size());
    }

    @Test
    public void b_getIssuesByPartialUsername() {
        var issues = issueServices.getIssuesByPartialUsername("joe", true);
        assertEquals(1, issues.size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void ba_getIssuesByPartialUsernameNotFound() {
        var issues = issueServices.getIssuesByPartialUsername("sdfsdfsdfsdfsdfsdf", true);
        assertEquals(issues.size(), 0);
    }

    @Test
    public void c_getIssuesByCreatedUserId() {
        var dataIssues = issueServices.getAllIssues(true);
        var randomIndex = new Random().nextInt(dataIssues.size());
        var toTest = dataIssues.get(randomIndex);
        var dataIssuesToTest = issueServices.getIssuesByCreatedUserId(toTest.getCreateduser().getId(), true);
        assertEquals(dataIssuesToTest.get(0).getCreateduser().getUsername(), toTest.getCreateduser().getUsername());
        assertEquals(4, dataIssuesToTest.size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void ca_getIssuesByCreatedUserIdNotFound() {
        var toTest = issueServices.getIssuesByCreatedUserId(1000000, true);
        assertEquals(toTest.size(), 0);
    }

    @Test
    public void d_getIssueById() {
        var issues = issueServices.getAllIssues(true);
        var toTest = issues.get(new Random().nextInt(issues.size()));
        var dataIssue = issueServices.getIssueById(toTest.getId(), true);
        assertEquals(toTest.getDescription(), dataIssue.getDescription());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void da_getIssueByIdNotFound() {
        var issue = issueServices.getIssueById(100000000, true);
        assertEquals(issue.getDescription(), "");
    }

    @Test
    public void e_save() {
        var userToCheck = userServices.findByName("admin");
        var newIssue = new Issue();
        newIssue.setCreateduser(userToCheck);
        newIssue.setWhatitried("TEST");
        newIssue.setTitle("TEST TITLE");
        newIssue.setDescription("SOME DESCRIPTION");
        newIssue.setCategory("SOME CATEGORY");
        var dataIssue = issueServices.save(newIssue);
        assertEquals(newIssue.getDescription(), dataIssue.getDescription());
        assertTrue(dataIssue.getId() > 0);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void ea_saveUserNotFound() {
        var userToCheck = userServices.findByName("admin");
        userToCheck.setId(1000000);
        var newIssue = new Issue();
        newIssue.setCreateduser(userToCheck);
        newIssue.setWhatitried("TEST");
        newIssue.setTitle("TEST TITLE");
        newIssue.setDescription("SOME DESCRIPTION");
        newIssue.setCategory("SOME CATEGORY");
        var dataIssue = issueServices.save(newIssue);
        assertEquals(newIssue.getDescription(), dataIssue.getDescription());
        assertTrue(dataIssue.getId() > 0);
    }

    @Test(expected = ResourceFoundException.class)
    public void ec_saveWithAnswers() {
        var userToCheck = userServices.findByName("admin");
        var newIssue = new Issue();
        newIssue.setCreateduser(userToCheck);
        newIssue.setWhatitried("TEST");
        newIssue.setTitle("TEST TITLE");
        newIssue.setDescription("SOME DESCRIPTION");
        newIssue.setCategory("SOME CATEGORY");
        Answer a = new Answer();
        a.setCreateduser(userToCheck);
        a.setIssue(newIssue);
        a.setAnswer("TEST");
        newIssue.getAnswers().add(a);
        var dataIssue = issueServices.save(newIssue);
        assertEquals(newIssue.getDescription(), dataIssue.getDescription());
        assertTrue(dataIssue.getId() > 0);
    }

    @Transactional
    @WithUserDetails("joedeertay")
    @Test
    public void f_update() {
        var joe = userServices.findByName("joedeertay");
        var issue = (Issue)joe.getIssues().toArray()[0];
        var toUpdate = new Issue();
        toUpdate.setTitle("New Title");
        toUpdate.setId(issue.getId());
        toUpdate.setWhatitried("New What I Tried");
        toUpdate.setDescription("New Description");
        toUpdate.setCategory("New Category");
        var updated = issueServices.update(toUpdate.getId(), toUpdate);
        assertEquals(updated.getDescription(), toUpdate.getDescription());
    }

    @Transactional
    @WithUserDetails("TestStudent")
    @Test(expected = ResourceNotFoundException.class)
    public void fa_updateNotAllowed() {
        var joe = userServices.findByName("joedeertay");
        var issue = (Issue)joe.getIssues().toArray()[0];
        var toUpdate = new Issue();
        toUpdate.setTitle("New Title");
        toUpdate.setId(issue.getId());
        toUpdate.setWhatitried("New What I Tried");
        toUpdate.setDescription("New Description");
        toUpdate.setCategory("New Category");
        var updated = issueServices.update(toUpdate.getId(), toUpdate);
        assertEquals(updated.getDescription(), toUpdate.getDescription());
    }

    @Transactional
    @Test
    @WithUserDetails("joedeertay")
    public void g_markResolved() {
        var joe = userServices.findByName("joedeertay");
        var joeIssue = (Issue) joe.getIssues().toArray()[0];
        issueServices.markResolved(joeIssue.getId(), true);
        var joeIssueResolved = issueServices.getIssueById(joeIssue.getId(), true);
        assertTrue(joeIssueResolved.isIsresolved());
    }

    @Transactional
    @Test(expected = ResourceNotFoundException.class)
    @WithUserDetails("TestStudent")
    public void ga_markResolvedNotAllowed() {
        var joe = userServices.findByName("joedeertay");
        var joeIssue = (Issue) joe.getIssues().toArray()[0];
        issueServices.markResolved(joeIssue.getId(), true);
        var joeIssueResolved = issueServices.getIssueById(joeIssue.getId(), true);
        assertTrue(joeIssueResolved.isIsresolved());
    }

    @Transactional
    @Test
    @WithUserDetails("joedeertay")
    public void gb_markResolvedNotAllowed() {
        var joe = userServices.findByName("joedeertay");
        var joeIssue = (Issue) joe.getIssues().toArray()[0];
        issueServices.markResolved(joeIssue.getId(), false);
        var joeIssueResolved = issueServices.getIssueById(joeIssue.getId(), false);
        assertTrue(!joeIssueResolved.isIsresolved());
    }

}