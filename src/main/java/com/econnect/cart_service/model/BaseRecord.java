package com.econnect.cart_service.model;

import java.util.Date;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@SuperBuilder(toBuilder = true)
public class BaseRecord {
  @Column(name = "created_date")
  private Date createdDate;
  @Column(name = "modified_date")
  private Date modifiedDate;
  private String creator;
  private String modifier;
}
