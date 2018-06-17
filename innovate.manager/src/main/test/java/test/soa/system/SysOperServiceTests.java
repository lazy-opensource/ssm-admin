package test.soa.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.lzy.innovate.dubbo.system.ISysOperServiceSoa;
import com.lzy.innovate.entity.SysOper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.BaseTest;

import java.util.List;

/**
 * Created by lzy on 2017/3/9.
 */
public class SysOperServiceTests extends BaseTest {

    @Autowired
    private ISysOperServiceSoa iSysOperServiceSoa;

    @Test
    public void test1(){
        Page<String> page = new Page();

        List<SysOper> list = iSysOperServiceSoa.findOpersByUserId("1" , page);
        System.out.println(list.size());
    }
}
