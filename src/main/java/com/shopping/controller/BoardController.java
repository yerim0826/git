package com.shopping.controller;

import com.shopping.entity.Board;
import com.shopping.service.BoardService;
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
@RequestMapping("/board")
public class BoardController{
    private final BoardService boardService ;

    // RowBounds 공부 요망
    
    @GetMapping(value = {"/list"})
    public String SelectAll(Model model){ // 게시물 목록 보기
        List<Board> boardList = this.boardService.SelectAll() ;

        model.addAttribute("list", boardList) ;
        return "/board/boardList" ;
    }

    @GetMapping(value = {"/insert"})
    public String doGetInsert(Model model){ // 게시물 등록(get 방식)
        model.addAttribute("board", new Board()) ;
        return "/board/boardInsert" ;
    }

    @PostMapping(value = {"/insert"})
    public String Insert(Board board, Model model){ // 게시물 등록(post 방식)
        int cnt = -9999 ;
        cnt = this.boardService.Insert(board) ;

        if(cnt==1){
            return "redirect:/board/list" ;
        }else{
            return "/board/boardInsert" ;
        }
    }

    @GetMapping(value = {"/detail/{no}"})
    public String SelectOne(@PathVariable("no") Integer no, Model model){ // 게시물 상세 보기
        Board board = this.boardService.SelectOne(no);
        model.addAttribute("board", board) ;
        return "/board/boardDetail" ;
    }

    @GetMapping(value = {"/update/{no}"})
    public String doGetUpdate(@PathVariable("no") Integer no, Model model){
        Board board = this.boardService.SelectOne(no);
        model.addAttribute("board", board) ;
        return "/board/boardUpdate" ;
    }

    @PostMapping(value = {"/update"})
    public String Update(Board board, Model model){
        int cnt = -9999 ;
        cnt = this.boardService.Update(board) ;

        if(cnt==1){
            return "redirect:/board/list" ;
        }else{
            return "/board/boardUpdate" ;
        }
    }

    @GetMapping(value = {"/delete/{no}"}) // 게시물 삭제하기
    public String Delete(@PathVariable("no") Integer no, Model model) {  // BoardController07
        int cnt = -999  ;
        cnt = boardService.Delete(no);
        return "redirect:/board/list";
    }
}




