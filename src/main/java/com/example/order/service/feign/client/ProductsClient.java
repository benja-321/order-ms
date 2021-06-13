package com.example.order.service.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.order.service.feign.dto.ProductResponseDto;

import feign.FeignException.FeignClientException;

@FeignClient(value = "busqueda-datos-producto", url = "${productClient.url}")
public interface ProductsClient {

	@GetMapping(value = "/product/{id}")
	ProductResponseDto getProductById(@PathVariable("id") Long id) throws FeignClientException;
}
