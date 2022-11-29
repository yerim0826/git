package com.shopping.controller;

import com.shopping.entity.BoardQA;
import com.shopping.service.BoardQAService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boardQA")
public class BoardQAController {
    private final BoardQAService boardQAService ;

    // RowBounds 공부 요망
    
    @GetMapping(value = {"/list"})
    public String SelectAll(Model model){ // 게시물 목록 보기
        List<BoardQA> boardQAList = this.boardQAService.SelectAll() ;

        model.addAttribute("list", boardQAList) ;
        return "/boardQA/boardQAList" ;
    }

    @GetMapping(value = {"/insert"})
    public String doGetInsert(Model model){ // 게시물 등록(get 방식)
        model.addAttribute("boardQA", new BoardQA()) ;
        return "/boardQA/boardQAInsert" ;
    }

    @PostMapping(value = {"/insert"})
    public String Insert(BoardQA boardQA, Model model){ // 게시물 등록(post 방식)
        int cnt = -9999 ;
        cnt = this.boardQAService.Insert(boardQA) ;

        if(cnt==1){
            return "redirect:/boardQA/list" ;
        }else{
            return "/boardQA/boardInsert" ;
        }
    }

    @GetMapping(value = {"/detail/{no}"})
    public String SelectOne(@PathVariable("no") Integer no, Model model){ // 게시물 상세 보기
        BoardQA boardQA = this.boardQAService.SelectOne(no);
        model.addAttribute("boardQA", boardQA) ;
        return "/boardQA/boardQADetail" ;
    }

    @GetMapping(value = {"/update/{no}"})
    public String doGetUpdate(@PathVariable("no") Integer no, Model model){
        BoardQA boardQA = this.boardQAService.SelectOne(no);
        model.addAttribute("boardQA", boardQA) ;
        return "/boardQA/boardQAUpdate" ;
    }

    @PostMapping(value = {"/update"})
    public String Update(BoardQA boardQA, Model model){
        int cnt = -9999 ;
        cnt = this.boardQAService.Update(boardQA) ;

        if(cnt==1){
            return "redirect:/boardQA/list" ;
        }else{
            return "/boardQA/boardQAUpdate" ;
        }
    }

    @GetMapping(value = {"/delete/{no}"}) // 게시물 삭제하기
    public String Delete(@PathVariable("no") Integer no, Model model) {  // BoardController07
        int cnt = -999  ;
        cnt = boardQAService.Delete(no);
        return "redirect:/boardQA/list";
    }
}




