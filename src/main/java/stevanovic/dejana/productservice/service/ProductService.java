package stevanovic.dejana.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import stevanovic.dejana.productservice.dto.CreateProductRequest;
import stevanovic.dejana.productservice.dto.SearchProductsRequest;
import stevanovic.dejana.productservice.dto.UpdateProductRequest;
import stevanovic.dejana.productservice.model.Product;
import stevanovic.dejana.productservice.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static stevanovic.dejana.productservice.util.ErrorCodes.DELETE_PRODUCT_NOT_EXISTING_IN_REQUEST;
import static stevanovic.dejana.productservice.util.ErrorCodes.UPDATE_PRODUCT_DIFFERENT_IDS_IN_REQUEST;
import static stevanovic.dejana.productservice.util.ErrorCodes.UPDATE_PRODUCT_NOT_EXISTING_IN_REQUEST;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(CreateProductRequest createProductRequest) {
        Product product = Product.builder()
                .name(createProductRequest.getName())
                .description(createProductRequest.getDescription())
                .category(createProductRequest.getCategory())
                .build();

        return productRepository.save(product);
    }

    public void updateProduct(Long id, UpdateProductRequest updateProductRequest) {
        if (!id.equals(updateProductRequest.getId())) {
            throw new IllegalArgumentException(UPDATE_PRODUCT_DIFFERENT_IDS_IN_REQUEST.name());
        }

        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isEmpty()) {
            throw new IllegalArgumentException(UPDATE_PRODUCT_NOT_EXISTING_IN_REQUEST.name());
        }

        Product updatedProduct = existingProductOptional.get().toBuilder()
                .name(updateProductRequest.getName())
                .description(updateProductRequest.getDescription())
                .category(updateProductRequest.getCategory())
                .build();

        productRepository.save(updatedProduct);
        log.info("Product {} is updated", updatedProduct.getId());
    }

    public void deleteProduct(Long id) {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isEmpty()) {
            throw new IllegalArgumentException(DELETE_PRODUCT_NOT_EXISTING_IN_REQUEST.name());
        }

        productRepository.deleteById(id);
        log.info("Product {} is deleted", id);
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().toList();
    }

    public Product getById(Long id) {
        return productRepository.findById(id).get();
    }

    public List<Product> searchProducts(SearchProductsRequest searchProductsRequest) {
        return productRepository.searchByNameDescriptionAndCategory(searchProductsRequest.getName(),
                searchProductsRequest.getDescription(), searchProductsRequest.getCategory());
    }
}
