package com.mysite.sbb.Community.communityAnswer;

import com.mysite.sbb.Community.communityQuestion.CommunityQuestion;
import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommunityAnswerService {
    private final CommunityAnswerRepository communityAnswerRepository;

    public void create(CommunityQuestion communityQuestion, String content, SiteUser author) {
        CommunityAnswer communityAnswer = new CommunityAnswer();
        communityAnswer.setContent(content);
        communityAnswer.setCreateDate(LocalDateTime.now());
        communityAnswer.setCommunityQuestion(communityQuestion);
        communityAnswer.setCommunityQuestion(communityQuestion);
        communityAnswer.setAuthor(author);
        this.communityAnswerRepository.save(communityAnswer);
    }
    public CommunityAnswer getCommunityAnswer(Integer id) {
        Optional<CommunityAnswer> answer = this.communityAnswerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(CommunityAnswer communityAnswer, String content) {
        communityAnswer.setContent(content);
        communityAnswer.setModifyDate(LocalDateTime.now());
        this.communityAnswerRepository.save(communityAnswer);
    }

    public void delete(CommunityAnswer communityAnswer) {
        this.communityAnswerRepository.delete(communityAnswer);
    }

}
