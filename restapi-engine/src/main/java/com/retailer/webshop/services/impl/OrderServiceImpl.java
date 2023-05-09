package com.retailer.webshop.services.impl;

import com.retailer.webshop.entities.Cart;
import com.retailer.webshop.entities.CartItem;
import com.retailer.webshop.entities.Customer;
import com.retailer.webshop.entities.Order;
import com.retailer.webshop.entities.OrderItem;
import com.retailer.webshop.entities.Product;
import com.retailer.webshop.exceptions.OrderNotFound;
import com.retailer.webshop.repositories.OrderItemRepository;
import com.retailer.webshop.repositories.OrderRepository;
import com.retailer.webshop.services.OrderService;
import com.retailer.webshop.services.ProductService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final ProductService productService;

  @Override
  public Order getOrder(Long orderId) {
    Optional<Order> optionalOrder;
    optionalOrder = orderRepository.findById(orderId);

    if (optionalOrder.isPresent()) {
      return optionalOrder.get();
    } else {
      throw new OrderNotFound(orderId);
    }
  }

  @Override
  @Transactional
  public Order createOrder(Cart cart, Customer customer) {
    List<OrderItem> orderItems = new ArrayList<>();
    Map<Long, Integer> decreaseQuantities = new HashMap<>();

    for (CartItem cartItem : cart.getCartItems()) {
      Product product = productService.getProduct(cartItem.getProduct().getId());

      OrderItem orderItem = new OrderItem();
      orderItem.setProduct(product);

      int quantity = Math.min(cartItem.getQuantity(), product.getAvailableQuantity());
      orderItem.setQuantity(quantity);
      orderItem.setTotalAmount(quantity * product.getPrice());

      orderItems.add(orderItem);

      decreaseQuantities.put(product.getId(), quantity);
    }

    Order order = new Order();
    order.setTotalAmount(cart.getTotalAmount());
    order.setCreatedAt(LocalDateTime.now());
    order.setCustomer(customer);

    orderRepository.save(order);

    for (OrderItem orderItem : orderItems) {
      orderItem.setOrder(order);
    }
    order.setOrderItems(orderItems);
    orderItemRepository.saveAll(orderItems);

    for (Entry<Long, Integer> entry : decreaseQuantities.entrySet()) {
      productService.decreaseAvailableQuantity(entry.getKey(), entry.getValue());
    }

    return order;
  }
}