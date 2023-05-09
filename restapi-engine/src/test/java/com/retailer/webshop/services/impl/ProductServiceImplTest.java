package com.retailer.webshop.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.retailer.webshop.entities.Product;
import com.retailer.webshop.exceptions.ProductNotFound;
import com.retailer.webshop.repositories.ProductRepository;
import com.retailer.webshop.services.impl.ProductServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

  @Mock
  private ProductRepository productRepository;

  private ProductServiceImpl productService;

  @BeforeEach
  void init() {
    productService = new ProductServiceImpl(productRepository);
  }

  @Test
  void getProduct_validId_shouldReturnProduct() {
    // Given
    long id = 1L;
    Product product = new Product();
    product.setId(id);
    when(productRepository.findById(id)).thenReturn(Optional.of(product));

    // When
    Product result = productService.getProduct(id);

    // Then
    assertThat(result).isEqualTo(product);
    verify(productRepository).findById(id);
  }

  @Test
  void getProduct_validCharacters_shouldReturnAllPossibleResults() {
    // Given
    Product product1 = new Product();
    product1.setId(1L);
    product1.setName("Product1");
    Product product2 = new Product();
    product2.setId(2L);
    product2.setName("Product2");
    when(productRepository.findByNameIgnoreCaseContaining("Pro")).thenReturn(
        Arrays.asList(product1, product2));

    // When
    List<Product> result = productService.getProduct("Pro");

    // Then
    assertEquals(Arrays.asList(product1, product2), result);
    verify(productRepository).findByNameIgnoreCaseContaining("Pro");
  }

  @Test
  void decreaseAvailableQuantity_validProductId_shouldUpdateProductQuantity() {
    // Given
    long productId = 1L;
    int usedQuantityCount = 2;
    Product product = new Product();
    product.setId(productId);
    product.setName("Test Product");
    product.setAvailableQuantity(5);

    when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    // When
    productService.decreaseAvailableQuantity(productId, usedQuantityCount);

    // Then
    verify(productRepository, times(1)).findById(productId);
    verify(productRepository, times(1)).save(product);

    assertEquals(3, product.getAvailableQuantity());
  }

  @Test
  void decreaseAvailableQuantity_invalidProductId_throwInvalidIdException() {
    // Given
    long productId = 1L;
    int usedQuantityCount = 2;

    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    // When / Then
    assertThatThrownBy(
        () -> productService.decreaseAvailableQuantity(productId, usedQuantityCount)).isInstanceOf(
            ProductNotFound.class)
        .hasMessage("The product with ID '1' could not be found.");
    verify(productRepository, times(1)).findById(productId);
    verify(productRepository, never()).save(any());
  }
}