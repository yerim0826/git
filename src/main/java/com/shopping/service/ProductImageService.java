package com.shopping.service;

import com.shopping.entity.ProductImage;
import com.shopping.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageService { // ProductImageService01
    @Value("${productImageLocation}") // ProductImageService01
    private String productImageLocation ; // 상품 이미지가 업로드 되는 경로

    private final FileService fileService ; // ProductImageService01
    private final ProductImageRepository productImageRepository ; // ProductImageService01

    // 해당 상품에 대한 이미지 정보를 저장합니다.
    public void saveProductImage(ProductImage productImage, MultipartFile uploadedFile) throws Exception{ // ProductImageService01
        String oriImageName = uploadedFile.getOriginalFilename() ; // 업로드 하고자 하는 파일 원본 이름
        String imageName = "" ; // 로컬에 저장할 uuid 형식의 이미지 파일 이름
        String imageUrl = "" ; // 상품 이미지를 읽어 들인 운영

        System.out.println("productImageLocation : " + productImageLocation);

        if(!StringUtils.isEmpty(oriImageName)){ // 원본 파일 이름이 존재하면 업로드하자.
            imageName = fileService.uploadFile(productImageLocation, oriImageName, uploadedFile.getBytes()) ;
            imageUrl = "/images/products/" + imageName ;

            System.out.println("imageName : " + imageName);
            System.out.println("imageUrl : " + imageUrl);
        }

        // 해당 상품에 대한 이미지 정보를 저장합니다.
        productImage.updateProduct(oriImageName, imageName, imageUrl);

        this.productImageRepository.save(productImage) ;
    }

    // ProductImageService02
    public void updateProductImage(Long productImageId, MultipartFile uploadedFile) throws Exception{
        // productImageId) 특정 상품 이미지에 대한 id 번호
        // uploadedFile) 상품의 이미지 파일 정보
        if(!uploadedFile.isEmpty()){
            // 이전 상품 정보를 읽어 옵니다.
            ProductImage previousImage = this.productImageRepository.findById(productImageId)
                    .orElseThrow(EntityNotFoundException::new) ;

            // 기존에 등록된 상품 이미지가 존재하면 해당 파일을 삭제합니다.
            if(!StringUtils.isEmpty(previousImage.getImageName())){
                String deletedFile = productImageLocation + "/" + previousImage.getImageName();
                fileService.deleteFile(deletedFile);
            }

            // 업데이트 하고자 하는 상품 이미지 파일을 업로드합니다.
            String oriImageName = uploadedFile.getOriginalFilename() ;
            String imageName = fileService.uploadFile(productImageLocation, oriImageName, uploadedFile.getBytes()) ;
            String imageUrl = "/images/products/" + imageName ;

            previousImage.updateProduct(oriImageName, imageName, imageUrl);
        }
    }
}









