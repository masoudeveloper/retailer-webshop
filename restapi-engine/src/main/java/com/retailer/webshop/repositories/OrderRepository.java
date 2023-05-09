package com.retailer.webshop.repositories;

import com.retailer.webshop.entities.Order;
import com.retailer.webshop.models.ProductReport;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query(value =
      "SELECT NEW com.retailer.webshop.models.ProductReport(i.product.id, i.product.name, "
          + "SUM(i.quantity), i.product.availableQuantity, "
          + "SUM(i.totalAmount), "
          + "CASE WHEN i.product.availableQuantity < :lowQuantityThreshold THEN true ELSE false END) "
          + "FROM Order o "
          + "JOIN o.orderItems i "
          + "WHERE o.createdAt BETWEEN :from AND :to "
          + "GROUP BY i.product")
  List<ProductReport> getSalesData(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to,
      @Param("lowQuantityThreshold") Integer lowQuantityThreshold);

  @Query(value =
      "SELECT NEW com.retailer.webshop.models.ProductReport(i.product.id, i.product.name, "
          + "SUM(i.quantity), i.product.availableQuantity, "
          + "SUM(i.totalAmount), "
          + "CASE WHEN i.product.availableQuantity < :lowQuantityThreshold THEN true ELSE false END) "
          + "FROM Order o "
          + "JOIN o.orderItems i "
          + "WHERE o.createdAt BETWEEN :from AND :to "
          + "GROUP BY i.product "
          + "ORDER BY SUM(i.quantity) DESC "
          + "LIMIT 5")
  List<ProductReport> getTopProductsBySoldQuantity(@Param("from") LocalDateTime from,
      @Param("to") LocalDateTime to, @Param("lowQuantityThreshold") Integer lowQuantityThreshold);

  @Query(value =
      "SELECT NEW com.retailer.webshop.models.ProductReport(i.product.id, i.product.name, "
          + "SUM(i.quantity), i.product.availableQuantity, "
          + "SUM(i.totalAmount), "
          + "CASE WHEN i.product.availableQuantity < :lowQuantityThreshold THEN true ELSE false END) "
          + "FROM Order o "
          + "JOIN o.orderItems i "
          + "WHERE o.createdAt BETWEEN :from AND :to "
          + "GROUP BY i.product "
          + "ORDER BY SUM(i.quantity) ASC "
          + "LIMIT 5")
  List<ProductReport> getLeastProductsBySoldQuantity(@Param("from") LocalDateTime from,
      @Param("to") LocalDateTime to, @Param("lowQuantityThreshold") Integer lowQuantityThreshold);
}