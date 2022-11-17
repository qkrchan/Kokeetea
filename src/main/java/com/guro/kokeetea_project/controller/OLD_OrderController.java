package com.guro.kokeetea_project.controller;

import com.guro.kokeetea_project.dto.OLD_OrderDto;
import com.guro.kokeetea_project.dto.OLD_OrderHistDto;
import com.guro.kokeetea_project.service.OLD_OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OLD_OrderController {

    private final OLD_OrderService OLDOrderService;

    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity<String> order (@RequestBody @Valid OLD_OrderDto OLDOrderDto, BindingResult bindingResult, Principal principal){

        if (bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long orderId;

        try {
            orderId = OLDOrderService.order(OLDOrderDto, email);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(orderId.toString(), HttpStatus.OK);
    }

    @GetMapping(value = {"/orders","/orders/{page}"})
    public String orderHist(@PathVariable("page")Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,4);

        Page<OLD_OrderHistDto> orderHistDtoList = OLDOrderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page",pageable.getPageNumber());
        model.addAttribute("maxPage",5);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity<String> cancelOrder(@PathVariable("orderId") Long orderId, Principal principal){

        if (!OLDOrderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<>("주문 취소 권한이 없습니다.",HttpStatus.FORBIDDEN);
        }

        OLDOrderService.cancelOrder(orderId);
        return new ResponseEntity<>(orderId.toString(), HttpStatus.OK);
    }
}
