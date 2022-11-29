package com.shopping.controller;

import com.shopping.dto.PersonDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/person")
public class PersonDtoController {
    @GetMapping(value = "/ex01")
    public String personExam01(Model model){
        model.addAttribute("hello", "안녕하세요.");
        return "personTest/personEx01" ;
    }

    @GetMapping(value = "/ex02")
    public String personExam02(Model model){
        PersonDto personDto = this.createPerson();
        model.addAttribute("personDto", personDto);
        return "personTest/personEx02" ;
    }

    private PersonDto createPerson() {
        PersonDto personDto = new PersonDto() ;
        personDto.setAddress("마포");
        personDto.setAge(30);
        personDto.setGender("남자");
        personDto.setName("김철수");
        personDto.setRegTime(LocalDateTime.now());
        return personDto ;
    }

    @GetMapping(value = "/ex03")
    public String personExam03(Model model){
        List<PersonDto> personDtoList = new ArrayList<PersonDto>();
        for (int i = 1; i <= 10 ; i++) {
            personDtoList.add(this.createPerson());
        }
        model.addAttribute("personDtoList", personDtoList);
        return "personTest/personEx03" ;
    }

    @GetMapping(value = "/ex04")
    public String personExam04(Model model){
        List<PersonDto> personDtoList = new ArrayList<PersonDto>();
        for (int i = 1; i <= 10 ; i++) {
            personDtoList.add(this.createPerson());
        }
        model.addAttribute("personDtoList", personDtoList);
        return "personTest/personEx04" ;
    }

    @GetMapping(value = "/ex05")
    public String personExam05(Model model){
        List<PersonDto> personDtoList = new ArrayList<PersonDto>();
        for (int i = 1; i <= 10 ; i++) {
            personDtoList.add(this.createPerson());
        }
        model.addAttribute("personDtoList", personDtoList);
        return "personTest/personEx05" ;
    }

    @GetMapping(value = "/ex06")
    public String personExam06(){
        return "personTest/personEx06" ;
    }

    @GetMapping(value = "/ex07") // personEx06.html에서 클릭을 하시면 됩니다.
    public String personExam07(String su1, String su2, Model model){
        if(su1 == null){su1="100";}
        if(su2 == null){su2="200";}

        model.addAttribute("su1", Integer.parseInt(su1));
        model.addAttribute("su2", Integer.parseInt(su2));

        return "personTest/personEx07" ;
    }

    @GetMapping(value = "/ex08")
    public String personExam08(){
        return "personTest/personEx08" ;
    }
}