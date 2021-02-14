package com.ssafy.sns.controller;

import com.ssafy.sns.domain.hashtag.Hashtag;
import com.ssafy.sns.dto.search.SearchHashtagDto;
import com.ssafy.sns.dto.search.SearchResponseDto;
import com.ssafy.sns.dto.user.SimpleUserDto;
import com.ssafy.sns.jwt.JwtService;
import com.ssafy.sns.service.SearchServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Search 관련 Controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
public class SearchController {
    public static final Logger logger = LoggerFactory.getLogger(IndoorController.class);

    private final SearchServiceImpl searchService;
    private final JwtService jwtService;

    // return 형태 List<SearchResponseDto> -> (tagname, List<IndoorResponseDto>)
    @ApiOperation("키워드 검색을 통해 게시물 받아오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "검색명", required = true)
    })
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> searchAll(@RequestParam("keyword") String keyword,
                                                         HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            map.put("feedList", searchFeed(keyword, request));
            map.put("userList", searchUser(keyword));
        } catch (Exception e) {
            logger.warn("전체 검색 에러 : {}", e.getMessage());
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(map, status);

    }

    @GetMapping("/feed")
    public ResponseEntity<List<SearchResponseDto>> searchFeed(@RequestParam("keyword") String keyword,
                                                              HttpServletRequest request) {
        List<Hashtag> hashtagList = searchService.searchHashtags(keyword);
        List<SearchResponseDto> searchResponseDto = new ArrayList<>(); //  태그명, 태그게시물 List
        for (Hashtag h : hashtagList) {
            searchResponseDto.add(new SearchResponseDto(h.getTagName(), searchService.searchFeeds(
                    jwtService.findId(request.getHeader("Authorization")), h)));
        }
        return new ResponseEntity<>(searchResponseDto, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<SimpleUserDto>> searchUser(@RequestParam("keyword") String keyword){
        List<SimpleUserDto> userDtoList = searchService.searchUsers(keyword);
        for (SimpleUserDto simpleUserDto : userDtoList) {
            System.out.println("simpleUserDto.getNickname() = " + simpleUserDto.getNickname());
        }
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

//    @GetMapping("/group")
//    public ResponseEntity<List<SearchResponseDto>> searchGroup(@RequestParam("keyword") String keyword){
//        return null;
//    }

    @ApiOperation("태그 자동 완성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hashtag", value = "해시태그 명", required = true),
    })
    @GetMapping("/auto/tag")
    public ResponseEntity<List<SearchHashtagDto>> getAutoTag(@RequestParam("hashtag") String hashtag) {
        HttpStatus status = HttpStatus.ACCEPTED;
        List<SearchHashtagDto> result = null;
        try {
            result = searchService.hashtagAutocomplete(hashtag);
            logger.info("getAutoTag - 태그 자동완성 : {}", hashtag);
            status = HttpStatus.OK;
        } catch (Exception e) {
            logger.warn("getAutoTag - 태그 자동완성 에러 : {}", e.getMessage());
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(result, status);
    }

    @ApiOperation("유저 태그 자동 완성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "유저 닉네임", required = true),
    })
    @GetMapping("/auto/user")
    public ResponseEntity<List<SimpleUserDto>> getAutoUser(@RequestParam("nickname") String nickname,
                                                           HttpServletRequest request) {
        HttpStatus status = HttpStatus.ACCEPTED;
        List<SimpleUserDto> result = null;
        try {
            result = searchService.userAutocomplete(jwtService.findId(request.getHeader("Authorization")), nickname);
            logger.info("getAutoUser - 유저 자동완성 : {}", nickname);
            status = HttpStatus.OK;
        } catch (Exception e) {
            logger.warn("getAutoUser - 유저 자동완성 에러 : {}", e.getMessage());
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(result, status);
    }
}
