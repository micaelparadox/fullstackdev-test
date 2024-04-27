package com.micaeltest.QIMA.fullstackdev.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private Category category;

	private double price;
	private double weight;

	@Size(max = 255)
	private String description;

	private String imageName;

	public String getCategoryPath() {
		return category != null ? category.getCategoryPath() : null;
	}
}