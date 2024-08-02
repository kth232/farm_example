package com.joyfarm.member.repositories;

import com.joyfarm.member.entities.Authorities;
import com.joyfarm.member.entities.AuthoritiesId;
import com.joyfarm.member.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authorities, AuthoritiesId>, QuerydslPredicateExecutor<Authorities> {

    List<Authorities> findByMember(Member member);
}