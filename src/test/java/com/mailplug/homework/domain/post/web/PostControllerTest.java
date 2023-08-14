package com.mailplug.homework.domain.post.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailplug.homework.domain.board.BoardType;
import com.mailplug.homework.domain.post.application.PostService;
import com.mailplug.homework.domain.post.web.dto.WritePostRequest;
import com.mailplug.homework.global.resolver.MemberIdResolver;
import com.mailplug.homework.global.security.AuthorizationExtractor;
import com.mailplug.homework.global.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@SpyBean({AuthorizationExtractor.class, MemberIdResolver.class})
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Spy
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("게시글 작성")
    class WritePost {

        @Test
        @DisplayName("요청시 정상적으로 저장되어야 한다.")
        void write_post() throws Exception {

            // given
            final String accessToken = jwtTokenProvider.createAccessToken(1L);

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
            perform.andExpect(status().isCreated());
        }

        @Test
        @DisplayName("요청시 회원이 아니라면 저장에 실패한다.")
        void write_post_not_member() throws Exception {

            // given
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

        @Test
        @DisplayName("요청시 게시글 제목이 100자를 초과하면 저장에 실패한다.")
        void write_post_over_title() throws Exception {

            // given
            final String accessToken = jwtTokenProvider.createAccessToken(1L);

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
    }
}