package com.joyfarm.member.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joyfarm.member.constants.Authority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@IdClass(AuthoritiesId.class)
@NoArgsConstructor
@AllArgsConstructor
public class Authorities {
    
    @Id
    @JsonIgnore //순환참조 방지
    @ManyToOne(fetch= FetchType.LAZY)
    private Member member;

    @Id
    @Column(length=20)
    @Enumerated(EnumType.STRING)
    private Authority authority;
}