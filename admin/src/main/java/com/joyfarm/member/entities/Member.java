package com.joyfarm.member.entities;

import com.joyfarm.file.entities.FileInfo;
import com.joyfarm.global.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity //JPA에서만 사용함, persistence 패키지에 포함됨, redis와는 다른 기술
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    //데이터만 담아주는 데이터 클래스
    @Id
    @GeneratedValue
    private Long seq;

    @Column(length=45, nullable = false)
    private String gid;

    @Column(length=65, unique = true, nullable = false)
    private String email;

    @Column(length=65, nullable = false)
    private String password;

    @Column(length=40, nullable = false)
    private String userName;

    @Column(length=15, nullable = false)
    private String mobile;

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<Authorities> authorities;

    @Transient
    private FileInfo profileImage; //2차 가공형태로 넣어줌
}
