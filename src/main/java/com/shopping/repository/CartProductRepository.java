package com.shopping.repository;

import com.shopping.dto.CartDetailDto;
import com.shopping.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> { // CartProductRepository01

    // CartProductRepository01
    CartProduct findByCartIdAndProductId(Long cartId, Long productId);

    // CartProductRepository02
    // 다음과 같이 select 구문에 new 키워드를 사용하여 DTO의 생성자를 사용할 수 있습니다.
    // 장바구니에 담겨 있는 상품의 대표 이미지만 조회합니다.
    @Query(" select new com.shopping.dto.CartDetailDto(cp.id, i.name, i.price, cp.count, pi.imageUrl) " +
            " from CartProduct cp, ProductImage pi " +
            " join cp.product i " +
            " where cp.cart.id = :cartId " +
            " and pi.product.id = cp.product.id " +
            " and pi.repImageYesNo = 'Y' " +
            " order by cp.regDate desc"
    )
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);
}
