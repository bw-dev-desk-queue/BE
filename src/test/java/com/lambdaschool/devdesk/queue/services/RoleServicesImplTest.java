package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.DevDeskQueueApplication;
import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.Role;
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

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevDeskQueueApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoleServicesImplTest {

    @Autowired
    RoleServices roleServices;

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
    public void a_getAllRoles() {
        assertEquals(4, roleServices.getAllRoles().size());
    }

    @Test
    public void ba_getRoleById() throws ResourceNotFoundException {
        List<Role> roles = roleServices.getAllRoles();
        var toTest = roles.get(new Random().nextInt(roles.size()));
        assertEquals(toTest.getName(), roleServices.getRoleById(toTest.getId()).getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void bb_getRoleByIdNotFound() throws ResourceNotFoundException
    {
        Role r = roleServices.getRoleById(100);
        assertEquals(r.getName(), "");
    }

    @Test
    public void c_save() throws ResourceNotFoundException {
        Role newRole = new Role();
        newRole.setName("TEST");
        var saved = roleServices.save(newRole);
        assertTrue(saved.getId() > 0);
    }

    @Test
    public void ca_saveMin()
    {

    }

    @Test
    public void d_saveWithUsers() throws ResourceNotFoundException
    {
        Role newRole = new Role();
        newRole.setName("USERTEST");
        newRole.getUsers().add(new UserRoles(userServices.getAllUsers().get(0), newRole));
        Role saved = roleServices.save(newRole);
        assertEquals(saved.getUsers().size(), 1);
    }

    @Test
    public void e_findByName() throws ResourceNotFoundException {
        var roles = roleServices.getAllRoles();
        var roleToTest = roles.get(new Random().nextInt(roles.size()));
        assertEquals(roleToTest.getName(), roleServices.findByName(roleToTest.getName()).getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void eb_findByNameNotFound() throws ResourceNotFoundException
    {
        var r = roleServices.findByName("lskdflsdjflkj");
        assertEquals(r.getName(), "");
    }
}