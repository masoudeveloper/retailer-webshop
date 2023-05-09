package com.retailer.webshop;

import com.retailer.webshop.controllers.CartController;
import com.retailer.webshop.events.NotificationEventPublisher;
import com.retailer.webshop.externals.GmailProperties;
import com.retailer.webshop.externals.GmailService;
import com.retailer.webshop.externals.TwilioProperties;
import com.retailer.webshop.externals.TwilioService;
import com.retailer.webshop.mappers.ObjectMapper;
import com.retailer.webshop.mappers.ObjectMapperImpl;
import com.retailer.webshop.repositories.CartItemRepository;
import com.retailer.webshop.repositories.CartRepository;
import com.retailer.webshop.repositories.CustomerRepository;
import com.retailer.webshop.repositories.OrderItemRepository;
import com.retailer.webshop.repositories.OrderRepository;
import com.retailer.webshop.repositories.ProductRepository;
import com.retailer.webshop.services.CartService;
import com.retailer.webshop.services.CustomerService;
import com.retailer.webshop.services.NotificationService;
import com.retailer.webshop.services.OrderService;
import com.retailer.webshop.services.ProductService;
import com.retailer.webshop.services.ReportService;
import com.retailer.webshop.services.impl.CartServiceImpl;
import com.retailer.webshop.services.impl.CustomerServiceImpl;
import com.retailer.webshop.services.impl.NotificationServiceImpl;
import com.retailer.webshop.services.impl.OrderServiceImpl;
import com.retailer.webshop.services.impl.ProductServiceImpl;
import com.retailer.webshop.services.impl.ReportServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration
@ComponentScan(basePackages = "com.retailer.webshop")
@EnableJpaRepositories(basePackages = "com.retailer.webshop.repositories")
@EnableConfigurationProperties({GmailProperties.class, TwilioProperties.class})
public class WebshopAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public CartService cartService(CartRepository cartRepository,
      CartItemRepository cartItemRepository, ProductService productService,
      OrderService orderService,
      CustomerService customerService, NotificationEventPublisher notificationEventPublisher) {
    return new CartServiceImpl(cartRepository, cartItemRepository, productService,
        orderService, notificationEventPublisher, customerService);
  }

  @Bean
  @ConditionalOnMissingBean
  public ProductService productService(ProductRepository productRepository) {
    return new ProductServiceImpl(productRepository);
  }

  @Bean
  @ConditionalOnMissingBean
  public CustomerService customerService(CustomerRepository customerRepository) {
    return new CustomerServiceImpl(customerRepository);
  }

  @Bean
  @ConditionalOnMissingBean
  public OrderService orderService(OrderRepository orderRepository,
      OrderItemRepository orderItemRepository, ProductService productService) {
    return new OrderServiceImpl(orderRepository, orderItemRepository, productService);
  }

  @Bean
  @ConditionalOnMissingBean
  public ReportService reportService(OrderRepository orderRepository,
      ProductService productService) {
    return new ReportServiceImpl(orderRepository, productService);
  }

  @Bean
  @ConditionalOnMissingBean
  public ObjectMapper objectMapper() {
    return new ObjectMapperImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public NotificationEventPublisher notificationEventPublisher(
      ApplicationEventPublisher applicationEventPublisher) {
    return new NotificationEventPublisher(applicationEventPublisher);
  }

  @Bean
  @ConditionalOnMissingBean
  public CartController cartController(CartService cartService, ObjectMapper objectMapper) {
    return new CartController(cartService, objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean
  public GmailService gmailService(GmailProperties gmailProperties) {
    return new GmailService(gmailProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public TwilioService twilioService(TwilioProperties twilioProperties) {
    return new TwilioService(twilioProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public NotificationService notificationService(GmailService gmailService,
      TwilioService twilioService) {
    return new NotificationServiceImpl(gmailService, twilioService);
  }
}
