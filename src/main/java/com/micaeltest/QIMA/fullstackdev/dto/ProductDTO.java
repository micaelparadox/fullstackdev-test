package com.micaeltest.QIMA.fullstackdev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private Integer categoryId;
    private String categoryName;
    private double price;
    private double weight;
    private String description;
    private String imageName;
}