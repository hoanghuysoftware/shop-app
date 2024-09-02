package org.family.hihishop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.family.hihishop.dto.CategoryDTO;
import org.family.hihishop.dto.ProductDTO;
import org.family.hihishop.utils.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<String> doGetAll(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok("Successfully Product !!!" + " page: " + page + " limit: " + limit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> doGetA(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok("Successfully Product !!!" + "id: " + id);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> doCreate(@Valid @ModelAttribute ProductDTO productDTO,
                                      BindingResult result
//                                      @RequestPart("file") MultipartFile file
    ) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(errorMessage.getErrorMessages(result));
            }
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<MultipartFile>() : files;
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
                // Luu file vao DB
            }
            return ResponseEntity.ok("Successfully Product created !!!\n" + productDTO.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("Not successfully Product created !!! ");
        }
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
}
