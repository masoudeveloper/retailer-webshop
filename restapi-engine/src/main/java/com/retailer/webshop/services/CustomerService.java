package com.retailer.webshop.services;

import com.retailer.webshop.entities.Customer;

public interface CustomerService {

  Customer saveCustomer(String firstName, String lastName, String address, String email,
      String mobile);
}