package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.neil.medical.pojo.PrisonMedicalInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nhu on 4/9/2017.
 */
@Service
public class PrisonMedicalService {

    public static final String INMATE_REQUIRED_MEDICAL_COLLECTION = "inmate-medical";

    @Autowired
    private WrappedMongoTemplate<PrisonMedicalInfo> wrappedTemplate;

    @Autowired
    private MongoTemplate template;


    public List<JSONObject> getPrisonMedicalInfo(String code) {
        JSONObject queryCondition = new JSONObject();
        if (!code.equalsIgnoreCase("all")) {
            queryCondition.put("code", code);
        }
        return wrappedTemplate.query(INMATE_REQUIRED_MEDICAL_COLLECTION, queryCondition);
    }

    public void insertInmateMedicalInfo(List<PrisonMedicalInfo> prisonMedicalInfos) {
        DBCollection inmateMedical = template.getCollection(INMATE_REQUIRED_MEDICAL_COLLECTION);
        inmateMedical.remove(new BasicDBObject("code", prisonMedicalInfos.get(0).getCode()));
        for (PrisonMedicalInfo pmi : prisonMedicalInfos) {
            JSONObject mapPmi = (JSONObject) JSONObject.toJSON(pmi);
            inmateMedical.update(new BasicDBObject("code", pmi.getCode())
                            .append("medical", pmi.getMedical())
                            .append("time", pmi.getTime()),
                    new BasicDBObject(mapPmi), true, false);

        }
    }


}
