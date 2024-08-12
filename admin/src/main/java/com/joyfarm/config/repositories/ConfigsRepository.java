package com.joyfarm.config.repositories;

import com.joyfarm.config.entities.Configs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigsRepository extends JpaRepository<Configs, String> {
}
