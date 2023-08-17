package com.mailplug.homework.domain.post.web;

import com.mailplug.homework.domain.board.BoardType;
import com.mailplug.homework.domain.post.application.PostService;
import com.mailplug.homework.domain.post.web.dto.UpdatePostRequest;
import com.mailplug.homework.domain.post.web.dto.WritePostRequest;
import com.mailplug.homework.global.resolver.MemberId;
import com.mailplug.homework.global.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "게시글", description = "게시글 관련 API")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 작성", description = "게시글 작성 API - 비회원 불가")
    @PostMapping
    public ResponseEntity<CommonResponse> writePost(@RequestBody @Valid final WritePostRequest request,
                                                    @MemberId final Long memberId) {

        final var response = postService.writePost(request, memberId);
        return ResponseEntity.status(CREATED).body(CommonResponse.newInstance(response));
    }

    @Operation(summary = "게시글 단건 조회", description = "게시글 단건 조회 API - 비회원 가능")
    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse> readPost(@PathVariable final Long postId) {

        final var response = postService.readPost(postId);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @Operation(summary = "게시글 목록 조회", description = "게시판에 속한 게시글 목록 페이징 조회 API - 비회원 가능")
    @GetMapping
    public ResponseEntity<CommonResponse> readPostList(@RequestParam(name = "name") final String boardType,
                                                       final Pageable pageable) {

        final var response = postService.readPostList(BoardType.valueOf(boardType.toUpperCase()), pageable);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @Operation(summary = "게시글 검색", description = "게시글 제목으로 검색 API - 비회원 가능")
    @GetMapping("/search")
    public ResponseEntity<CommonResponse> readPostListByPostTitle(@RequestParam(name = "keyword") final String keyword,
                                                                  final Pageable pageable) {

        final var response = postService.readPostListByPostTitle(keyword, pageable);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정 API - 비회원 불가, 작성자만 가능")
    @PatchMapping("/{postId}")
    public ResponseEntity<CommonResponse> updatePost(@PathVariable("postId") final Long postId,
                                                     @MemberId final Long memberId,
                                                     @RequestBody @Valid final UpdatePostRequest request) {

        final var response = postService.updatePost(postId, memberId, request);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제 API - 비회원 불가, 작성자만 가능")
    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable("postId") final Long postId,
                                                     @MemberId final Long memberId) {

        final var response = postService.deletePost(postId, memberId);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }
}
