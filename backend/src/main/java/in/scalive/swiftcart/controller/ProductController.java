package in.scalive.swiftcart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.swiftcart.dto.response.PageResponseDTO;
import in.scalive.swiftcart.dto.request.ProductRequestDTO;
import in.scalive.swiftcart.dto.response.ProductResponseDTO;
import in.scalive.swiftcart.dto.request.UpdateProductRequestDTO;
import in.scalive.swiftcart.dto.response.ApiResponseDTO;
import in.scalive.swiftcart.service.ProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
	private ProductService serv;

	@Autowired
	public ProductController(ProductService serv) {
		this.serv = serv;
	}
	@PostMapping
	public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> createProduct(@Valid @RequestBody ProductRequestDTO dto) {
		ProductResponseDTO respDTO=serv.createProduct(dto);
		return new ResponseEntity<>(ApiResponseDTO.success("Product created successfully", respDTO),HttpStatus.CREATED);
	}
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<ProductResponseDTO>>getProductById(@PathVariable Long id){
		ProductResponseDTO respDTO=serv.getProductById(id);
		return ResponseEntity.ok(ApiResponseDTO.success(respDTO));
	}
	@GetMapping("/sku/{"
			+ "sku}")
	public ResponseEntity<ApiResponseDTO<ProductResponseDTO>>getProductBySku(@PathVariable String sku){
		ProductResponseDTO respDTO=serv.getProductBySku(sku);
		return ResponseEntity.ok(ApiResponseDTO.success(respDTO));
	}
	@GetMapping("/all")
	public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>>getAllProducts(){
		List<ProductResponseDTO>pList=serv.getAllProducts();
		return ResponseEntity.ok(ApiResponseDTO.success("fetched "+pList.size()+" products",pList));
	}
	@GetMapping
	public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProductResponseDTO>>> getAllProductsPaginated(@RequestParam(defaultValue="0") int page, 
			@RequestParam(defaultValue="10") int size,
			@RequestParam(defaultValue="id") String sortBy,
			@RequestParam(defaultValue="asc") String sortDir){
		    PageResponseDTO<ProductResponseDTO>products= serv.getAllProductsPaginated(page, size, sortBy, sortDir);
		    return ResponseEntity.ok(ApiResponseDTO.success(products));
	}
	@GetMapping("/available")
	public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getAvailableProducts(){
		List<ProductResponseDTO>pList=serv.getAvailableProducts();
		return ResponseEntity.ok(ApiResponseDTO.success(pList));
	}
	@GetMapping("/category/{category}")
	public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getProductsByCategory(@PathVariable String category){
		List<ProductResponseDTO>pList=serv.getProductsByCategory(category);
		return ResponseEntity.ok(ApiResponseDTO.success(pList));
	}
	@GetMapping("/price-range")
	public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getProductsByPriceRange(@RequestParam double min,@RequestParam double max){
		List<ProductResponseDTO>pList=serv.getProductsByPriceRange(min, max);
				return ResponseEntity.ok(ApiResponseDTO.success(pList));
	}
	@GetMapping("/search")
	public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProductResponseDTO>>> searchProducts(@RequestParam String keyword,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size){
		PageResponseDTO<ProductResponseDTO>products= serv.searchProducts(keyword, page, size);
	    return ResponseEntity.ok(ApiResponseDTO.success(products));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDTO<ProductResponseDTO>>updateProduct(@PathVariable Long id,@Valid @RequestBody UpdateProductRequestDTO dto){
		ProductResponseDTO respDTO=serv.updateProduct(id, dto);
		return ResponseEntity.ok(ApiResponseDTO.success("Product updated successfully",respDTO));
	}
	@PatchMapping("/{id}/stock")
	public ResponseEntity<ApiResponseDTO<Void>> updateStock(@PathVariable Long id,@RequestParam Integer quantity){
		serv.updateStock(id, quantity);
		return ResponseEntity.ok(ApiResponseDTO.success("Stock updated successfully"));
	}
	@GetMapping("/low-stock")
	public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getLowStockProducts(@RequestParam(defaultValue = "10") Integer threshold){
		List<ProductResponseDTO>pList=serv.getLowStockProducts(threshold);
		return ResponseEntity.ok(ApiResponseDTO.success(pList));
	}
	@GetMapping("/check-sku/{sku}")
	public ResponseEntity<ApiResponseDTO<Boolean>>checkSkuExists(@PathVariable String sku){
		boolean exists=serv.existsBySku(sku);
		return ResponseEntity.ok(ApiResponseDTO.success(exists?"SKU already exists":"SKU is available",exists));
		
	}
}
