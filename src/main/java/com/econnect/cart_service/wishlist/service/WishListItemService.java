package com.econnect.cart_service.wishlist.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.econnect.cart_service.dto.ItemDetailResponse;
import com.econnect.cart_service.feign.ItemProxy;
import com.econnect.cart_service.model.WishListItem;
import com.econnect.cart_service.model.WishListItem.WishListItemEmbeddedKey;
import com.econnect.cart_service.wishlist.dto.WishListRequest;
import com.econnect.cart_service.wishlist.repository.WishListItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class WishListItemService {

  private final WishListItemRepository wishListItemRepository;

  private final ItemProxy itemClient;

  public List<ItemDetailResponse> getByWishListId(Long wishListId) {
    List<ItemDetailResponse> response = null;
    Optional<List<WishListItem>> wishListItems = wishListItemRepository.findByIdWishListIdAndIsActiveTrue(wishListId);
    if (ObjectUtils.isEmpty(wishListItems)) {
      return null;
    }
    response = wishListItems.get().stream().map(wishListItem -> itemClient.get(wishListItem.getId().getItemId()))
        .toList();
    return response;
  }

  public ItemDetailResponse post(WishListRequest wishListRequest) {
    Long itemId = wishListRequest.getItemId();
    Long wishListId = wishListRequest.getWishListId();

    ItemDetailResponse itemDetailResponse = itemClient.get(itemId);

    Optional<WishListItem> wishListItems = wishListItemRepository
        .findByIdWishListIdAndIdItemIdAndIsActiveTrue(wishListId, itemId);
    if (ObjectUtils.isEmpty(wishListItems)) {
      WishListItem wishListItem = WishListItem.builder()
          .id(WishListItemEmbeddedKey.builder().wishListId(wishListId).itemId(itemId).build()).isActive(true).build();
      wishListItemRepository.save(wishListItem);
      return itemDetailResponse;
    }
    return null;
  }

  public Boolean del(WishListRequest wishListRequest) {
    Long itemId = wishListRequest.getItemId();
    Long wishListId = wishListRequest.getWishListId();
    List<WishListItem> delWishListItems = new LinkedList<>();
    if (ObjectUtils.isEmpty(itemId)) {
      Optional<WishListItem> wishListItem = wishListItemRepository
          .findByIdWishListIdAndIdItemIdAndIsActiveTrue(wishListId, itemId);
      if (ObjectUtils.isNotEmpty(wishListItem)) {
        delWishListItems.add(wishListItem.get().toBuilder().isActive(false).build());
      } else {
        return false;
      }
    } else {
      Optional<List<WishListItem>> wishListItems = wishListItemRepository.findByIdWishListIdAndIsActiveTrue(wishListId);
      if (ObjectUtils.isNotEmpty(wishListItems)) {
        wishListItems.get().stream()
            .forEach(wishListItem -> delWishListItems.add(wishListItem.toBuilder().isActive(false).build()));
      } else {
        return false;
      }
    }
    wishListItemRepository.saveAll(delWishListItems);
    return true;
  }
}
