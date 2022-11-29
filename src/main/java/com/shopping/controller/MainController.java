package com.shopping.controller;

import com.shopping.dto.MainProductDto;
import com.shopping.dto.ProductSearchDto;
import com.shopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor // MainController02
public class MainController {
//    @GetMapping(value = "/")
//    public String main(){ // MainController01
//        return "main" ;
//    }

    private final ProductService productService ; // MainController02

    @GetMapping(value = "/") // MainController02
    public String main(ProductSearchDto dto, Optional<Integer> page, Model model){
        int pageNumber = page.isPresent() ? page.get() : 0 ;
        int pageSize = 3 ;
        Pageable pageable = PageRequest.of(pageNumber, pageSize) ;

        System.out.println("dto info");
        System.out.println(dto.getSearchQuery());
        if(dto.getSearchQuery()==null || dto.getSearchQuery().equals("null")){
            dto.setSearchQuery("");
        }

        Page<MainProductDto> products = this.productService.getMainProductPage(dto, pageable);

        model.addAttribute("products", products);
        model.addAttribute("searchDto", dto); // for 검색 조건 보존
        model.addAttribute("maxPage", 5);

        for(MainProductDto bean : products){
            System.out.println(bean.getImageUrl());
        }

        return "main" ;
    }
}
