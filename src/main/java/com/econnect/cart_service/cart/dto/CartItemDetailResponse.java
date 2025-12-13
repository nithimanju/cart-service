package com.econnect.cart_service.cart.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.econnect.cart_service.dto.ItemDetailResponse;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CartItemDetailResponse implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long cartItemId;

  private BigDecimal sellingPrice;
  private BigDecimal totalPrice;
  private BigDecimal listPrice;
  private BigDecimal discountPrice;
  private BigDecimal taxAmount;
  private BigDecimal miscAmount;
  private BigDecimal quantity;
  private ItemDetailResponse itemDetailResponse;
}