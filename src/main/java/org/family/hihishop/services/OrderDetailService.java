package org.family.hihishop.services;

import lombok.RequiredArgsConstructor;
import org.family.hihishop.dto.OrderDetailDTO;
import org.family.hihishop.exception.DataNotFoundException;
import org.family.hihishop.model.Order;
import org.family.hihishop.model.OrderDetail;
import org.family.hihishop.model.Product;
import org.family.hihishop.repository.OrderDetailRepository;
import org.family.hihishop.repository.OrderRepository;
import org.family.hihishop.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    public OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Not found order by id "+ orderDetailDTO.getOrderId()));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Not found product by id "+ orderDetailDTO.getProductId()));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .price(orderDetailDTO.getPrice())
                .totalPrice(orderDetailDTO.getTotalPrice())
                .color(orderDetailDTO.getColor())
                .build();
        orderDetailRepository.save(orderDetail);
        return modelMapper.map(orderDetail, OrderDetailDTO.class);
    }

    @Override
    public OrderDetailDTO getOrderDetailByID(Long id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Not found order detail for id " + id));
        return modelMapper.map(orderDetail, OrderDetailDTO.class);
    }

    @Override
    public OrderDetailDTO updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Not found order by id "+ orderDetailDTO.getOrderId()));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Not found product by id "+ orderDetailDTO.getProductId()));
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Not found order detail for id " + id));

        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setColor(orderDetailDTO.getColor());
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setTotalPrice(orderDetailDTO.getTotalPrice());
        orderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        return modelMapper.map(orderDetailRepository.save(orderDetail), OrderDetailDTO.class);
    }

    @Override
    public void deleteOrderDetail(Long id) { // Xoa cung
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Not found order detail for id " + id));
        orderDetailRepository.delete(orderDetail);
    }

    @Override
    public List<OrderDetailDTO> geOrderDetailByOrderId(Long orderId) {
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrder_Id(orderId);
        return orderDetailList.stream()
                .map(orderDetail-> modelMapper.map(orderDetail, OrderDetailDTO.class))
                .toList();
    }
}
