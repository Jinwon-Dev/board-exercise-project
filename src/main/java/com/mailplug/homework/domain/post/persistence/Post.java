package com.mailplug.homework.domain.post.persistence;

import com.mailplug.homework.domain.BaseTimeEntity;
import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.member.persistence.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.mailplug.homework.global.exception.post.PostExceptionExecutor.WriterForbidden;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int views;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private Member member;

    @JoinColumn(name = "board_id", nullable = false)
    @ManyToOne(fetch = LAZY)
    private Board board;

    private void setMember(final Member member) {
        this.member = member;
        member.getPosts().add(this);
    }

    private void setBoard(final Board board) {
        this.board = board;
        board.getPosts().add(this);
    }

    public Post(final String title, final String content, final Member member, final Board board) {
        this.title = title;
        this.content = content;
        this.views = 0;
        setMember(member);
        setBoard(board);
    }

    /**
     * 조회수 증가 비즈니스 로직
     */
    public void increaseView() {
        this.views++;
    }

    /**
     * 게시글 수정 비즈니스 로직
     */
    public void update(final Long memberId, final String title, final String content) {

        if (memberId.equals(this.member.getId())) {
            this.title = title;
            this.content = content;
        } else {
            throw WriterForbidden();
        }
    }
}
