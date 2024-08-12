package com.joyfarm.config.services;

import com.joyfarm.config.entities.Configs;
import com.joyfarm.config.repositories.ConfigsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigDeleteService {
    private final ConfigsRepository repository;

    public void delete(String code) {
        Configs config = repository.findById(code).orElse(null);
        if (config == null) {
            return;
        }

        repository.delete(config);
        repository.flush();
    }
}
