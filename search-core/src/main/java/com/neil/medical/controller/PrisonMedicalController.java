package com.neil.medical.controller;

import com.alibaba.fastjson.JSONObject;
import com.neil.medical.pojo.PrisonMedicalInfo;
import com.neil.medical.service.PrisonMedical;
import com.neil.medical.service.PrisonPrescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by nhu on 4/9/2017.
 */
@Controller
public class PrisonMedicalController {

    @Autowired
    private PrisonMedical prisonMedical;

    @Autowired
    private PrisonPrescription prescription;


    @RequestMapping(value = "inmate/medical/{changeBy}", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject inmateMedicalInfo(@RequestBody List<PrisonMedicalInfo> prisonMedical, @PathVariable String changeBy) {
        this.prescription.recordChange(prisonMedical, changeBy);
        return new JSONObject().fluentPut("success", true);
    }

    @RequestMapping(value = "inmate/medical/{code}", method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> getPrisonMedicalInfo(@PathVariable String code) {
        return prisonMedical.getPrisonMedicalInfo(code);
    }



}
