package com.example.order.service.feign.dto;

import lombok.Data;

@Data
public class ProductResponseDto {

	private Long codigo;
	private String descripcion;
	private String nombre;
	private Double precio;
}
