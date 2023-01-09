package com.mehdilagdimi.myrh;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;


@SpringBootTest
@PropertySource("classpath:application-${spring.profiles.active:dev}.properties")
class MyrhApplicationTests {

	@Test
	void contextLoads() {
	}

}
