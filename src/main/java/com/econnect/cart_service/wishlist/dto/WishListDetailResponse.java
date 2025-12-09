package com.econnect.cart_service.wishlist.dto;

import java.io.Serializable;
import java.util.List;

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
public class WishListDetailResponse implements Serializable {
      private static final long serialVersionUID = 1L;
    
    private Long wishListId;
    private Long userId;
    private String wishListName;
    private List<ItemDetailResponse> itemDetailResponses;
}
