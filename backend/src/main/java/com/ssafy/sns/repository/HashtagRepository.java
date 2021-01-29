package com.ssafy.sns.repository;

import com.ssafy.sns.domain.hashtag.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    List<Hashtag> findAllByTagNameContains(String tag);

}
