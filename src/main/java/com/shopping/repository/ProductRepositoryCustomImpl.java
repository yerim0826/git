package com.shopping.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shopping.constant.ProductStatus;
import com.shopping.dto.MainProductDto;
import com.shopping.dto.ProductSearchDto;
import com.shopping.dto.QMainProductDto;
import com.shopping.entity.Product;
import com.shopping.entity.QProduct;
import com.shopping.entity.QProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom{
    // JPAQueryFactory) Query Builder
    private JPAQueryFactory queryFactory ;

    public ProductRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression dateRange(String searchDataType){
        // 사용자가 지정한 특정 기간 내의 데이터만 조회해주는 메소드입니다.
        LocalDateTime dateTime = LocalDateTime.now() ;

        if(StringUtils.equals("all", searchDataType) || searchDataType == null){
            return null ;
        }else if(StringUtils.equals("1d", searchDataType)){ // 1일
            dateTime = dateTime.minusDays(1) ;

        }else if(StringUtils.equals("1w", searchDataType)){ // 1주
            dateTime = dateTime.minusWeeks(1) ;

        }else if(StringUtils.equals("1m", searchDataType)){ // 1개월
            dateTime = dateTime.minusMonths(1) ;

        }else if(StringUtils.equals("6m", searchDataType)){ // 6개월
            dateTime = dateTime.minusMonths(6) ;
        }
        // after, before 등이 있습니다.
        return QProduct.product.regDate.after(dateTime) ;
    }

    private BooleanExpression sellStatusCondition(ProductStatus productStatus){
        // 3가지 상태) 전체, 판매중, 품절
        // 판매 상태가 '전체'이면 parameter의 값이 null으로 넘어 옵니다.
        return productStatus == null ? null : QProduct.product.productStatus.eq(productStatus);
    }

    private BooleanExpression searchByCondition(String searchBy,String searchQuery){
        // searchBy ) 조회할 컬럼 이름
        // searchQuery ) 조회할 검색 키워드

        if(StringUtils.equals("name", searchBy)){ // '상품 이름'으로 검색
            return QProduct.product.name.like("%" + searchQuery + "%")  ;

        }else if(StringUtils.equals("createdBy", searchBy)){ // '상품 등록자'로 검색
            return QProduct.product.createdBy.like("%" + searchQuery + "%")  ;
        }
        return null;
    }

    @Override // 검색 조건(dto)과 페이징 객체(pageable)를 이용하여 데이터를 조회합니다.
    public Page<Product> getAdminProductPage(ProductSearchDto dto, Pageable pageable) {
        QueryResults<Product> result = this.queryFactory
                .selectFrom(QProduct.product)
                .where(dateRange(dto.getSearchDataType()),
                        sellStatusCondition(dto.getProductStatus()),
                        searchByCondition(dto.getSearchBy(), dto.getSearchQuery()))
                .orderBy(QProduct.product.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults() ;

        List<Product> content = result.getResults() ;
        long total = result.getTotal() ;

        // Page 인터페이스의 구현체인 PageImpl 클래스에 대한 객체를 반환해 줍니다.
        return new PageImpl<>(content, pageable, total);
    }

    // ProductRepositoryCustomImpl02
    // 상품과 상품 이미지 엔터티를 사용하여 상품 목록을 조회합니다.
    // 검색 조건을 지정하여, 대표 이미지이고, 최신 이미지가 먼저 나오도록 조회합니다.
    @Override
    public Page<MainProductDto> getMainProductPage(ProductSearchDto dto, Pageable pageable) {
        QProduct product = QProduct.product ;
        QProductImage productImage = QProductImage.productImage ;

        QueryResults<MainProductDto> result = this.queryFactory
                .select(
                        new QMainProductDto(
                                product.id,
                                product.name,
                                product.description,
                                productImage.imageUrl,
                                product.price
                        )
                )
                .from(productImage)
                .join(productImage.product, product)
                .where(productImage.repImageYesNo.eq("Y"))
                .where(likeCondition(dto.getSearchQuery()))
                .orderBy(product.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults() ;

        List<MainProductDto> content = result.getResults() ;
        long total = result.getTotal() ;

        return new PageImpl<>(content, pageable, total);
    }

    // ProductRepositoryCustomImpl02
    private BooleanExpression likeCondition(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QProduct.product.name.like("%" + searchQuery + "%");
    }
}
