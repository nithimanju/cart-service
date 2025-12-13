package com.econnect.cart_service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ItemDetailResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long itemId;
  private String itemNumber;
  private List<Media> medias;
  private Map<String, String> itemTitles;
  private Map<String, List<String>> itemDescriptions;
  private List<Category> parentCategories;
  private Brand brand;
  private BigDecimal price;
  private BigDecimal listPrice;
  private String currency;
  private float discountPercentage;
  private float rating;
  private Availability availability;
  private List<Dealer> dealers;

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class Dealer {
    private String dealerId;
    private String dealerRef;
    private Map<String, String> dealerNames;
    private Map<String, String> dealerDescription;
    private List<Media> dealerMedias;
    private String dealerUrl;
  }
  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class Category {
    private String categoryId;
    private Map<String, String> categoryNames;
    private String categoryUrl;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class Brand {
    private String brandId;
    private Map<String, String> brandNames;
    private String brandUrl;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Getter
  public static class Availability {
    private String availabilityDescription;
    private int availabilityCount;
  }
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Getter
  public static class Media {
    private String mediaId;
    private String mediaName;
    private String mediaPath;
    private String sequence;
}
}
