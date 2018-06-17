package test.soa.system;

import com.lzy.innovate.dubbo.system.ISysUserServiceSoa;
import com.lzy.innovate.entity.SysUser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import test.BaseTest;

/**
 * Created by lzy on 2017/3/8.
 */
public class SysUserServiceSoaTests extends BaseTest {

    @Autowired
    private ISysUserServiceSoa iSysUserServiceSoa;

    private static Logger logger = LoggerFactory.getLogger(SysUserServiceSoaTests.class);


    @Test
    public void test1(){

        logger.debug("init logger --------------------------------------------------------------");
        SysUser sysUser = iSysUserServiceSoa.selectByNameAndPwd("admin" , "123");
        System.out.println(sysUser);
    }
}
