package com.tomaszwasilonek.vaults.ws.shared;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UtilsTest {
	
	@Autowired
	Utils utils;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGenerateUserId() {
		String userId = utils.generateUserId(30);
		String userId2 = utils.generateUserId(30);
		
		assertNotNull(userId);
		assertNotNull(userId2);
		
		assertTrue(userId.length() == 30);
		assertTrue(!userId.equalsIgnoreCase(userId2));
	}

}
