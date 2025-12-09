package com.econnect.cart_service.wishlist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.econnect.cart_service.dto.ItemDetailResponse;
import com.econnect.cart_service.model.WishList;
import com.econnect.cart_service.wishlist.dto.WishListDetailResponse;
import com.econnect.cart_service.wishlist.dto.WishListRequest;
import com.econnect.cart_service.wishlist.repository.WishListRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class WishListService {

  private final WishListRepository wishListRepository;
  private final WishListItemService wishListItemService;

  public WishListDetailResponse get(WishListRequest wishListRequest) {

    Long wishListId = wishListRequest.getWishListId();

    Optional<WishList> wishListOpt = wishListRepository.findById(wishListId);
    if (ObjectUtils.isEmpty(wishListOpt)) {
      return null;
    }
    WishList wishList = wishListOpt.get();
    List<ItemDetailResponse> itemDetailResponse = wishListItemService.getByWishListId(wishListId);

    return WishListDetailResponse.builder().itemDetailResponses(itemDetailResponse).wishListId(wishListId)
        .userId(wishList.getUserId()).wishListName(wishList.getWishListName()).build();
  }

  public Long post(WishListRequest wishListRequest) {
    WishList wishList = WishList.builder().userId(wishListRequest.getUserId()).isActive(true).build();
    WishList savedWishList = wishListRepository.save(wishList);
    return savedWishList.getWishListId();
  }

  public Long put(WishListRequest wishListRequest) {
    WishList wishList = get(wishListRequest.getWishListId(), wishListRequest.getUserId());
    if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(wishList)) {
      wishList = wishList.toBuilder().isActive(false).build();
      wishListRepository.save(wishList);
      wishListItemService.del(wishListRequest);
    }
    return post(wishListRequest);
  }

  private WishList get(Long wishListId, Long userId) {
    Optional<WishList> wishListOpt = wishListRepository.findByWishListIdAndUserId(wishListId, userId);
    if (ObjectUtils.isEmpty(wishListOpt)) {
      return null;
    }
    return wishListOpt.get();
  }
}
