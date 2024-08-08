package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestHelper {
    static CreateUserRequest getCreateUserRequest(String password) {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Willie");
        createUserRequest.setPassword(password);
        createUserRequest.setConfirmPassword("test12345");

        return createUserRequest;
    }

    static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Willie");
        user.setPassword("test12345");
        user.setCart(getCart(user));

        return user;
    }

    private static Cart getCart(User user) {
        Cart cart = new Cart();
        cart.setId(1L);
        List<Item> items = new ArrayList<>();
        items.add(getItem());
        cart.setItems(items);
        cart.setUser(user);
        cart.setTotal(BigDecimal.TEN);

        return cart;
    }

    static Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Glass");
        item.setDescription("A pair of glasses");
        item.setPrice(BigDecimal.valueOf(10000));

        return item;
    }

    static List<Item> getListItem() {
        List<Item> items = new ArrayList<>();
        for (long i = 1; i <=5; i++) {
            Item item = getItem();
            item.setId(i);
            items.add(item);
        }

        return items;
    }

    static ModifyCartRequest getModifyCartRequest(String username, long itemId) {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setQuantity(10);
        modifyCartRequest.setItemId(itemId);

        return modifyCartRequest;
    }

    static UserOrder getOrder(User user) {
        UserOrder order = new UserOrder();
        order.setId(1L);
        order.setUser(user);
        order.setItems(getListItem());
        order.setTotal(BigDecimal.TEN);

        return order;
    }

    static List<UserOrder> getListOrder(User user) {
        List<UserOrder> orders = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            UserOrder userOrder = getOrder(user);
            userOrder.setId(i);

            orders.add(userOrder);
        }

        return orders;
    }
}
