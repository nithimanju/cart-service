package com.econnect.cart_service.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class CartController {
    
    @GetMapping("/cart")
    public ResponseEntity<String> get(@RequestParam Long cartId) {

        return new ResponseEntity<>("null", HttpStatus.OK);
    } 
}
