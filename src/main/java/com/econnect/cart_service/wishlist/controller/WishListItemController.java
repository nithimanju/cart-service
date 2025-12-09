package com.econnect.cart_service.wishlist.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.econnect.cart_service.dto.ItemDetailResponse;
import com.econnect.cart_service.wishlist.dto.WishListRequest;
import com.econnect.cart_service.wishlist.service.WishListItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class WishListItemController {
  private final WishListItemService wishListItemService;

  @PostMapping("/wishListItem")
  public ResponseEntity<ItemDetailResponse> post(@RequestBody WishListRequest wishListRequest) {
    log.debug("Request for creating new WishList Item for user: {} and cartId: {}", wishListRequest.getUserId());
    ItemDetailResponse itemDetailResponse = null;
    if (ObjectUtils.isEmpty(wishListRequest.getUserId()) || ObjectUtils.isEmpty(wishListRequest.getWishListId()) || ObjectUtils.isEmpty(wishListRequest.getItemId())) {
      log.error("Required Parameters missing in the request: {}", wishListRequest);
      return new ResponseEntity<>(itemDetailResponse, HttpStatus.BAD_REQUEST);
    }
    try {
      itemDetailResponse = wishListItemService.post(wishListRequest);
      if (ObjectUtils.isEmpty(itemDetailResponse)) {
        log.warn("No WishList has been created : {}", wishListRequest.getUserId());
        return new ResponseEntity<>(itemDetailResponse, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching WishList-detail for WishList id: {}", wishListRequest.getUserId(), e);
      return new ResponseEntity<>(itemDetailResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Item detail {} for requested itemId: {}", itemDetailResponse, wishListRequest.getUserId());
    return new ResponseEntity<>(itemDetailResponse, HttpStatus.OK);
  }

  @DeleteMapping("/wishListItem")
  public ResponseEntity<Boolean> del(@RequestBody WishListRequest wishListRequest) {
    log.debug("Request for creating new WishList for user: {}", wishListRequest.getUserId());
    Boolean wishListResponse = null;
    if (ObjectUtils.isEmpty(wishListRequest.getUserId())) {
      log.error("Required Parameters missing in the request: {}", wishListRequest);
      return new ResponseEntity<>(wishListResponse, HttpStatus.BAD_REQUEST);
    }
    try {
      wishListResponse = wishListItemService.del(wishListRequest);
      if (ObjectUtils.isEmpty(wishListResponse)) {
        log.warn("No WishList has been created : {}", wishListRequest.getUserId());
        return new ResponseEntity<>(wishListResponse, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      log.error("Error occured while fetching WishList-detail for WishList id: {}", wishListRequest.getUserId(), e);
      return new ResponseEntity<>(wishListResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    log.debug("Deactivated successfullly for WishListId: {}", wishListRequest.getWishListId(), wishListRequest.getUserId());
    return new ResponseEntity<>(wishListResponse, HttpStatus.OK);
  }
}
