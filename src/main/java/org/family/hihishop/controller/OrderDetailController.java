package org.family.hihishop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.family.hihishop.dto.OrderDetailDTO;
import org.family.hihishop.services.OrderDetailService;
import org.family.hihishop.utils.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/order-details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final ErrorMessage errorMessage;
    private final OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<?> doCreate(@Valid @RequestBody OrderDetailDTO orderDetailDTO, BindingResult result){
        try{
            // check value input
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(errorMessage.getErrorMessages(result));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(orderDetailService.createOrderDetail(orderDetailDTO));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> doGetById(@PathVariable Long id){
        // logic get order detail by id
        return ResponseEntity.ok(orderDetailService.getOrderDetailByID(id));
    }

    @GetMapping("/order/{orderId}") // get list order detail or order by order_id
    public ResponseEntity<?> doGetByOrderId(@PathVariable Long orderId){
        // logic get order detail by order id
        return ResponseEntity.ok(orderDetailService.geOrderDetailByOrderId(orderId));
    }

    @PutMapping("/{id}") // update quantity or price of order detail
    public ResponseEntity<?> doUpdate(@PathVariable Long id,
                                      @Valid @RequestBody OrderDetailDTO orderDetailDTO,
                                      BindingResult result){
        try{
            // check value input
            if(result.hasErrors()){
                return ResponseEntity.badRequest().body(errorMessage.getErrorMessages(result));
            }

            return ResponseEntity.status(HttpStatus.OK).body(orderDetailService.updateOrderDetail(id, orderDetailDTO));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> doDelete(@PathVariable Long id){
        // logic delete order detail by id
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.noContent().build();
    }
}
