package com.retailer.webshop.services.impl;

import com.retailer.webshop.entities.Product;
import com.retailer.webshop.models.ProductReport;
import com.retailer.webshop.repositories.OrderRepository;
import com.retailer.webshop.services.ProductService;
import com.retailer.webshop.services.ReportService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final OrderRepository orderRepository;
  private final ProductService productService;

  @Override
  public List<ProductReport> getSalesDataByDate(LocalDate date, int lowQuantityThreshold) {
    LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIDNIGHT);
    LocalDateTime endOfDay = startOfDay.plusDays(1);

    return orderRepository.getSalesData(startOfDay, endOfDay, lowQuantityThreshold);
  }

  @Override
  public List<ProductReport> getTop5ProductsBySoldQuantity(LocalDate date,
      int lowQuantityThreshold) {
    LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIDNIGHT);
    LocalDateTime endOfDay = startOfDay.plusDays(1);
    List<ProductReport> productReports = orderRepository.getTopProductsBySoldQuantity(
        startOfDay, endOfDay, lowQuantityThreshold);
    for (ProductReport productReport : productReports) {
      Product product = productService.getProduct(productReport.getProductId());
      productReport.setAvailableQuantity(product.getAvailableQuantity());
      if (productReport.getAvailableQuantity() < 10) {
        productReport.setLowQuantity(true);
      }
    }
    return productReports;
  }

  public List<ProductReport> getLeast5ProductsBySoldQuantity(int year, int month,
      int lowQuantityThreshold) {
    LocalDate date = LocalDate.of(year, month, 1);
    LocalDateTime startOfMonth = LocalDateTime.of(date, LocalTime.MIDNIGHT);
    LocalDateTime endOfMonth = startOfMonth.plusMonths(1);

    List<ProductReport> productReports = orderRepository.getLeastProductsBySoldQuantity(
        startOfMonth, endOfMonth, lowQuantityThreshold);

    for (ProductReport productReport : productReports) {
      Product product = productService.getProduct(productReport.getProductId());
      productReport.setAvailableQuantity(product.getAvailableQuantity());
      if (productReport.getAvailableQuantity() < 10) {
        productReport.setLowQuantity(true);
      }
    }
    return productReports;
  }

}
