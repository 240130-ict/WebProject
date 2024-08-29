package com.mysite.sbb.template;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Integer> {
    Template findByTemplateId(String templateId);
    Template findByTemplateName(String name);
    Page<Template> findAll(Pageable pageable);
    Page<Template> findAll(Specification<Template> spec, Pageable pageable);

}
