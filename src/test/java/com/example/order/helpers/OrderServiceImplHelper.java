package com.example.order.helpers;

import java.time.LocalDate;
import java.util.List;

import com.example.order.domain.Order;
import com.example.order.domain.OrderDetail;
import com.example.order.dto.OrderDetailDto;
import com.example.order.dto.OrderDto;
import com.example.order.service.feign.dto.ProductResponseDto;

public class OrderServiceImplHelper {

	public static Order createOrder() {
		Order newOrder = new Order();
		newOrder.setId(1L);
		newOrder.setActive(true);
		newOrder.setDate(LocalDate.now());
		newOrder.setShipmentDate(LocalDate.now());
		newOrder.setTotal(10.0);
		return newOrder;
	}
	
	public static OrderDetail createOrderDetail() {
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setId(1L);
		orderDetail.setIdProduct(1L);
		orderDetail.setQuantity(2);
		return orderDetail;
	}
	
	public static OrderDto createOrderDto() {
		OrderDetailDto orderDetailDto = new OrderDetailDto();
		orderDetailDto.setCantidad(2);
		orderDetailDto.setCodigoProducto(1L);
		
		OrderDto orderDto = new OrderDto();
		orderDto.setFechaEnvio(LocalDate.now());
		orderDto.setDetalleOrden(List.of(orderDetailDto));
		return orderDto;
	}
	
	public static OrderDto createOrderDtoUpdates() {
		OrderDto orderDto = new OrderDto();
		orderDto.setFechaEnvio(LocalDate.now());
		orderDto.setDetalleOrden(List.of());
		return orderDto;
	}
	
	public static OrderDto createOrderDtoUpdatesWithDetails() {
		OrderDetailDto orderDetailDto = new OrderDetailDto();
		orderDetailDto.setCantidad(4);
		orderDetailDto.setCodigoProducto(2L);
		
		OrderDto orderDto = new OrderDto();
		orderDto.setFechaEnvio(LocalDate.now());
		orderDto.setDetalleOrden(List.of(orderDetailDto));
		return orderDto;
	}
	
	public static Order createOrderUpdate() {
		Order newOrder = new Order();
		newOrder.setId(1L);
		newOrder.setActive(true);
		newOrder.setDate(LocalDate.now());
		newOrder.setShipmentDate(LocalDate.now());
		newOrder.setTotal(20.0);
		return newOrder;
	}
	
	public static OrderDetail createOrderDetailUpdates() {
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setId(2L);
		orderDetail.setIdProduct(2L);
		orderDetail.setQuantity(4);
		return orderDetail;
	}
	
	public static ProductResponseDto createProduct() {
		ProductResponseDto product = new ProductResponseDto();
		product.setCodigo(1L);
		product.setDescripcion("abc");
		product.setNombre("qwe");
		product.setPrecio(5.0);
		return product;
	}
	
	public static Order createOrderEliminated() {
		Order newOrder = new Order();
		newOrder.setId(1L);
		newOrder.setActive(false);
		newOrder.setDate(LocalDate.now());
		newOrder.setShipmentDate(LocalDate.now());
		newOrder.setTotal(10.0);
		return newOrder;
	}
}
