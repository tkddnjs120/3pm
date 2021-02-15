package com.ssafy.sns.service;

import com.ssafy.sns.domain.hashtag.Hashtag;
import com.ssafy.sns.dto.newsfeed.FeedResponseDto;
import com.ssafy.sns.dto.search.SearchHashtagDto;
import com.ssafy.sns.dto.search.SearchUserDto;
import com.ssafy.sns.dto.user.SimpleUserDto;

import java.util.List;

public interface SearchService {

    public List<Hashtag> searchHashtags(String keyword);

    public List<FeedResponseDto> searchFeeds(Long userId, Hashtag hash);

    public List<SimpleUserDto> searchUsers(String keyword);

    List<SearchHashtagDto> hashtagAutocomplete(String text);

    List<SearchUserDto> userAutocomplete(Long userId, String text);
}
