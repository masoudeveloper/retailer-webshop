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
@Table(name = "cart_items")
public class CartItem {

  public CartItem(Product product, int quantity, double totalAmount) {
    this.product = product;
    this.quantity = quantity;
    this.totalAmount = totalAmount;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private int quantity;

  @ManyToOne
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @Column(name = "total_amount")
  private double totalAmount;
}
