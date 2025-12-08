package com.econnect.cart_service.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.econnect.cart_service.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}
