package com.econnect.cart_service.wishlist.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.econnect.cart_service.wishlist.dto.WishListDetailResponse;
import com.econnect.cart_service.wishlist.dto.WishListRequest;
import com.econnect.cart_service.wishlist.service.WishListService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class WishListController {
  private final WishListService wishListService;

  @GetMapping("/cart")
  public ResponseEntity<WishListDetailResponse> get(@RequestParam(required = false) Long wishListId, @RequestParam Long userId) {
    log.debug("Request for fetching Cart-detail for Cart id: {}", wishListId, userId);
    WishListRequest wishListRequest = WishListRequest.builder().wishListId(wishListId).userId(userId).build();
    WishListDetailResponse wishListDetailResponse = null;
    try {
      wishListDetailResponse = wishListService.get(wishListRequest);
      if (ObjectUtils.isEmpty(wishListDetailResponse)) {
        log.warn("No Cart detail found for requested Cart-id : {}", wishListId, userId);
        return new ResponseEntity<>(wishListDetailResponse, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching Cart-detail for Cart id: {}", wishListId, userId, e);
      return new ResponseEntity<>(wishListDetailResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Cart detail {} for requested wishListId: {}", wishListDetailResponse, wishListId, userId);
    return new ResponseEntity<>(wishListDetailResponse, HttpStatus.OK);
  }

  @PostMapping("/cart")
  public ResponseEntity<Long> post(@RequestBody WishListRequest wishListRequest) {
    log.debug("Request for creating new Cart for user: {}", wishListRequest.getUserId());
    Long wishListId = null;
    if (ObjectUtils.isEmpty(wishListRequest.getUserId())) {
      log.error("Required Parameters missing in the request: {}", wishListRequest);
      return new ResponseEntity<>(wishListId, HttpStatus.BAD_REQUEST);
    }
    try {
      wishListId = wishListService.post(wishListRequest);
      if (ObjectUtils.isEmpty(wishListId)) {
        log.warn("No Cart has been created : {}", wishListRequest.getUserId());
        return new ResponseEntity<>(wishListId, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching item-detail for item id: {}", wishListRequest.getUserId(), e);
      return new ResponseEntity<>(wishListId, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Item detail {} for requested itemId: {}", wishListId, wishListRequest.getUserId());
    return new ResponseEntity<>(wishListId, HttpStatus.OK);
  }

  @PutMapping("/cart")
  public ResponseEntity<Long> put(@RequestBody WishListRequest wishListRequest) {
    log.debug("Request for disabling cart: {} and creat new Cart for user: {}", wishListRequest.getWishListId(),
        wishListRequest.getUserId());
    Long wishListId = null;
    if (ObjectUtils.isEmpty(wishListRequest.getWishListId()) || ObjectUtils.isEmpty(wishListRequest.getUserId())) {
      log.error("Required Parameters missing in the request: {}", wishListRequest);
      return new ResponseEntity<>(wishListId, HttpStatus.BAD_REQUEST);
    }
    try {
      wishListId = wishListService.put(wishListRequest);
      if (ObjectUtils.isEmpty(wishListId)) {
        log.warn("No Cart has been created : {}", wishListRequest.getUserId());
        return new ResponseEntity<>(wishListId, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching item-detail for item id: {}", wishListRequest.getUserId(), e);
      return new ResponseEntity<>(wishListId, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Item detail {} for requested itemId: {}", wishListId, wishListRequest.getUserId());
    return new ResponseEntity<>(wishListId, HttpStatus.OK);
  }
}
