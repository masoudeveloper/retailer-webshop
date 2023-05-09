package com.retailer.webshop.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "carts")
public class Cart {

  public Cart(UUID id) {
    this.id = id;
  }

  @Id
  private UUID id = UUID.randomUUID();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart", orphanRemoval = true)
  @Fetch(FetchMode.JOIN)
  private List<CartItem> cartItems = new ArrayList<>();

  private double totalAmount;
}
