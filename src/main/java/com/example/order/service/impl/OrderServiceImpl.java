package com.example.order.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.order.domain.Order;
import com.example.order.domain.OrderDetail;
import com.example.order.dto.OrderDto;
import com.example.order.mapper.OrderMapper;
import com.example.order.repository.OrderDetailRepository;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import com.example.order.service.feign.client.ProductsClient;
import com.example.order.service.feign.dto.ProductResponseDto;

import feign.FeignException;
import feign.FeignException.FeignClientException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService{

	private OrderRepository orderRepository;
	
	private OrderDetailRepository orderDetailRepository;

	private OrderMapper orderMapper;

	private ProductsClient productsClient;
	
	public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, OrderMapper orderMapper, ProductsClient productsClient) {
		this.orderRepository = orderRepository;
		this.orderDetailRepository = orderDetailRepository;
		this.orderMapper = orderMapper;
		this.productsClient = productsClient;
	}
	
	@Override
	@Transactional
	public OrderDto insert(OrderDto orderDto) throws Exception{
		Order newOrder = orderMapper.mapOrderDtoToOrder(orderDto);
		List<OrderDetail> orderDetailList = orderMapper.mapListOrderDetailDtoToListOrderDetail(orderDto.getDetalleOrden());
		newOrder.setTotal(totalAssignment(orderDetailList,newOrder));
		orderRepository.save(newOrder);
		orderDetailRepository.saveAll(orderDetailList);
		return orderMapper.mapOrderAndDetailsToOrderDto(newOrder,orderDetailList);
	}
	
	@Override
	public OrderDto getById(Long id) throws NotFoundException{
		Optional<Order> order = orderRepository.findById(id);
		if(order.isEmpty()) {
			log.error("Orden no encontrada");
			throw new NotFoundException(String.format("La orden %d no ha sido encontrada", id));
		}
		List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(id).get();
		return orderMapper.mapOrderAndDetailsToOrderDto(order.get(), orderDetails);
	}
	
	@Override
	@Transactional
	public OrderDto update(Long id, OrderDto updates) throws Exception{
		Optional<Order> order = orderRepository.findById(id);
		if(order.isEmpty()) {
			throw new NotFoundException(String.format("La orden %d no ha sido encontrada", id));
		}
		if(updates.getFechaEnvio() != null) {
			order.get().setShipmentDate(updates.getFechaEnvio());
		}
		List<OrderDetail> orderDetailList = orderMapper.mapListOrderDetailDtoToListOrderDetail(updates.getDetalleOrden());
		if(!orderDetailList.isEmpty()) {
			orderDetailRepository.deleteAllByOrderId(id);
			order.get().setTotal(totalAssignment(orderDetailList, order.get()));
			orderRepository.save(order.get());
			orderDetailRepository.saveAll(orderDetailList);
		} else {
			orderRepository.save(order.get());
		}
		return orderMapper.mapOrderAndDetailsToOrderDto(order.get(),orderDetailList);
	}
	
	private Double totalAssignment(List<OrderDetail> orderDetailList, Order order) throws Exception{
		Double total = 0.0;
		ProductResponseDto product = new ProductResponseDto();
		for(OrderDetail orderDetail : orderDetailList) {
			orderDetail.setOrder(order);
			try {
				product = productsClient.getProductById(orderDetail.getIdProduct());
			} catch (FeignException ex) {
				log.error("No hay conexion con el servicio de productos");
				throw new Exception("No hay conexion con el servicio de productos");
			}
			total = total + product.getPrecio() * orderDetail.getQuantity();
		}
		return total;
	}
	
	
	@Override
	public String delete(Long id) throws NotFoundException{
		Optional<Order> order = orderRepository.findById(id);
		if(order.isEmpty()) {
			throw new NotFoundException(String.format("La orden %d no ha sido encontrada", id));
		}
		order.get().setActive(Boolean.FALSE);
		orderRepository.save(order.get());
		return "Orden eliminada con exito";
	}
}
