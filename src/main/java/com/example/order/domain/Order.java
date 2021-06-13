package com.example.order.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Data;

@Entity
@Data
@Table(name = "TB_ORDER")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Boolean active;
	
	@CreationTimestamp
	private LocalDate date;
	
	private LocalDate shipmentDate;
	
	private Double total;
}
