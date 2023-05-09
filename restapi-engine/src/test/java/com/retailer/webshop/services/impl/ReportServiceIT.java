package com.retailer.webshop.services.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.retailer.webshop.BaseIT;
import com.retailer.webshop.entities.Cart;
import com.retailer.webshop.entities.Customer;
import com.retailer.webshop.entities.Product;
import com.retailer.webshop.models.ProductReport;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReportServiceIT extends BaseIT {

  @BeforeEach
  void setUp() {
    // Given
    Product product1 = new Product();
    product1.setName("Product1");
    product1.setPrice(100);
    product1.setAvailableQuantity(20);
    Product savedProduct1 = productRepository.save(product1);

    Product product2 = new Product();
    product2.setName("Product2");
    product2.setPrice(200);
    product2.setAvailableQuantity(20);
    Product savedProduct2 = productRepository.save(product2);

    Product product3 = new Product();
    product3.setName("Product3");
    product3.setPrice(200);
    product3.setAvailableQuantity(20);
    Product savedProduct3 = productRepository.save(product3);

    Customer customer = customerService.saveCustomer("John", "Smith", "Amsterdam, ...", "1234567",
        "email@gmail.com");

    Cart cart1 = cartService.createCart();
    Cart updatedCart1 = cartService.addProductToCart(cart1.getId(), savedProduct1.getId(), 5);
    orderService.createOrder(updatedCart1, customer);

    Cart cart2 = cartService.createCart();
    cartService.addProductToCart(cart2.getId(), savedProduct1.getId(), 10);
    Cart updatedCart2 = cartService.addProductToCart(cart2.getId(), savedProduct2.getId(), 7);
    orderService.createOrder(updatedCart2, customer);

    Cart cart3 = cartService.createCart();
    Cart updatedCart3 = cartService.addProductToCart(cart3.getId(), savedProduct3.getId(), 2);
    orderService.createOrder(updatedCart3, customer);
  }

  @Test
  public void getSalesDataByDate_hasData_shouldReturnReport() {
    // When
    List<ProductReport> productReports = reportService.getSalesDataByDate(LocalDate.now(), 10);

    // Then
    assertThat(productReports).hasSize(3);
    assertThat(productReports.get(0).getProductName()).isEqualTo("Product1");
    assertThat(productReports.get(0).isLowQuantity()).isTrue();
    assertThat(productReports.get(0).getAvailableQuantity()).isEqualTo(5);
    assertThat(productReports.get(0).getSoldQuantity()).isEqualTo(15);
    assertThat(productReports.get(0).getTotalSaleAmount()).isEqualTo(1500.0);
    assertThat(productReports.get(1).getProductName()).isEqualTo("Product2");
    assertThat(productReports.get(1).isLowQuantity()).isFalse();
    assertThat(productReports.get(1).getAvailableQuantity()).isEqualTo(13);
    assertThat(productReports.get(1).getSoldQuantity()).isEqualTo(7);
    assertThat(productReports.get(1).getTotalSaleAmount()).isEqualTo(1400.0);
    assertThat(productReports.get(2).getProductName()).isEqualTo("Product3");
    assertThat(productReports.get(2).isLowQuantity()).isFalse();
    assertThat(productReports.get(2).getAvailableQuantity()).isEqualTo(18);
    assertThat(productReports.get(2).getSoldQuantity()).isEqualTo(2);
    assertThat(productReports.get(2).getTotalSaleAmount()).isEqualTo(400.0);
  }

  @Test
  public void getTopProductsBySoldQuantity_hasData_shouldReturnReport() {
    // When
    List<ProductReport> productReports = reportService.getTop5ProductsBySoldQuantity(
        LocalDate.now(), 2);

    // Then
    assertThat(productReports).hasSizeLessThanOrEqualTo(5);
    assertThat(productReports.get(0).getProductName()).isEqualTo("Product1");
    assertThat(productReports.get(0).getTotalSaleAmount()).isEqualTo(1500.0);
  }

  @Test
  public void getLeastProductsBySoldQuantity_hasData_shouldReturnReport() {
    // When
    List<ProductReport> productReports = reportService.getLeast5ProductsBySoldQuantity(
        LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 2);

    // Then
    assertThat(productReports).hasSizeLessThanOrEqualTo(5);
    assertThat(productReports.get(0).getProductName()).isEqualTo("Product3");
    assertThat(productReports.get(0).getTotalSaleAmount()).isEqualTo(400.0);
  }

}