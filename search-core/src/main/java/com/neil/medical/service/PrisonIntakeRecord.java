package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.neil.medical.pojo.Medical;
import com.neil.medical.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nhu on 5/2/2017.
 */
@Service
public class PrisonIntakeRecord {
    public static final String INTAKE_RECORD_COLLECTION = "intake_records";

    @Autowired
    private WrappedMongoTemplate template;

    @Autowired
    private MongoTemplate rawTemplate;

    @Autowired
    private MedicalInventory medicalInventory;

    public void inmateConfirmMedicalIntake(String code, List<JSONObject> medicals) {
        template.save(INTAKE_RECORD_COLLECTION, new JSONObject().fluentPut("code", code)
                .fluentPut("timestamp", new Date().getTime())
                .fluentPut("date", TimeUtil.getCurrentDate())
                .fluentPut("medicals", medicals)
                .fluentPut("checked", false));
        //reduce medical num
        List<Medical> usedMedical = new ArrayList<>();
        for (JSONObject data : medicals) {
            usedMedical.add(new Medical(data.getString("medical"), (0 - data.getDouble("amount"))));
        }
        medicalInventory.insertOrUpdateMedicalInfo(usedMedical, code);
    }

    public List<JSONObject> getPrisonIntakeRecord(String code, String timespan) {
        DBObject queryCondition = new BasicDBObject();
        if (!code.equalsIgnoreCase("all")) {
            queryCondition.put("code", code);
        }
        JSONObject timespanRange = TimeUtil.parseTimespan(timespan);
        queryCondition.put("timestamp", new BasicDBObject(timespanRange));
        return template.getListFromCursor(rawTemplate.getCollection(INTAKE_RECORD_COLLECTION).find(queryCondition));
    }


}
