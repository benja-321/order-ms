package com.example.order.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.order.controller.OrderController;
import com.example.order.dto.OrderDto;
import com.example.order.helpers.OrderControllerHelper;
import com.example.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private OrderService orderService;
	
	protected static ObjectMapper om = new ObjectMapper();
	
	@Before
	public void setUp() {
		ObjectMapper om = new ObjectMapper();
		om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		JacksonTester.initFields(this, om);
	}
	
	@Test
	public void testInsertOrderSuccessfully() throws Exception{
		OrderDto orderDto = OrderControllerHelper.createOrderDto();
		
		Mockito.when(orderService.insert(orderDto)).thenReturn(orderDto);
		
		mockMvc.perform(
				post("/order")
					.content(om.writeValueAsBytes(orderDto))
					.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isCreated());		
	}
	
	@Test
	public void testGetOrderByIdSuccessfully() throws Exception{
		OrderDto orderDto = OrderControllerHelper.createOrderDto();
		Long id = 1L;
		
		Mockito.when(orderService.getById(Mockito.anyLong())).thenReturn(orderDto);
		
		mockMvc.perform(
					get("/order/{id}", id)
					.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	public void testUpdateOrderSuccessfully() throws Exception{
		OrderDto orderDto = OrderControllerHelper.createOrderDto();
		Long id = 1L;
		
		Mockito.when(orderService.update(Mockito.anyLong(), Mockito.refEq(orderDto))).thenReturn(orderDto);
		
		mockMvc.perform(put("/order/{id}", id)
					.content(om.writeValueAsBytes(orderDto))
					.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	public void testDeleteOrderSuccessfully() throws Exception{
		String mensaje = "Orden eliminada con exito";
		Long id = 1L;
		
		Mockito.when(orderService.delete(Mockito.anyLong())).thenReturn(mensaje);
		
		mockMvc.perform(patch("/order/{id}", id)
					.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk());
	}
}
