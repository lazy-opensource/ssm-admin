package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext_test.xml" })
public class BaseTest {

	@Test
	public void testInit() {
	}

	@Before
	public void begin(){}

	@After
	public void after(){}
}
