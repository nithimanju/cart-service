package com.econnect.cart_service.cart.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
public class CartDetailResponse implements Serializable {
      private static final long serialVersionUID = 1L;
      
    private Long cartId;

    private String description;
    private List<CartItemDetailResponse> cartItems;
    private BigDecimal topSellingPrice;
    private BigDecimal totalListPrice;
    private BigDecimal totalDiscountPrice;
    private BigDecimal totalTaxAmount;
    private BigDecimal totalMiscAmount;
}