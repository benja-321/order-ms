package com.example.order.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.order.domain.Order;
import com.example.order.domain.OrderDetail;
import com.example.order.dto.OrderDetailDto;
import com.example.order.dto.OrderDto;

@Mapper(componentModel = "spring")
public interface OrderMapper {
	
	@Mapping(source = "codigoProducto", target = "idProduct")
	@Mapping(source = "cantidad", target = "quantity")
	OrderDetail mapOrderDetailDtoToOrderDetail(OrderDetailDto orderDetailDto);
	
	List<OrderDetail> mapListOrderDetailDtoToListOrderDetail(List<OrderDetailDto> detallesOrden);
	
	@Mapping(source = "fechaEnvio", target = "shipmentDate")
	@Mapping(expression = "java(Boolean.TRUE)", target = "active")
	Order mapOrderDtoToOrder(OrderDto orderDto);
	
	@Mapping(source = "idProduct", target = "codigoProducto")
	@Mapping(source = "quantity", target = "cantidad")
	OrderDetailDto mapOrderDetailToOrderDetailDto(OrderDetail orderDetail);
	
	@Mapping(source = "order.shipmentDate", target = "fechaEnvio")
	@Mapping(source = "orderDetails", target = "detalleOrden")
	OrderDto mapOrderAndDetailsToOrderDto(Order order, List<OrderDetail> orderDetails);
	
}
