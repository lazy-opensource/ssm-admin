package test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dell on 2017/3/10.
 */
public class LoggerTests extends BaseTest {

    private Logger logger = LoggerFactory.getLogger("innovate_web_log");

    @Test
    public void test(){
        logger.info("info");
        logger.debug("debug");
        logger.error("error");
    }
}
