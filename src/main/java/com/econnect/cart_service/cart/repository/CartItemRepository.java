package com.econnect.cart_service.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.econnect.cart_service.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<List<CartItem>> findByCartCartIdAndIsActiveTrue(Long cartId);
    Optional<CartItem> findByCartCartIdAndItemIdAndIsActiveTrue(Long cartId, Long itemId);
}