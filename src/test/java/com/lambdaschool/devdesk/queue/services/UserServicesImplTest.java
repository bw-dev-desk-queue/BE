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

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void a_getAllUsers() {
        assertEquals(14, userServices.getAllUsers().size());
    }

    @Test
    public void b_getById() throws ResourceNotFoundException {
        var users = userServices.getAllUsers();
        var userToTest = users.get(new Random().nextInt(users.size()));
        assertEquals(userToTest.getUsername(), userServices.getById(userToTest.getId()).getUsername());
    }

    @Test
    public void c_save() throws ResourceNotFoundException {
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

    @Test
    public void d_findByName()
    {
        var toFind = "joedeertay";
        var u = userServices.findByName(toFind);
        assertEquals(u.getUsername(), "joedeertay");
    }

    @Test
    public void e_findByNameLike()
    {
        var users = userServices.findByNameLike("joedeer");
        assertEquals(users.size(), 1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void f_findByNameNotFound()
    {
        var user = userServices.findByName("zztopisdabomb");
        assertEquals(user.getUsername(), "");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void g_findByNameLikeNotFound()
    {
        var users = userServices.findByNameLike("zztopisdabomb");
        assertEquals(users, null);
    }
}