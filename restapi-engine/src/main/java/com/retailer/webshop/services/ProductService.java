package com.retailer.webshop.services;

import com.retailer.webshop.entities.Product;
import java.util.List;

public interface ProductService {

  Product getProduct(Long id);

  List<Product> getProduct(String name);

  void decreaseAvailableQuantity(long productId, int usedQuantityCount);
}
