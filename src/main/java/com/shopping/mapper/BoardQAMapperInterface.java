package com.shopping.mapper;

import com.shopping.entity.BoardQA;
import org.apache.ibatis.annotations.*;

import java.util.List;

// @MapperScan 공부 요망
@Mapper // 스프링이 자동으로 객체를 생성하고, sql 구문을 분석해 줍니다.
public interface BoardQAMapperInterface {
    @Select("select * from boardsQA")
    List<BoardQA> SelectAll() ;

    String sql = " insert into boardsQA(writer, subject, description) values(#{boardQA.writer}, #{boardQA.subject}, #{boardQA.description})";
    @Insert(sql)
    int Insert(@Param("boardQA") final BoardQA boardQA);

    @Select("select * from boardsQA where no = #{no}")
    BoardQA SelectOne(@Param("no") final Integer no);

    String sql2 = " update boardsQA set writer=#{boardQA.writer}, subject=#{boardQA.subject}, description=#{boardQA.description} where no=#{boardQA.no}" ;
    @Update(sql2)
    int Update(@Param("boardQA") final BoardQA boardQA);

    @Delete("delete from boardsQA where no = #{no}")
    int Delete(@Param("no") final Integer no);
}





