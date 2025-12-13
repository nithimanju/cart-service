package com.econnect.cart_service.cart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.econnect.cart_service.cart.dto.CartDetailResponse;
import com.econnect.cart_service.cart.dto.CartItemDetailResponse;
import com.econnect.cart_service.cart.dto.CartRequest;
import com.econnect.cart_service.cart.repository.CartRepository;
import com.econnect.cart_service.model.Cart;
import com.econnect.cart_service.model.CartItem;
import com.econnect.cart_service.utils.PredicateUtilsService;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartService {

  private final CartRepository cartRepository;
  private final CartItemService cartItemService;
  private final PredicateUtilsService<Cart> predicateUtilsService;

  public CartDetailResponse get(CartRequest cartRequest) {
    if (ObjectUtils.isEmpty(cartRequest.getCartStatusIds())) {
      cartRequest = cartRequest.toBuilder().cartStatusIds(getCartStatusIds()).build();
    }
    CartDetailResponse cartDetailResponse = null;
    Cart cart = getEntity(cartRequest);
    log.debug("Cart found for Requested CartId and UserId");
    return buildCartDetailResponse(cart);
  }

  private Cart getEntity(CartRequest cartRequest) {
    Specification<Cart> cartSpecifications = buildCartQuerySpecifications(cartRequest);
    List<Cart> cartList = cartRepository.findAll(cartSpecifications);
    if (ObjectUtils.isEmpty(cartList)) {
      log.warn("Cart Not found for Requested CartId and UserId");
      return null;
    } else if (cartList.size() > 1) {
      log.warn("Mulitple active carts found for user");
      return null;
    }
    return cartList.getFirst();
  }

  private Optional<Cart> findActiveCart(CartRequest cartRequest) {
    return cartRepository.findByUserIdAndIsActiveTrue(
        cartRequest.getUserId());
  }

  private Optional<Cart> findActiveUserCart(CartRequest cartRequest) {
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
        .statusId(getCartStatusIds().getFirst())
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
    Cart cart = getEntity(cartRequest.toBuilder().cartStatusIds(getCartStatusIds()).build());
    if (ObjectUtils.isEmpty(cart)) {
      log.debug("No Cart found for requested userId and cartId");
      throw new Exception("No Active Cart for CartId and UserID");
    } else {
      log.debug("Deactiving Cart for useId: {}, cartId: {}", cart.getUserId(), cart.getCartId());
      cart = cart.toBuilder().statusId(cartRequest.getCartStatusIds().getFirst()).isActive(true).build();
      cartRepository.save(cart);
    }
    log.debug("Deactivated and Request for new Cart creation");
    return post(cartRequest);
  }

  public BigDecimal updateCartPrices(CartRequest cartRequest) throws Exception {
    Cart cart = getEntity(cartRequest.toBuilder().cartStatusIds(getCartStatusIds()).build());
    if (ObjectUtils.isEmpty(cart)) {
      log.debug("No Cart found for requested userId and cartId");
      throw new Exception("No Active Cart for CartId and UserID");
    } else {
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
      for (CartItem cartItem : cartItems) {
        if (ObjectUtils.isNotEmpty(cartItem)) {
          calculatedTotalList = calculatedTotalList.add(getBigDecimalValue(cartItem.getListPrice()));
          calculatedTotalTax = calculatedTotalTax.add(getBigDecimalValue(cartItem.getTaxAmount()));
          calculatedTotalDiscount = calculatedTotalDiscount.add(getBigDecimalValue(cartItem.getDiscountPrice()));
          calculatedTotalAmount = calculatedTotalAmount.add(getBigDecimalValue(cartItem.getTotalPrice()));
        }
      }
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

  private Specification<Cart> buildCartQuerySpecifications(CartRequest cartRequest) {
    return (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      predicates = predicateUtilsService.andValidePredicate(predicates, root, "cartId", cartRequest.getCartId());
      predicates = predicateUtilsService.andValidePredicate(predicates, root, "userId", cartRequest.getUserId());
      builder.isTrue(root.get("isActive").equalTo(true));
      if (ObjectUtils.isNotEmpty(cartRequest)) {
        predicates.add(builder.isTrue(root.get("statusId").in(cartRequest.getCartStatusIds())));
      }
      query.distinct(true);
      return builder.and(predicates.stream().toArray(Predicate[]::new));
    };
  }

  private List<Long> getCartStatusIds() {
    return List.of(3L);
  }
}
