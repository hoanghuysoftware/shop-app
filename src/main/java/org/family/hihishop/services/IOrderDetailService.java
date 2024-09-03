package org.family.hihishop.services;

import org.family.hihishop.dto.OrderDetailDTO;
import org.family.hihishop.model.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO);
    OrderDetailDTO getOrderDetailByID(Long id);
    OrderDetailDTO updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO);
    void deleteOrderDetail(Long id);
    List<OrderDetailDTO> geOrderDetailByOrderId(Long orderId);
}
