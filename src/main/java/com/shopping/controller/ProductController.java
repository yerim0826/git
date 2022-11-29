package com.shopping.controller;

import com.shopping.dto.ProductFormDto;
import com.shopping.dto.ProductSearchDto;
import com.shopping.entity.Product;
import com.shopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProductController { // ProductController01
//    @GetMapping(value = "admin/products/new")
//    public String productInsert(){// ProductController01
//        return "product/prInsertForm" ;
//    }

    @GetMapping(value = "/admin/products/new") // 관리자가 [상품 등록] 메뉴 클릭
    public String productInsert(Model model){// ProductController02
        model.addAttribute("productFormDto", new ProductFormDto()) ;
        return "product/prInsertForm" ;
    }

    // ProductController03
    // 파라미터 "productImageFile"가 넘겨준 데이터들을 productImageFileList에 저장해 줍니다.
    // List<MultipartFile>라고 한 이유는 <input type="file">이 5개이기 때문입니다.

    private final ProductService productService ;

    @PostMapping(value = "/admin/products/new") // 상품 등록 화면에서 [등록] 버튼 클릭 // ProductController03
    public String productNew(
            @Valid ProductFormDto productFormDto,
            BindingResult error,
            Model model,
            @RequestParam("productImageFile") List<MultipartFile> productImageFileList
        ){
        // productFormDto) 상품 등록을 위하여 사용자가 기입한 데이터(command 객체)
        // error) 유효성 검사에 문제 발생시 여기에 기록이 됩니다.
        // model) 저장할 데이터 또는 별도의 메시지 등을 html 파일에 보여 주기 위한 용도로 사용됩니다.
        // productImageFileList) 업로드를 위한 상품 이미지를 저장하고 있는 컬렉션

        if(error.hasErrors()){
            model.addAttribute("errorMessage", "유효성 검사에 문제가 발생했습니다.");
            return "product/prInsertForm" ;
        }

        if(productImageFileList.get(0).isEmpty() && productFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫 번째 이미지는 필수 입력 사항입니다.");
            return "product/prInsertForm" ;
        }

        try {
            this.productService.saveProduct(productFormDto, productImageFileList) ;

        }catch (Exception err){
            err.printStackTrace();
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.") ;
            return "product/prInsertForm" ;
        }

        return "redirect:/" ; // 메인 페이지로 이동할께요.
    }

    // ProductController04) 상품 수정 페이지로 이동하기
    // http://localhost:8989/admin/product/상품id
    @GetMapping(value = "/admin/product/{productId}")
    public String productDetail(@PathVariable("productId") Long productId, Model model){
        try{
            // 조회된 상품 데이터 정보를 model에 바인딩하여 뷰(html)로 전달해 줍니다.
            ProductFormDto formDto = this.productService.getProductDetail(productId) ;
            model.addAttribute("productFormDto", formDto);

        }catch (Exception err){
            model.addAttribute("errorMessage", "존재 하지 않는 상품입니다.");
            model.addAttribute("productFormDto", new ProductFormDto());
        }
        return "product/prUpdateForm" ;
    }

    // ProductController05) 상품 수정 화면에서 [수정] 버튼을 눌렀습니다.
    @PostMapping(value = "/admin/product/{productId}")
    public String productUpdate(
            @Valid ProductFormDto dto,
            BindingResult error,
            @RequestParam("productImageFile") List<MultipartFile> uploadedFile,
            Model model
        ){
        String whenError = "product/prUpdateForm" ;

        if(error.hasErrors()){
            return whenError ;
        }

        if(uploadedFile.get(0).isEmpty() && dto.getId()==null){
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 필수 입력 사항입니다.") ;
            return whenError ;
        }

        try{
            this.productService.updateProduct(dto, uploadedFile) ;

        }catch (Exception err){
            model.addAttribute("errorMessage", "상품 수정 중에 오류가 발생하였습니다.") ;
            err.printStackTrace();
            return whenError ;
        }

        return "redirect:/" ; // 메인 페이지로 이동
    }

    // ProductController06)
    @GetMapping(value = {"/admin/products/list", "/admin/products/{page}"})
    public String productManage(ProductSearchDto dto, @PathVariable("page") Optional<Integer> page, Model model){
        // dto) 검색 조건
        // page) 조회할 페이지 번호로써, 옵션 사항(없는 경우 0으로 대체)
        // model) 데이터 저장용 모델 객체

        int pageSize = 3 ;
        int pageNumber = page.isPresent() ? page.get() : 0 ;

        Pageable pageable = PageRequest.of(pageNumber, pageSize) ;

        Page<Product> products = this.productService.getAdminProductPage(dto, pageable) ;

        model.addAttribute("products", products) ;
        model.addAttribute("searchDto", dto) ; // for 검색 조건 유지
        model.addAttribute("maxPage", 5) ; // 하단에 보여줄 페이지 번호의 최대 개수

        return "product/prList" ;
    }

    // ProductController07) 상품 클릭시 상세 보기 페이지로 가기
    @GetMapping(value = "/product/{productId}")
    public String productDetail(Model model, @PathVariable("productId") Long productId){
        // 메인 페이지에서 특정 상품을 클릭했을 때 상품 상세 페이지로 이동시키는 컨트롤러 메소드입니다.
        ProductFormDto dto = this.productService.getProductDetail(productId);

        model.addAttribute("product", dto) ;
        return "product/prDetail" ;
    }
}







