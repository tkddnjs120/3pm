package com.ssafy.sns.service;

import com.ssafy.sns.dto.newsfeed.*;

public interface FeedService {

    // 내가 쓴 글 불러오기
    public FeedListResponseDto readMyList(Long id, int num);

    // 한페이지 불러오기
    public FeedListResponseDto readList(int num);

    // 피드 글 불러오기
    public FeedResponseDto read(Long id);

    // 피드 글쓰기
    public Long write(FeedRequestDto feedRequestDto);

    // 피드 글삭제
    public void delete(Long id);

    // 피드 글수정
    public Long modify(Long id, FeedRequestDto feedRequestDto);
}
