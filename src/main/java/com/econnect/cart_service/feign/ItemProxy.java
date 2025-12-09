package com.econnect.cart_service.feign;

import org.springframework.stereotype.Service;

import com.econnect.cart_service.dto.ItemDetailResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class ItemProxy {
  private final ItemClient itemClient;

  public ItemDetailResponse get(Long id) {
    try {
      return itemClient.get(id).getBody();
    } catch (Exception e) {
      return null;
    }
  }
}