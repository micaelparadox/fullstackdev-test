package com.micaeltest.QIMA.fullstackdev.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
	private Integer id;
	private String name;
	private Integer parentId;
}