package com.shopping.dto;

import com.shopping.constant.ProductStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter // 상품 검색시 조건을 명시해주는 클래스입니다.
public class ProductSearchDto { // ProductSearchDto01
    // all, 1d(하루), 1w(일주일), 1m(한달), 6m(6개월)
    private String searchDataType ; // 검색 조회 기간
    
    private ProductStatus productStatus ; // 상품의 판매 상태
    
    private String searchBy ; // 검색 모드) name(상품명), createdBy(등록자)
    private String searchQuery ; // 검색 키워드 (예시 : 사과, 김철수)
}
