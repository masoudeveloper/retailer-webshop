package com.retailer.webshop;

import com.retailer.webshop.repositories.CartItemRepository;
import com.retailer.webshop.repositories.CartRepository;
import com.retailer.webshop.repositories.CustomerRepository;
import com.retailer.webshop.repositories.OrderItemRepository;
import com.retailer.webshop.repositories.OrderRepository;
import com.retailer.webshop.repositories.ProductRepository;
import com.retailer.webshop.services.CartService;
import com.retailer.webshop.services.CustomerService;
import com.retailer.webshop.services.OrderService;
import com.retailer.webshop.services.ReportService;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseIT {

  @Autowired
  protected CartService cartService;

  @Autowired
  protected CartRepository cartRepository;

  @Autowired
  protected CartItemRepository cartItemRepository;

  @Autowired
  protected ProductRepository productRepository;

  @Autowired
  protected OrderRepository orderRepository;

  @Autowired
  protected OrderItemRepository orderItemRepository;

  @Autowired
  protected ReportService reportService;

  @Autowired
  protected OrderService orderService;

  @Autowired
  protected CustomerRepository customerRepository;

  @Autowired
  protected CustomerService customerService;

  @AfterEach
  void tearDown() {
    orderItemRepository.deleteAll();
    orderRepository.deleteAll();
    cartItemRepository.deleteAll();
    cartRepository.deleteAll();
    productRepository.deleteAll();
    customerRepository.deleteAll();
  }
}
