package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.DevDeskQueueApplication;
import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.Role;
import com.lambdaschool.devdesk.queue.models.User;
import com.lambdaschool.devdesk.queue.models.UserRoles;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevDeskQueueApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServicesImplTest {

    @Autowired
    UserServices userServices;

    @Autowired
    RoleServices roleServices;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAllUsers() {
        assertEquals(11, userServices.getAllUsers().size());
    }

    @Test
    public void getById() throws ResourceNotFoundException {
        var users = userServices.getAllUsers();
        var userToTest = users.get(new Random().nextInt(users.size()));
        assertEquals(userToTest.getUsername(), userServices.getById(userToTest.getId()).getUsername());
    }

    @Test
    public void save() throws ResourceNotFoundException {
        var roles = roleServices.getAllRoles();
        User u = new User();
        u.setPassword("test");
        u.setUsername("TEST");
        for(Role r : roles)
        {
            u.getRoles().add(new UserRoles(u, r));
        }
        User saved = userServices.save(u);
        assertTrue(saved.getId() > 0);
        assertEquals(saved.getUsername(), u.getUsername().toLowerCase());
    }
}