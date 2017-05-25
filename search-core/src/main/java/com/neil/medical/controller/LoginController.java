package com.neil.medical.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Created by nhu on 3/31/2017.
 */
@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject login(@RequestBody JSONObject login) {
        LOGGER.info("request body:" + login);
        if (!login.getString("key").equals("13951928868"))
            return new JSONObject().fluentPut("identity", "prison");
        return new JSONObject().fluentPut("identity", "police");
    }
}
