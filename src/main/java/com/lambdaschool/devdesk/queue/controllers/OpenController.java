package com.lambdaschool.devdesk.queue.controllers;

import com.lambdaschool.devdesk.queue.exceptions.ResourceFoundException;
import com.lambdaschool.devdesk.queue.exceptions.ResourceNotFoundException;
import com.lambdaschool.devdesk.queue.models.Role;
import com.lambdaschool.devdesk.queue.models.User;
import com.lambdaschool.devdesk.queue.models.UserMinimum;
import com.lambdaschool.devdesk.queue.models.UserRoles;
import com.lambdaschool.devdesk.queue.services.RoleServices;
import com.lambdaschool.devdesk.queue.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class OpenController
{
    /**
     * A method in this controller adds a new user to the application so needs access to User Services to do this.
     */
    @Autowired
    private UserServices userService;

    /**
     * A method in this controller adds a new user to the application with the role User so needs access to Role Services to do this.
     */
    @Autowired
    private RoleServices roleService;

    /**
     * This endpoint always anyone to create an account with the default role of USER. That role is hardcoded in this method.
     *
     * @param httpServletRequest the request that comes in for creating the new user
     * @param newminuser         A special minimum set of data that is needed to create a new user
     * @return The token access and other relevent data to token access. Status of CREATED. The location header to look up the new user.
     * @throws URISyntaxException we create some URIs during this method. If anything goes wrong with that creation, an exception is thrown.
     */
    @PostMapping(value = "/createnewuser",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<?> addSelf(
            HttpServletRequest httpServletRequest,
            @Valid
            @RequestBody
                    UserMinimum newminuser)
            throws
            URISyntaxException
    {
        // Test to see if the username already exists in the database
        {
            User testuser = null;
            try{
                testuser = userService.findByName(newminuser.getUsername());
            }
            catch(ResourceNotFoundException ex)
            {

            }
            finally {
                if(testuser != null)
                {
                    throw new ResourceFoundException("That username already exists");
                }
            }
        }
        // Create the user
        User newuser = new User();
        List<Role> roles = new ArrayList<>();
        System.out.println(newminuser.getRoles());
        for(String r : newminuser.getRoles())
        {
            var role = roleService.findByName(r);
            roles.add(role);
        }


        newuser.setUsername(newminuser.getUsername());
        newuser.setPassword(newminuser.getPassword());

        // add the default role of user
        Set<UserRoles> newRoles = new HashSet<>();
        newRoles.add(new UserRoles(newuser,
                roleService.findByName("user")));
        for(Role r : roles)
        {
            newRoles.add(new UserRoles(newuser, r));
        }
        newuser.setRoles(newRoles);

        newuser = userService.save(newuser);

        // set the location header for the newly created resource
        // The location comes from a different controller!
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromUriString("localhost" + ":" + httpServletRequest.getLocalPort() + "/users/user/{id}")
                .buildAndExpand(newuser.getId())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        // return the access token
        // To get the access token, surf to the endpoint /login just as if a client had done this.
        RestTemplate restTemplate = new RestTemplate();
        String requestURI = "http://" + "localhost" + ":" + httpServletRequest.getLocalPort() + "/login";

        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON);

        String clientId = (System.getenv("CLIENTID") == null ? "client" : System.getenv("CLIENTID"));
        String clientSecret = (System.getenv("CLIENTSECRET") == null ? "secret" : System.getenv("CLIENTSECRET"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(acceptableMediaTypes);
        headers.setBasicAuth(clientId,
                clientSecret);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type",
                "password");
        map.add("scope",
                "read write trust");
        map.add("username",
                newminuser.getUsername());
        map.add("password",
                newminuser.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,
                headers);

        String theToken = restTemplate.postForObject(requestURI,
                request,
                String.class);

        return new ResponseEntity<>(theToken,
                responseHeaders,
                HttpStatus.CREATED);
    }

    /**
     * Prevents no favicon.ico warning from appearing in the logs. @ApiIgnore tells Swagger to ignore documenting this as an endpoint.
     */
    @ApiIgnore
    @GetMapping("favicon.ico")
    public void returnNoFavicon()
    {

    }

}
