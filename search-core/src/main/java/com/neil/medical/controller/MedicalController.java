package com.neil.medical.controller;

import com.alibaba.fastjson.JSONObject;
import com.neil.medical.pojo.MedicalList;
import com.neil.medical.service.MedicalInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by nhu on 3/31/2017.
 */
@RestController
public class MedicalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicalController.class);
    @Autowired
    private MedicalInfo medicalInfo;

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index.html");
    }


    @RequestMapping(value = "medicals", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject medicalUpdate(@RequestBody MedicalList medicals) {
        medicalInfo.insertOrUpdateMedicalInfo(medicals);
        return new JSONObject().fluentPut("success", true);
    }

    @RequestMapping(value = "medicals", method = RequestMethod.GET)
    @ResponseBody
    public MedicalList medicals() {
        return medicalInfo.getMedicals();
    }


}
