package com.mysite.sbb.notices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public List<Notice> findAll() {
        return noticeRepository.findAll();
    }

    public Notice findById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid notice ID: " + id));
    }

    @Transactional
    public void save(Notice notice) {
        if (notice.getId() == null) {
            notice.setCreatedDate(LocalDateTime.now());
        } else {
            notice.setUpdatedDate(LocalDateTime.now());
        }
        noticeRepository.save(notice);
    }

    @Transactional
    public void deleteById(Long id) {
        noticeRepository.deleteById(id);
    }
}

