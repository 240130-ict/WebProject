package com.mysite.sbb;

import com.mysite.sbb.template.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private TemplateRepository templateRepository;
	private TemplateController templateController;
	@Test
	void testJpa() {
		Template t1 = new Template();





	}

}
