package com.example.order.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.order.domain.Order;
import com.example.order.domain.OrderDetail;
import com.example.order.dto.OrderDto;
import com.example.order.helpers.OrderServiceImplHelper;
import com.example.order.mapper.OrderMapper;
import com.example.order.repository.OrderDetailRepository;
import com.example.order.repository.OrderRepository;
import com.example.order.service.feign.client.ProductsClient;
import com.example.order.service.feign.dto.ProductResponseDto;
import com.example.order.service.impl.OrderServiceImpl;

import feign.FeignException;
import feign.FeignException.FeignClientException;
import javassist.NotFoundException;

@RunWith(SpringRunner.class)
public class OrderServiceImplTest {

	private OrderService orderService;
	
	@MockBean
	private OrderRepository orderRepository;
	@MockBean
	private OrderDetailRepository orderDetailRepository;
	@MockBean
	private ProductsClient productsClient;
	@MockBean
	private OrderMapper orderMapper;
	
	@Before
	public void setup() {
		orderService = new OrderServiceImpl(orderRepository, orderDetailRepository, orderMapper, productsClient);
	}
	
	@Test
	public void testInsertSuccessfully() throws Exception{
		ProductResponseDto product = OrderServiceImplHelper.createProduct();
		OrderDto orderDto = OrderServiceImplHelper.createOrderDto();
		Order newOrder = OrderServiceImplHelper.createOrder();
		List<OrderDetail> orderDetails = List.of(OrderServiceImplHelper.createOrderDetail());
		
		Mockito.when(orderMapper.mapOrderDtoToOrder(orderDto)).thenReturn(newOrder);
		Mockito.when(orderMapper.mapListOrderDetailDtoToListOrderDetail(orderDto.getDetalleOrden())).thenReturn(orderDetails);
		Mockito.when(productsClient.getProductById(Mockito.anyLong())).thenReturn(product);
		Mockito.when(orderRepository.save(Mockito.refEq(newOrder))).thenReturn(newOrder);
		Mockito.when(orderDetailRepository.saveAll(Mockito.eq(orderDetails))).thenReturn(orderDetails);
		Mockito.when(orderMapper.mapOrderAndDetailsToOrderDto(newOrder, orderDetails)).thenReturn(orderDto);
		
		OrderDto newOrderDto = orderService.insert(orderDto);
		
		Assertions.assertThat(newOrderDto.getFechaEnvio()).isEqualTo(orderDto.getFechaEnvio());
		Assertions.assertThat(newOrderDto.getDetalleOrden().get(0).getCodigoProducto()).isEqualTo(orderDto.getDetalleOrden().get(0).getCodigoProducto());
		Assertions.assertThat(newOrderDto.getDetalleOrden().get(0).getCantidad()).isEqualTo(orderDto.getDetalleOrden().get(0).getCantidad());
	}
	
	@Test(expected = Exception.class)
	public void testInsertNotConnection() throws Exception{
		Order newOrder = OrderServiceImplHelper.createOrder();
		List<OrderDetail> orderDetails = List.of(OrderServiceImplHelper.createOrderDetail());
		OrderDto orderDto = OrderServiceImplHelper.createOrderDto();
		
		Mockito.when(orderMapper.mapOrderDtoToOrder(orderDto)).thenReturn(newOrder);
		Mockito.when(orderMapper.mapListOrderDetailDtoToListOrderDetail(orderDto.getDetalleOrden())).thenReturn(orderDetails);
		Mockito.when(productsClient.getProductById(Mockito.anyLong())).thenThrow(FeignClientException.class);
		
		orderService.insert(orderDto);
		/*try {
			orderService.insert(orderDto);
		} catch (FeignClientException ex) {
			Mockito.verify(productsClient).getProductById(Mockito.anyLong());
			throw ex;
		}*/
	}
	
	/*
	@Test(expected = FeignException.class)
	public void testInsertIdNotFound() throws FeignException{
		Order newOrder = OrderServiceImplHelper.createOrder();
		List<OrderDetail> orderDetails = List.of(OrderServiceImplHelper.createOrderDetail());
		OrderDto orderDto = OrderServiceImplHelper.createOrderDto();
		
		Mockito.when(orderMapper.mapOrderDtoToOrder(orderDto)).thenReturn(newOrder);
		Mockito.when(orderMapper.mapListOrderDetailDtoToListOrderDetail(orderDto.getDetalleOrden())).thenReturn(orderDetails);
		Mockito.when(productsClient.getProductById(Mockito.anyLong())).thenThrow(FeignException.class);
		
		try {
			orderService.insert(orderDto);
		} catch (FeignException ex) {
			Mockito.verify(productsClient).getProductById(Mockito.anyLong());
			throw ex;
		}
	}*/
	
	@Test
	public void testGetByIdSuccessfully() throws NotFoundException{
		
		OrderDto orderDto = OrderServiceImplHelper.createOrderDto();		
		List<OrderDetail> orderDetails = List.of(OrderServiceImplHelper.createOrderDetail());
		Order newOrder = OrderServiceImplHelper.createOrder();
		
		Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(newOrder));
		Mockito.when(orderDetailRepository.findAllByOrderId(Mockito.anyLong())).thenReturn(Optional.of(orderDetails));
		Mockito.when(orderMapper.mapOrderAndDetailsToOrderDto(Mockito.refEq(newOrder), Mockito.refEq(orderDetails))).thenReturn(orderDto);
		
