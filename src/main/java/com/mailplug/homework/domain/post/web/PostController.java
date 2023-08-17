package com.mailplug.homework.domain.post.web;

import com.mailplug.homework.domain.board.BoardType;
import com.mailplug.homework.domain.post.application.PostService;
import com.mailplug.homework.domain.post.web.dto.UpdatePostRequest;
import com.mailplug.homework.domain.post.web.dto.WritePostRequest;
import com.mailplug.homework.global.resolver.MemberId;
import com.mailplug.homework.global.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<CommonResponse> writePost(@RequestBody @Valid final WritePostRequest request,
                                                    @MemberId final Long memberId) {

        final var response = postService.writePost(request, memberId);
        return ResponseEntity.status(CREATED).body(CommonResponse.newInstance(response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<CommonResponse> readPost(@PathVariable final Long postId) {

        final var response = postService.readPost(postId);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse> readPostList(@RequestParam(name = "name") final String boardType,
                                                       final Pageable pageable) {

        final var response = postService.readPostList(BoardType.valueOf(boardType.toUpperCase()), pageable);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse> readPostListByPostTitle(@RequestParam(name = "keyword") final String keyword,
                                                                  final Pageable pageable) {

        final var response = postService.readPostListByPostTitle(keyword, pageable);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<CommonResponse> updatePost(@PathVariable("postId") final Long postId,
                                                     @MemberId final Long memberId,
                                                     @RequestBody @Valid final UpdatePostRequest request) {

        final var response = postService.updatePost(postId, memberId, request);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<CommonResponse> deletePost(@PathVariable("postId") final Long postId,
                                                     @MemberId final Long memberId) {

        final var response = postService.deletePost(postId, memberId);
        return ResponseEntity.ok(CommonResponse.newInstance(response));
    }
}
