package com.shopping.service;

import com.shopping.dto.MainProductDto;
import com.shopping.dto.ProductFormDto;
import com.shopping.dto.ProductImageDto;
import com.shopping.dto.ProductSearchDto;
import com.shopping.entity.Product;
import com.shopping.entity.ProductImage;
import com.shopping.repository.ProductImageRepository;
import com.shopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService { // 상품을 등록하는 서비스 입니다.
    private final ProductRepository productRepository ; // ProductService01
    private final ProductImageService productImageService ; // ProductService01

    public Long saveProduct(ProductFormDto productFormDto, List<MultipartFile> productImageFileList) throws Exception{ // ProductService01
        // productFormDto) 등록 폼 화면에서 넘어온 command 객체
        // 이 객체를 이용하여 Entity를 생성합니다.
        // 01. 상품 등록하기
        Product product = productFormDto.createProduct() ;
        this.productRepository.save(product) ;

        // 02. 상품에 들어 가는 각각의 이미지 등록하기
        for (int i = 0; i < productImageFileList.size(); i++) {
            ProductImage productImage = new ProductImage() ;

            // 해당 상품과 이미지에 대하여 연결 고리를 만들어 줍니다.
            // 그래야만, 실제 데이터 베이스에 들어갈 때, 동일한 product_id 값이 들어 갑니다.
            productImage.setProduct(product);

            if(i==0){ // 1번째 이미지는 대표 이미지로써 필수 사항입니다.
                productImage.setRepImageYesNo("Y");
            }else{
                productImage.setRepImageYesNo("N");
            }

            // System.out.println("productImageFileList.get(i).getOriginalFilename()");
            // System.out.println(productImageFileList.get(i).getOriginalFilename().equals(""));
            boolean bool = productImageFileList.get(i).getOriginalFilename().equals("") ;

            // 상품의 이미지 정보를 저장합니다.
            if(!bool){ // 업로드할 이미지가 있는 경우에만 저장
                productImageService.saveProductImage(productImage, productImageFileList.get(i));
            }

        }// end for
        return product.getId();
    }

    // ProductService02
    private final ProductImageRepository productImageRepository ;

    // 등록된 상품 정보를 불러오는 메소드를 구현합니다.
    public ProductFormDto getProductDetail(Long productId){ // ProductService02
        // 수정할 상품 id에 대하여 관련 이미지 목록을 등록된 순서대로 읽어 들입니다.
        List<ProductImage> productImageList = this.productImageRepository.findByProductIdOrderByIdAsc(productId) ;

        // dto 객체를 저장시킬 List 컬렉션
        List<ProductImageDto> dtoList = new ArrayList<ProductImageDto>();

        // 조회된 ProductImage 목록을 dto로 변환시켜 List 컬렉션에 저장합니다.
        for(ProductImage bean : productImageList){
            ProductImageDto dto = ProductImageDto.of(bean);
            dtoList.add(dto);
        }

        // 상품의 id를 이용하여 상품 Entity 객체를 조회합니다.
        Product product = productRepository.findById(productId).orElseThrow(EntityNotFoundException::new);
        ProductFormDto formDto = ProductFormDto.of(product) ;
        formDto.setProductImageDtoList(dtoList);

        return formDto ;
    }

    // ProductService03
    // 화면(폼)에서 넘겨진 dto를 이용하여 상품 정보를 업데이트합니다.
    public Long updateProduct(ProductFormDto dto, List<MultipartFile> uploadedFile) throws Exception{
        // dto) 상품 수정 화면에서 넘겨진 command 객체
        // uploadedFile) 수정할 상품 이미지를 저장하고 있는 List 컬렉션

        // 화면에서 전달 받은 dto 객체를 이용하여 해당 Entity 정보를 취득합니다.
        Product product = this.productRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new) ;

        // dto를 Entity에게 전달하기
        product.updateProduct(dto);

        // 5개의 이미지에 대한 각각의 id 목록(상품 이미지에 대한 id 리스트)
        List<Long> productImageIds = dto.getProductImageIds() ;

        // 각각의 상품 이미지 정보를 수정해 줍니다.
        for (int i = 0; i < uploadedFile.size(); i++) {
            // 이전에 등록된 이미지 개수와 새로 추가된 개수가 안맞는 문제 발생 ??
            if(i < productImageIds.size()){ // 이전 이미지들은 그대로 수정하면 됩니다.
                this.productImageService.updateProductImage(productImageIds.get(i), uploadedFile.get(i));

            }else{ // 신규로 추가된 이미지들에 대한 추가된 코드입니다.
                ProductImage productImage = new ProductImage() ;
                productImage.setProduct(product);
                productImage.setRepImageYesNo("N");
                this.productImageService.saveProductImage(productImage, uploadedFile.get(i));
            }
        }

        return product.getId() ;
    }

    // ProductService04) 검색 조건과 페이징 객체를 이용하여 페이지 결과를 반환해 줍니다.
    public Page<Product> getAdminProductPage(ProductSearchDto dto, Pageable pageable){
        return this.productRepository.getAdminProductPage(dto, pageable) ;
    }

    // ProductService05) 메인 페이지에 보여줄 상품 정보를 반환해 줍니다.
    public Page<MainProductDto> getMainProductPage(ProductSearchDto dto, Pageable pageable){
        return this.productRepository.getMainProductPage(dto, pageable) ;
    }
}












