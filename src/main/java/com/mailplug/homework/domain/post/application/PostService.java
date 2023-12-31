package com.mailplug.homework.domain.post.application;

import com.mailplug.homework.domain.board.BoardType;
import com.mailplug.homework.domain.board.persistence.Board;
import com.mailplug.homework.domain.board.persistence.BoardRepository;
import com.mailplug.homework.domain.member.persistence.Member;
import com.mailplug.homework.domain.member.persistence.MemberRepository;
import com.mailplug.homework.domain.post.persistence.Post;
import com.mailplug.homework.domain.post.persistence.PostRepository;
import com.mailplug.homework.domain.post.web.dto.*;
import com.mailplug.homework.global.exception.board.BoardExceptionExecutor;
import com.mailplug.homework.global.exception.member.MemberExceptionExecutor;
import com.mailplug.homework.global.exception.post.PostExceptionExecutor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostMapper postMapper;

    /**
     * 게시글 작성
     */
    @Transactional
    public WritePostResponse writePost(final WritePostRequest request,
                                       final Long memberId) {

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberExceptionExecutor::MemberNotFound);

        final Board board = boardRepository.findBoardByName(request.boardType())
                .orElseThrow(BoardExceptionExecutor::BoardTypeInvalid);

        final Post post = postMapper.writePostRequestToEntity(request, member, board);

        postRepository.save(post);

        logger.info("{}회원의 게시글 작성에 성공하였습니다.", memberId);

        return postMapper.entityToWritePostResponse(post);
    }

    /**
     * 게시글 단건 조회
     */
    @Transactional
    public ReadPostResponse readPost(final Long postId) {

        final Post post = postRepository.findById(postId)
                .orElseThrow(PostExceptionExecutor::PostNotFound);

        post.increaseView();

        logger.info("{}게시글 조회에 성공하였습니다.", postId);

        return postMapper.entityToReadPostResponse(post);
    }

    /**
     * 게시판별 게시글 목록 조회
     */
    public Page<ReadPostListResponse> readPostList(final BoardType boardType, final Pageable pageable) {

        final Page<Post> posts = postRepository.findAllPostsByBoardName(boardType, pageable);

        logger.info("{}게시판의 게시글 목록 조회에 성공하였습니다.", boardType);

        return postMapper.entityToReadPostListResponse(posts);
    }

    /**
     * 게시글 제목으로 게시글 목록 조회
     */
    public Page<ReadPostListResponse> readPostListByPostTitle(final String keyword, final Pageable pageable) {

        final Page<Post> posts = postRepository.findAllPostsByPostTitle(keyword, pageable);

        logger.info("{}가 포함된 게시글 검색에 성공하였습니다.", keyword);

        return postMapper.entityToReadPostListResponse(posts);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public UpdatePostResponse updatePost(final Long postId, final Long memberId, final UpdatePostRequest request) {

        final Post post = postRepository.findById(postId)
                .orElseThrow(PostExceptionExecutor::PostNotFound);

        post.update(
                memberId,
                request.title(),
                request.content()
        );

        logger.info("{}회원의 {}게시글 수정에 성공하였습니다.", memberId, postId);

        return postMapper.entityToUpdatePostResponse(post);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public DeletePostResponse deletePost(final Long postId, final Long memberId) {

        final Post post = postRepository.findById(postId)
                .orElseThrow(PostExceptionExecutor::PostNotFound);

        if (!memberId.equals(post.getMember().getId())) {
            throw PostExceptionExecutor.WriterForbidden();
        }

        postRepository.delete(post);

        logger.info("{}회원의 {}게시글 삭제에 성공하였습니다.", memberId, postId);

        return postMapper.entityToDeletePostResponse(post);
    }
}
