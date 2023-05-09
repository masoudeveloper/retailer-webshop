package com.retailer.webshop.services;

import com.retailer.webshop.models.ProductReport;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {

  List<ProductReport> getSalesDataByDate(LocalDate date, int lowQuantityThreshold);

  List<ProductReport> getTop5ProductsBySoldQuantity(LocalDate date, int lowQuantityThreshold);

  List<ProductReport> getLeast5ProductsBySoldQuantity(int year, int month,
      int lowQuantityThreshold);
}