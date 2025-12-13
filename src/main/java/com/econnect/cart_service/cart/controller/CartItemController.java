package com.econnect.cart_service.cart.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.econnect.cart_service.cart.dto.CartItemDetailResponse;
import com.econnect.cart_service.cart.dto.CartItemRequest;
import com.econnect.cart_service.cart.dto.CartRequest;
import com.econnect.cart_service.cart.service.CartItemService;
import com.econnect.cart_service.cart.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class CartItemController {

  private final CartItemService cartItemService;
  private final CartService cartService;

  @PostMapping("/cartItem")
  public ResponseEntity<CartItemDetailResponse> post(@RequestBody CartItemRequest cartItemRequest, @RequestParam Long userId, @RequestParam Boolean isGuestUser) {
    log.debug("Request for creating new Cart Item for user: {} and cartId: {}", userId, cartItemRequest.getCartId());
    CartItemDetailResponse cartItemDetailResponse = null;
    cartItemRequest = cartItemRequest.toBuilder().userId(userId).build();
    if (ObjectUtils.isEmpty(cartItemRequest.getUserId()) || ObjectUtils.isEmpty(cartItemRequest.getCartId())
        || ObjectUtils.isEmpty(cartItemRequest.getItemId())) {
      log.error("Required Parameters missing in the request: {}", cartItemRequest);
      return new ResponseEntity<>(cartItemDetailResponse, HttpStatus.BAD_REQUEST);
    }
    try {
      cartItemDetailResponse = cartItemService.post(cartItemRequest);
      cartService.updateCartPrices(CartRequest.builder().userId(userId).cartId(cartItemRequest.getCartId()).build());
      if (ObjectUtils.isEmpty(cartItemDetailResponse)) {
        log.warn("No Cart has been created : {}", cartItemRequest.getUserId());
        return new ResponseEntity<>(cartItemDetailResponse, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching item-detail for item id: {}", cartItemRequest.getUserId(), e);
      return new ResponseEntity<>(cartItemDetailResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Item detail {} for requested itemId: {}", cartItemDetailResponse, cartItemRequest.getUserId());
    return new ResponseEntity<>(cartItemDetailResponse, HttpStatus.OK);
  }

  @PutMapping("/cartItem")
  public ResponseEntity<CartItemDetailResponse> put(@RequestBody CartItemRequest cartItemRequest, @RequestParam Long userId, @RequestParam Boolean isGuestUser) {
    log.debug("Request for creating new Cart for user: {}", cartItemRequest.getUserId());
    CartItemDetailResponse cartItemDetailResponse = null;
    if (ObjectUtils.isEmpty(cartItemRequest.getUserId())) {
      log.error("Required Parameters missing in the request: {}", cartItemRequest);
      return new ResponseEntity<>(cartItemDetailResponse, HttpStatus.BAD_REQUEST);
    }
    try {
      cartItemDetailResponse = cartItemService.put(cartItemRequest);
      cartService.updateCartPrices(CartRequest.builder().userId(userId).cartId(cartItemRequest.getCartId()).build());
      if (ObjectUtils.isEmpty(cartItemDetailResponse)) {
        log.warn("No Cart has been created : {}", cartItemRequest.getUserId());
        return new ResponseEntity<>(cartItemDetailResponse, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching item-detail for item id: {}", cartItemRequest.getUserId(), e);
      return new ResponseEntity<>(cartItemDetailResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Item detail {} for requested itemId: {}", cartItemDetailResponse, cartItemRequest.getUserId());
    return new ResponseEntity<>(cartItemDetailResponse, HttpStatus.OK);
  }

  @DeleteMapping("/cartItem")
  public ResponseEntity<Boolean> del(@RequestBody CartItemRequest cartItemRequest) {
    log.debug("Request for creating new Cart for user: {}", cartItemRequest.getUserId());
    Boolean cartItemDetailResponse = null;
    if (ObjectUtils.isEmpty(cartItemRequest.getUserId())) {
      log.error("Required Parameters missing in the request: {}", cartItemRequest);
      return new ResponseEntity<>(cartItemDetailResponse, HttpStatus.BAD_REQUEST);
    }
    try {
      cartItemDetailResponse = cartItemService.del(cartItemRequest);
      if (ObjectUtils.isEmpty(cartItemDetailResponse)) {
        log.warn("No Cart has been created : {}", cartItemRequest.getUserId());
        return new ResponseEntity<>(cartItemDetailResponse, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching item-detail for item id: {}", cartItemRequest.getUserId(), e);
      return new ResponseEntity<>(cartItemDetailResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Item detail {} for requested itemId: {}", cartItemRequest.getCartId(), cartItemRequest.getUserId());
    return new ResponseEntity<>(cartItemDetailResponse, HttpStatus.OK);
  }
}
