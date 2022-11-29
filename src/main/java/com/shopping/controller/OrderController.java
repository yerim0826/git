package com.shopping.controller;

import com.shopping.dto.OrderDto;
import com.shopping.dto.OrderHistDto;
import com.shopping.service.OrderService;
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
public class OrderController { // OrderController01
    private final OrderService orderService ; // OrderController01

    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity  order(@RequestBody @Valid OrderDto orderDto, BindingResult error, Principal principal){ // OrderController01

        if(error.hasErrors()){
            StringBuilder sb = new StringBuilder( );
            List<FieldError> fieldErrors = error.getFieldErrors() ;

            for(FieldError ferr : fieldErrors){
                sb.append(ferr.getDefaultMessage()) ;
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST) ;
        }

        // SecurityConfig.java 파일과 연관 있슴
        // .usernameParameter("email")
        String email = principal.getName() ;
        Long orderId ;

        try{
            // (상품번호+수량+누가)를 가지고 주문을 합니다.
            orderId = orderService.order(orderDto, email);

        }catch (Exception err){
            err.printStackTrace();
            return new ResponseEntity<String>(err.getMessage(), HttpStatus.BAD_REQUEST) ;
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK) ;
    }

    // OrderController02) 주문 내역을 조회하는 컨트롤러 메소드
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        int pageNumber = page.isPresent() ? page.get() : 0 ;
        int pageSize = 4 ; // 한 페이지에 몇개 보여 줄꺼니?

        String email = principal.getName() ;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderHistDto> orderHistDtoList =  this.orderService.getOrderList(email, pageable) ;

        model.addAttribute("orders", orderHistDtoList) ;
        model.addAttribute("page", pageable.getPageNumber()) ;
        model.addAttribute("maxPage", 5) ;

        return "order/orderHist" ;
    }

    // OrderController03)
    // 취소할 주문 번호는 조작이 될 수 있으므로, 주문 취소 권한부터 검사하고 진행합니다.
    @PostMapping(value = "/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(
            @PathVariable("orderId") Long orderId, Principal principal){

        String email = principal.getName() ;
        if(!this.orderService.validateOrder(orderId, email)){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN) ;
        }
        this.orderService.cancelOrder(orderId); // 주문 취소 로직 호출
        return new ResponseEntity<Long>(orderId, HttpStatus.OK) ;
    }
}
