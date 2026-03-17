package com.prostory.service.serviceImpl;

import com.prostory.dto.request.ProductRequest;
import com.prostory.dto.response.ProductResponse;
import com.prostory.entity.Category;
import com.prostory.entity.Product;
import com.prostory.exception.exceptions.NotFoundException;
import com.prostory.repository.CategoryRepository;
import com.prostory.repository.ProductRepository;
import com.prostory.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Category with id %s not found", request.categoryId())));

        Product product = Product.builder()
                .article(UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .quantity(request.quantity())
                .image(request.image())
                .inStock(request.quantity() > 0)
                .createdAt(LocalDate.now())
                .category(category)
                .build();

        productRepository.save(product);
        log.info("Product {} successfully saved!", product.getName());

        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Product with id %s not found", id)));
        return mapToResponse(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Product with id %s not found", id)));
        productRepository.delete(product);
        log.info("Product {} successfully deleted!", product.getName());
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Product with id %s not found", id)));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Category with id %s not found", request.categoryId())));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        product.setImage(request.image());
        product.setInStock(request.quantity() > 0);
        product.setCategory(category);

        productRepository.save(product);
        log.info("Product {} successfully updated!", product.getName());

        return mapToResponse(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getArticle(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getDiscountPercent(),
                product.getRating(),
                product.getImage(),
                product.isInStock(),
                product.getCategory().getName()
        );
    }
}
