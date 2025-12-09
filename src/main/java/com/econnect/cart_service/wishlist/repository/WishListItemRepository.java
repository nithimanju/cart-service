package com.econnect.cart_service.wishlist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.econnect.cart_service.model.WishListItem;
import com.econnect.cart_service.model.WishListItem.WishListItemEmbeddedKey;

@Repository
public interface WishListItemRepository extends JpaRepository<WishListItem, WishListItemEmbeddedKey> {
  Optional<List<WishListItem>> findByIdWishListIdAndIsActiveTrue(Long wishListId);

  Optional<WishListItem> findByIdWishListIdAndIdItemIdAndIsActiveTrue(Long wishListId, Long itemId);
}
