package org.family.hihishop.controller;

import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.family.hihishop.dto.CategoryDTO;
import org.family.hihishop.dto.ProductDTO;
import org.family.hihishop.dto.ProductImageDTO;
import org.family.hihishop.dto.response.ProductResponse;
import org.family.hihishop.dto.response.ProductResponsePage;
import org.family.hihishop.exception.DataNotFoundException;
import org.family.hihishop.model.Product;
import org.family.hihishop.model.ProductImage;
import org.family.hihishop.repository.ProductImageRepository;
import org.family.hihishop.repository.ProductRepository;
import org.family.hihishop.services.ProductService;
import org.family.hihishop.utils.ErrorMessage;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final ErrorMessage errorMessage;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @GetMapping("/search")
    public ResponseEntity<?> searchProductByName(@RequestParam(value = "name") String name){
        List<ProductResponse> products = productService.getProductByName(name);
        return ResponseEntity.ok(products);
    }


    @GetMapping
    public ResponseEntity<ProductResponsePage> doGetAll(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        int totalPage = productService.getAllProducts(PageRequest.of(page, limit)).getTotalPages();

        return ResponseEntity.ok(ProductResponsePage.builder()
                .products(productService.getAllProducts(PageRequest.of(page, limit)).getContent())
                .totalPage(totalPage)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> doGetA(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping(value = "")
    @Transactional
    public ResponseEntity<?> doCreate(@Valid @RequestBody ProductDTO productDTO,
                                      BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(errorMessage.getErrorMessages(result));
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Not successfully Product created !!! ");
        }
    }

    @PostMapping(value = "upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@ModelAttribute("files") List<MultipartFile> files, @PathVariable Long id) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Not found product"));

        List<ProductImage> images = new ArrayList<>();
        files = files == null ? new ArrayList<>() : files;
        boolean isUpdateThumbnailImage = false;
        for (MultipartFile file : files) {
            // Kiem tra kich thuoc va dinh dang file
            if (file.getSize() == 0) continue;
            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File is too large, please try again later with a larger file size 10MB");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("File must be an image");
            }
            String fileName = storeFile(file);
            if (!isUpdateThumbnailImage) {
                product.setThumbnail(fileName);
                productRepository.save(product);
                isUpdateThumbnailImage = true;
            }
            // Luu file anh vao DB
            ProductImage productImage = productService.createProductImage(
                    product.getId(),
                    ProductImageDTO
                            .builder()
                            .imageUrl(fileName)
                            .productId(product.getId())
                            .build()
            );
            images.add(productImage);
        }

        return ResponseEntity.ok(images);
    }

    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // them UUID de khong trung anh
        String uniqueFileName = UUID.randomUUID().toString() + "_" + filename;
        Path uploadedPath = Paths.get("uploads");
        if (!Files.exists(uploadedPath)) {
            Files.createDirectories(uploadedPath);
        }
        // Duong dan den file
        Path destination = Paths.get(uploadedPath.toString(), uniqueFileName);
        // Sao chep file vao thu muc
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> doUpdate(@PathVariable Long id) {
        return ResponseEntity.ok("Successfully Product updated !!! " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> doDelete(@PathVariable Long id) {
        return ResponseEntity.ok("Successfully Product deleted !!! " + id);
    }

    @PostMapping("/fake-data")
    private ResponseEntity<String> generateFakerData() {
        Faker faker = new Faker();
        for (int i = 0; i < 1_000_000; i++) {
            String name = faker.commerce().productName();
            if (productService.existsProductByName(name)) continue;
            ProductDTO productDTO = ProductDTO.builder()
                    .name(name)
                    .price((float) faker.number().numberBetween(0, 90_000_000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(1, 3))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Successfully generated fake data");
    }


    @GetMapping("img/{name}")
    public ResponseEntity<?> getImgByUrl(@PathVariable String name) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/" + name);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Not found image");
        }
    }


}
