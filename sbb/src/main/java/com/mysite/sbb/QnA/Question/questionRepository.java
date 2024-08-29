package com.mysite.sbb.QnA.Question;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface questionRepository extends JpaRepository<question, Integer> {
    question findBySubject(String subject);
    question findBySubjectAndContent(String subject, String content);
    List<question> findBySubjectLike(String subject);
    Page<question> findAll(Pageable pageable);
    Page<question> findAll(Specification<question> spec, Pageable pageable);
}

