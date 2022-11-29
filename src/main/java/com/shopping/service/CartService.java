package com.shopping.service;

import com.shopping.dto.CartDetailDto;
import com.shopping.dto.CartOrderDto;
import com.shopping.dto.CartProductDto;
import com.shopping.dto.OrderDto;
import com.shopping.entity.Cart;
import com.shopping.entity.CartProduct;
import com.shopping.entity.Member;
import com.shopping.entity.Product;
import com.shopping.repository.CartProductRepository;
import com.shopping.repository.CartRepository;
import com.shopping.repository.MemberRepository;
import com.shopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService { // CartService01
    private final ProductRepository productRepository ;
    private final MemberRepository memberRepository ;
    private final CartRepository cartRepository ;
    private final CartProductRepository cartProductRepository ;

    // CartService01
    // 상품 id, 수량, 이메일 정보를 이용하여 카트 상품(CartProduct) Entity를 생성해 줍니다.
    // 신규 '카트 상품'이면, 내 카트에 추가하고, 아니면 기존 수량에 값을 누적시켜 줍니다.
    public Long addCart(CartProductDto cartProductDto, String email){
        Long productId = cartProductDto.getProductId() ;
        Product product = this.productRepository.findById(productId)
                .orElseThrow(EntityNotFoundException::new) ;

        Member member = this.memberRepository.findByEmail(email) ;

        Long memberId = member.getId() ;
        Cart cart = this.cartRepository.findByMemberId(memberId) ;

        if(cart==null){
            cart = Cart.createCart(member) ;
            cartRepository.save(cart) ;
        }

        Long cartId = cart.getId() ;
        CartProduct savedCartProduct = this.cartProductRepository.findByCartIdAndProductId(cartId, productId);

        int count = cartProductDto.getCount() ;

        if(savedCartProduct != null){
            System.out.println("해당 장바구니에 이미 상품이 들어 있는 경우");
            savedCartProduct.addCount(count);
            System.out.println("savedCartProduct.getId()=" + savedCartProduct.getId());
            this.cartProductRepository.save(savedCartProduct) ; // 누적 안되는 문제 해결
            return savedCartProduct.getId() ;

        }else{
            System.out.println("신규 상품을 장바구니에 담는 경우");
            CartProduct cartProduct = CartProduct.createCartProduct(cart, product, count) ;
            System.out.println("cartProduct.getId()=" + cartProduct.getId());
            this.cartProductRepository.save(cartProduct) ;
            return cartProduct.getId() ;
        }
    }

    // CartService02
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){
        // 현재 상품 목록이 0개 들어 있습니다.
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = this.memberRepository.findByEmail(email) ; // 로그인 한 사람 정보
        Long memberId = member.getId() ;

        // 로그인 한 사람의 카트 찾기
        Cart cart = this.cartRepository.findByMemberId(memberId) ;

        if(cart!=null){ // 이전에 상품을 담은 적이 없는 경우
            Long cartId = cart.getId() ;
            cartDetailDtoList = this.cartProductRepository.findCartDetailDtoList(cartId);
        }
        return cartDetailDtoList ;
    }

    // CartService03) 로그인 한 사람과 장바구니 상품을 저장한 회원이 동일한지 체크
    public boolean validateCartProduct(Long cartProductId, String email){
        Member member = this.memberRepository.findByEmail(email); // 로그인 한 회원

        CartProduct cartProduct = this.cartProductRepository.findById(cartProductId)
                .orElseThrow(EntityNotFoundException::new) ;

        Member savedMember = cartProduct.getCart().getMember() ; // 장바구니에 상품을 저정한 회원

        if(StringUtils.equals(member.getEmail(), savedMember.getEmail())){
            return true ;
        }else{
            return false ;
        }
    }

    // CartService03) 장바구니의 수량을 업데이트하는 메소드
    public void updateCartProduct(Long cartProductId, int count){
        CartProduct cartProduct = this.cartProductRepository.findById(cartProductId)
                .orElseThrow(EntityNotFoundException::new) ;
        cartProduct.updateCount(count);
        this.cartProductRepository.save(cartProduct) ;
    }

    // CartService04) 카트의 상품 번호를 이용하여 해당 상품을 삭제합니다.
    public void deleteCartProduct(Long cartProductId){
        CartProduct cartProduct = this.cartProductRepository.findById(cartProductId)
                .orElseThrow(EntityNotFoundException::new) ;
        cartProductRepository.delete(cartProduct);
    }

    // CartService05
    private final OrderService orderService;

    // CartService05
    public Long orderCartProduct(List<CartOrderDto> cartOrderDtoList, String email){
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartProduct cartProduct = cartProductRepository
                    .findById(cartOrderDto.getCartProductId())
                    .orElseThrow(EntityNotFoundException::new);

            // OrderDto : 상품 상세 페이지에서 넘겨 주는 상품 아이디와 주문 수량을 위한 클래스
            OrderDto orderDto = new OrderDto();
            orderDto.setProductId(cartProduct.getProduct().getId());
            orderDto.setCount(cartProduct.getCount());
            orderDtoList.add(orderDto);
        }

        // 장바구니에 담은 상품을 주문하도록 주문 로직을 호출합니다.
        Long orderId = orderService.orders(orderDtoList, email);

        // 주문된 상품들은 상바구니 목록에서 제거되어야 합니다.
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartProduct cartProduct = cartProductRepository
                    .findById(cartOrderDto.getCartProductId())
                    .orElseThrow(EntityNotFoundException::new);
            cartProductRepository.delete(cartProduct);
        }

        return orderId;
    }
}
