package com.mysite.sbb.template;

import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String templateName;

    @Column(length = 100)
    private String templateId;

    private LocalDateTime createDate;

    private String templateSubject;

    @ManyToOne
    private SiteUser author;
}
