package com.econnect.cart_service.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.econnect.cart_service.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {
    Optional<Cart> findByCartIdAndUserIdAndIsActiveTrue(Long cartId, Long usreId);
    Optional<Cart> findByUserIdAndIsActiveTrue(Long usreId);
}
