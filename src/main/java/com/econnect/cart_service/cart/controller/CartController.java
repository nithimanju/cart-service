package com.econnect.cart_service.cart.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.econnect.cart_service.cart.dto.CartDetailResponse;
import com.econnect.cart_service.cart.dto.CartRequest;
import com.econnect.cart_service.cart.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class CartController {

  private CartService cartService;

  @GetMapping("/cart")
  public ResponseEntity<CartDetailResponse> get(@RequestParam Long cartId, @RequestParam Long userId) {
    CartRequest cartRequest = CartRequest.builder().cartId(cartId).userId(userId).build();
    CartDetailResponse cartDetailResponse = null;
    try {
      cartDetailResponse = cartService.get(cartRequest);
      if (ObjectUtils.isEmpty(cartDetailResponse)) {
        return new ResponseEntity<>(cartDetailResponse, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(cartDetailResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(cartDetailResponse, HttpStatus.OK);
  }
}
