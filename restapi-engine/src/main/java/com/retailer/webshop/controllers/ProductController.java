package com.retailer.webshop.controllers;

import com.retailer.webshop.mappers.ObjectMapper;
import com.retailer.webshop.rest.api.ProductApi;
import com.retailer.webshop.rest.model.ProductDto;
import com.retailer.webshop.services.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductApi {

  private final ProductService productService;
  private final ObjectMapper objectMapper;

  @ExceptionHandler()
  public ResponseEntity<List<ProductDto>> getProducts(String name) {

    return ResponseEntity.ok(objectMapper.mapProductList(productService.getProduct(name)));
  }
}
