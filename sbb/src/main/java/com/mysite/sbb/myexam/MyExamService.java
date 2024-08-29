package com.mysite.sbb.myexam;

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
public class MyExamService {
    private final MyExamRepository myExamRepository;

    public Page<MyExam> getList(int page, String kw,String category){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<MyExam> spec = search(kw, category);
        return this.myExamRepository.findAll(spec,pageable);
    }

    public  List<MyExam> getList(){return this.myExamRepository.findAll();}

    public MyExam getMyExam(int id) {
        Optional<MyExam> myexam = this.myExamRepository.findById(id);
        if(myexam.isPresent()) {
            return myexam.get();
        }else {
            throw new DataNotFoundException("exam not found");
        }
    }

    public void create(String subject,String title, String documentId,String templateId, SiteUser user,String tempowner, String status) {
        MyExam m = new MyExam();
        m.setSubject(subject);
        m.setTitle(title);
        m.setDocumentId(documentId);
        m.setTemplateId(templateId);
        m.setCreateDate(LocalDateTime.now());
        m.setAuthor(user);
        m.setTempOwner(tempowner);
        m.setStatus(status);
        this.myExamRepository.save(m);
    }

    public void modify(MyExam myExam, String status, String point) {
        myExam.setStatus(status);
        myExam.setPoint(point);
        this.myExamRepository.save(myExam);
    }

    private Specification<MyExam> search(String kw, String category) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<MyExam> m, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<MyExam, SiteUser> u1 = m.join("author", JoinType.LEFT);
                switch (category.toLowerCase()) {
                    case "title":
                        return cb.like(m.get("title"), "%" + kw + "%"); // 제목으로 검색

                    case "documentid":
                        return cb.like(m.get("documentId"), "%" + kw + "%"); // ID로 검색

                    case "tempowner":
                        return cb.like(m.get("tempOwner"), "%" + kw + "%"); // 템플릿 작성자로 검색

                    case "username":
                        return cb.like(u1.get("username"), "%" + kw + "%"); // 답변 작성자로 검색

                    default:
                        // 기본적으로 모든 필드에 대해 검색
                        return cb.or(
                                cb.like(m.get("title"), "%" + kw + "%"),
                                cb.like(m.get("documentId"), "%" + kw + "%"),
                                cb.like(m.get("tempOwner"), "%" + kw + "%"),
                                cb.like(u1.get("username"), "%" + kw + "%")
                        );
                }
            }
        };
    }

}
