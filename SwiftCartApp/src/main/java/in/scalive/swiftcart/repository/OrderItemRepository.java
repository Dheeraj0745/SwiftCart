package in.scalive.swiftcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.scalive.swiftcart.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
