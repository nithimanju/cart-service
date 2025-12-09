package com.econnect.cart_service.wishlist.dto;

import java.io.Serializable;

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
public class WishListRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long wishListId;
    private Long itemId;
}
