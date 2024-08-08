package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.example.demo.TestHelper.getListOrder;
import static com.example.demo.TestHelper.getUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OrderControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void testSubmitOK() {
        User user = getUser();
        when(userRepository.findByUsername(any())).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testSubmitNotFoundUser() {
        when(userRepository.findByUsername(any())).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("any");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetOrderForUserOK() {
        User user = getUser();
        when(userRepository.findByUsername(any())).thenReturn(user);
        List<UserOrder> orderList = getListOrder(user);
        when(orderRepository.findByUser(any())).thenReturn(orderList);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().size(), orderList.size());
    }

    @Test
    public void testGetOrderNotFoundUser() {
        when(userRepository.findByUsername(any())).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("any");
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
