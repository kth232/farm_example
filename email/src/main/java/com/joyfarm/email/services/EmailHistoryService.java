package com.joyfarm.email.services;

import com.joyfarm.email.entities.EmailHistory;
import com.joyfarm.email.repositories.EmailHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailHistoryService {

    private final EmailHistoryRepository repository;

    public void save(EmailHistory history) {
        repository.saveAndFlush(history);
    }


}