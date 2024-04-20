package stevanovic.dejana.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import stevanovic.dejana.productservice.model.Category;
import stevanovic.dejana.productservice.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p " +
            "WHERE (:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:description IS NULL OR p.description LIKE %:description%) " +
            "AND (:category IS NULL OR p.category = :category)")
    List<Product> searchByNameDescriptionAndCategory(
            @Param("name") String name,
            @Param("description") String description,
            @Param("category") Category category);
}
