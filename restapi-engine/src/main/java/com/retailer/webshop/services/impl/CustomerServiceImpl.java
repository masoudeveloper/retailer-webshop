package com.retailer.webshop.services.impl;

import com.retailer.webshop.entities.Customer;
import com.retailer.webshop.repositories.CustomerRepository;
import com.retailer.webshop.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;

  @Override
  public Customer saveCustomer(String firstName, String lastName, String address, String email,
      String mobile) {
    Customer customer = new Customer();
    customer.setFirstName(firstName);
    customer.setLastName(lastName);
    customer.setAddress(address);
    customer.setMobile(mobile);
    customer.setEmail(email);

    return customerRepository.save(customer);
  }
}