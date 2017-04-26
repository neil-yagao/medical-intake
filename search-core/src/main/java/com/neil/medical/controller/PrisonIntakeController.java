package com.neil.medical.controller;

import com.alibaba.fastjson.JSONObject;
import com.neil.medical.service.RefuseIntake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by nhu on 4/23/2017.
 */
@Controller
public class PrisonIntakeController {


    @Autowired
    private RefuseIntake refuseIntake;

    @RequestMapping(value = "intake/miss", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateCheckedRecords(List<JSONObject> records) {
        refuseIntake.updateCheckedRecords(records);
        return new JSONObject();
    }


    @RequestMapping(value = "intake/miss")
    @ResponseBody
    public List<JSONObject> findMissIntakeRecording() {
        return refuseIntake.getAllRefusingRecord();
    }
}
