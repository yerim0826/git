package com.shopping.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shopping.constant.ProductStatus;
import com.shopping.entity.Product;
import com.shopping.entity.QProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest // 이 클래스는 테스트용으로 사용할 겁니다.
public class ProductRepositoryTest {
    @Autowired // 클래스 외부에서 객체에 의미 있는 값을 부여하기 위하여 사용됩니다.
    ProductRepository productRepository ;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createProductTest(){
        Product product = new Product();
        product.setName("사과");
        product.setPrice(10000);
        product.setDescription("사과는 맛있어요");
        product.setProductStatus(ProductStatus.SELL);
        product.setStock(100);
        product.setRegDate(LocalDateTime.now());
        product.setUpdateDate(LocalDateTime.now());

        Product savedItem = productRepository.save(product) ;
        System.out.println(savedItem.toString());
    }

    @Test
    @DisplayName("상품 여러개 추가하기 테스트")
    public void createProductTestMany(){
        String[] fruit = {"사과", "배", "오렌지"};
        String[] description = {"달아요", "맛있어요", "맛없어요", "떫어요"};
        int[] stock = {100, 200, 300, 400} ;
        int[] price = {111, 222, 333, 444, 555} ;

        for (int i = 0; i < 10 ; i++) {
            Product product = new Product();
            // setting
            product.setName(fruit[i % fruit.length]);
            product.setPrice(price[i % price.length]);
            product.setDescription(description[i % description.length]);
            product.setProductStatus(ProductStatus.SELL);
            product.setStock(stock[i % stock.length]);
            product.setRegDate(LocalDateTime.now());
            product.setUpdateDate(LocalDateTime.now());
            Product savedBean = this.productRepository.save(product);
            System.out.println(savedBean.toString());
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findProductByNameTest(){
        String name = "오렌지" ;
        List<Product> itemList = this.productRepository.findProductByName(name);

        for(Product product : itemList){
            System.out.println(product.toString());
        }
    }

    @Test
    @DisplayName("가격 less than 테스트")
    public void findByPriceLessThanTest(){
        Integer price = 300 ;
        List<Product> itemList = this.productRepository.findByPriceLessThan(price);

        for(Product product : itemList){
            System.out.println(product.toString());
        }
    }

    @Test
    @DisplayName("가격 less than 또는 가격 내림차순 테스트")
    public void findByPriceLessThanOrderByPriceDescTest(){
        Integer price = 300 ;
        List<Product> itemList = this.productRepository.findByPriceLessThanOrderByPriceDesc(price);

        for(Product product : itemList){
            System.out.println(product.toString());
        }
    }

    @Test
    @DisplayName("@Query를 사용한 상품 조회 테스트 01")
    public void findByProductDetail01(){
        String find = "아" ; // 상품 설명에 '아'라는 글자들 찾기
        List<Product> itemList = this.productRepository.findByProductDetail01(find) ;
        for(Product product : itemList){
            System.out.println(product.toString());
        }
    }

    @Test
    @DisplayName("@Query를 사용한 상품 조회 테스트 02")
    public void findByProductDetail02(){
        String find = "아" ;
        List<Product> itemList = this.productRepository.findByProductDetail02(find) ;
        for(Product product : itemList){
            System.out.println(product.toString());
        }
    }

    @Test
    @DisplayName("데이터 생성(for QuerydslPredicateExecutor)")
    public void createProductListNew(){
        String[] fruit = {"사과", "배", "오렌지"};
        String[] description = {"달아요", "맛있어요", "맛없어요", "떫어요"};
        int[] stock = {100, 200, 300, 400, 500, 600} ;
        int[] price = {111, 222, 333, 444, 555} ;

        for (int i = 1; i <= 30 ; i++) {
            Product product = new Product();
            // setting
            product.setName(fruit[i % fruit.length]);
            product.setPrice(price[i % price.length]);
            product.setDescription(description[i % description.length]);

            if(i%2 ==0){
                product.setProductStatus(ProductStatus.SELL);
            }else{
                product.setProductStatus(ProductStatus.SOLD_OUT);
            }

            product.setStock(stock[i % stock.length]);
            product.setRegDate(LocalDateTime.now());
            product.setUpdateDate(LocalDateTime.now());
            Product savedBean = this.productRepository.save(product);
            System.out.println(savedBean.toString());
        }
    }

    @PersistenceContext // JPA가 동작하는 가상의 공간(space), 이 어노테이션이  em에게 의미있는 데이터를 주입(injection)해 줍니다.
    EntityManager em ; // 엔터티 관리자

    @Test
    @DisplayName("query Dsl Test01")
    public void queryDslTest01(){
        // 상품 중에서 현재 판매중(SELL)인 상품을 가격 내림차순으로 조회합니다.
        // 상품 설명에 "아" 글자가 포함되어 있는 행만 조회합니다.
        /*
            select * from products
            where product_status = 'SELL' and description like '%아%'
            order by price desc ;
        */
        // JPAQueryFactory는 쿼리를 만들어 내기 위한 클래스
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QProduct qbean = QProduct.product ;
        JPAQuery<Product> query = queryFactory
                .selectFrom(qbean)
                .where(qbean.productStatus.eq(ProductStatus.SELL))
                .where(qbean.description.like("%" + "아" + "%"))
                .orderBy(qbean.price.desc()) ;

        List<Product> itemList = query.fetch();
        for(Product bean : itemList){
            System.out.println(bean.toString());
        }
    }

    @Test
    @DisplayName("query Dsl Test02")
    public void queryDslTest02(){
        // 현재 판매중(SELL)인 상품 중에서, 단가가 200초과이고, 세부 설명에 '어'가 포함되어 있는 상품을 조회해 보세요.
        // 페이지당 2개씩 볼건데, 2페이지를 볼 예정입니다.
        /*
            select * from products
            where product_status = 'SELL'
            and price > 200
            and description like '%어%'
            LIMIT 2, 2;
         */

        QProduct qbean = QProduct.product ;

        BooleanBuilder booleanBuilder = new BooleanBuilder() ;

        String description = "어" ;
        booleanBuilder.and(qbean.description.like("%" + description + "%"));

        int price = 200 ;
        booleanBuilder.and(qbean.price.gt(price)) ;

        booleanBuilder.and(qbean.productStatus.eq(ProductStatus.SELL));

        int pageNumber = 2 - 1 ; // 현재 보고자 하는 페이지 번호
        int pageSize = 2 ; // 페이지당 볼 건수
        Pageable pageable = PageRequest.of(pageNumber, pageSize) ;

        //                                                      조건식         , 페이징객체
        Page<Product> pageList = this.productRepository.findAll(booleanBuilder, pageable) ;

        System.out.println("total element : " + pageList.getTotalElements());

        List<Product> itemList = pageList.getContent() ;
        for(Product bean : itemList){
            System.out.println(bean.toString());
        }
    }
}