package com.mysite.sbb.template;

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
public class TemplateService {

    private final TemplateRepository templateRepository;

    public Page<Template> getList(int page, String kw, String category){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Template> spec = search(kw, category);
        return this.templateRepository.findAll(spec,pageable);
    }
    public List<Template> getList() {
        return this.templateRepository.findAll();
    }

    public Template getTemplate(int id) {
        Optional<Template> template = this.templateRepository.findById(id);
        if(template.isPresent()) {
            return template.get();
        }else {
            throw new DataNotFoundException("template not found");
        }
    }

    public void create(String templateName, String templateId, SiteUser user, String templateSubject) {
        Template t = new Template();
        t.setTemplateId(templateId);
        t.setTemplateName(templateName);
        t.setCreateDate(LocalDateTime.now());
        t.setAuthor(user);
        t.setTemplateSubject(templateSubject);
        this.templateRepository.save(t);
    }

    private Specification<Template> search(String kw, String category) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Template> t, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Template, SiteUser> u1 = t.join("author", JoinType.LEFT);
                switch (category.toLowerCase()) {
                    case "templateName":
                        return cb.like(t.get("templateName"), "%" + kw + "%"); // 제목으로 검색

                    case "subject":
                        return cb.like(t.get("templateSubject"), "%" + kw + "%"); // 과목으로 검색

                    default:
                        // 기본적으로 모든 필드에 대해 검색
                        return cb.or(
                                cb.like(t.get("templateName"), "%" + kw + "%"),
                                cb.like(t.get("templateSubject"), "%" + kw + "%")
                        );
                }

            }
        };
    }

}
