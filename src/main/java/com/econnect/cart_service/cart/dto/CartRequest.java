package com.econnect.cart_service.cart.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CartRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long cartId;
  private Long userId;
  private Boolean isGuest;
  private List<Long> cartStatusIds;
}
