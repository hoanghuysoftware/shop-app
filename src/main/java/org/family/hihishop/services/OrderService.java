package org.family.hihishop.services;

import lombok.RequiredArgsConstructor;
import org.family.hihishop.dto.OrderDTO;
import org.family.hihishop.dto.response.OrderResponse;
import org.family.hihishop.exception.DataNotFoundException;
import org.family.hihishop.model.Order;
import org.family.hihishop.model.OrderStatus;
import org.family.hihishop.model.User;
import org.family.hihishop.repository.OrderRepository;
import org.family.hihishop.repository.ProductRepository;
import org.family.hihishop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) {
        // check existing user by user id
        User user = userRepository.getUserById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found at method createOrder"));
        //Convert order DTO to Order (use library ModelMapper)
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId)); // bo qua id
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(String.valueOf(OrderStatus.PENDING));
        // kiem tra shipping date phai > hom nay, neu khong truyen gia tri thi se ship ngay hom nay
        LocalDate shippedDate = orderDTO.getShippingDate()==null ?  LocalDate.now() : orderDTO.getShippingDate();
        if(shippedDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today !");
        }
        order.setActive(true);
        order.setShippingDate(shippedDate);

        // save order va truyen vao response
        orderRepository.save(order);
        modelMapper.typeMap(Order.class, OrderResponse.class);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return modelMapper.map(orderRepository.findById(id), OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getAllOrdersByUserId(Long userId) {
        return orderRepository.findOrderByUserId(userId)
                .stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .toList();
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) {
        User existUser = userRepository.getUserById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found at method createOrder"));
        Order existOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Not found Order by id " + id));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        modelMapper.map(orderDTO, existOrder);
        // kiem tra shipping date phai > hom nay, neu khong truyen gia tri thi se ship ngay hom nay
        LocalDate shippedDate = orderDTO.getShippingDate()==null ?  LocalDate.now() : orderDTO.getShippingDate();
        if(shippedDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Date must be at least today !");
        }
        existOrder.setShippingDate(shippedDate);
        existOrder.setUser(existUser);

        modelMapper.typeMap(Order.class, OrderResponse.class);
        return modelMapper.map(orderRepository.save(existOrder), OrderResponse.class);
    }

    @Override
    public void deleteOrder(Long id) {
        Order existOrder = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Not found Order by id " + id));
        existOrder.setActive(false);
        orderRepository.save(existOrder);
    }
}
