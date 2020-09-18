package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.Role;

import java.lang.module.ResolutionException;
import java.util.List;

public interface RoleServices {
    List<Role> getAllRoles();
    Role getRoleById(long id) throws ResourceNotFoundException;
    Role save(Role role) throws ResourceNotFoundException;
    Role findByName(String name) throws ResourceNotFoundException;
}
