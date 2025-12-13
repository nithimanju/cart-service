package com.econnect.cart_service.cart.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.econnect.cart_service.cart.dto.CartDetailResponse;
import com.econnect.cart_service.cart.dto.CartItemDetailResponse;
import com.econnect.cart_service.cart.dto.CartRequest;
import com.econnect.cart_service.cart.repository.CartRepository;
import com.econnect.cart_service.model.Cart;
import com.econnect.cart_service.model.CartItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartService {

  private final CartRepository cartRepository;
  private final CartItemService cartItemService;

  public CartDetailResponse get(CartRequest cartRequest) {
    CartDetailResponse cartDetailResponse = null;
    Optional<Cart> optCart = null;
    if (ObjectUtils.isEmpty(cartRequest.getCartId())) {
      optCart = findActiveCart(cartRequest);
    } else {
      optCart = findActiveUserCart(cartRequest);
    }
    if (ObjectUtils.isEmpty(optCart)) {
      log.debug("Cart Not found for Requested CartId and UserId");
      return cartDetailResponse;
    }
    log.debug("Cart found for Requested CartId and UserId");
    return buildCartDetailResponse(optCart.get());
  }

  private Optional<Cart> findActiveCart(CartRequest cartRequest) {
    return cartRepository.findByUserIdAndIsActiveTrue(
        cartRequest.getUserId());
  }

  public Optional<Cart> findActiveUserCart(CartRequest cartRequest) {
    return cartRepository.findByCartIdAndUserIdAndIsActiveTrue(cartRequest.getCartId(),
        cartRequest.getUserId());
  }

  private CartDetailResponse buildCartDetailResponse(Cart cart) {
    List<CartItemDetailResponse> cartItems = cartItemService.getCartItems(cart);
    log.debug("Cart Items successfully fetched from cart service count: {}", cartItems.size());
    return CartDetailResponse.builder()
        .cartItems(cartItems)
        .cartId(cart.getCartId())
        .description(cart.getDescription())
        .totalAmount(cart.getTotalAmount())
        .totalDiscountPrice(cart.getDiscountPrice())
        .totalTaxAmount(cart.getTaxAmount())
        .totalMiscAmount(cart.getMiscAmount())
        .totalListPrice(cart.getTotalListPrice())
        .build();
  }

  public Long post(CartRequest cartRequest) {
    Cart cart = Cart.builder()
        .userId(cartRequest.getUserId())
        .isActive(true)
        .createdDate(new Date())
        .totalAmount(new BigDecimal(0))
        .discountPrice(new BigDecimal(0))
        .taxAmount(new BigDecimal(0))
        .miscAmount(new BigDecimal(0))
        .build();
    Cart savedCart = cartRepository.save(cart);
    if (ObjectUtils.isEmpty(savedCart)) {
      log.debug("Cart model not returned from saving the cart");
      return null;
    }
    log.debug("New Cart created succesffully");
    return savedCart.getCartId();
  }

  public Long put(CartRequest cartRequest) throws Exception {
    Optional<Cart> optCart = findActiveCart(cartRequest);
    if (ObjectUtils.isEmpty(optCart)) {
      log.debug("No Cart found for requested userId and cartId");
      throw new Exception("No Active Cart for CartId and UserID");
    } else {
      Cart cart = optCart.get();
      log.debug("Deactiving Cart for useId: {}, cartId: {}", cart.getUserId(), cart.getCartId());
      cart = cart.toBuilder().isActive(false).build();
      cartRepository.save(cart);
    }
    log.debug("Deactivated and Request for new Cart creation");
    return post(cartRequest);
  }

  public BigDecimal updateCartPrices(CartRequest cartRequest) throws Exception {
    Optional<Cart> optCart = findActiveCart(cartRequest);
    if (ObjectUtils.isEmpty(optCart)) {
      log.debug("No Cart found for requested userId and cartId");
      throw new Exception("No Active Cart for CartId and UserID");
    } else {
      Cart cart = optCart.get();
      cart = calculateTotal(cart);
      log.debug("Deactiving Cart for useId: {}, cartId: {}", cart.getUserId(), cart.getCartId());
      cart = cart.toBuilder().isActive(true).build();
      cartRepository.save(cart);
      return cart.getTotalAmount();
    }
  }

  public Cart calculateTotal(Cart cart) {
    List<CartItem> cartItems = cartItemService.getAll(cart.getCartId());
    BigDecimal calculatedTotalList = new BigDecimal(0);
    BigDecimal calculatedTotalTax = new BigDecimal(0);
    BigDecimal calculatedTotalDiscount = new BigDecimal(0);
    BigDecimal calculatedTotalAmount = new BigDecimal(0);
    if (ObjectUtils.isNotEmpty(cartItems)) {
      cartItems.stream().filter(ObjectUtils::isNotEmpty).forEach(cartItem -> {
        calculatedTotalList.add(getBigDecimalValue(cartItem.getListPrice()));
        calculatedTotalTax.add(getBigDecimalValue(cartItem.getTaxAmount()));
        calculatedTotalDiscount.add(getBigDecimalValue(cartItem.getDiscountPrice()));
        calculatedTotalAmount.add(getBigDecimalValue(cartItem.getTotalPrice()));
      });
    }
    cart = cart.toBuilder().totalListPrice(calculatedTotalList).taxAmount(calculatedTotalAmount)
        .discountPrice(calculatedTotalDiscount).taxAmount(calculatedTotalTax).totalAmount(calculatedTotalAmount)
        .build();
    return cart;
  }

  private BigDecimal getBigDecimalValue(BigDecimal value) {
    if (ObjectUtils.isEmpty(value)) {
      return new BigDecimal(0);
    }
    return value;
  }
}
