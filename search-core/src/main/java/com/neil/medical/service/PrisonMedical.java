package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.neil.medical.pojo.PrisonMedicalInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nhu on 4/9/2017.
 */
@Service
public class PrisonMedical {

    public static final String INMATE_REQUIRED_MEDICAL_COLLECTION = "inmate-medical";

    @Autowired
    private WrappedMongoTemplate wrappedTemplate;

    @Autowired
    private MongoTemplate template;


    public List<PrisonMedicalInfo> getPrisonMedicalInfo(String code) {
        Query condition = new Query();
        if(!code.equalsIgnoreCase("all")){
            condition.addCriteria(new Criteria("code").is(code));
        }
        return template.find(condition,PrisonMedicalInfo.class,INMATE_REQUIRED_MEDICAL_COLLECTION);
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
