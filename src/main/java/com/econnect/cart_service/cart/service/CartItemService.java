package com.econnect.cart_service.cart.service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.econnect.cart_service.cart.dto.CartDetailResponse;
import com.econnect.cart_service.cart.dto.CartItemDetailResponse;
import com.econnect.cart_service.cart.dto.CartRequest;
import com.econnect.cart_service.cart.repository.CartRepository;
import com.econnect.cart_service.dto.ItemDetailResponse;
import com.econnect.cart_service.model.Cart;
import com.econnect.cart_service.model.CartItem;
import com.econnect.cart_service.proxy.item.ItemProxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartItemService {

  private final ItemProxy itemProxy;

  public List<CartItemDetailResponse> getCartItems(Cart cart) {
    List<CartItemDetailResponse> cartItemDetailResponses = new LinkedList<>();
    if (ObjectUtils.isNotEmpty(cart.getCartItems())) {
      cart.getCartItems().parallelStream().forEach(
          cartItem -> cartItemDetailResponses.add(buildCartItemResponse(cartItem)));
    }
    return cartItemDetailResponses;
  }

  private ItemDetailResponse fetchItemDetails(Long itemId) {
    return itemProxy.get(itemId);
  }

  private CartItemDetailResponse buildCartItemResponse(CartItem cartItem) {
    return CartItemDetailResponse.builder()
        .cartItemId(cartItem.getCartItemId())
        .sellingPrice(cartItem.getSellingPrice())
        .msrpPrice(cartItem.getMsrpPrice())
        .listPrice(cartItem.getListPrice())
        .discountPrice(cartItem.getDiscountPrice())
        .taxAmount(cartItem.getTaxAmount())
        .miscAmount(cartItem.getMiscAmount())
        .itemDetailResponse(fetchItemDetails(cartItem.getItemId()))
        .build();
  }
}
