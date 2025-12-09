package com.econnect.cart_service.cart.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.econnect.cart_service.cart.dto.CartDetailResponse;
import com.econnect.cart_service.cart.dto.CartItemDetailResponse;
import com.econnect.cart_service.cart.dto.CartRequest;
import com.econnect.cart_service.cart.repository.CartRepository;
import com.econnect.cart_service.model.Cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartService {

  private CartRepository cartRepository;
  private CartItemService cartItemService;

  public CartDetailResponse get(CartRequest cartRequest) {
    CartDetailResponse cartDetailResponse = null;
    Optional<Cart> optCart = cartRepository.findByCartIdAndUserIdAndIsActiveTrue(cartRequest.getCartId(),
        cartRequest.getUserId());
    if (ObjectUtils.isEmpty(optCart)) {
      return cartDetailResponse;
    }

    return buCartDetailResponse(optCart.get());
  }

  private CartDetailResponse buCartDetailResponse(Cart cart) {
    List<CartItemDetailResponse> cartItems = cartItemService.getCartItems(cart);
    return CartDetailResponse.builder()
        .cartItems(cartItems)
        .cartId(cart.getCartId())
        .description(cart.getDescription())
        .topSellingPrice(cart.getSellingPrice())
        .totalListPrice(cart.getListPrice())
        .totalDiscountPrice(cart.getDiscountPrice())
        .totalTaxAmount(cart.getTaxAmount())
        .totalMiscAmount(cart.getMiscAmount())
        .build();
  }
}
