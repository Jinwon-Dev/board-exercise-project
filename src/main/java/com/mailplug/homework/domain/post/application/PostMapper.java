package com.mailplug.homework.domain.post.application;

import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.domain.post.persistence.Post;
import com.mailplug.homework.domain.post.web.dto.WritePostRequest;
import com.mailplug.homework.domain.post.web.dto.WritePostResponse;
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
}
