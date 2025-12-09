package com.econnect.cart_service.proxy;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public abstract class RestService<T, S> {

    private RestClient restClient;

    public abstract URI getURI(T t);

    public abstract Class<S> getType();

    public S get(T t) {
        return restClient.get()
                .uri(getURI(t))
                .retrieve().body(getType());
    }

}
