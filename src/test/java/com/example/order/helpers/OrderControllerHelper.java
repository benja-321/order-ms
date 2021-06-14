package com.example.order.helpers;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.example.order.dto.OrderDetailDto;
import com.example.order.dto.OrderDto;

public class OrderControllerHelper {

	public static OrderDto createOrderDto() {
		OrderDetailDto orderDetailDto = new OrderDetailDto();
		orderDetailDto.setCantidad(2);
		orderDetailDto.setCodigoProducto(1L);
		
		OrderDto orderDto = new OrderDto();
		orderDto.setFechaEnvio(new Date());
		orderDto.setDetalleOrden(List.of(orderDetailDto));
		return orderDto;
	}
}
