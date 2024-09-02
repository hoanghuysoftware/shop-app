package org.family.hihishop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

    @JsonProperty("order_id")
    @Min(value = 1, message = "The order id must be >=1")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "The product id must be >=1")
    private Long productId;

    @Min(value = 0, message = "Price of product must be gather 0")
    private Float price;

    @JsonProperty("number_of_products")
    @Min(value = 1, message = "Number of products must be >=1")
    private int numberOfProducts;

    @Min(value = 0, message = "Total Price of product must be gather 0")
    @JsonProperty("total_price")
    private Float totalPrice;

    private String color;
}
