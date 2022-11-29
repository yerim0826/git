package com.shopping.dto;

import com.shopping.entity.ProductImage;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter // 상품 등록시 이미지 화면과 연관이 있습니다.
public class ProductImageDto{ // ProductImageDto01
    private Long id ;
    private String imageName ; // 이미지 파일 이름('UUID'.확장자 형식의 이름)
    private String oriImageName ; // 업로드 된 파일의 원본 이미지 이름
    private String imageUrl ; // 이미지 조회 경로
    private String repImageYesNo ; // 대표 이미지이면 "Y" 값을 가집니다.

    private static ModelMapper modelMapper = new ModelMapper() ;

    public static ProductImageDto of(ProductImage productImage){
        // 상품의 이미지 정보(productImage)를 상품 이미지 Dto(ProductImageDto) 객체로 변환해 줍니다.
        return modelMapper.map(productImage, ProductImageDto.class) ;
    }
}
