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
        for(Issue i : user.getIssues())
        {
            newUser.getIssues().add(i);
        }
        for(Answer a : user.getAnswers())
        {
            newUser.getAnswers().add(a);
        }
        for(UserRoles uRole : user.getRoles())
        {
            Role dataRole = roleServices.findByName(uRole.getRole().getName());
            newUser.getRoles().add(new UserRoles(newUser, dataRole));
        }
        return usersRepository.save(newUser);
    }
}
