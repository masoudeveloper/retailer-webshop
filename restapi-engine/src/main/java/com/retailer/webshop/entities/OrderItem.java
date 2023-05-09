package com.retailer.webshop.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {

  public OrderItem(Long id, Product product, int quantity, double totalAmount) {
    this.id = id;
    this.product = product;
    this.quantity = quantity;
    this.totalAmount = totalAmount;
  }

  public OrderItem(Product product, int quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(name = "quantity", nullable = false)
  private int quantity;

  @Column(name = "total_amount")
  private double totalAmount;
}