package com.shopping.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "carts")
@Setter @Getter @ToString
public class Cart extends BaseEntity{ // Cart01,  Cart02
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ; // Cart01

    // Cart는 Member의 id와 일대일 연관 관계를 맺도록 하겠습니다.
    // @JoinColumn에 매핑이 되는 외래키를 지정해 줍니다.
    @OneToOne(fetch = FetchType.LAZY) // Cart02
    @JoinColumn(name = "id") // Cart01
    private Member member ; // Cart가 Member를 참조하고 있습니다.

    // Cart03) 회원 Entity를 매개 변수로 받아서 장바구니 Entity를 반환해주는 메소드
    public static Cart createCart(Member member){
        Cart cart = new Cart() ;
        cart.setMember(member); // 장바구니는 특정 회원의 것입니다.
        return cart ;
    }
}
