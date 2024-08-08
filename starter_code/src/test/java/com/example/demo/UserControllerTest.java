package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static com.example.demo.TestHelper.getCreateUserRequest;
import static com.example.demo.TestHelper.getUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void testFindByIdSuccess() {
        User user = getUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(1L);

        Assertions.assertThat(response.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    public void testFindByUserNameOK() {
        User user = getUser();
        when(userRepository.findByUsername(any())).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("Willie");

        Assertions.assertThat(response.getStatusCode().equals(HttpStatus.OK));
        assertEquals(response.getBody().getUsername(), "Willie");
    }

    @Test
    public void testFindByUserNameFail() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<User> response = userController.findByUserName("Willie");

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateUserOK() {
        CreateUserRequest createUserRequest = getCreateUserRequest("test12345");

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getUsername(), "Willie");
    }

    @Test
    public void testCreateUserFail() {
        CreateUserRequest createUserRequest = getCreateUserRequest("test");

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
