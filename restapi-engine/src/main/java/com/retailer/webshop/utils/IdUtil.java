package com.retailer.webshop.utils;

import com.retailer.webshop.exceptions.InvalidIdException;
import java.util.UUID;

public class IdUtil {

  public static UUID getUUID(String id) {
    try {
      return UUID.fromString(id);
    } catch (IllegalArgumentException exception) {
      throw new InvalidIdException();
    }
  }
}
