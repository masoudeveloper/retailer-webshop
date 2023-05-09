package com.retailer.webshop.services;

import com.retailer.webshop.entities.Cart;
import com.retailer.webshop.entities.Customer;
import com.retailer.webshop.entities.Order;

public interface OrderService {

  Order getOrder(Long orderId);

  Order createOrder(Cart cart, Customer customer);
}
