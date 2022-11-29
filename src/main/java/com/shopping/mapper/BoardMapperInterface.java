package com.shopping.mapper;

import com.shopping.entity.Board;
import org.apache.ibatis.annotations.*;

import java.util.List;

// @MapperScan 공부 요망
@Mapper // 스프링이 자동으로 객체를 생성하고, sql 구문을 분석해 줍니다.
public interface BoardMapperInterface {
    @Select("select * from boards")
    List<Board> SelectAll() ;

    String sql = " insert into boards(writer, subject, description) values(#{board.writer}, #{board.subject}, #{board.description})";
    @Insert(sql)
    int Insert(@Param("board") final Board board);

    @Select("select * from boards where no = #{no}")
    Board SelectOne(@Param("no") final Integer no);

    String sql2 = " update boards set writer=#{board.writer}, subject=#{board.subject}, description=#{board.description} where no=#{board.no}" ;
    @Update(sql2)
    int Update(@Param("board") final Board board);

    @Delete("delete from boards where no = #{no}")
    int Delete(@Param("no") final Integer no);
}





