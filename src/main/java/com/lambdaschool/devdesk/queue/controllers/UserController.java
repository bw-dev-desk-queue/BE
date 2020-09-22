package com.lambdaschool.devdesk.queue.controllers;

import com.lambdaschool.devdesk.queue.models.User;
import com.lambdaschool.devdesk.queue.models.UserMinimum;
import com.lambdaschool.devdesk.queue.services.HelperFunctions;
import com.lambdaschool.devdesk.queue.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserServices userServices;

    @Autowired
    private HelperFunctions helperFunctions;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers()
    {
        List<User> users = userServices.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/whoami", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrentUser()
    {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = userServices.findByName(auth.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable long id)
    {
        User u = userServices.getById(id);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }
}
