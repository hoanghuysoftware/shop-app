package org.family.hihishop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Name must be not blank !")
    @Size(min = 3, max = 200, message = "Name must be at least 3 characters and at most 200 characters !")
    private String name;
    @Min(value = 0, message = "Price of product must be gather value 0 !")
    private Float price;
    private String description;
    private String thumbnail;
    @JsonProperty("category_id")
    private Long categoryId;
}
