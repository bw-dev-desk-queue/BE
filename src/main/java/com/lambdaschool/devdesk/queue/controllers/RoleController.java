package com.lambdaschool.devdesk.queue.controllers;

import com.lambdaschool.devdesk.queue.services.RoleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleServices roleServices;

    @GetMapping(path = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllRoles()
    {
        var roles = roleServices.getAllMinRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

}
