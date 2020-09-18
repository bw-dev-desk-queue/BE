package com.lambdaschool.devdesk.queue.repositories;

import com.lambdaschool.devdesk.queue.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByNameIgnoreCase(String name);
}
