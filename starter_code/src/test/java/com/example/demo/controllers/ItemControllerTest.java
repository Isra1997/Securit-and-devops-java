package com.example.demo.controllers;

import com.example.demo.TestUtilities;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest extends TestCase {

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private ItemController itemController;

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtilities.injectObject(itemController,"itemRepository",itemRepository);
    }

    @Test
    public void test_happy_scenario_getItems(){
        //Arrange
        when(itemRepository.findAll()).thenReturn(new ArrayList<>(List.of(getTestItem())));
        //Act
        ResponseEntity<List<Item>> items = itemController.getItems();
        //Assert
        assertEquals(200, items.getStatusCodeValue());
        assertEquals(1,items.getBody().size());
        assertEquals("shirt",items.getBody().get(0).getName());

    }

    @Test
    public void test_happy_scenario_getItem_By_Id(){
        //Arrange
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(getTestItem()));
        //Act
        ResponseEntity<Item> item = itemController.getItemById(1L);
        //Assert
        assertEquals(200, item.getStatusCodeValue());
        assertEquals("shirt",item.getBody().getName());

    }

    @Test
    public void test_happy_scenario_getItem_By_name(){
        //Arrange
        when(itemRepository.findByName("shirt")).thenReturn(new ArrayList<>(List.of(getTestItem())));
        //Act
        ResponseEntity<List<Item>> items = itemController.getItemsByName("shirt");
        //Assert
        assertEquals(200, items.getStatusCodeValue());
        assertEquals(1,items.getBody().size());
        assertEquals("shirt",items.getBody().get(0).getName());

    }

    @Test
    public void test_sad_scenario_getItem_By_name(){
        //Arrange
        when(itemRepository.findByName("shirt")).thenReturn(null);
        //Act
        ResponseEntity<List<Item>> items = itemController.getItemsByName("shirt");
        //Assert
        assertEquals(404, items.getStatusCodeValue());

    }


    public Item getTestItem(){
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(20));
        item.setName("shirt");
        item.setDescription("amazing shirt");
        return item;
    }

}