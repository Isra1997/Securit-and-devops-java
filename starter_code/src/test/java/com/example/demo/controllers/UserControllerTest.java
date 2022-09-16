package com.example.demo.controllers;

import com.example.demo.TestUtilities;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest extends TestCase {

    private UserRepository userRepo = mock(UserRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    private CartRepository cartRepo = mock(CartRepository.class);

    private UserController userController;


    @Before
    public void setUp(){
        userController = new UserController();
        TestUtilities.injectObject(userController,"userRepository",userRepo);
        TestUtilities.injectObject(userController,"cartRepository",cartRepo);
        TestUtilities.injectObject(userController,"bCryptPasswordEncoder", bCryptPasswordEncoder);
    }


    @Test
    public void test_the_happy_test_create_user(){
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("testPassword");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> createdUser = userController.createUser(createUserRequest);

        assertNotNull(createdUser);
        assertEquals(200, createdUser.getStatusCodeValue());

        User u = createdUser.getBody();
        assertNotNull(u);
        assertEquals("test", u.getUsername());
        assertEquals("testPassword", u.getPassword());
    }

    @Test
    public void test_the_happy_get_user_by_id(){
        // Creating User
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        //asset that the user was created successfully
        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

        // Testing user lookup by Id
        when(userRepo.findById(u.getId())).thenReturn(Optional.of(u));
        final ResponseEntity<User> response2 = userController.findById(u.getId());
        assertEquals(200, response2.getStatusCodeValue());
        assertEquals("test", response2.getBody().getUsername());
        assertEquals("thisIsHashed",  response2.getBody().getPassword());
    }

    @Test
    public void test_the_happy_get_user_by_username(){
        // Creating User
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        //asset that the user was created successfully
        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

        // Testing user lookup by Id
        when(userRepo.findByUsername(u.getUsername())).thenReturn(u);
        final ResponseEntity<User> response2 = userController.findByUserName(u.getUsername());
        assertEquals(200, response2.getStatusCodeValue());
        assertEquals("test", response2.getBody().getUsername());
        assertEquals("thisIsHashed",  response2.getBody().getPassword());

    }

    @Test
    public void test_the_sad_get_user_by_username(){
        // Creating User
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        //asset that the user was created successfully
        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

        // Testing user lookup by Id
        when(userRepo.findByUsername("unknownUsername")).thenReturn(u);
        final ResponseEntity<User> response2 = userController.findByUserName(u.getUsername());
        assertEquals(404, response2.getStatusCodeValue());

    }



}