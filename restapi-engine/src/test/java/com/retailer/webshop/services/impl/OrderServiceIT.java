package com.retailer.webshop.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.retailer.webshop.BaseIT;
import com.retailer.webshop.entities.Cart;
import com.retailer.webshop.entities.Customer;
import com.retailer.webshop.entities.Order;
import com.retailer.webshop.entities.Product;
import com.retailer.webshop.exceptions.OrderNotFound;
import com.retailer.webshop.repositories.OrderRepository;
import com.retailer.webshop.repositories.ProductRepository;
import com.retailer.webshop.services.CartService;
import com.retailer.webshop.services.CustomerService;
import com.retailer.webshop.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceIT extends BaseIT {

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CartService cartService;

  @Autowired
  private CustomerService customerService;

  long orderId;
  double cartTotalPrice;

  @BeforeEach
  void setUp() {
    Product product1 = new Product();
    product1.setName("product1");
    product1.setPrice(100);
    Product savedProduct1 = productRepository.save(product1);

    Cart cart = cartService.createCart();
    cartService.addProductToCart(cart.getId(), savedProduct1.getId(), 5);

    cartTotalPrice = cart.getCartItems().stream()
        .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
        .sum();

    Customer customer = customerService.saveCustomer("John", "Smith", "Amsterdam, ...",
        "email@gmail.com", "1234567");

    orderId = orderService.createOrder(cart, customer).getId();
  }

  @Test
  public void getOrder_validAndAvailableOrderIdProvided_returnOrder() {
    // When
    Order fetchedOrder = orderService.getOrder(orderId);

    // Then
    assertThat(fetchedOrder).isNotNull();
    assertThat(fetchedOrder.getTotalAmount()).isEqualTo(cartTotalPrice);
  }

  @Test
  public void getOrder_notPersistedOrderIdProvided_throwOrderNotFoundException() {
    // Given
    long invalidId = 34567;

    // When / Then
    assertThatThrownBy(
        () -> orderService.getOrder(invalidId)).isInstanceOf(OrderNotFound.class)
        .hasMessage("The order with ID '%d' could not be found.".formatted(invalidId));
  }
}