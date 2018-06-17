package com.lzy.innovate.controller.system;

import ch.qos.logback.classic.Logger;
import com.lzy.innovate.dubbo.system.IDemoServiceSoa;
import com.lzy.innovate.entity.Demo;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lzy on 2017/3/4.
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    private static Logger logger = (Logger) LoggerFactory.getLogger("innovate_web");
    private static Logger logger2 = (Logger) LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private IDemoServiceSoa iDemoServiceSoa;

    @RequestMapping(value = "/hello" , method = RequestMethod.GET)
    @ResponseBody
    public String testAdd(){

        String uuid = UUID.randomUUID().toString();
        logger.info(uuid);
        Demo demo = new Demo();
        demo.setUuid(uuid);
        demo.setName("zhangsan3");

        iDemoServiceSoa.insert(demo);
        logger.info("test logger info");
        logger.debug("test logger debug");
        logger.error("test logger error");

        logger2.info("test logger2 info");
        logger2.debug("test logger2 debug");
        logger2.error("test logger2 error");
        return "Test Sussess！";
    }

    @RequestMapping(value = "/demo" , method = RequestMethod.GET)
    public void testHttp(){


        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        Template t = velocityEngine.getTemplate("demo.html");
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("name" , "lzy");

        Map<String,String> list = new HashMap<String,String>();
        list.put("hao123" , "https://www.hao123.com/");
        list.put("百度" , "http://baidu.com");

        velocityContext.put("list" , list);

        StringWriter stringWriter = new StringWriter();
        t.merge(velocityContext, stringWriter);

        try {
            stringWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/demo2")
    public String testHttp(Model model){


        Map<String,String> list = new HashMap<String,String>();
        list.put("hao123" , "https://www.hao123.com/");
        list.put("百度" , "http://baidu.com");

        model.addAttribute("list" , list);

        return "demo";

    }


}
