package com.example.order.service;

import com.example.order.dto.OrderDto;
import javassist.NotFoundException;

public interface OrderService {

	public OrderDto insert(OrderDto orderDto) throws Exception;
	
	public OrderDto getById(Long id) throws NotFoundException;
	
	public OrderDto update(Long id, OrderDto updates) throws Exception;
	
	public String delete(Long id) throws NotFoundException;
}
