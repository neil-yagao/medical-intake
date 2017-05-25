package com.neil.fpdatabase.controller;

import com.alibaba.fastjson.JSONObject;
import com.neil.fpdatabase.service.ExistingFingerPrints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by nhu on 4/21/2017.
 */
@Controller
public class ExistingFingerController {

    @Autowired
    private ExistingFingerPrints existingFingerPrints;

    @RequestMapping("existing")
    @ResponseBody
    public List<JSONObject> getRegisteredIdentities() {
        return existingFingerPrints.getRegisteredIdentities();
    }

    @RequestMapping(value = "identity/{code}", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject deleteExistingIdentity(@PathVariable String code) {
        existingFingerPrints.deleteRegisteredIdentities(code);
        return new JSONObject();
    }
}
