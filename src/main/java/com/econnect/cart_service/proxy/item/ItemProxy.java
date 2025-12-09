package com.econnect.cart_service.proxy.item;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.econnect.cart_service.dto.ItemDetailResponse;
import com.econnect.cart_service.proxy.RestService;

@Service
public class ItemProxy extends RestService<Long, ItemDetailResponse> {

    public ItemProxy(RestClient restClient) {
        super(restClient);
    }

    private String getBaseURL() {
        return "https://item-service";
    }

    private String getQueryParameter() {
        return "/part-detail/{Id}";
    }

    @Override
    public Class<ItemDetailResponse> getType() {
        return ItemDetailResponse.class;
    }

    @Override
    public URI getURI(Long t) {
        String uriTemplate = getBaseURL().concat(getQueryParameter());
        URI uri = UriComponentsBuilder.fromUriString(uriTemplate)
                .buildAndExpand(t)
                .toUri();
        return uri;
    }
}
