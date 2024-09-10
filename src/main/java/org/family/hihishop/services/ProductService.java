package org.family.hihishop.services;

import lombok.RequiredArgsConstructor;
import org.family.hihishop.dto.ProductDTO;
import org.family.hihishop.dto.ProductImageDTO;
import org.family.hihishop.dto.response.ProductResponse;
import org.family.hihishop.exception.DataNotFoundException;
import org.family.hihishop.exception.InvalidParamsException;
import org.family.hihishop.model.Category;
import org.family.hihishop.model.Product;
import org.family.hihishop.model.ProductImage;
import org.family.hihishop.repository.CategoryRepository;
import org.family.hihishop.repository.ProductImageRepository;
import org.family.hihishop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) {
        // find category by productDTO id
        Category category = categoryRepository.findCategoryById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category of product is not found"));
        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(category)
                .build();
        return productRepository.save(product);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return ProductResponse.fromProduct(productRepository.findById(id).get());
    }

    @Override
    public List<ProductResponse> getProductByName(String name) {
        return productRepository.getProductsByNameContains(name)
                .stream()
                .map(ProductResponse::fromProduct)
                .toList();
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        // get product by page(so cua page) and limit(phan tu trong 1 page)
        return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product is not found with id:"));
        Category category = categoryRepository.findCategoryById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category of product is not found"));

        // can use object mapper or modal mapper
        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setThumbnail(productDTO.getThumbnail());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setCategory(category);
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product is not found with id:"));
        productRepository.delete(existingProduct);
    }

    @Override
    public boolean existsProductByName(String name) {
        return productRepository.existsByName(name);
    }

    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product is not found with id:"));

        ProductImage productImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        // Not allows insert gather 5 images
        int numbers = productImageRepository.getProductImageByProductId(productId).size();
        if (numbers >= 5) {
            throw new InvalidParamsException("Numbers of images must be less than 5");
        }
        return productImageRepository.save(productImage);
    }


}
