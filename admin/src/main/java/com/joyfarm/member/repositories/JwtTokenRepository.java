package com.joyfarm.member.repositories;

import com.joyfarm.member.entities.JwtToken;
import org.springframework.data.repository.CrudRepository;

public interface JwtTokenRepository extends CrudRepository<JwtToken, String> {
}
