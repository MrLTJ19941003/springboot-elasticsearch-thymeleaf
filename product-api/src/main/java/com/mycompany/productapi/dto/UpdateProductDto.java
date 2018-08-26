package com.mycompany.productapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class UpdateProductDto {

    @ApiModelProperty(value = "product name", example = "Apple 15\" MacBook Pro")
    private String name;

    @ApiModelProperty(
            position = 1,
            value = "product description",
            example = "Apple 15\" MacBook Pro, Retina Display, 2.3GHz Intel Core i5 Dual Core, 8GB RAM, 128GB SSD, Space Gray, MPXQ2LL/A ")
    private String description;

    @ApiModelProperty(position = 2, value = "product price", example = "1599.90")
    private BigDecimal price;

    @ApiModelProperty(position = 3, value = "product categories", example = "[\"laptops\", \"apple\"]")
    private Set<String> categories;

}
