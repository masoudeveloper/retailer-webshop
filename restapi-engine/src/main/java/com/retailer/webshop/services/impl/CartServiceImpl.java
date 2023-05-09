package com.retailer.webshop.services.impl;

import com.retailer.webshop.entities.Cart;
import com.retailer.webshop.entities.CartItem;
import com.retailer.webshop.entities.Customer;
import com.retailer.webshop.entities.Order;
import com.retailer.webshop.entities.Product;
import com.retailer.webshop.events.NotificationEventPublisher;
import com.retailer.webshop.events.NotificationType;
import com.retailer.webshop.exceptions.CartNotFound;
import com.retailer.webshop.exceptions.InvalidIdException;
import com.retailer.webshop.exceptions.InvalidQuantityException;
import com.retailer.webshop.repositories.CartItemRepository;
import com.retailer.webshop.repositories.CartRepository;
import com.retailer.webshop.services.CartService;
import com.retailer.webshop.services.CustomerService;
import com.retailer.webshop.services.OrderService;
import com.retailer.webshop.services.ProductService;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductService productService;
  private final OrderService orderService;
  private final NotificationEventPublisher notificationEventPublisher;
  private final CustomerService customerService;

  @Override
  public Cart getCart(UUID id) {
    if (id == null) {
      throw new InvalidIdException();
    }

    Optional<Cart> cart = cartRepository.findById(id);
    if (cart.isPresent()) {
      return cart.get();
    }

    throw new CartNotFound(id);
  }

  @Override
  public Cart createCart() {
    return cartRepository.save(new Cart());
  }

  @Override
  public Cart addProductToCart(UUID cartId, Long productId, int quantity) {
    if (quantity < 1) {
      throw new InvalidQuantityException();
    }

    Cart cart = getCart(cartId);
    List<CartItem> cartItems = new ArrayList<>();

    Product product = productService.getProduct(productId);

    Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
        .filter(item -> item.getProduct().getId().equals(productId))
        .findFirst();

    double totalAmount = cart.getTotalAmount();
    double newAddedTotalAmount = product.getPrice() * quantity;

    if (optionalCartItem.isPresent()) {
      CartItem cartItem = optionalCartItem.get();
      cartItem.setQuantity(cartItem.getQuantity() + quantity);
      cartItem.setTotalAmount(cartItem.getTotalAmount() + newAddedTotalAmount);
      cartItemRepository.save(cartItem);
    } else {
      CartItem cartItem = new CartItem();
      cartItem.setCart(cart);
      cartItem.setProduct(product);
      cartItem.setQuantity(quantity);
      cartItem.setTotalAmount(newAddedTotalAmount);
      cartItems.add(cartItem);
      cartItems = cartItemRepository.saveAll(cartItems);
    }

    totalAmount += newAddedTotalAmount;

    cart.getCartItems().addAll(cartItems);
    cart.setTotalAmount(totalAmount);
    return cartRepository.save(cart);
  }

  @Override
  public Order checkout(UUID cartId, String firstName, String lastName, String address,
      @NotNull String mobile, @NotNull String email) {
    Cart cart = getCart(cartId);

    Customer customer = customerService.saveCustomer(firstName, lastName, address, email, mobile);

    Order order = orderService.createOrder(cart, customer);

    String content = "Your cart has been checked out successfully! The order id is %d and the total price is %s euros.".formatted(
        order.getId(), order.getTotalAmount());
    notificationEventPublisher.publishCheckoutEvent(NotificationType.SMS, mobile, content, "");
    notificationEventPublisher.publishCheckoutEvent(NotificationType.EMAIL, email, content,
        "Order created");

    cartRepository.delete(cart);

    return order;
  }
}
