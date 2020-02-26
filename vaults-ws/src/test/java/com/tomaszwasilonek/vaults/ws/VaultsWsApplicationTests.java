package com.tomaszwasilonek.vaults.ws;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-dev.properties")
public class VaultsWsApplicationTests {

	@Test
	public void contextLoads() {
	}

}
