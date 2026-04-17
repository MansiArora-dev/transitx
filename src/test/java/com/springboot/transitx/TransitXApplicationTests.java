package com.springboot.transitx;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainerConfiguration.class)
class TransitXApplicationTests {

	@Test
	void contextLoads() {
	}

}
