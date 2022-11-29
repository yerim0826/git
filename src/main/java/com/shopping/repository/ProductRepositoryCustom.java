package com.shopping.repository;

import com.shopping.dto.MainProductDto;
import com.shopping.dto.ProductSearchDto;
import com.shopping.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom { // ProductRepositoryCustom01
    // ProductRepositoryCustom01
    // dto) 상품을 조회하는 어떠한 조건
    // pageable) 페이징 정보를 담고 있는 객체

    // ProductRepositoryCustom01
    // 관리자가 [상품 관리] 메뉴를 클릭할 때 사용합니다.
    Page<Product> getAdminProductPage(ProductSearchDto dto, Pageable pageable) ;

    // ProductRepositoryCustom02
    // 메인 페이지에서 상품 목록을 보여 주고자 할 때 사용합니다.
    Page<MainProductDto> getMainProductPage(ProductSearchDto dto, Pageable pageable) ;
}


