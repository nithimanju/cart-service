package com.econnect.cart_service.wishlist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.econnect.cart_service.model.WishList;

@Repository
public interface WishListRepository extends JpaRepository<WishList,Long>{
    Optional<WishList> findByWishListIdAndUserId(Long wishListId, Long userId);
}
