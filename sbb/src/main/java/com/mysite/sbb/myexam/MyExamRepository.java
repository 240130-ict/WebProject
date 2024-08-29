package com.mysite.sbb.myexam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MyExamRepository extends JpaRepository<MyExam, Integer> {
    Page<MyExam> findAll(Pageable pageable);
    Page<MyExam> findAll(Specification<MyExam> spec, Pageable pageable);
}
