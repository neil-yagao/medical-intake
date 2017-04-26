package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.neil.medical.pojo.PrisonMedicalInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nhu on 4/9/2017.
 */
@Service
public class PrisonMedicalService {

    public static final String INTAKE_RECORD_COLLECTION = "intake_records";
    @Autowired
    private MongoTemplate template;

    public List<PrisonMedicalInfo> getPrisonMedicalInfo(String code) {
        DBCollection prisonMedicalCollection = template.getCollection("inmate-medical");
        DBObject queryCondition = new BasicDBObject();
        if (!code.equalsIgnoreCase("all")) {
            queryCondition.put("code", code);
        }
        DBCursor cursor = prisonMedicalCollection.find(queryCondition);
        List<PrisonMedicalInfo> medicalInfoList = new ArrayList<>();
        while (cursor.hasNext()) {
            medicalInfoList.add(new JSONObject(cursor.next().toMap()).toJavaObject(PrisonMedicalInfo.class));

        }
        return medicalInfoList;
    }

    public void insertInmateMedicalInfo(List<PrisonMedicalInfo> prisonMedicalInfos) {
        DBCollection inmateMedical = template.getCollection("inmate-medical");
        for (PrisonMedicalInfo pmi : prisonMedicalInfos) {
            JSONObject mapPmi = (JSONObject) JSONObject.toJSON(pmi);
            inmateMedical.update(new BasicDBObject("code", pmi.getCode())
                            .append("medical", pmi.getMedical())
                            .append("time", pmi.getTime()),
                    new BasicDBObject(mapPmi), true, false);

        }
    }

    public void inmateConfirmMedicalIntake(String code) {
        DBCollection intakeRecords = template.getCollection(INTAKE_RECORD_COLLECTION);
        intakeRecords.save(new BasicDBObject("code", code)
                .append("timestamp", new Date().getTime())
                .append("checked", false));
    }
}
