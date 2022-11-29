package com.shopping.service;

import com.shopping.constant.ProductStatus;
import com.shopping.constant.Role;
import com.shopping.dto.CartProductDto;
import com.shopping.entity.CartProduct;
import com.shopping.entity.Member;
import com.shopping.entity.Product;
import com.shopping.repository.CartProductRepository;
import com.shopping.repository.MemberRepository;
import com.shopping.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.Random;

@SpringBootTest
//@Transactional // @Transactional 어노테이션이 명시되어 있으면, 테스트 후 모든 data를 rollback 합니다.
public class CartServiceTest {
    @Autowired
    private CartService cartService ;

    @Autowired
    private ProductRepository productRepository ;

    @Autowired
    private MemberRepository memberRepository ;

    @Autowired
    private CartProductRepository cartProductRepository ;

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Product product = this.createProduct();
        Long productId = product.getId() ;

        Member member = this.createMember();
        String email = member.getEmail() ;

        // 상품 상세 화면에서 다음 상품에 대하여 [장바구니 담기] 버튼을 클릭한다고 가정합니다.
        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setCount(5); ;
        cartProductDto.setProductId(productId);

        Long cartProductId = this.cartService.addCart(cartProductDto, email) ;
        System.out.println("cartProductId=" + cartProductId);

        // CartProduct(장바구니 상품) 정보를 조회해 봅니다.
        CartProduct cartProduct = this.cartProductRepository.findById(cartProductId)
                .orElseThrow(EntityNotFoundException::new);

        // CartProduct, Members, Cart의 기본 키 생성 전략) IDENTITY
        // 상품 아이디가 동일한가?
        Assertions.assertEquals(product.getId(), cartProduct.getProduct().getId());

        // 상품 수량이 동일한가?
        Assertions.assertEquals(cartProductDto.getCount(), cartProduct.getCount());

        // 데이터 베이스 확인 : 회원, 상품, 카트, 카트 상품
    }
    private Member createMember() { // 1개의 회원 Entity를 만들어 줍니다.
        Member member = new Member();

        String email = "test" + new Random().nextInt(1234567) + "@naver.com" ;
        member.setEmail(email);
        member.setAddress("영등포구 신길동");
        member.setRole(Role.USER);
        member.setPassword("1234");
        member.setName("김철수");
        member.setId(new Random().nextLong());
        return this.memberRepository.save(member) ;
    }

    private Product createProduct() { // 1개의 상품 Entity를 만들어 줍니다.
        Product product = new Product() ;
        product.setProductStatus(ProductStatus.SELL);
        product.setName("아동복 신상");
        product.setStock(1000);
        product.setDescription("옷이 넘 이쁘요.");
        product.setPrice(12345);
        product.setId(new Random().nextLong());
        return this.productRepository.save(product) ;
    }
}
