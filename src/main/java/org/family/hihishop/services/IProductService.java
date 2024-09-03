package org.family.hihishop.services;

import org.family.hihishop.dto.ProductDTO;
import org.family.hihishop.dto.response.ProductResponse;
import org.family.hihishop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);
    ProductResponse getProductById(Long id);
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    boolean existsProductByName(String name);

}
