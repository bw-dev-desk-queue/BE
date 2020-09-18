package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.models.Role;
import com.lambdaschool.devdesk.queue.models.User;
import com.lambdaschool.devdesk.queue.models.UserRoles;
import com.lambdaschool.devdesk.queue.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
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
