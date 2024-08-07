package com.joyfarm.member.services;

import com.joyfarm.member.MemberInfo;
import com.joyfarm.member.constants.Authority;
import com.joyfarm.member.entities.Authorities;
import com.joyfarm.member.entities.Member;
import com.joyfarm.member.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username));

        List<Authorities> tmp = member.getAuthorities();
        if(tmp == null || tmp.isEmpty()) {
            tmp = List.of(Authorities.builder().member(member).authority(Authority.USER).build());
        }
        List<SimpleGrantedAuthority> authorities = tmp.stream()
                .map(a->new SimpleGrantedAuthority(a.getAuthority().name())).toList();

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }
}