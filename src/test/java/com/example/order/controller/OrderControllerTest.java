package com.example.order.controller;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.order.controller.OrderController;
import com.example.order.dto.OrderDetailDto;
import com.example.order.dto.OrderDto;
import com.example.order.service.OrderService;

import javassist.NotFoundException;

@RunWith(SpringRunner.class)
public class OrderControllerTest {
	
	@MockBean
	private OrderService orderService;
	
	private OrderController orderController;
	
	@Before
	public void setup() {
		orderController = new OrderController(orderService);
	}
	
	@Test
	public void testInsertOrderSuccessfully() throws Exception{
		OrderDetailDto orderDetailDto = new OrderDetailDto();
		orderDetailDto.setCantidad(2);
		orderDetailDto.setCodigoProducto(1L);
		
		OrderDto orderDto = new OrderDto();
		orderDto.setFechaEnvio(LocalDate.now());
		orderDto.setDetalleOrden(List.of(orderDetailDto));
		
		Mockito.when(orderService.insert(orderDto)).thenReturn(orderDto);
		
		ResponseEntity<OrderDto> response = orderController.insertOrder(orderDto);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
		Assertions.assertThat(response.getBody()).isEqualTo(orderDto);
	}
	
	@Test
	public void testGetOrderByIdSuccessfully() throws NotFoundException{
		OrderDetailDto orderDetailDto = new OrderDetailDto();
		orderDetailDto.setCantidad(2);
		orderDetailDto.setCodigoProducto(1L);
		
		OrderDto orderDto = new OrderDto();
		orderDto.setFechaEnvio(LocalDate.now());
		orderDto.setDetalleOrden(List.of(orderDetailDto));
		
		Mockito.when(orderService.getById(Mockito.anyLong())).thenReturn(orderDto);
		
		ResponseEntity<OrderDto> response = orderController.getOrderById(1L);
		
		Assertions.assertThat(response.getBody()).isEqualTo(orderDto);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void testUpdateOrderSuccessfully() throws Exception{
		OrderDetailDto orderDetailDto = new OrderDetailDto();
		orderDetailDto.setCantidad(2);
		orderDetailDto.setCodigoProducto(1L);
		
		OrderDto orderDto = new OrderDto();
		orderDto.setFechaEnvio(LocalDate.now());
		orderDto.setDetalleOrden(List.of(orderDetailDto));
		
		Mockito.when(orderService.update(Mockito.anyLong(), Mockito.refEq(orderDto))).thenReturn(orderDto);
		
		ResponseEntity<OrderDto> response = orderController.updateOrder(1L, orderDto);
		
		Assertions.assertThat(response.getBody()).isEqualTo(orderDto);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	public void testDeleteOrderSuccessfully() throws NotFoundException{
		String mensaje = "Orden eliminada con exito";
		
		Mockito.when(orderService.delete(Mockito.anyLong())).thenReturn(mensaje);
		
		ResponseEntity<String> response = orderController.deleteOrder(1L);
		
		Assertions.assertThat(response.getBody()).isEqualTo(mensaje);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
}
