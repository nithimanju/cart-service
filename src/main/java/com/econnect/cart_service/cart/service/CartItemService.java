package com.econnect.cart_service.cart.service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.econnect.cart_service.cart.dto.CartItemDetailResponse;
import com.econnect.cart_service.cart.dto.CartItemRequest;
import com.econnect.cart_service.cart.repository.CartItemRepository;
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
  private final CartItemRepository cartItemRepository;

  private final BigDecimal ZERO = new BigDecimal(0);

  public List<CartItemDetailResponse> getCartItems(Cart cart) {
    List<CartItemDetailResponse> cartItemDetailResponses = new LinkedList<>();
    if (ObjectUtils.isNotEmpty(cart.getCartItems())) {
      cart.getCartItems().parallelStream().forEach(
          cartItem -> cartItemDetailResponses.add(buildCartItemResponse(cartItem, null)));
    }
    return cartItemDetailResponses;
  }

  private ItemDetailResponse fetchItemDetails(Long itemId) {
    ItemDetailResponse itemDetailResponse = null;
    try {
      itemDetailResponse = itemProxy.get(itemId);
    } catch (Exception e) {
      log.error("Error while fetching item detail response for Item Id: {}", itemId, e);
      return null;
    }
    log.debug("Successfully fetched the item details");
    return itemDetailResponse;
  }

  private CartItemDetailResponse buildCartItemResponse(CartItem cartItem, ItemDetailResponse itemDetailResponse) {
    log.debug("Building CartItem detail response for the cartItem");
    return CartItemDetailResponse.builder()
        .cartItemId(cartItem.getCartItemId())
        .sellingPrice(cartItem.getSellingPrice())
        .totalPrice(cartItem.getTotalPrice())
        .listPrice(cartItem.getListPrice())
        .discountPrice(cartItem.getDiscountPrice())
        .taxAmount(cartItem.getTaxAmount())
        .miscAmount(cartItem.getMiscAmount())
        .itemDetailResponse(
            ObjectUtils.isEmpty(itemDetailResponse) ? fetchItemDetails(cartItem.getItemId()) : itemDetailResponse)
        .build();
  }

  private List<CartItem> getAll(Long cartId) {
    Optional<List<CartItem>> cartItemsOpt = cartItemRepository.findByCartCartIdAndIsActiveTrue(cartId);
    if (ObjectUtils.isEmpty(cartItemsOpt)) {
      return null;
    }
    return cartItemsOpt.get();
  }

  public CartItemDetailResponse post(CartItemRequest cartItemRequest) {
    Long itemId = cartItemRequest.getItemId();
    ItemDetailResponse itemDetailResponse = fetchItemDetails(itemId);
    log.debug("Successfully fetched Item details");

    CartItem saveCartItem = null;
    BigDecimal sellingPrice = new BigDecimal(itemDetailResponse.getPrice());
    BigDecimal quantity = new BigDecimal(cartItemRequest.getQuantity());

    CartItem cartItem = getCartItemforCartIdItemId(cartItemRequest.getCartId(), itemId);
    if (ObjectUtils.isNotEmpty(cartItem)) {
      saveCartItem = modifyItemQuantity(cartItem, quantity);
    } else {
      BigDecimal totalAmount = sellingPrice.multiply(quantity);
      saveCartItem = CartItem.builder().itemId(itemId).totalPrice(totalAmount).quantity(quantity)
          .userId(cartItemRequest.getUserId())
          .sellingPrice(sellingPrice).taxAmount(ZERO).discountPrice(ZERO).miscAmount(ZERO).build();
    }

    saveCartItem = save(saveCartItem);
    return buildCartItemResponse(saveCartItem, itemDetailResponse);
  }

  public CartItemDetailResponse put(CartItemRequest cartItemRequest) throws Exception {
    Long itemId = cartItemRequest.getItemId();
    ItemDetailResponse itemDetailResponse = fetchItemDetails(cartItemRequest.getItemId());

    BigDecimal quantity = new BigDecimal(cartItemRequest.getQuantity());
    if (quantity.compareTo(ZERO) <= 0) {
      if (del(cartItemRequest)) {
        return null;
      }
      throw new Exception("Provided quantity is less than proper value and delete of CartItem failed");
    }

    CartItem cartItem = getCartItemforCartIdItemId(cartItemRequest.getCartId(), itemId);
    if (ObjectUtils.isNotEmpty(cartItem)) {
      CartItem saveCartItem = modifyItemQuantity(cartItem, new BigDecimal(cartItemRequest.getQuantity()));
      saveCartItem = save(saveCartItem);
      return buildCartItemResponse(saveCartItem, itemDetailResponse);
    } else {
      throw new Exception("No CartItem present for ItemId in the active cart");
    }
  }

  private CartItem modifyItemQuantity(CartItem cartItem, BigDecimal quantity) {
    return cartItem.toBuilder().quantity(quantity)
        .totalPrice(calculateTotalAmount(cartItem, quantity)).build();
  }

  private BigDecimal calculateTotalAmount(CartItem cartItem, BigDecimal quantity) {
    BigDecimal listPrice = getBigDecimalValue(cartItem.getListPrice());
    BigDecimal discountPrice = getBigDecimalValue(cartItem.getDiscountPrice());
    BigDecimal miscPrice = getBigDecimalValue(cartItem.getMiscAmount());
    BigDecimal taxAmount = getBigDecimalValue(cartItem.getTaxAmount());
    return listPrice.multiply(quantity).add(taxAmount).add(miscPrice).subtract(discountPrice);
  }

  public Boolean del(CartItemRequest cartItemRequest) {
    Optional<CartItem> cartItem = cartItemRepository.findById(cartItemRequest.getCartItemId());
    if (ObjectUtils.isNotEmpty(cartItem)) {
      CartItem deletedCartItem = cartItem.get().toBuilder().isActive(false).build();
      save(deletedCartItem);
      log.debug("Successfully delted the requested cartItem", cartItemRequest.getCartItemId());
      return true;
    }
    log.debug("No CartItem found for the provided CartItemId");
    return false;
  }

  private CartItem getCartItemforCartIdItemId(Long cartId, Long itemId) {
    Optional<CartItem> cartItems = cartItemRepository.findByCartCartIdAndItemIdAndIsActiveTrue(cartId, itemId);
    if (ObjectUtils.isEmpty(cartItems)) {
      return null;
    }
    return cartItems.get();
  }

  private BigDecimal getBigDecimalValue(BigDecimal value) {
    if (ObjectUtils.isEmpty(value)) {
      return new BigDecimal(0);
    }
    return value;
  }

  private CartItem save(CartItem cartItem) {
    return cartItemRepository.save(cartItem);
  }
}
