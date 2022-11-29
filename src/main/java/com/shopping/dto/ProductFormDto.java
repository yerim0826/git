package com.shopping.dto;

import com.shopping.constant.ProductStatus;
import com.shopping.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter // 상품 등록시 기입한 데이터 정보를 전달해주는  dto 클래스(command 객체)
public class ProductFormDto { // ProductFormDto01
    private Long id ;

    @NotBlank(message = "상품명은 필수 입력 사항입니다.")
    private String name ;

    @NotNull(message = "가격은 필수 입력 사항입니다.")
    private Integer price ;

    @NotBlank(message = "상세 설명은 필수 입력 사항입니다.")
    private String description ;

    @NotNull(message = "재고 수량은 필수 입력 사항입니다.")
    private Integer stock ;

    private ProductStatus productStatus ;

    // 상품 등록시 첨부할 이미지 목록(최대 5개)을 저장할 리스트 컬렉션입니다.
    private List<ProductImageDto> productImageDtoList = new ArrayList<>() ;

    // 상품 이미지들의 id 목록을 저장합니다.
    // 차후 수정시 참조할 id입니다.
    private List<Long> productImageIds = new ArrayList<>() ;

    private static ModelMapper modelMapper = new ModelMapper() ;

    public Product createProduct(){
        // ProductFormDto(dto) 객체의 정보를 Product(entity)에 매핑해줍니다.
        return modelMapper.map(this, Product.class) ;
    }

    public static ProductFormDto of(Product product){
        // Product(entity) 객체의 정보를 ProductFormDto(dto)에 매핑해줍니다.
        return modelMapper.map(product, ProductFormDto.class) ;
    }
}