package stevanovic.dejana.productservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import stevanovic.dejana.productservice.dto.CreateProductRequest;
import stevanovic.dejana.productservice.dto.SearchProductsRequest;
import stevanovic.dejana.productservice.dto.UpdateProductRequest;
import stevanovic.dejana.productservice.model.Product;
import stevanovic.dejana.productservice.service.AuthenticationService;
import stevanovic.dejana.productservice.service.ProductService;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final AuthenticationService authenticationService;


    @PostMapping
    public ResponseEntity<?> create(HttpServletRequest request,
                                    @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                    @RequestBody CreateProductRequest createProductRequest) throws URISyntaxException {
        if (authenticationService.validateAdminToken(jwtToken)) {
            Product product = productService.createProduct(createProductRequest);

            String uri = request.getRequestURI() + "/" + product.getId();
            return ResponseEntity
                    .created(new URI(uri))
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                    @PathVariable Long id, @RequestBody UpdateProductRequest updateProductRequest) {
        if (authenticationService.validateAdminToken(jwtToken)) {
            productService.updateProduct(id, updateProductRequest);

            return ResponseEntity
                    .noContent()
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                    @PathVariable Long id) {
        if (authenticationService.validateAdminToken(jwtToken)) {
            productService.deleteProduct(id);

            return ResponseEntity
                    .noContent()
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> get(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                 @PathVariable Long id) {
        if (authenticationService.validateToken(jwtToken)) {
            return ResponseEntity.ok(productService.getById(id));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        if (authenticationService.validateToken(jwtToken)) {
            return ResponseEntity.ok(productService.getAllProducts());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> searchProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
            SearchProductsRequest searchProductsRequest) {
        if (authenticationService.validateToken(jwtToken)) {
            return ResponseEntity.ok(productService.searchProducts(searchProductsRequest));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
