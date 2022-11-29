package com.shopping.controller;

import com.shopping.dto.ProductDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {
    @GetMapping(value ="/ex01")
    public String thymeleafExam01(Model model){ // 컨트롤러 메소드
        model.addAttribute("data", "타임 리프 1번 예시입니다.") ;
        // request.setAttribute("data", "타임 리프 1번 예시입니다.") ;
        model.addAttribute("newdata", "5 > 3") ;
        return "thymeleafEx/viewEx01" ;
    }

    @GetMapping(value ="/ex02")
    public String thymeleafExam02(Model model){
        ProductDto bean = new ProductDto() ;
        bean.setName("사과");
        bean.setPrice(1234);
        bean.setDescription("맛있어요.");
        bean.setRegDate(LocalDateTime.now());

        model.addAttribute("bean", bean) ;
        return "thymeleafEx/viewEx02" ;
    }

    private List<ProductDto> getProductDtoData(int gaesu){
        // 샘플용 데이터 gaesu개 만큼 만들어서 반환해 줍니다.
        List<ProductDto> itemDtoList = new ArrayList<ProductDto>();

        for (int i = 1; i <= gaesu; i++) {
            ProductDto bean = new ProductDto() ;
            bean.setName("테스트 상품 " + i);
            bean.setPrice(1000*i);
            bean.setDescription("상품 상세 설명" + i);
            bean.setRegDate(LocalDateTime.now());

            itemDtoList.add(bean) ;
        }
        return itemDtoList ;
    }

    @GetMapping(value ="/ex03")
    public String thymeleafExam03(Model model){
        List<ProductDto> itemDtoList = this.getProductDtoData(10) ;
        model.addAttribute("itemDtoList", itemDtoList) ;
        return "thymeleafEx/viewEx03" ;
    }

    @GetMapping(value ="/ex04")
    public String thymeleafExam04(Model model){
        List<ProductDto> itemDtoList = this.getProductDtoData(5) ;
        model.addAttribute("itemDtoList", itemDtoList) ;
        return "thymeleafEx/viewEx04" ;
    }

    @GetMapping(value ="/ex05")
    public String thymeleafExam05(Model model){
        List<ProductDto> itemDtoList = this.getProductDtoData(20) ;
        model.addAttribute("itemDtoList", itemDtoList) ;
        return "thymeleafEx/viewEx05" ;
    }

    @GetMapping(value ="/ex06")
    public String thymeleafExam06(){
        return "thymeleafEx/viewEx06" ;
    }

    @GetMapping(value ="/ex07")
    public String thymeleafExam07(String param1, String param2, Model model){
        if(param1==null){param1="호호호";}
        if(param2==null){param2="def";}

        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);

        return "thymeleafEx/viewEx07" ;
    }

    @GetMapping(value ="/ex08")
    public String thymeleafExam08(){
        return "thymeleafEx/viewEx08" ;
    }
}
