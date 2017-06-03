package com.neil.medical.controller;

import com.alibaba.fastjson.JSONObject;
import com.neil.medical.service.Identities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by nhu on 5/31/2017.
 */
@Controller
public class PrisonController {

    @Autowired
    private Identities identities;

    @RequestMapping("inmates/{code}")
    @ResponseBody
    public List<JSONObject> getPrisonIdentities(@PathVariable String code){
        return identities.getIdentities(code);
    }
}
