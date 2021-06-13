package com.example.order.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto {

	private LocalDate fechaEnvio;
	private List<OrderDetailDto> detalleOrden;
}
