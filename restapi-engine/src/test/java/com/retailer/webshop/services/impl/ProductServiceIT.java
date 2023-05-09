package com.retailer.webshop.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.retailer.webshop.BaseIT;
import com.retailer.webshop.entities.Product;
import com.retailer.webshop.exceptions.ProductNotFound;
import com.retailer.webshop.repositories.ProductRepository;
import com.retailer.webshop.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceIT extends BaseIT {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  @Test
  public void getProduct_validIdProvided_returnProduct() {
    // Given
    Product product = new Product();
    product.setName("product");
    Product savedProduct = productRepository.save(product);
    long productId = savedProduct.getId();

    // When
    Product fetchedProduct = productService.getProduct(productId);

    // Then
    assertThat(fetchedProduct).isNotNull();
    assertThat(fetchedProduct.getName()).isEqualTo("product");
  }

  @Test
  public void getProduct_notPersistedProductIdProvided_throwProductNotFoundException() {
    // Given
    long productId = 123;

    // When / Then
    assertThatThrownBy(() -> productService.getProduct(productId)).isInstanceOf(
            ProductNotFound.class)
        .hasMessage("The product with ID '%s' could not be found.".formatted(productId));
  }

  @Test
  public void decreaseAvailableQuantity() {

  }
}