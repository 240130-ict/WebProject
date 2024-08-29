package com.mysite.sbb.Community.communityQuestion;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityQuestionRepository extends JpaRepository<CommunityQuestion, Integer> {
    CommunityQuestion findBySubject(String subject);
    CommunityQuestion findBySubjectAndContent(String subject, String content);
    List<CommunityQuestion> findBySubjectLike(String subject);
    Page<CommunityQuestion> findAll(Pageable pageable);
    Page<CommunityQuestion> findAll(Specification<CommunityQuestion> spec, Pageable pageable);
}

