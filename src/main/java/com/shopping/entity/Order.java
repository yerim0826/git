package com.shopping.entity;

import com.shopping.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter @Getter @ToString
public class Order extends BaseEntity{ // Order01, Order06
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @ManyToOne
    @JoinColumn(name = "id")
    private Member member ;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus ;

    private LocalDateTime orderDate ; // 주문 일자

    // Order06) BaseEntity 상속을 받으므로 다음 코드 삭제됨
//    private LocalDateTime regDate ; // 작성 일자
//    private LocalDateTime updateDate ; // 수정 일자

    // Order02) 양방향 매핑을 위하여 추가됨
    // 1개의 'Order'에는 여러 개의 'OrderProduct'가 담길 수 있습니다.
    // "order"는 多 쪽에 있는 해당 변수의 이름
    // Order03) for cascade 옵션
    // Order04) for orphanRemoval = true 옵션
    // Order05) for FetchType.LAZY 옵션
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) 
    private List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();

    // Order07)
    public void addOrderProduct(OrderProduct orderProduct){
        orderProducts.add(orderProduct) ;
        orderProduct.setOrder(this);
    }

    // Order07)
    public static Order createOrder(Member member, List<OrderProduct> orderProductList){
        Order order = new Order() ;
        order.setMember(member); // 주문자가 누구인지 지정

        // 주문에 담길 모든 상품 정보 등록
        for(OrderProduct orderProduct : orderProductList){
            order.addOrderProduct(orderProduct);
        }

        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order ;
    }

    // Order07)
    public int getTotalPrice(){
        int totalPrice = 0 ;
        for(OrderProduct orderProduct:orderProducts){
            totalPrice += orderProduct.getTotalPrice();
        }
        return totalPrice ;
    }

    // Order08) 주문 상태를 '취소'로 변경하고, 주문 취소된 수량을 재고에 더해 줍니다.
    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL ;

        // 이번 주문시 주문했던 모든 상품들에 대한 재고를 각각 더해 줘야 합니다.
        for(OrderProduct bean : this.orderProducts){
            bean.cancel();
        }
    }
}
