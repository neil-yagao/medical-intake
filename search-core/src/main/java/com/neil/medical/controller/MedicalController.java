package com.neil.medical.controller;

import com.alibaba.fastjson.JSONObject;
import com.neil.medical.pojo.MedicalList;
import com.neil.medical.service.MedicalInventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * Created by nhu on 3/31/2017.
 */
@RestController
public class MedicalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MedicalController.class);
    @Autowired
    private MedicalInventory medicalInventory;

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index.html");
    }


    @RequestMapping(value = "medicals", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject medicalInbound(@RequestBody MedicalList medicals) {
        medicalInventory.insertOrUpdateMedicalInfo(medicals,"");
        return new JSONObject().fluentPut("success", true);
    }

    @RequestMapping(value = "medicals", method = RequestMethod.GET)
    @ResponseBody
    public MedicalList medicals() {
        return medicalInventory.getMedicals();
    }

    @RequestMapping(value = "medicals/inbound/{code}/{timespan}")
    @ResponseBody
    public List<JSONObject> inboundRecords(@PathVariable String code, @PathVariable String timespan){
        return medicalInventory.getMedicalInboundRecord(code, timespan);
    }

    @RequestMapping(value = "medicals/outbound/{code}/{timespan}")
    @ResponseBody
    public List<JSONObject> outboundRecords(@PathVariable String code, @PathVariable String timespan){
        return medicalInventory.getMedicalOutboundRecord(code, timespan);
    }

}
