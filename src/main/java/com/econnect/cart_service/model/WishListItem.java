package com.econnect.cart_service.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Getter
@Table
public class WishListItem extends BaseRecord {

  @Id
  private WishListItemEmbeddedKey id;
  @Column(name = "ACTIVE")
  private Boolean isActive;

  @ManyToOne
  @JoinColumn(name = "WISHLIST_ID", insertable = false, updatable = false)
  private WishList wishList;

  @Embeddable
  @Getter
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode
  public static class WishListItemEmbeddedKey {

    @Column(name = "WISHLIST_ID")
    private Long wishListId;
    @Column(name = "ITEM_ID")
    private Long itemId;
  }
}
