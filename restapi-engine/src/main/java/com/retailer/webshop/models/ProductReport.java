package com.retailer.webshop.models;

import lombok.Data;

@Data
public class ProductReport {

  public ProductReport(Long productId, String productName, long soldQuantity,
      long availableQuantity,
      double totalSaleAmount, boolean isLowQuantity) {
    this.productId = productId;
    this.productName = productName;
    this.soldQuantity = soldQuantity;
    this.availableQuantity = availableQuantity;
    this.totalSaleAmount = totalSaleAmount;
    this.isLowQuantity = isLowQuantity;
  }

  private Long productId;
  private String productName;
  private long soldQuantity;
  private long availableQuantity;
  private double totalSaleAmount;
  private boolean isLowQuantity;
}