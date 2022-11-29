package com.shopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/song")
public class NationalSongController {
    @GetMapping(value ="/ex01")
    public String thymeleafExam01(){
        return "nationalSong/song01" ;
    }

    @GetMapping(value ="/ex02")
    public String thymeleafExam02(){
        return "nationalSong/song02" ;
    }
}
