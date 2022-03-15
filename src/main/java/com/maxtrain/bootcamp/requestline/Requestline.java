package com.maxtrain.bootcamp.requestline;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.maxtrain.bootcamp.product.Product;
import com.maxtrain.bootcamp.request.Request;

@Entity
public class Requestline {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(nullable=false)
	private int quantity;
	
	@JsonBackReference
	@ManyToOne(optional=false)
	@JoinColumn(name="requestId", columnDefinition="int")
	private Request request;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="productId", columnDefinition="int")
	private Product product;
	
	public Requestline() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
