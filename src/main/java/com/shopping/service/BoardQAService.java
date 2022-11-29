package com.shopping.service;

import com.shopping.entity.BoardQA;
import com.shopping.mapper.BoardQAMapperInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardQAService {
    private final BoardQAMapperInterface boardQAMapperInterface ;

    public List<BoardQA> SelectAll(){
        return this.boardQAMapperInterface.SelectAll() ;
    }

    public int Insert(BoardQA boardQA) {
        return this.boardQAMapperInterface.Insert(boardQA) ;
    }

    public BoardQA SelectOne(Integer no) {
        return this.boardQAMapperInterface.SelectOne(no) ;
    }

    public int Update(BoardQA boardQA) {
        return this.boardQAMapperInterface.Update(boardQA) ;
    }

    public int Delete(Integer no) {
        return this.boardQAMapperInterface.Delete(no) ;
    }
}
