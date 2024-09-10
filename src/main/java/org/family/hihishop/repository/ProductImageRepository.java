package org.family.hihishop.repository;

import org.family.hihishop.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage , Long> {
    List<ProductImage> getProductImageByProductId(long productId);
    ProductImage getProductImageByImageUrl(String imageUrl);
}
