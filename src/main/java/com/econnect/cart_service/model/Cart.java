package com.econnect.cart_service.model;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Cart extends BaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ID")
    private Long cartId;
    @Column(name = "CART_TYPE_ID")
    private Long cartTypeId;
    private String description;
    @Column(name = "STATUS_ID")
    private Long statusId;
    @Column(name = "expiration_date")
    private Date modifiedDate;
    @Column(name = "OWNER_USER_ID")
    private Long userId;
    @Column(name = "SELLING_PRICE")
    private BigDecimal sellingPrice;
    @Column(name = "MSRP_PRICE")
    private BigDecimal msrpPrice;
    @Column(name = "LIST_PRICE")
    private BigDecimal listPrice;
    @Column(name = "TOTAL_DISCOUNT")
    private BigDecimal discountPrice;
    @Column(name = "TOTAL_TAX")
    private BigDecimal taxAmount;
    @Column(name = "TAOTAL_MISC_AMOUNT")
    private BigDecimal miscAmount;
}
