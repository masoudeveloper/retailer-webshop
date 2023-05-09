package com.retailer.webshop.mappers;

import com.retailer.webshop.entities.Cart;
import com.retailer.webshop.entities.CartItem;
import com.retailer.webshop.entities.Order;
import com.retailer.webshop.entities.OrderItem;
import com.retailer.webshop.entities.Product;
import com.retailer.webshop.models.ProductReport;
import com.retailer.webshop.rest.model.CartDto;
import com.retailer.webshop.rest.model.OrderDto;
import com.retailer.webshop.rest.model.ProductDto;
import com.retailer.webshop.rest.model.ProductReportDto;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper
public interface ObjectMapper {

  ObjectMapper INSTANCE = Mappers.getMapper(ObjectMapper.class);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "totalAmount", target = "totalAmount")
  @Mapping(source = "cartItems", target = "cartItems")
  CartDto map(Cart cart);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "price", target = "price")
  ProductDto map(Product product);

  List<ProductDto> mapProductList(List<Product> products);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "totalAmount", target = "totalAmount")
  @Mapping(source = "orderItems", target = "orderItems")
  @Mapping(source = "createdAt", target = "createdAt")
  OrderDto map(Order order);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "product", target = "product")
  @Mapping(source = "quantity", target = "quantity")
  @Mapping(source = "totalAmount", target = "totalAmount")
  OrderItem map(CartItem cartItem);

  @Mapping(source = "productId", target = "productId")
  @Mapping(source = "productName", target = "productName")
  @Mapping(source = "totalSaleAmount", target = "totalSaleAmount")
  @Mapping(source = "availableQuantity", target = "availableQuantity")
  @Mapping(source = "soldQuantity", target = "soldQuantity")
  @Mapping(source = "lowQuantity", target = "isLowQuantity")
  ProductReportDto map(ProductReport productReport);

  List<ProductReportDto> map(List<ProductReport> productReports);

  default OffsetDateTime map(LocalDateTime localDateTime) {
    return localDateTime.atOffset(ZoneOffset.UTC);
  }
}