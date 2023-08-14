package com.mailplug.homework.domain.board.persistence;

import com.mailplug.homework.domain.board.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT board FROM Board board WHERE board.name = :name")
    Optional<Board> findBoardByName(@Param("name") final BoardType boardType);
}
