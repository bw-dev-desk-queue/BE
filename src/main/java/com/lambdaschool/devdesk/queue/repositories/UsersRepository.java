package com.lambdaschool.devdesk.queue.repositories;

import com.lambdaschool.devdesk.queue.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsersRepository extends CrudRepository<User, Long> {
    User findByUsernameIgnoreCase(String name);
    List<User> findByUsernameContainingIgnoreCase(String name);
}
