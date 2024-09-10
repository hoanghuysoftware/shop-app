package org.family.hihishop.repository;

import org.family.hihishop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    List<Product> getProductsByNameContains(String name);
    Page<Product> findAll(Pageable pageable); // phan trang trong spring
}
