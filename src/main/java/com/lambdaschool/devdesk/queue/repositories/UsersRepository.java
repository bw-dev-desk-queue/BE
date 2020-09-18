package com.lambdaschool.devdesk.queue.repositories;

import com.lambdaschool.devdesk.queue.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {
}
