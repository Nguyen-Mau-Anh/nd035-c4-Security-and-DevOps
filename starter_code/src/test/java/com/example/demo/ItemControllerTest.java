package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.TestHelper.getItem;
import static com.example.demo.TestHelper.getListItem;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    @Test
    public void testGetItemById() {
        Item item = getItem();
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetItems() {
        List<Item> listItem = getListItem();
        when(itemRepository.findAll()).thenReturn(listItem);
        ResponseEntity<List<Item>> response = itemController.getItems();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().size(), listItem.size());
    }

    @Test
    public void testGetItemsByNameOK() {
        List<Item> listItem = getListItem();
        when(itemRepository.findByName(any())).thenReturn(listItem);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Willie");

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().size(), listItem.size());
        assertEquals(response.getBody().stream().map(Item::getName).distinct().count(), 1);
        assertEquals(response.getBody().get(0).getName(), listItem.get(0).getName());
    }

    @Test
    public void testGetItemsByNameNull() {
        when(itemRepository.findByName(any())).thenReturn(null);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Willie");

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetItemsByNameEmpty() {
        when(itemRepository.findByName(any())).thenReturn(new ArrayList<>());
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Willie");

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
