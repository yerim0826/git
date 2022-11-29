package com.shopping.controller;

import com.shopping.dto.CartDetailDto;
import com.shopping.dto.CartOrderDto;
import com.shopping.dto.CartProductDto;
import com.shopping.service.CartService;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService ; // CartController01

    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity addCart( // CartController01
            @RequestBody @Valid CartProductDto cartProductDto,
            BindingResult error,
            Principal principal){
            // cartProductDto는 커맨드 객체라고 하는 데, 각 변수의 값을 저장하고 있는 화면에서 넘어온 객체
            // principal 객체는 시스템을 사용하는 사용자 정보가 들어 있는 객체
            // SecurityConfig 파일의 usernameParameter("email")와 관련이 있습니다.

        System.out.println("cartProductDto.toString()");
        System.out.println(cartProductDto.toString());

        if(error.hasErrors()){ // dto 변수(filed)에 문제가 있는 경우
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = error.getFieldErrors();
            for(FieldError fe : fieldErrors){
                sb.append(fe.getDefaultMessage()) ;
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName() ;

        try{
            Long cartProductId = this.cartService.addCart(cartProductDto, email) ;
            return new ResponseEntity<Long>(cartProductId, HttpStatus.OK);

        }catch (Exception err){
            err.printStackTrace();
            return new ResponseEntity<String>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value = "/cart") // CartController02
    public String orderHist(Principal principal, Model model){
        // 로그인 한 사람의 이메일 정보를 이용하여 장바구니에 담은 상품 리스트를 구합니다.
        String email = principal.getName() ;
        List<CartDetailDto> cartDetailDtoList = this.cartService.getCartList(email) ;

        // 뷰로 전달할 데이터를 바인딩합니다.
        model.addAttribute("cartProducts", cartDetailDtoList);

        return "cart/cartList" ;
    }


    // @PatchMapping) 요청된 자원의 일부(여기서는 상품 수량)만 변경하고자 할 때 사용됩니다.
    @PatchMapping(value = "/cartProduct/{cartProductId}")
    public @ResponseBody ResponseEntity updateCartProduct( // CartController03
            @PathVariable("cartProductId") Long cartProductId,
            int count,
            Principal principal){

        String email = principal.getName();

        if(count < 0){
            return new ResponseEntity<String>("최소 1개 이상 담아 주세요.", HttpStatus.BAD_REQUEST);

        }else if(!this.cartService.validateCartProduct(cartProductId, email)){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        this.cartService.updateCartProduct(cartProductId, count);
        return new ResponseEntity<Long>(cartProductId, HttpStatus.OK);
    }

    // CartController04) 상품 삭제 요청을 구현합니다.
    @DeleteMapping(value = "/cartProduct/{cartProductId}")
    public @ResponseBody ResponseEntity deleteCartProduct(
            @PathVariable("cartProductId") Long cartProductId,
            Principal principal){

        // 권한 체크를 합니다.
        String email = principal.getName() ;
        if(!this.cartService.validateCartProduct(cartProductId, email)){
            return new ResponseEntity<String>("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        this.cartService.deleteCartProduct(cartProductId); // 장바구니에 들어 있는 상품을 삭제합니다.

        return new ResponseEntity<Long>(cartProductId, HttpStatus.OK);
    }

    // CartController05
    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity orderCartProduct(@RequestBody CartOrderDto cartOrderDto, Principal principal){

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        System.out.println("cartOrderDtoList.size()");
        System.out.println(cartOrderDtoList.size());

        // 주문 상품을 선택하지 않았는 지 확인합니다.
        if(cartOrderDtoList == null || cartOrderDtoList.size() == 0){
            System.out.println("여기로 오나요.");
            return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
        }

        for (CartOrderDto cartOrder : cartOrderDtoList) {
            // 주문 가능한지 권한을 체크합니다.
            if(!cartService.validateCartProduct(cartOrder.getCartProductId(), principal.getName())){
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }
        // 주문 로직 호출 결과 생성된 주문 번호를 반환 받습니다.
        Long orderId = cartService.orderCartProduct(cartOrderDtoList, principal.getName());

        // 생성된 주문 번호와 함께 HTTP 응답 상태 코드를 반환해 줍니다.
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
