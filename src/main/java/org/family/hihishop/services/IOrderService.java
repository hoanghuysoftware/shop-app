package org.family.hihishop.services;

import org.family.hihishop.dto.OrderDTO;
import org.family.hihishop.dto.response.OrderResponse;
import org.family.hihishop.model.Order;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO);
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getAllOrdersByUserId(Long userId);
    OrderResponse updateOrder(Long id, OrderDTO orderDTO);
    void deleteOrder(Long id);
}
