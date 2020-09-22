package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.exceptions.ResourceFoundException;
import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.Role;
import com.lambdaschool.devdesk.queue.models.RoleMinimum;
import com.lambdaschool.devdesk.queue.models.User;
import com.lambdaschool.devdesk.queue.models.UserRoles;
import com.lambdaschool.devdesk.queue.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class RoleServicesImpl implements RoleServices {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserServices userServices;

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        roleRepository.findAll().iterator().forEachRemaining(roles::add);
        return roles;
    }

    @Override
    public List<RoleMinimum> getAllMinRoles() {
        var roles = getAllRoles();
        List<RoleMinimum> minRoles = new ArrayList<>();
        for(Role r : roles)
        {
            var newMinRole = new RoleMinimum();
            newMinRole.setId(r.getId());
            newMinRole.setName(r.getName());
            minRoles.add(newMinRole);
        }
        return minRoles;
    }

    @Override
    public Role getRoleById(long id) {
        Role r = roleRepository.findById(id).orElseThrow(() -> {
            return new ResourceNotFoundException(String.format("Role with id %d not found", id));
        });
        return r;
    }

    @Transactional
    @Override
    public Role save(Role role) {
        Role newRole = new Role();
        newRole.setName(role.getName());
        if(role.getUsers().size() > 0)
        {
            for(UserRoles uRole : role.getUsers())
            {
                User u = userServices.getById(uRole.getUser().getId());
                newRole.getUsers().add(new UserRoles(u, newRole));
            }
        }
        return roleRepository.save(newRole);
    }

    @Transactional
    @Override
    public Role save(RoleMinimum role) {
        var toSave = new Role();
        if(role.getName() == null)
        {
            throw new ResourceNotFoundException(String.format("Role name was not passed"));
        }
        var dataRole = roleRepository.findByNameIgnoreCase(role.getName());
        if(dataRole != null)
        {
            throw new ResourceFoundException(String.format("Role with that name already created"));
        }
        toSave.setName(role.getName());
        return roleRepository.save(toSave);
    }

    @Override
    public Role findByName(String name) {
        Role r = roleRepository.findByNameIgnoreCase(name);
        if(r == null)
        {
            throw new ResourceNotFoundException(String.format("Role with name %s not found", name));
        }
        return r;
    }
}
