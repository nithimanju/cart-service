package com.econnect.cart_service.utils;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Path;

@Service
public class PredicateUtilsService<T> {

  public List<Predicate> andValidePredicate(List<Predicate> predicates, Path<T> root, String fieldName,
      Object fieldValue) {

    if (ObjectUtils.isNotEmpty(fieldValue)) {
      predicates.add(root.get(fieldName).equalTo(fieldValue));
    }
    return predicates;
  }

}
