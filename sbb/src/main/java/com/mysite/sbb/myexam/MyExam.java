package com.mysite.sbb.myexam;

import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

@Entity
public class MyExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String title;

    @Column(length = 100)
    private String documentId;

    @Column(length = 100)
    private String templateId;

    private LocalDateTime createDate;

    private String tempOwner;

    private String status;

    private String point;

    private String subject;

    @ManyToOne
    private SiteUser author;

}
