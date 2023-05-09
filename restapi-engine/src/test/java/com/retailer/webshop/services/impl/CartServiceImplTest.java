package com.retailer.webshop.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.retailer.webshop.entities.Cart;
import com.retailer.webshop.entities.Customer;
import com.retailer.webshop.entities.Order;
import com.retailer.webshop.events.NotificationEventPublisher;
import com.retailer.webshop.exceptions.CartNotFound;
import com.retailer.webshop.exceptions.InvalidIdException;
import com.retailer.webshop.repositories.CartItemRepository;
import com.retailer.webshop.repositories.CartRepository;
import com.retailer.webshop.services.CustomerService;
import com.retailer.webshop.services.OrderService;
import com.retailer.webshop.services.ProductService;
import com.retailer.webshop.services.impl.CartServiceImpl;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

  private CartServiceImpl cartService;

  @Mock
  private CartRepository cartRepository;

  @Mock
  private CartItemRepository cartItemRepository;

  @Mock
  private ProductService productService;

  @Mock
  private OrderService orderService;

  @Mock
  private NotificationEventPublisher notificationEventPublisher;

  @Mock
  private CustomerService customerService;

  @BeforeEach
  public void setUp() {
    cartService = new CartServiceImpl(cartRepository, cartItemRepository, productService,
        orderService, notificationEventPublisher, customerService);
  }

  @Test
  public void getCart_validCartId_returnCart() {
    // Given
    UUID cartId = UUID.randomUUID();
    Cart cart = new Cart();
    cart.setId(cartId);

    given(cartRepository.findById(cartId)).willReturn(Optional.of(cart));

    // When
    Cart result = cartService.getCart(cartId);

    // Then
    assertThat(result).isEqualTo(cart);
  }

  @Test
  public void getCart_nullCartId_throwInvalidIdException() {
    // Given
    UUID cartId = null;

    // When
    assertThatThrownBy(() -> cartService.getCart(cartId))
        .isInstanceOf(InvalidIdException.class);
  }

  @Test
  public void getCart_invalidCartId_throwCartNotFoundException() {
    // Given
    UUID cartId = UUID.randomUUID();

    given(cartRepository.findById(cartId)).willReturn(Optional.empty());

    // When / Then
    assertThatThrownBy(() -> cartService.getCart(cartId)).isInstanceOf(CartNotFound.class);
  }

  @Test
  void checkout_validCart_returnsNewOrder() {
    // Given
    UUID cartId = UUID.randomUUID();
    Cart cart = new Cart();
    cart.setId(cartId);
    cart.setTotalAmount(100.0);

    String firstName = "John";
    String lastName = "Smith";
    String address = "Amsterdam, ...";
    String mobile = "1234567";
    String email = "info@gmail.com";

    Customer customer = new Customer();
    customer.setFirstName(firstName);
    customer.setLastName(lastName);
    customer.setAddress(address);
    customer.setMobile(mobile);
    customer.setEmail(email);

    Order expectedOrder = new Order();
    expectedOrder.setCustomer(customer);

    CartRepository cartRepository = mock(CartRepository.class);
    when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

    CustomerService customerService = mock(CustomerService.class);
    when(customerService.saveCustomer(firstName, lastName, address, email, mobile)).thenReturn(
        customer);

    OrderService orderService = mock(OrderService.class);
    when(orderService.createOrder(cart, customer)).thenReturn(expectedOrder);

    NotificationEventPublisher notificationEventPublisher = mock(NotificationEventPublisher.class);

    CartServiceImpl cartService = new CartServiceImpl(cartRepository, null, null, orderService,
        notificationEventPublisher, customerService);

    // When
    Order actualOrder = cartService.checkout(cartId, firstName, lastName, address, mobile, email);

    // Then
    verify(customerService).saveCustomer(firstName, lastName, address, email, mobile);
    verify(orderService).createOrder(cart, customer);
    verify(cartRepository).delete(cart);
    assertThat(actualOrder).isEqualTo(expectedOrder);
  }
}