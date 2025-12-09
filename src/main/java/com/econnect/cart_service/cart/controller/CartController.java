package com.econnect.cart_service.cart.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  private final CartService cartService;

  @GetMapping("/cart")
  public ResponseEntity<CartDetailResponse> get(@RequestParam(required = false) Long cartId, @RequestParam Long userId) {
    log.debug("Request for fetching Cart-detail for Cart id: {}", cartId, userId);
    CartRequest cartRequest = CartRequest.builder().cartId(cartId).userId(userId).build();
    CartDetailResponse cartDetailResponse = null;
    try {
      cartDetailResponse = cartService.get(cartRequest);
      if (ObjectUtils.isEmpty(cartDetailResponse)) {
        log.warn("No Cart detail found for requested Cart-id : {}", cartId, userId);
        return new ResponseEntity<>(cartDetailResponse, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching Cart-detail for Cart id: {}", cartId, userId, e);
      return new ResponseEntity<>(cartDetailResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Cart detail {} for requested CartId: {}", cartDetailResponse, cartId, userId);
    return new ResponseEntity<>(cartDetailResponse, HttpStatus.OK);
  }

  @PostMapping("/cart")
  public ResponseEntity<Long> post(@RequestBody CartRequest cartRequest) {
    log.debug("Request for creating new Cart for user: {}", cartRequest.getUserId());
    Long cartId = null;
    if (ObjectUtils.isEmpty(cartRequest.getUserId())) {
      log.error("Required Parameters missing in the request: {}", cartRequest);
      return new ResponseEntity<>(cartId, HttpStatus.BAD_REQUEST);
    }
    try {
      cartId = cartService.post(cartRequest);
      if (ObjectUtils.isEmpty(cartId)) {
        log.warn("No Cart has been created : {}", cartRequest.getUserId());
        return new ResponseEntity<>(cartId, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching item-detail for item id: {}", cartRequest.getUserId(), e);
      return new ResponseEntity<>(cartId, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Item detail {} for requested itemId: {}", cartId, cartRequest.getUserId());
    return new ResponseEntity<>(cartId, HttpStatus.OK);
  }

  @PutMapping("/cart")
  public ResponseEntity<Long> put(@RequestBody CartRequest cartRequest) {
    log.debug("Request for disabling cart: {} and creat new Cart for user: {}", cartRequest.getCartId(),
        cartRequest.getUserId());
    Long cartId = null;
    if (ObjectUtils.isEmpty(cartRequest.getCartId()) || ObjectUtils.isEmpty(cartRequest.getUserId())) {
      log.error("Required Parameters missing in the request: {}", cartRequest);
      return new ResponseEntity<>(cartId, HttpStatus.BAD_REQUEST);
    }
    try {
      cartId = cartService.put(cartRequest);
      if (ObjectUtils.isEmpty(cartId)) {
        log.warn("No Cart has been created : {}", cartRequest.getUserId());
        return new ResponseEntity<>(cartId, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching item-detail for item id: {}", cartRequest.getUserId(), e);
      return new ResponseEntity<>(cartId, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Item detail {} for requested itemId: {}", cartId, cartRequest.getUserId());
    return new ResponseEntity<>(cartId, HttpStatus.OK);
  }
}
