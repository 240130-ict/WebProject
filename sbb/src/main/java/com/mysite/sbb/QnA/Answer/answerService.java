package com.mysite.sbb.QnA.Answer;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.QnA.Question.question;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class answerService {
    private final answerRepository answerRepository;

    public void create(question Question, String content, SiteUser author) {
        answer answer = new answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(Question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
    }
    public answer getAnswer(Integer id) {
        Optional<answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    public void delete(answer answer) {
        this.answerRepository.delete(answer);
    }

}
