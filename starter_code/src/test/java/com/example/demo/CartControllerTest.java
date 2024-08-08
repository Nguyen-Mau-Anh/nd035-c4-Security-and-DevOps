package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.example.demo.TestHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartController cartController;

    @Test
    public void testAddToCartOK() {
        User user = getUser();
        when(userRepository.findByUsername(any())).thenReturn(user);
        Item item = getItem();
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        ModifyCartRequest request = getModifyCartRequest(user.getUsername(), item.getId());

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testAddToCartNotFoundUser() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        ModifyCartRequest request = getModifyCartRequest("any", 1L);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testAddToCartNotFoundItem() {
        User user = getUser();
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(itemRepository.findById(any())).thenReturn(Optional.empty());
        ModifyCartRequest request = getModifyCartRequest(user.getUsername(), 1L);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testRemoveFromCartOK() {
        User user = getUser();
        when(userRepository.findByUsername(any())).thenReturn(user);
        Item item = getItem();
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        ModifyCartRequest request = getModifyCartRequest(user.getUsername(), item.getId());

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody().getItems().stream().noneMatch(x -> x.getId().equals(item.getId())));
    }

    @Test
    public void testRemoveFromCartNotFoundUser() {
        when(userRepository.findByUsername(any())).thenReturn(null);
        ModifyCartRequest request = getModifyCartRequest("any", 1L);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testRemoveFromCartNotFoundItem() {
        User user = getUser();
        when(userRepository.findByUsername(any())).thenReturn(user);
        when(itemRepository.findById(any())).thenReturn(Optional.empty());
        ModifyCartRequest request = getModifyCartRequest(user.getUsername(), 1L);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
