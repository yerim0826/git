package com.shopping.entity;

import com.shopping.constant.ProductStatus;
import com.shopping.dto.ProductFormDto;
import com.shopping.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "products") // name 속성의 값이 실제 테이블 이름이 됩니다.
@Getter @Setter @ToString
public class Product extends BaseEntity{ // Product01, Product02
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO) // 기본 키 생성 전략
    private Long id ; // 상품 코드(primary key)

    // 상품 이름은 최대 50글자까지이고, 필수 입력 사항입니다.
    @Column(nullable = false, length = 50)
    private String name ; // 상품 이름

    @Column(nullable = false, name = "price")
    private int price; // 단가

    @Column(nullable = false)
    private int stock ; // 재고 수량

    @Lob // CLOB(Character Large Object) BLOL(Binary Large Object)
    @Column(nullable = false)
    private String description; // 상품 설명

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus ; // 상품 판매 상태

    // Product02) BaseEntity 상속받으므로 다음 코드 삭제됨
//    private LocalDateTime regDate ; // 상품 등록 시간
//    private LocalDateTime updateDate ; // 상품 수정 시간

    // Product03) 상품(Product)에 대한 정보를 업데이트해주는 메소드
    public void updateProduct(ProductFormDto dto){
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.stock = dto.getStock();
        this.description = dto.getDescription();
        this.productStatus = dto.getProductStatus();
    }

    // Product04) 상품 주문시 재고 수량을 감소시키는 로직
    public void removeStock(int vstock){
        int restStock = this.stock - vstock ; // 주문 후 남은 재고 수량
        if(restStock < 0){ // 재고 부족시 예외 발생
            String message = "상품의 재고가 부족합니다.(현재 재고 수량 : " +  this.stock + ")" ;
            throw new OutOfStockException(message);
        }
        this.stock = restStock ;
    }

    // Product05) 주문 취소시 재고를 다시 증가시키기
    public void addStock(int vstock){
        this.stock += vstock ;
    }
}
