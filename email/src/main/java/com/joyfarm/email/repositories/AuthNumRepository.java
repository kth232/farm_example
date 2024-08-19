package com.joyfarm.email.repositories;

import com.joyfarm.email.entities.AuthNum;
import org.springframework.data.repository.CrudRepository;

public interface AuthNumRepository extends CrudRepository<AuthNum, String> {
}
