package com.mysite.sbb.template;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateForm {
    @NotEmpty(message = "제목은 필수 항목입니다.")
    @Size(max=200)
    private String templateName;

//    @NotEmpty(message = "내용은 필수 항목입니다.")
    private String templateId;

    private String templateSubject;

}
