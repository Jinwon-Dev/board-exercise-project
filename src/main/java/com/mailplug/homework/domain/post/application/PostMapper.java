package com.mailplug.homework.domain.post.application;

import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.domain.post.persistence.Post;
import com.mailplug.homework.domain.post.web.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    Post writePostRequestToEntity(final WritePostRequest request, final Member member, final Board board) {

        return new Post(
                request.title(),
                request.content(),
                member,
                board
        );
    }

    WritePostResponse entityToWritePostResponse(final Post post) {

        return new WritePostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getMember().getId()
        );
    }

    ReadPostResponse entityToReadPostResponse(final Post post) {

        return new ReadPostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getMember().getEmail(),
                post.getCreateAt(),
                post.getViews()
        );
    }

    Page<ReadPostListResponse> entityToReadPostListResponse(final Page<Post> posts) {

        return posts.map(post -> new ReadPostListResponse(
                post.getId(),
                post.getTitle(),
                post.getMember().getEmail(),
                post.getCreateAt(),
                post.getViews()
        ));
    }

    UpdatePostResponse entityToUpdatePostResponse(final Post post) {

        return new UpdatePostResponse(post.getId(), post.getTitle(), post.getContent());
    }
}
