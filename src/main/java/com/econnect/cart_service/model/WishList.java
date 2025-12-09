package com.econnect.cart_service.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
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
public class WishList extends BaseRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "WISHLIST_ID")
  private Long wishListId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "WISHLIST_NAME")
  private String wishListName;

  @Column(name = "ACTIVE")
  private Boolean isActive;

  @OneToMany(mappedBy = "wishList")
  private List<WishListItem> wishListItems;
}
