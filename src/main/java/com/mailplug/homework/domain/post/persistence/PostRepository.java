package com.mailplug.homework.domain.post.persistence;

import com.mailplug.homework.domain.board.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT post FROM Post post" +
            " JOIN FETCH post.board board" +
            " WHERE post.board.name = :name" +
            " ORDER BY post.createAt DESC")
    Page<Post> findAllPostsByBoardName(@Param("name") final BoardType boardType, final Pageable pageable);

    @Query("SELECT post FROM Post post" +
            " WHERE post.title like %:keyword%" +
            " ORDER BY post.createAt DESC")
    Page<Post> findAllPostsByPostTitle(@Param("keyword") final String keyword, final Pageable pageable);
}
