package com.retailer.webshop.services.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.retailer.webshop.entities.Customer;
import com.retailer.webshop.repositories.CustomerRepository;
import com.retailer.webshop.services.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

  @Mock
  private CustomerRepository customerRepository;

  @InjectMocks
  private CustomerServiceImpl customerService;

  @Test
  public void saveCustomer_validData_customerShouldBeCreated() {
    // Given
    String firstName = "John";
    String lastName = "Smith";
    String address = "Amsterdam, ...";
    String email = "info@gmail.com";
    String mobile = "1234567";

    Customer savedCustomer = new Customer();
    savedCustomer.setId(1L);
    savedCustomer.setFirstName(firstName);
    savedCustomer.setLastName(lastName);
    savedCustomer.setAddress(address);
    savedCustomer.setEmail(email);
    savedCustomer.setMobile(mobile);

    Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(savedCustomer);

    // When
    Customer result = customerService.saveCustomer(firstName, lastName, address, email, mobile);

    // Then
    Mockito.verify(customerRepository, Mockito.times(1)).save(Mockito.any(Customer.class));
    assertThat(result).isEqualTo(savedCustomer);
  }
}
