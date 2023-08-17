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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.IntStream;

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

    @Nested
    @DisplayName("게시글 조회")
    class ReadPost {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 게시글 단건 조회에 성공한다.")
        void read_post(final Member member) {

            // given
            memberRepository.save(member);

            final Board board = boardRepository.save(new Board(BoardType.NOTICE));

            final var post = new Post(
                    "title",
                    "content",
                    member,
                    board
            );
            postRepository.save(post);

            // when
            final var findPost = postRepository.findById(post.getId()).get();

            // then
            assertSoftly(softly -> {
                softly.assertThat(findPost.getTitle()).isEqualTo(post.getTitle());
                softly.assertThat(findPost.getContent()).isEqualTo(post.getContent());
            });
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("게시판별 게시글 목록 조회시 게시판에 맞는 게시글 목록이 페이징되어 조회된다.")
        void read_posts_by_paging(final Member member) {

            // given
            memberRepository.save(member);

            final Board board = boardRepository.save(new Board(BoardType.INQUIRY));

            savePostsForTest(member, board);

            // when
            final Pageable pageable = PageRequest.of(0, 5);
            final var posts = postRepository.findAllPostsByBoardName(BoardType.INQUIRY, pageable);

            // then
            assertSoftly(softly -> {
                softly.assertThat(posts.getContent().get(0).getTitle()).isEqualTo("title9");
                softly.assertThat(posts.getContent().get(0).getBoard().getName()).isEqualTo(BoardType.INQUIRY);
                softly.assertThat(posts.getSize()).isEqualTo(5);
                softly.assertThat(posts.getTotalPages()).isEqualTo(2);
            });
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("제목으로 게시글을 검색하면 키워드가 제목에 포함된 게시글이 검색된다.")
        void read_posts_by_post_title(final Member member) {

            // given
            memberRepository.save(member);

            final Board board = boardRepository.save(new Board(BoardType.INQUIRY));

            savePostsForTest(member, board);

            // when
            final Pageable pageable = PageRequest.of(0, 5);
            final var posts = postRepository.findAllPostsByPostTitle("title", pageable);

            // then
            assertSoftly(softly -> {
                softly.assertThat(posts.getSize()).isEqualTo(5);
                softly.assertThat(posts.getTotalPages()).isEqualTo(2);
            });
        }

        private void savePostsForTest(final Member member, final Board board) {

            postRepository.saveAll(
                    IntStream.range(0, 10)
                            .mapToObj(i -> new Post("title" + i, "content", member, board))
                            .toList()
            );
        }
    }
}