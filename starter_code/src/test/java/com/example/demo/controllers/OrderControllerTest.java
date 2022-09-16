package com.example.demo.controllers;

import com.example.demo.TestUtilities;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest extends TestCase {
    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private  OrderController orderController;

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtilities.injectObject(orderController,"userRepository",userRepository);
        TestUtilities.injectObject(orderController,"orderRepository",orderRepository);
    }

    @Test
    public void test_happy_scenario_submit_order(){
        //Arrange
        User testUser = createUser();
        when(userRepository.findByUsername("testUsername")).thenReturn(testUser);
        when(orderRepository.save(UserOrder.createFromCart(testUser.getCart()))).thenReturn(UserOrder.createFromCart(testUser.getCart()));
        //Act
        ResponseEntity<UserOrder> result = orderController.submit(testUser.getUsername());

        //Assert
        assertEquals(200,result.getStatusCodeValue());
        assertEquals(result.getBody().getItems().size(),1);
        assertEquals(result.getBody().getUser().getUsername(),"testUsername");
        assertEquals(result.getBody().getTotal(),BigDecimal.valueOf(2));
    }

    @Test
    public void test_happy_scenario_getOrdersForUser(){
        //Arrange
        User testUser = createUser();
        when(userRepository.findByUsername("testUsername")).thenReturn(testUser);
        ResponseEntity<UserOrder> result = orderController.submit(testUser.getUsername());
        when(orderRepository.findByUser(testUser)).thenReturn(new ArrayList<>(List.of(result.getBody())));

        //Act
        final ResponseEntity<List<UserOrder>> result2 =orderController.getOrdersForUser("testUsername");

        //Assert
        assertEquals(200,result2.getStatusCodeValue());
        assertEquals(0,result.getBody().getUser().getId());
        assertEquals("cocacola",result.getBody().getItems().get(0).getName());
    }

    @Test
    public void test_sadd_scenario_getOrdersForUser(){
        //Arrange
        when(userRepository.findByUsername("testUsername")).thenReturn(null);


        //Act
        final ResponseEntity<List<UserOrder>> result2 =orderController.getOrdersForUser("testUsername");

        //Assert
        assertEquals(404,result2.getStatusCodeValue());
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
}