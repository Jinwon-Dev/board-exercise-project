package com.mailplug.homework.domain.post.application;

import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.board.persistence.BoardRepository;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.domain.member.persistence.MemberRepository;
import com.mailplug.homework.domain.post.persistence.Post;
import com.mailplug.homework.domain.post.persistence.PostRepository;
import com.mailplug.homework.domain.post.web.dto.WritePostRequest;
import com.mailplug.homework.domain.post.web.dto.WritePostResponse;
import com.mailplug.homework.global.exception.board.BoardExceptionExecutor;
import com.mailplug.homework.global.exception.member.MemberExceptionExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostMapper postMapper;

    @Transactional
    public WritePostResponse writePost(final WritePostRequest request,
                                       final Long memberId) {

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        final Board board = boardRepository.findBoardByName(request.boardType())
                .orElseThrow(BoardExceptionExecutor::BoardTypeInvalid);

        final Post post = postMapper.writePostRequestToEntity(request, member, board);

        postRepository.save(post);

        return postMapper.entityToWritePostResponse(post);
    }
}
