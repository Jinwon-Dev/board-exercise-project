package com.mailplug.homework.domain.post.web;

import com.mailplug.homework.domain.post.application.PostService;
import com.mailplug.homework.domain.post.web.dto.WritePostRequest;
import com.mailplug.homework.global.resolver.MemberId;
import com.mailplug.homework.global.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
