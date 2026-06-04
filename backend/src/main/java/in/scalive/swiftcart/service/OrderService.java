package in.scalive.swiftcart.service;

import in.scalive.swiftcart.dto.request.PlaceOrderRequestDTO;
import in.scalive.swiftcart.dto.request.UpdateOrderStatusRequestDTO;
import in.scalive.swiftcart.dto.response.OrderResponseDTO;
import in.scalive.swiftcart.dto.response.PageResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO placeOrder(PlaceOrderRequestDTO placeOrderRequestDTO);

    OrderResponseDTO getOrderById(Long orderId);

    OrderResponseDTO getOrderByOrderNumber(String orderNumber);

    List<OrderResponseDTO> getOrdersByUserId(Long userId);

    PageResponseDTO<OrderResponseDTO> getAllOrdersPaginated(int page, int size, String sortBy, String sortDir);

    List<OrderResponseDTO> getOrdersByStatus(String status);

    OrderResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusRequestDTO updateOrderStatusRequestDTO);

    OrderResponseDTO cancelOrder(Long orderId, String reason);

    PageResponseDTO<OrderResponseDTO> searchOrders(String keyword, int page, int size);
}
