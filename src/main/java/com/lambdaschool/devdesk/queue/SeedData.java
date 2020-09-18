package com.lambdaschool.devdesk.queue;

import com.github.javafaker.*;
import com.lambdaschool.devdesk.queue.models.*;
import com.lambdaschool.devdesk.queue.services.AnswerServices;
import com.lambdaschool.devdesk.queue.services.IssueServices;
import com.lambdaschool.devdesk.queue.services.RoleServices;
import com.lambdaschool.devdesk.queue.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Transactional
@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    UserServices userServices;

    @Autowired
    RoleServices roleServices;

    @Autowired
    IssueServices issueServices;

    @Autowired
    AnswerServices answerServices;

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
            var created = userServices.save(u);
            for(int x = 0 ; x < new Random().nextInt(4); x++)
            {
                Issue issue = new Issue();
                issue.setTitle(faker.beer().name());
                issue.setDescription(faker.beer().style());
                issue.setCreateduser(created);
                var savedIssue = issueServices.save(issue);
                for(int y = 0 ; y < new Random().nextInt(2); y++)
                {
                    var tempUsers = userServices.getAllUsers();
                    var answer = new Answer();
                    answer.setIssue(savedIssue);
                    answer.setCreateduser(tempUsers.get(new Random().nextInt(tempUsers.size())));
                    answer.setAnswer(faker.backToTheFuture().quote());
                    answerServices.save(answer);
                }
            }
        }
    }
}
