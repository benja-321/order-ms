package com.example.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "TB_ORDER_DETAIL")
public class OrderDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(
			name = "ID_TB_ORDER",
			nullable = false,
			foreignKey = @ForeignKey(name = "FK_ORDER_DETAIL_ORDER_ID"))
	private Order order;
	
	@Column(name = "ID_TB_PRODUCT")
	private Long idProduct;
	
	private Integer quantity;
	
}
