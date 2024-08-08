package com.example.demo;

import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.SecurityConstants;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static com.example.demo.TestHelper.getCreateUserRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiTest {
    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAuthorization() {
        ResponseEntity<User> response = doSignUp();
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        String findUserUrl = "http://localhost:" + port + "/api/user/id/1";
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(findUserUrl, User.class);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    public void testLogIn() throws JSONException {
//        ResponseEntity<User> responseSignup = doSignUp();
//        assertEquals(responseSignup.getStatusCode(), HttpStatus.OK);

        ResponseEntity<String> responseLogin = doLogin();
        assertEquals(responseLogin.getStatusCode(), HttpStatus.OK);
        assertTrue(!responseLogin.getHeaders().get(SecurityConstants.HEADER_STRING).isEmpty());
        assertTrue(responseLogin.getHeaders().get(SecurityConstants.HEADER_STRING)
                .stream().anyMatch(x -> x.contains(SecurityConstants.TOKEN_PREFIX)));
    }

    private ResponseEntity<User> doSignUp() {
        String signUpUrl = "http://localhost:" + port + "/api/user/create";

        CreateUserRequest createUserRequest = getCreateUserRequest("test12345");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<CreateUserRequest> request = new HttpEntity<>(createUserRequest, headers);

        ResponseEntity<User> response = restTemplate.postForEntity(signUpUrl, request, User.class);
        return response;
    }

    private ResponseEntity<String> doLogin() throws JSONException {
        String loginUrl = "http://localhost:" + port + "/login";

        JSONObject object = new JSONObject();
        object.put("username", "Willie");
        object.put("password", "test12345");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(object.toString(), headers);
        return restTemplate.postForEntity(loginUrl, request, String.class);
    }
}
