package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.*;
import com.lambdaschool.devdesk.queue.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServicesImpl implements UserServices {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RoleServices roleServices;

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        usersRepository.findAll().iterator().forEachRemaining(users::add);
        return users;
    }

    @Override
    public User getById(long id) {
        User toReturn = usersRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format("User with id %d not found", id)));
        return toReturn;
    }

    @Transactional
    @Override
    public User save(User user) {
        User newUser = new User();
        newUser.setPasswordNoEncrypt(user.getPassword());
        newUser.setUsername(user.getUsername().toLowerCase());
        for(UserRoles uRole : user.getRoles())
        {
            Role dataRole = roleServices.findByName(uRole.getRole().getName());
            newUser.getRoles().add(new UserRoles(newUser, dataRole));
        }
        var temp = usersRepository.save(newUser);
        for(Issue i : user.getIssues())
        {
            i.setCreateduser(temp);
            temp.getIssues().add(i);
        }
        for(Answer a : user.getAnswers())
        {
            a.setCreateduser(temp);
            temp.getAnswers().add(a);
        }
        return usersRepository.save(temp);
    }

    @Override
    public User findByName(String name) {
        var u = usersRepository.findByUsernameIgnoreCase(name);
        if(u == null)
        {
            throw new ResourceNotFoundException(String.format("Unable to find user with name %s", name));
        }
        return u;
    }
}
