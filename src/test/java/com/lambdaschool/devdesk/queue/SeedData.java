package com.lambdaschool.devdesk.queue;

import com.github.javafaker.Faker;
import com.lambdaschool.devdesk.queue.models.Role;
import com.lambdaschool.devdesk.queue.models.User;
import com.lambdaschool.devdesk.queue.models.UserRoles;
import com.lambdaschool.devdesk.queue.services.RoleServices;
import com.lambdaschool.devdesk.queue.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    UserServices userServices;

    @Autowired
    RoleServices roleServices;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Role admin = new Role("ADMIN");
        Role user = new Role("USER");
        Role student = new Role("STUDENT");
        Role helper = new Role("HELPER");
        roleServices.save(admin);
        roleServices.save(user);
        roleServices.save(student);
        roleServices.save(helper);
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("admin");
        adminUser.getRoles().add(new UserRoles(adminUser, admin));
        adminUser.getRoles().add(new UserRoles(adminUser, user));
        adminUser.getRoles().add(new UserRoles(adminUser, student));
        adminUser.getRoles().add(new UserRoles(adminUser, helper));
        userServices.save(adminUser);
        for(int i = 0 ; i < 10 ; i++)
        {
            User u = new User();
            String userName = faker.funnyName().name().replaceAll("[\\s|-|\\.]", "");
            u.setUsername(userName);
            u.setPassword(faker.internet().password());
            u.getRoles().add(new UserRoles(u, user));
            userServices.save(u);
        }
    }
}
