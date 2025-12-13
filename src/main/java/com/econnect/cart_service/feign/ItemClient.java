package com.econnect.cart_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.econnect.cart_service.dto.ItemDetailResponse;

@FeignClient(value = "part-service")
public interface ItemClient {
    @GetMapping(value = "/part-detail/{id}")
    public ResponseEntity<ItemDetailResponse> get(@PathVariable Long id);
}
