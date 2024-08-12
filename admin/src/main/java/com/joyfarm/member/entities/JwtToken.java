package com.joyfarm.member.entities;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash(timeToLive = 3600L) //1시간 유효
public class JwtToken implements Serializable {

    @Id
    private String sessionId; // 세션 ID

    private String token; // JWT 토큰
}
