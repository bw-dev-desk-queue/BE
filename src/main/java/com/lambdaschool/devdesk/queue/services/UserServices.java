package com.lambdaschool.devdesk.queue.services;

import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.User;

import java.util.List;

public interface UserServices {
    List<User> getAllUsers();
    User getById(long id);
    User save(User user);
    User findByName(String name);
    List<User> findByNameLike(String name);
    void deleteUserById(long id);
}
