package test.soa.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.lzy.innovate.dubbo.system.ISysMenuServiceSoa;
import com.lzy.innovate.entity.SysMenu;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.BaseTest;

import java.util.List;

/**
 * Created by lzy on 2017/3/9.
 */
public class SysMenuServiceSoaTests extends BaseTest{

    @Autowired
    private ISysMenuServiceSoa iSysMenuServiceSoa;

    @Test
    public void test1() {

        List<SysMenu> list = iSysMenuServiceSoa.findMenusByUserId("1");
        System.out.println(list.size());
        System.out.println(list);
    }

    @Test
    public void test2(){

        Page<SysMenu> page = new Page();
        page.setSize(3);
        page.setCurrent(2);

        iSysMenuServiceSoa.selectPage(page ,null);
        System.out.println(page.getSize());
        System.out.println(page.getRecords());
    }

    @Test
    public void test3(){

        List<SysMenu> list = iSysMenuServiceSoa.findOneMenusByUserId("1");

        System.out.println(list.size());
    }

    @Test
    public void test4(){

        List<SysMenu> list = iSysMenuServiceSoa.findChildMenusByParentId("1" , "1");
        System.out.println(list.size());
    }
}
