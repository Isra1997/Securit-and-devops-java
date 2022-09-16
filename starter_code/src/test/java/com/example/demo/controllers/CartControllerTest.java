package com.example.demo.controllers;

import com.example.demo.TestUtilities;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.PushBuilder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest extends TestCase {


    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private CartController cartController;

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtilities.injectObject(cartController,"userRepository",userRepository);
        TestUtilities.injectObject(cartController,"cartRepository",cartRepository);
        TestUtilities.injectObject(cartController,"itemRepository",itemRepository);
    }


    @Test
    public void test_happy_scenario_addTocart(){
        //Arrange
        ModifyCartRequest modifyCartRequestMock = getTestModifyCartRequest();
        User testUser = createUser();
        when(userRepository.findByUsername("testUsername")).thenReturn(testUser);
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(getTestItem()));
        when(cartRepository.save(testUser.getCart())).thenReturn(null);
        //Act
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequestMock);
        //Assert
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(3,response.getBody().getItems().size());

    }

    @Test
    public void test_sad_scenario_addTocart_user_not_found(){
        //Arrange
        ModifyCartRequest modifyCartRequestMock = getTestModifyCartRequest();
        when(userRepository.findByUsername("testUsername")).thenReturn(null);
        //Act
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequestMock);
        //Assert
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void test_sad_scenario_addTocart_item_not_found(){
        //Arrange
        ModifyCartRequest modifyCartRequestMock = getTestModifyCartRequest();
        User testUser = createUser();
        when(userRepository.findByUsername("testUsername")).thenReturn(testUser);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        //Act
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequestMock);
        //Assert
        assertEquals(404,response.getStatusCodeValue());
    }



    @Test
    public void test_happy_scenario_removeFromCart(){
        //Arrange
        ModifyCartRequest modifyCartRequestMock = getTestModifyCartRequest();
        User testUser = createUser();
        when(userRepository.findByUsername("testUsername")).thenReturn(testUser);
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(testUser.getCart().getItems().get(0)));
        when(cartRepository.save(testUser.getCart())).thenReturn(null);
        //Act
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequestMock);
        //Assert
        assertEquals(200,response.getStatusCodeValue());
        assertEquals(0,response.getBody().getItems().size());

    }

    @Test
    public void test_sad_scenario_removeFromCart_user_not_found(){
        //Arrange
        ModifyCartRequest modifyCartRequestMock = getTestModifyCartRequest();
        User testUser = createUser();
        when(userRepository.findByUsername("testUsername")).thenReturn(null);
        //Act
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequestMock);
        //Assert
        assertEquals(404,response.getStatusCodeValue());
    }


    @Test
    public void  test_sad_scenario_removeFromCart_item_not_found(){
        //Arrange
        ModifyCartRequest modifyCartRequestMock = getTestModifyCartRequest();
        User testUser = createUser();
        when(userRepository.findByUsername("testUsername")).thenReturn(testUser);
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        //Act
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequestMock);
        //Assert
        assertEquals(404,response.getStatusCodeValue());
    }



    public ModifyCartRequest getTestModifyCartRequest(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("testUsername");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(2);
        return modifyCartRequest;
    }

    public User createUser(){
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        Cart cart = new Cart();
        cart.setUser(user);
        Item item = new Item();
        item.setName("cocacola");
        item.setPrice(BigDecimal.valueOf(2));
        item.setId(1L);
        cart.addItem(item);
        user.setCart(cart);
        return user;
    }

    public Item getTestItem(){
        Item item = new Item();
        item.setId(2L);
        item.setPrice(BigDecimal.valueOf(20));
        item.setName("shirt");
        item.setDescription("amazing shirt");
        return item;
    }

}