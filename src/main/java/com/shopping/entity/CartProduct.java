package com.shopping.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart_products")
@Setter @Getter @ToString
public class CartProduct extends BaseEntity{ // CartProduct01, CartProduct03
    @Id
    @Column(name = "cart_product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ; // CartProduct01

    private int count ; // 담길 상품의 갯수 // CartProduct01

    @ManyToOne(fetch = FetchType.LAZY) // 여러 개의 CartProduct는 하나의  Cart에 담을 수 있습니다. // CartProduct02
    @JoinColumn(name = "cart_id")
    private Cart cart ; // CartProduct01

    @ManyToOne(fetch = FetchType.LAZY) // 하나의 Product는 여러 개의 CartProduct에 담길 수 있습니다. // CartProduct02
    @JoinColumn(name = "product_id")
    private Product product ; // CartProduct01

    // CartProduct04
    // 카트와 상품 및 담을 상품의 개수를 사용하여 CartProduct 객체를 생성합니다.
    public static CartProduct createCartProduct(Cart cart, Product product, int count){
        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setCount(count);
        // cartProduct.setId(new Random().nextLong());
        return cartProduct ;
    }

    // CartProduct04
    // 장바구니 수량(count)을 증가시키는 메소드
    public void addCount(int count){
        this.count += count ;
    }

    // CartProduct05
    // 장바구니 수량(count)을 변경하는 메소드
    public void updateCount(int count){
        this.count = count ;
    }
}
