package stevanovic.dejana.productservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import stevanovic.dejana.productservice.dto.CreateProductRequest;
import stevanovic.dejana.productservice.dto.UpdateProductRequest;
import stevanovic.dejana.productservice.model.Product;
import stevanovic.dejana.productservice.repository.ProductRepository;
import stevanovic.dejana.productservice.service.ProductService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    public void shouldCreateProduct() {
        CreateProductRequest createProductRequest = CreateProductRequest.builder()
                .name("test")
                .category("test")
                .description("test")
                .build();

        productService.createProduct(createProductRequest);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenUpdatingNonExistingProduct() {
        Long id = 1234L;
        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
                .id(id)
                .name("test")
                .category("test")
                .description("test")
                .build();

        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(id, updateProductRequest));
    }

    @Test
    public void shouldUpdateProduct() {
        Long id = 1234L;

        Product product = Product.builder()
                .id(id)
                .name("test")
                .category("test")
                .description("test")
                .build();

        UpdateProductRequest updateProductRequest = UpdateProductRequest.builder()
                .id(id)
                .name("test")
                .category("test")
                .description("test")
                .build();

        when(productRepository.findById(id))
                .thenReturn(Optional.of(Product.builder().build()));
        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        productService.updateProduct(id, updateProductRequest);

        verify(productRepository, times(1)).save(any(Product.class));
    }

}
