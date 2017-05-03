package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.neil.medical.pojo.Medical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

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
    private WrappedMongoTemplate<JSONObject> template;

    @Autowired
    private MongoTemplate rawTemplate;

    @Autowired
    private MedicalInfo medicalInfo;

    public void inmateConfirmMedicalIntake(String code, List<JSONObject> medicals) {
        template.save(INTAKE_RECORD_COLLECTION, new JSONObject().fluentPut("code", code)
                .fluentPut("timestamp", new Date().getTime())
                .fluentPut("medicals", medicals)
                .fluentPut("checked", false));
        //reduce medical num
        List<Medical> usedMedical = new ArrayList<>();
        for (JSONObject data : medicals) {
            usedMedical.add(new Medical(data.getString("medical"), (0 - data.getDouble("amount"))));
        }
        medicalInfo.insertOrUpdateMedicalInfo(usedMedical);
    }

    public List<JSONObject> getPrisonIntakeRecord(String code, String timespan) {
        DBObject queryCondition = new BasicDBObject();
        if (!code.equalsIgnoreCase("all")) {
            queryCondition.put("code", code);
        }
        String[] times = timespan.split(":");
        DBObject timespanRange = new BasicDBObject().append("$gte", Long.parseLong(times[0]));
        if (times.length > 1) {
            timespanRange.put("$lte", Long.parseLong(times[1]));
        }
        queryCondition.put("timestamp", timespanRange);
        return template.getListFromCursor(rawTemplate.getCollection(INTAKE_RECORD_COLLECTION).find(queryCondition));
    }
}
