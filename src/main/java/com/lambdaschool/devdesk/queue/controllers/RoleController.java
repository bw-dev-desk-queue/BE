package com.lambdaschool.devdesk.queue.controllers;

import com.lambdaschool.devdesk.queue.models.RoleMinimum;
import com.lambdaschool.devdesk.queue.services.RoleServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleServices roleServices;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(path = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllRoles()
    {
        var roles = roleServices.getAllMinRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping(path = "/role/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRoleById(@PathVariable long id)
    {
        var role = roleServices.getRoleById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(path = "/roles", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewRole(@Valid @RequestBody RoleMinimum role)
    {
        var createdRole = roleServices.save(role);

        URI issueLocation = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path("/roles/role/{id}")
                .buildAndExpand(createdRole.getId())
                .toUri();
        var headers = new HttpHeaders();
        headers.setLocation(issueLocation);
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

}
