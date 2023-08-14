package com.mailplug.homework.domain.post;

import autoparams.AutoSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailplug.homework.domain.IntegrationTest;
import com.mailplug.homework.domain.board.BoardType;
import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.board.persistence.BoardRepository;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.domain.member.persistence.MemberRepository;
import com.mailplug.homework.domain.post.web.dto.WritePostRequest;
import com.mailplug.homework.global.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("게시글 작성")
    class WritePost {

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        void write_post(final Member member) throws Exception {

            // given
            final Member savedMember = memberRepository.save(member);
            boardRepository.save(new Board(BoardType.FREE));

            final String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId());

            final var request = new WritePostRequest(
                    "title",
                    "content",
                    BoardType.FREE
            );

            // when
            final var perform = mockMvc.perform(post("/posts")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.id").exists());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 로그인 상태여야 한다.")
        void write_post_not_member() throws Exception {

            // given
            boardRepository.save(new Board(BoardType.FREE));

            final var request = new WritePostRequest(
                    "title",
                    "content",
                    BoardType.FREE
            );

            // when
            final var perform = mockMvc.perform(post("/posts")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 제목은 100자를 초과해서 작성할 수 없다.")
        void write_post_over_title(final Member member) throws Exception {

            // given
            final Member savedMember = memberRepository.save(member);
            boardRepository.save(new Board(BoardType.FREE));

            final String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId());

            final var request = new WritePostRequest(
                    IntStream.range(0, 21)
                            .mapToObj(i -> "title")
                            .collect(Collectors.joining()),
                    "content",
                    BoardType.FREE
            );

            // when
            final var perform = mockMvc.perform(post("/posts")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("요청시 존재하지 않는 게시판이면 실패한다.")
        void write_post_not_exist_board(final Member member) throws Exception {

            // given
            final Member savedMember = memberRepository.save(member);

            final String accessToken = jwtTokenProvider.createAccessToken(savedMember.getId());

            final var request = new WritePostRequest(
                    "title",
                    "content",
                    BoardType.NOTICE
            );

            // when
            final var perform = mockMvc.perform(post("/posts")
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "bearer " + accessToken)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isBadRequest());
        }
    }
}
