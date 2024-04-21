package stevanovic.dejana.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import stevanovic.dejana.productservice.dto.CreateProductRequest;
import stevanovic.dejana.productservice.dto.SearchProductsRequest;
import stevanovic.dejana.productservice.dto.UpdateProductRequest;
import stevanovic.dejana.productservice.model.Product;
import stevanovic.dejana.productservice.service.AuthenticationService;
import stevanovic.dejana.productservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final AuthenticationService authenticationService;


//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> create(@RequestHeader("Authorization") String jwtToken,
                               @RequestBody CreateProductRequest createProductRequest) {
        return authenticationService.authenticateUser(jwtToken)
                .flatMap(authResponse -> {
                    if (authResponse.isSuccess()) {
                        productService.createProduct(createProductRequest);
                        return Mono.just("Product created successfully");
                    } else {
                        return Mono.just("Unauthorized");
                    }
                });
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable Long id, @RequestBody UpdateProductRequest updateProductRequest) {
        productService.updateProduct(id, updateProductRequest);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product get(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> searchProducts(SearchProductsRequest searchProductsRequest) {
        return productService.searchProducts(searchProductsRequest);
    }

}
