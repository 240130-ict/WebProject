package com.mysite.sbb.QnA.Question;


import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.QnA.Answer.answer;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class questionService {

    public Page<question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
    }

    private final questionRepository questionRepository;

    public List<question> getList() {
        return questionRepository.findAll();
    }

    public question getQuestion(Integer id) {
        Optional<question> Question = this.questionRepository.findById(id);
        if (Question.isPresent()) {
            return Question.get();

        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, SiteUser user) {
        question Question = new question();
        Question.setSubject(subject);
        Question.setContent(content);
        Question.setCreateDate(LocalDateTime.now());
        Question.setAuthor(user);
        this.questionRepository.save(Question);
    }

    public void modify(question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(question question) {
        this.questionRepository.delete(question);
    }

    private Specification<question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<question, answer> a = q.join("answerlist", JoinType.LEFT);
                Join<answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }
}
