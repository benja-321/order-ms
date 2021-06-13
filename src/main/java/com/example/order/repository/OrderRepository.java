package com.example.order.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.order.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{

}
