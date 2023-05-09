package com.retailer.webshop.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.retailer.webshop.BaseIT;
import com.retailer.webshop.entities.Cart;
import com.retailer.webshop.entities.CartItem;
import com.retailer.webshop.entities.Product;
import com.retailer.webshop.exceptions.CartNotFound;
import com.retailer.webshop.exceptions.InvalidQuantityException;
import com.retailer.webshop.exceptions.ProductNotFound;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.transaction.annotation.Transactional;

class CartServiceIT extends BaseIT {

  UUID cartId;
  long productId1;
  long productId2;
  UUID randomCartId = UUID.randomUUID();
  long randomProductId = 54321;

  @BeforeEach
  void setUp() {
    Product product1 = new Product();
    product1.setName("product1");
    product1.setPrice(100);
    Product savedProduct1 = productRepository.save(product1);
    productId1 = savedProduct1.getId();

    Cart cart = cartRepository.save(new Cart());
    List<CartItem> cartItems = new ArrayList<>();
    CartItem cartItem = new CartItem(savedProduct1, 5, 500);
    cartItem.setCart(cart);
    cartItems.add(cartItem);
    cartItems = cartItemRepository.saveAll(cartItems);
    cart.setCartItems(cartItems);
    cart.setTotalAmount(500);
    cartRepository.save(cart);
    cartId = cart.getId();

    Product product2 = new Product();
    product2.setName("product2");
    product2.setPrice(110);
    Product savedProduct2 = productRepository.save(product2);
    productId2 = savedProduct2.getId();
  }

  @Test
  public void getCart_validAndAvailableCartIdProvided_returnCart() {
    // When
    Cart fetchedCart = cartService.getCart(cartId);

    // Then
    assertThat(fetchedCart).isNotNull();
    assertThat(fetchedCart.getTotalAmount()).isEqualTo(500);
  }

  @Test
  public void getCart_notPersistedCartIdProvided_throwCartNotFoundException() {
    // When / Then
    assertThatThrownBy(
        () -> cartService.getCart(randomCartId)).isInstanceOf(CartNotFound.class)
        .hasMessage("The cart with ID '%s' could not be found.".formatted(randomCartId));
  }

  @Test
  public void createCart_createdSuccessfully_shouldIncreaseCartsCountByOne() {
    // Given
    long countBefore = cartRepository.count();

    // When
    cartService.createCart();

    // Then
    assertThat(cartRepository.count()).isEqualTo(countBefore + 1);
  }

  @Test
  public void addProductToCart_notPersistedCartIdProvided_throwCartNotFoundException() {
    // When / Then
    assertThatThrownBy(
        () -> cartService.addProductToCart(randomCartId, productId2, 10)).isInstanceOf(
            CartNotFound.class)
        .hasMessage("The cart with ID '%s' could not be found.".formatted(randomCartId));
  }

  @Test
  public void addProductToCart_notPersistedProductIdProvided_throwProductNotFoundException() {
    // When / Then
    assertThatThrownBy(
        () -> cartService.addProductToCart(cartId, randomProductId, 10)).isInstanceOf(
            ProductNotFound.class)
        .hasMessage("The product with ID '%s' could not be found.".formatted(randomProductId));
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  public void addProductToCart_invalidQuantityProvided_throwInvalidQuantityException(
      int quantity) {
    // When / Then
    assertThatThrownBy(
        () -> cartService.addProductToCart(cartId, productId2, quantity)).isInstanceOf(
            InvalidQuantityException.class)
        .hasMessage("The quantity must be an integer greater than zero.");
  }

  @Transactional
  @Test
  public void addProductToCart_validParametersProvided_productIsAddedToTheCart() {
    cartService.addProductToCart(cartId, productId1, 10);

    Cart cart = cartRepository.findById(cartId).get();
    assertThat(cart.getCartItems()).isNotNull();
    assertThat(cart.getCartItems().size()).isOne();
    assertThat(cart.getTotalAmount()).isEqualTo(1500);
    assertThat(cart.getCartItems().get(0).getQuantity()).isEqualTo(15);
    assertThat(cart.getCartItems().get(0).getTotalAmount()).isEqualTo(1500);
  }

  @Transactional
  @Test
  public void addProductToCart_addedMultipleTimes_productsAreAddedToTheCart() {
    cartService.addProductToCart(cartId, productId1, 5);
    cartService.addProductToCart(cartId, productId2, 10);
    cartService.addProductToCart(cartId, productId1, 2);

    Cart cart = cartRepository.findById(cartId).get();
    assertThat(cart.getCartItems()).isNotNull();
    assertThat(cart.getCartItems().size()).isEqualTo(2);
    assertThat(cart.getTotalAmount()).isEqualTo(2300);
    assertThat(cart.getCartItems().get(0).getQuantity()).isEqualTo(12);
    assertThat(cart.getCartItems().get(0).getTotalAmount()).isEqualTo(1200);
    assertThat(cart.getCartItems().get(1).getQuantity()).isEqualTo(10);
    assertThat(cart.getCartItems().get(1).getTotalAmount()).isEqualTo(1100);
  }

  @Transactional
  @Test
  public void checkout_validParametersProvided_orderMustBeCreated() {
    // Given
    long currentNumberOfOrders = orderRepository.count();

    // When
    cartService.checkout(cartId, "John", "Smith", "Amsterdam, ...", "12345678", "test@test.com");

    // Then
    assertThat(orderRepository.count()).isEqualTo(currentNumberOfOrders + 1);
  }
}