		Assertions.assertThat(orderService.getById(1L)).isEqualTo(orderDto);
	}
	
	@Test(expected = NotFoundException.class)
	public void testGetByIdNotFound() throws NotFoundException{
		Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		try {
			orderService.getById(99L);
		} catch(NotFoundException ex) {
			Mockito.verify(orderRepository).findById(99L);
			throw ex;
		}
	}
	
	@Test
	public void testUpdateOnlyOrderSuccessfully() throws Exception{
		OrderDto updates = OrderServiceImplHelper.createOrderDtoUpdates();
		Order order = OrderServiceImplHelper.createOrder();
		Order orderUpdate = OrderServiceImplHelper.createOrderUpdate();
		
		Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
		Mockito.when(orderMapper.mapListOrderDetailDtoToListOrderDetail(Mockito.refEq(updates.getDetalleOrden()))).thenReturn(List.of());
		Mockito.when(orderRepository.save(Mockito.refEq(orderUpdate, "total"))).thenReturn(orderUpdate);
		Mockito.when(orderMapper.mapOrderAndDetailsToOrderDto(Mockito.any(),Mockito.any())).thenReturn(updates);
		
		Assertions.assertThat(orderService.update(1L, updates)).isEqualTo(updates);
	}
	
	@Test
	public void testUpdateOrderWithDetailsSuccessfully() throws Exception{
		OrderDto updates = OrderServiceImplHelper.createOrderDtoUpdatesWithDetails();
		Order order = OrderServiceImplHelper.createOrder();
		List<OrderDetail> orderDetailListUpdates = List.of(OrderServiceImplHelper.createOrderDetailUpdates());
		Order orderUpdate = OrderServiceImplHelper.createOrderUpdate();
		ProductResponseDto product = OrderServiceImplHelper.createProduct();
		
		Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
		Mockito.when(orderMapper.mapListOrderDetailDtoToListOrderDetail(Mockito.refEq(updates.getDetalleOrden()))).thenReturn(orderDetailListUpdates);
		Mockito.when(productsClient.getProductById(Mockito.anyLong())).thenReturn(product);
		Mockito.when(orderRepository.save(Mockito.refEq(orderUpdate, "total"))).thenReturn(orderUpdate);
		Mockito.when(orderDetailRepository.saveAll(Mockito.refEq(orderDetailListUpdates))).thenReturn(orderDetailListUpdates);
		Mockito.when(orderMapper.mapOrderAndDetailsToOrderDto(Mockito.any(),Mockito.any())).thenReturn(updates);
		
		Assertions.assertThat(orderService.update(1L, updates)).isEqualTo(updates);
		Mockito.verify(orderDetailRepository).deleteAllByOrderId(1L);
	}
	
	@Test(expected = NotFoundException.class)
	public void testUpdateIdNotFound() throws Exception{
		OrderDto updates = OrderServiceImplHelper.createOrderDtoUpdates();
		
		Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		orderService.update(99L,updates);
		
		/*try {
			orderService.update(99L,updates);
		} catch(NotFoundException ex) {
			Mockito.verify(orderRepository).findById(99L);
			throw ex;
		}*/
	}
	
	@Test(expected = Exception.class)
	public void testUpdateNotConnection() throws Exception{
		Order order = OrderServiceImplHelper.createOrder();
		List<OrderDetail> orderDetails = List.of(OrderServiceImplHelper.createOrderDetailUpdates());
		OrderDto updates = OrderServiceImplHelper.createOrderDtoUpdatesWithDetails();
		
		Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
		Mockito.when(orderMapper.mapListOrderDetailDtoToListOrderDetail(updates.getDetalleOrden())).thenReturn(orderDetails);
		Mockito.when(productsClient.getProductById(Mockito.anyLong())).thenThrow(FeignClientException.class);
		
		orderService.update(1L, updates);
		/*try {
			orderService.update(1L, updates);
		} catch (FeignClientException ex) {
			Mockito.verify(productsClient).getProductById(Mockito.anyLong());
			throw ex;
		}*/
	}
	
	/*@Test(expected = FeignException.class)
	public void testUpdateProductNotFound() throws FeignException, NotFoundException{
		Order order = OrderServiceImplHelper.createOrder();
		List<OrderDetail> orderDetails = List.of(OrderServiceImplHelper.createOrderDetailUpdates());
		OrderDto updates = OrderServiceImplHelper.createOrderDtoUpdatesWithDetails();
		
		Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
		Mockito.when(orderMapper.mapListOrderDetailDtoToListOrderDetail(updates.getDetalleOrden())).thenReturn(orderDetails);
		Mockito.when(productsClient.getProductById(Mockito.anyLong())).thenThrow(FeignException.class);
		
		try {
			orderService.update(1L, updates);
		} catch (FeignException ex) {
			Mockito.verify(productsClient).getProductById(Mockito.anyLong());
			throw ex;
		}
	}*/
	
	@Test
	public void testDeleteSuccessfully() throws NotFoundException{
		Order order = OrderServiceImplHelper.createOrder();
		Order orderEliminated = OrderServiceImplHelper.createOrderEliminated();
		
		Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
		Mockito.when(orderRepository.save(Mockito.refEq(orderEliminated))).thenReturn(orderEliminated);
		
		Assertions.assertThat(orderService.delete(1L)).isEqualTo("Orden eliminada con exito");
	}
	
	@Test(expected = NotFoundException.class)
	public void testDeleteNotFound() throws NotFoundException{		
		Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		
		try {
			orderService.delete(99L);
		} catch(NotFoundException ex) {
			Mockito.verify(orderRepository).findById(99L);
			throw ex;
		}
	}
}
