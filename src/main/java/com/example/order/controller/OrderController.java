package com.example.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order.dto.OrderDto;
import com.example.order.service.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javassist.NotFoundException;

@RestController
@Api
@RequestMapping("/order")
public class OrderController {

	private OrderService orderService;
	
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@ApiOperation(
			value = "Registrar Orden",
		    nickname = "Registrar Orden",
		    response = String.class)
	@PostMapping()
	public ResponseEntity<OrderDto> insertOrder(@RequestBody OrderDto orderDto) throws Exception{
		return new ResponseEntity<>(orderService.insert(orderDto), HttpStatus.CREATED);
	}
	
	@ApiOperation(
			value = "Mostrar Orden",
		    nickname = "Mostrar Orden",
		    response = String.class)
	@GetMapping("/{id}")
	public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long id) throws NotFoundException{
		return new ResponseEntity<>(orderService.getById(id),HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long id, @RequestBody OrderDto updates) throws Exception{
		return new ResponseEntity<>(orderService.update(id,updates),HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id) throws NotFoundException{
		return new ResponseEntity<>(orderService.delete(id), HttpStatus.OK);
	}
}
