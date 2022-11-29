package com.shopping.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shopping.entity.QSaram;
import com.shopping.entity.Saram;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
public class SaramRepositoryTest {
    @Autowired
    SaramRepository saramRepository;

    @Test
    @DisplayName("사람 저장 테스트")
    public void createSaramTest(){
        Saram bean = new Saram();
        bean.setId("kim");
        bean.setName("김철수");
        bean.setAddress("용산구 이태원동");
        bean.setSalary(1234);

        Saram savedBean = saramRepository.save(bean) ;
        System.out.println(savedBean.toString());
    }

    @Test
    @DisplayName("회원 여러명 생성하기")
    public void createSaramTestMany(){
        String[] name = {"김철수", "홍길동", "박흥식"};
        String[] ids = {"kim", "hong", "park"};
        String[] address = {"마포", "용산", "서대문", "은평"};
        int[] salary = {111, 222, 333, 444, 555} ;

        for (int i = 1; i < 21 ; i++) {
            Saram saram = new Saram();

            saram.setName(name[i % name.length] + i);
            saram.setId(ids[i % ids.length] + i);
            saram.setAddress(address[i % address.length]);
            saram.setSalary(salary[i % salary.length]);

            Saram saramBean = this.saramRepository.save(saram);
            System.out.println(saramBean.toString());
        }
    }

    @Test
    @DisplayName("이름순 정렬 테스트")
    public void findByOrderByNameAsc(){
        List<Saram> itemList = this.saramRepository.findByOrderByNameAsc();

        for(Saram bean : itemList){
            System.out.println(bean.toString());
        }
    }

    @Test
    @DisplayName("마포인 사람 조회")
    public void findByAddressEquals(){
        String address = "마포" ;
        List<Saram> itemList = this.saramRepository.findByAddressEquals(address);

        for(Saram Saram : itemList){
            System.out.println(Saram.toString());
        }
    }

    @Test
    @DisplayName("고소득자 우선 정렬")
    public void findByOrderBySalaryDesc(){
        List<Saram> itemList = this.saramRepository.findByOrderBySalaryDesc();

        for(Saram Saram : itemList){
            System.out.println(Saram.toString());
        }
    }

    @Test // select * from sarams where salary >= 500 ;
    @DisplayName("@Query를 사용한 회원 조회 테스트 01")
    public void findBySalaryGreaterThan01(){
        Integer salary = 500 ;
        List<Saram> itemList = this.saramRepository.findBySalaryGreaterThan01(salary) ;
        for(Saram bean : itemList){
            System.out.println(bean.toString());
        }
    }

    @Test // select * from sarams where salary >= 500 ;
    @DisplayName("@Query를 사용한 회원 조회 테스트 02")
    public void findBySalaryGreaterThan02(){
        Integer salary = 400 ;
        List<Saram> itemList = this.saramRepository.findBySalaryGreaterThan02(salary) ;
        for(Saram bean : itemList){
            System.out.println(bean.toString());
        }
    }

    @PersistenceContext
    EntityManager em ;

    @Test
    @DisplayName("query Dsl Test03")
    public void queryDslTest03(){
        // 급여가 400미만이고, 주소에 '포'를 포함하는 회원을 조회하되,
        // 이름의 역순으로 출력하는 queryDsl 구문을 작성하고 테스트해보세요.
        /*
            select * from sarams
            where salary < 400 and address like '%포%'
            order by name desc ;
        */
        // JPAQueryFactory는 쿼리를 만들어 내기 위한 클래스
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QSaram qbean = QSaram.saram;
        JPAQuery<Saram> query = queryFactory
                .selectFrom(qbean)
                .where(qbean.salary.lt(400))
                .where(qbean.address.like("%" + "포" + "%"))
                .orderBy(qbean.name.desc()) ;

        List<Saram> itemList = query.fetch();
        for(Saram bean : itemList){
            System.out.println(bean.toString());
        }
    }

    @Test
    @DisplayName("query Dsl Test04")
    public void queryDslTest04(){
        // 급여가 600이하이고, 주소에 '포'를 포함하는 회원을 조회하되,
        // 이름의 역순으로 출력하는 queryDsl 구문을 작성하고 테스트해보세요.
        // 페이지당 4개씩 볼건데, 2페이지를 볼 예정입니다.
        /*
            select * from sarams
            where salary <= 600 and address like '%포%'
            order by name desc
            limit 4, 4;
        */

        QSaram qbean = QSaram.saram ;

        BooleanBuilder booleanBuilder = new BooleanBuilder() ;

        String description = "포" ;
        booleanBuilder.and(qbean.address.like("%" + description + "%"));

        int salary = 600 ;
        booleanBuilder.and(qbean.salary.loe(salary)) ;

        int pageNumber = 2 - 1 ; // 현재 보고자 하는 페이지 번호
        int pageSize = 4 ; // 페이지당 볼 건수
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name").descending()) ;

        Page<Saram> pageList = this.saramRepository.findAll(booleanBuilder, pageable) ;

        System.out.println("total element : " + pageList.getTotalElements());

        List<Saram> itemList = pageList.getContent() ;
        for(Saram bean : itemList){
            System.out.println(bean.toString());
        }
    }
}
