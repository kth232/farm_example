package com.joyfarm.farmstival;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@RefreshScope //@RefreshScope는 설정 정보가 바뀌면 다시 불러올 수 있도록 도와줌
@SpringBootApplication
public class FarmstivalApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmstivalApplication.class, args);
	}

}
