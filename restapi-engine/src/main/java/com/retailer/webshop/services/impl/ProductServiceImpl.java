package com.retailer.webshop.services.impl;

import com.retailer.webshop.entities.Product;
import com.retailer.webshop.exceptions.InvalidIdException;
import com.retailer.webshop.exceptions.ProductNotFound;
import com.retailer.webshop.repositories.ProductRepository;
import com.retailer.webshop.services.ProductService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Override
  public Product getProduct(Long id) {
    if (id == null) {
      throw new InvalidIdException();
    }

    Optional<Product> product = productRepository.findById(id);
    if (product.isPresent()) {
      return product.get();
    }

    throw new ProductNotFound(id);
  }

  @Override
  public List<Product> getProduct(String name) {
    return productRepository.findByNameIgnoreCaseContaining(name);
  }

  @Override
  public void decreaseAvailableQuantity(long productId, int usedQuantityCount) {
    Product product = getProduct(productId);
    product.setAvailableQuantity(product.getAvailableQuantity() - usedQuantityCount);
    productRepository.save(product);
  }
}