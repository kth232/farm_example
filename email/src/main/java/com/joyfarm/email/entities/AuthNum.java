package com.joyfarm.email.entities;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash(value="authNum", timeToLive = 180)
public class AuthNum {
    @Id
    private String uid;

    private Integer authNum;
}