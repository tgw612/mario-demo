package com.mario.web.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MySession
 *
 * @author qiujingwang
 * @version 1.0
 * @date 2019/01/08 下午3:46
 */
@RestController
@RequestMapping(value = "/users")     // 通过这里配置使下面的映射都在/users下，可去除
public class TestSwagger2Controller {

}
