package org.family.hihishop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.family.hihishop.dto.OrderDTO;
import org.family.hihishop.dto.response.OrderResponse;
import org.family.hihishop.services.OrderService;
import org.family.hihishop.utils.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final ErrorMessage errorMessage;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> doCrete(@Valid @RequestBody OrderDTO orderDTO, BindingResult result) {
        try {
            // check errors of input values
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(errorMessage.getErrorMessages(result));
            }
            return ResponseEntity.ok(orderService.createOrder(orderDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> doGetById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<?> doGetByUserId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.getAllOrdersByUserId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> doUpdateById(@PathVariable Long id,
                                          @Valid @RequestBody OrderDTO orderDTO) {
        try {
            return ResponseEntity.ok(orderService.updateOrder(id, orderDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> doDeleteById(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
