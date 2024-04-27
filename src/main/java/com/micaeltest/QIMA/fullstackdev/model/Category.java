package com.micaeltest.QIMA.fullstackdev.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Category parentCategory;

	@OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
	private List<Category> subCategories = new ArrayList<>();

	public String getCategoryPath() {
		if (parentCategory != null) {
			return parentCategory.getCategoryPath() + " > " + name;
		}
		return name;
	}
}