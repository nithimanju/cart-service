package com.econnect.cart_service.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class CartItem extends BaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ITEM_ID")
    private Long cartItemId;
    @ManyToOne
    @JoinColumn(name = "CART_ID", insertable = false, updatable = false)
    private Cart cart;

    @Column(name = "CART_ID")
    private Long cartId;
    @Column(name = "OWNER_USER_ID")
    private Long userId;
    @Column(name = "SELLING_PRICE")
    private BigDecimal sellingPrice;
    @Column(name = "TOTAL_PRICE")
    private BigDecimal totalPrice;
    @Column(name = "LIST_PRICE")
    private BigDecimal listPrice;
    @Column(name = "DISCOUNT_AMOUNT")
    private BigDecimal discountPrice;
    @Column(name = "TAX_AMOUNT")
    private BigDecimal taxAmount;
    @Column(name = "MISC_AMOUNT")
    private BigDecimal miscAmount;
    @Column(name = "ITEM_ID")
    private Long itemId;
    @Column(name = "ACTIVE")
    private Boolean isActive;
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
}
