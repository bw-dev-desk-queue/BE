package com.lambdaschool.devdesk.queue;

import com.github.javafaker.Faker;
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

    private final String[] categories = {"Web Fundamentals",
            "Web Applications I",
            "Web Applications II",
            "Web API: Node",
            "Web API: Java",
            "Computer Science",
            "Lambda Labs"};

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Role admin = new Role("ADMIN");
        Role user = new Role("USER");
        Role student = new Role("STUDENT");
        Role helper = new Role("HELPER");
        admin = roleServices.save(admin);
        user = roleServices.save(user);
        student = roleServices.save(student);
        helper = roleServices.save(helper);
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("admin");
        adminUser.getRoles().add(new UserRoles(adminUser, admin));
        adminUser.getRoles().add(new UserRoles(adminUser, user));
        adminUser.getRoles().add(new UserRoles(adminUser, student));
        adminUser.getRoles().add(new UserRoles(adminUser, helper));
        userServices.save(adminUser);

        // Add a card coded Student
        {
            var newUser = new User();
            newUser.setUsername("TestStudent");
            newUser.setPassword("test123");
            newUser.getRoles().add(new UserRoles(newUser, student));
            newUser.getRoles().add(new UserRoles(newUser, user));
            var i = new Issue();
            i.setCategory(categories[new Random().nextInt(categories.length)]);
            i.setWhatitried("Absolutely Nothing");
            i.setCreateduser(newUser);
            i.setDescription("How do I do things with other things when things do some many other things? I just can't even.");
            i.setTitle("...Things?");
            newUser.getIssues().add(i);
            userServices.save(newUser);
        }
        // Add a hard coded Helper
        {
            var newUser = new User();
            newUser.setUsername("TestHelper");
            newUser.setPassword("test123");
            newUser.getRoles().add(new UserRoles(newUser, helper));
            newUser.getRoles().add(new UserRoles(newUser, user));
            userServices.save(newUser);
        }
        // Add a hard coded Student/Helper
        {
            var newUser = new User();
            newUser.setUsername("JoeDeertay");
            newUser.setPassword("digit123");
            newUser.getRoles().add(new UserRoles(newUser, student));
            newUser.getRoles().add(new UserRoles(newUser, user));
            newUser.getRoles().add(new UserRoles(newUser, helper));
            var i = new Issue();
            i.setCategory(categories[new Random().nextInt(categories.length)]);
            i.setWhatitried("Diggin' it");
            i.setCreateduser(newUser);
            i.setDescription("You're gonna stand there, owning a fireworks stand... and say you have no whistling bungholes... spleen splitters, whisker biscuits, honkey lighters, Hüsker Düs and don'ts. Cherry bombs, nipsy dazers, with or without the scooter stick... or one single whistling kitty-chaser? ");
            i.setTitle("Life's a garden. Dig it.");
            newUser.getIssues().add(i);
            userServices.save(newUser);
        }

        for(int i = 0 ; i < 10 ; i++)
        {
            User u = new User();
            String userName = faker.hobbit().character().replaceAll("[\\s|-|\\.|'|\"|\\(|\\)]", "");
            userName = String.format("%s%d", userName, i);
            u.setUsername(userName);
            u.setPassword(faker.internet().password());
            u.getRoles().add(new UserRoles(u, user));
            u.getRoles().add(new UserRoles(u, student));
            if(new Random().nextInt(10) > 5)
            {
                u.getRoles().add(new UserRoles(u, helper));
            }
            var created = userServices.save(u);
            for(int x = 0 ; x < new Random().nextInt(4); x++)
            {
                Issue issue = new Issue();
                issue.setTitle(faker.beer().name());
                issue.setDescription(faker.beer().style());
                issue.setWhatitried(faker.chuckNorris().fact());
                issue.setCategory(categories[new Random().nextInt(categories.length)]);
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
