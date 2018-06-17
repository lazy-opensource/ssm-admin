package com.lzy.innovate.controller.common;

import com.lzy.innovate.controller.enums.SysUserStatusEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by lzy on 2017/3/17.
 * 前台获得枚举值控制器，支持异步
 */
@Controller
@RequestMapping("/enum")
public class GetEnumToPageController {

    /**
     * 获得用户状态枚举值
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findUserStatusEnum" , method = {RequestMethod.POST})
    public Map<String, String> findUserStatusEnum(){
        return SysUserStatusEnum.convertToMap();
    }
}
