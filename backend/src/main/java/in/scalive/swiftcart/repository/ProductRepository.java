package in.scalive.swiftcart.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.scalive.swiftcart.entity.Product;
import in.scalive.swiftcart.entity.User;


public interface ProductRepository extends JpaRepository<Product, Long>{
	Optional<Product> findBySku(String sku);
	boolean existsBySku(String sku);
	List<Product>findByIsAvailableTrue();
	List<Product>findByCategoryIgnoreCase(String category);
	List<Product>findByPriceBetween(double minPrice,double maxPrice);
	
	@Query("SELECT p FROM Product p WHERE (LOWER(p.name) like LOWER(CONCAT('%',:keyword,'%'))"+
            "OR LOWER(p.description) like LOWER(CONCAT('%',:keyword,'%'))) AND p.isAvailable=true")
    public Page<Product> searchProducts(@Param("keyword")String keyword,Pageable pageable);
    
	@Query("Update Product p SET p.stockQuantity=p.stockQuantity+:quantity where p.id=:productId ")
	@Modifying
	int increaseStock(@Param("productId") Long productId,@Param("quantity") Integer quantity);
	
	@Query("Update Product p SET p.stockQuantity=p.stockQuantity-:quantity where p.id=:productId And p.stockQuantity>=:quantity")
	@Modifying
	int decreaseStock(@Param("productId") Long productId,@Param("quantity") Integer quantity);
	
	@Query("Select p from Product p where p.stockQuantity<=:threshold and p.isAvailable=true")
	List<Product>findLowStockProducts(@Param("threshold")Integer threshold);
}
