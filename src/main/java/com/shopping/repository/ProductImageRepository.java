package com.shopping.repository;

import com.shopping.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 상품에 대한 이미지 정보를 관리하기 위한 Repository입니다.
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> { // ProductImageRepository01

    // ProductImageRepository02
    // 상품 이미지 목록을 조회하되, id 오름차순으로 정렬시켜 조회합니다.
    List<ProductImage> findByProductIdOrderByIdAsc(Long productId) ;

    // ProductImageRepository03
    ProductImage findByProductIdAndRepImageYesNo(Long id, String repImageYesNo) ;
}
