package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.Role;
import com.lambdaschool.devdesk.queue.models.RoleMinimum;

import java.util.List;

public interface RoleServices {
    List<Role> getAllRoles();
    List<RoleMinimum> getAllMinRoles();
    Role getRoleById(long id) throws ResourceNotFoundException;
    Role save(Role role) throws ResourceNotFoundException;
    Role findByName(String name) throws ResourceNotFoundException;
}
