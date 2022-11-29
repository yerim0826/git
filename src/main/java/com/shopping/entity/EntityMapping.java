package com.shopping.entity;

import com.shopping.constant.ProductStatus;
import com.shopping.constant.Role;
import com.shopping.repository.MemberRepository;
import com.shopping.repository.OrderProductRepository;
import com.shopping.repository.OrderRepository;
import com.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

public class EntityMapping {
    @Autowired
    ProductRepository productRepository ;

    @Autowired
    OrderRepository orderRepository ;

    @Autowired
    OrderProductRepository orderProductRepository ;

    @PersistenceContext
    EntityManager em ; // 엔터티 관리자(in JPA)

    protected Product createProduct(int i) {
        Product product = new Product() ;

        String[] name = {"사과", "배", "블루베리"};
        String[] taste = {"맛있어요.", "달아요.", "달콤해요."};

        product.setName(name[i]);
        product.setPrice(10000*(i+1));
        product.setDescription(taste[i]);
        product.setProductStatus(ProductStatus.SELL);
        product.setStock(10*(i+1));
        product.setRegDate(LocalDateTime.now());
        product.setUpdateDate(LocalDateTime.now());

        return product ;
    }

    @Autowired
    MemberRepository memberRepository ;

    protected Order createOrder() {
        Order order = new Order() ;
        for (int i = 0; i < 3; i++) {
            Product product = this.createProduct(i) ;
            this.productRepository.save(product) ;

            OrderProduct orderProduct = new OrderProduct() ;
            orderProduct.setProduct(product);
            orderProduct.setCount(10);
            orderProduct.setOrderPrice(1000*(i+1));
            orderProduct.setOrder(order);

            order.getOrderProducts().add(orderProduct) ;
        }
        Member member = this.createMember() ;
        this.memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order) ;

        return order ;
    }

    private Member createMember() {
        Member member = new Member() ;
        member.setRole(Role.USER);
        member.setEmail("hello2@naver.com");
        member.setAddress("금천구 가산동");
        member.setName("김철식");
        return member ;
    }
}
