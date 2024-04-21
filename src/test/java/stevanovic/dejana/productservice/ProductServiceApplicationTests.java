package stevanovic.dejana.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import stevanovic.dejana.productservice.controller.ProductController;
import stevanovic.dejana.productservice.dto.CreateProductRequest;
import stevanovic.dejana.productservice.repository.ProductRepository;
import stevanovic.dejana.productservice.service.AuthenticationService;
import stevanovic.dejana.productservice.service.ProductService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductServiceApplicationTests {

    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @MockBean
    private ProductService productService;
    @MockBean
    private AuthenticationService authenticationService;

    static {
        postgreSQLContainer.start();
    }


    @Test
    @Disabled("TODO: need to be fixed,since mock is not working")
    void shouldCreateProduct() throws Exception {
        String mockJwtToken = "mock-token";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, mockJwtToken);

        when(authenticationService.validateToken(mockJwtToken)).thenReturn(true);

        CreateProductRequest productRequest = getCreateProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("http://product-service/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content(productRequestString))
                .andExpect(status().isCreated());

        assertEquals(1, productRepository.findAll().size());
    }

    private CreateProductRequest getCreateProductRequest() {
        return CreateProductRequest.builder()
                .name("Honor Magic Lite 6")
                .description("Android")
                .category("Mobile phone")
                .build();
    }

}

