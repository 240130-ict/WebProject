package com.mysite.sbb.Community.communityQuestion;

import com.mysite.sbb.Community.communityAnswer.CommunityAnswer;
import com.mysite.sbb.DataNotFoundException;
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
public class CommunityQuestionService {

    public Page<CommunityQuestion> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<CommunityQuestion> spec = search(kw);
        return this.communityQuestionRepository.findAll(spec, pageable);
    }

    private final CommunityQuestionRepository communityQuestionRepository;

    public List<CommunityQuestion> getList() {
        return communityQuestionRepository.findAll();
    }

    public CommunityQuestion getCommunityQuestion(Integer id) {
        Optional<CommunityQuestion> communityQuestion = this.communityQuestionRepository.findById(id);
        if (communityQuestion.isPresent()) {
            return communityQuestion.get();

        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, SiteUser user) {
        CommunityQuestion communityQuestion = new CommunityQuestion();
        communityQuestion.setSubject(subject);
        communityQuestion.setContent(content);
        communityQuestion.setCreateDate(LocalDateTime.now());
        communityQuestion.setAuthor(user);
        this.communityQuestionRepository.save(communityQuestion);
    }

    public void modify(CommunityQuestion question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.communityQuestionRepository.save(question);
    }

    public void delete(CommunityQuestion question) {
        this.communityQuestionRepository.delete(question);
    }

    private Specification<CommunityQuestion> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<CommunityQuestion> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<CommunityQuestion, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<CommunityQuestion, CommunityAnswer> a = q.join("communityAnswerlist", JoinType.LEFT);
                Join<CommunityAnswer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }
}
