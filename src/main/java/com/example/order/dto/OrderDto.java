package com.example.order.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto {

	private Date fechaEnvio;
	private List<OrderDetailDto> detalleOrden;
}
