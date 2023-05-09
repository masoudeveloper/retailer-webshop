package com.retailer.webshop.controllers;

import com.retailer.webshop.mappers.ObjectMapper;
import com.retailer.webshop.models.ProductReport;
import com.retailer.webshop.rest.api.ManagementApi;
import com.retailer.webshop.rest.model.OrderDto;
import com.retailer.webshop.rest.model.ProductReportDto;
import com.retailer.webshop.rest.model.SalesOverallReportDto;
import com.retailer.webshop.services.OrderService;
import com.retailer.webshop.services.ReportService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ManagementController implements ManagementApi {

  private final ReportService reportService;
  private final OrderService orderService;

  @PreAuthorize("hasRole('ADMIN')")
  @Override
  public ResponseEntity<List<ProductReportDto>> getLeastFiveSalesReport(Integer year,
      Integer month) {
    List<ProductReport> result = reportService.getLeast5ProductsBySoldQuantity(year, month, 10);

    return ResponseEntity.ok(ObjectMapper.INSTANCE.map(result));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Override
  public ResponseEntity<OrderDto> getOrder(Long orderId) {
    return ResponseEntity.ok(
        ObjectMapper.INSTANCE.map(orderService.getOrder(orderId)));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Override
  public ResponseEntity<List<SalesOverallReportDto>> getOverallSalesReport(LocalDate from,
      LocalDate to) {
    List<SalesOverallReportDto> result = new ArrayList<>();

    LocalDate date = from;
    while (!date.isAfter(to)) {
      List<ProductReport> salesDetail = reportService.getSalesDataByDate(date, 10);

      if (!salesDetail.isEmpty()) {
        SalesOverallReportDto salesOverallReportDto = new SalesOverallReportDto();
        salesOverallReportDto.date(date);
        salesOverallReportDto.setDetail(ObjectMapper.INSTANCE.map(salesDetail));
        salesOverallReportDto.setSaleAmount(
            salesDetail.stream().mapToDouble(ProductReport::getTotalSaleAmount).sum());

        result.add(salesOverallReportDto);
      }

      date = date.plusDays(1);
    }

    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Override
  public ResponseEntity<List<ProductReportDto>> getTopFiveSalesReport(LocalDate date) {
    List<ProductReport> result = reportService.getTop5ProductsBySoldQuantity(date, 10);

    return ResponseEntity.ok(ObjectMapper.INSTANCE.map(result));
  }
}