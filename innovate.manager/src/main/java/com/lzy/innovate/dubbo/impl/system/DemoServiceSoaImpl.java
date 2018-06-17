package com.lzy.innovate.dubbo.impl.system;


import com.lzy.innovate.dubbo.base.impl.BaseServiceImpl;
import com.lzy.innovate.dubbo.system.IDemoServiceSoa;
import com.lzy.innovate.entity.Demo;
import com.lzy.innovate.service.IDemoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author laizy
 * @since 2017-02-28
 */
@Service
public class DemoServiceSoaImpl extends BaseServiceImpl<IDemoService, Demo> implements IDemoServiceSoa {
	
}
