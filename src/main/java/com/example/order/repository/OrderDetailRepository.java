package com.example.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.order.domain.OrderDetail;

public interface OrderDetailRepository extends CrudRepository<OrderDetail, Long>{

	Optional<List<OrderDetail>> findAllByOrderId(Long id);
	
	void deleteAllByOrderId(Long id);
}
