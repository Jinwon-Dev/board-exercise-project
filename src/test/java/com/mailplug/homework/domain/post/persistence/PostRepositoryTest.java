package com.mailplug.homework.domain.post.persistence;

import autoparams.AutoSource;
import com.mailplug.homework.domain.board.BoardType;
import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.board.persistence.BoardRepository;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.domain.member.persistence.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Nested
    @DisplayName("게시글 작성")
    class WritePost {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        void write_post(final Member member) {

            // given
            memberRepository.save(member);

            Board board = boardRepository.save(new Board(BoardType.FREE));

            final var post = new Post(
                    "title",
                    "content",
                    member,
                    board
            );

            // when
            final var savedPost = postRepository.save(post);

            // then
            final var findPost = postRepository.findById(savedPost.getId()).get();
            assertSoftly(softly -> {
                softly.assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
                softly.assertThat(findPost.getContent()).isEqualTo(post.getContent());
            });
        }
    }
}