package com.neil.medical.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by nhu on 4/23/2017.
 */
@Service
public class RefuseIntake {

    public static final String MISSING_INTAKE_COLLECTION = "missing_intake";
    @Autowired
    private MongoTemplate template;

    @Autowired
    private WrappedMongoTemplate wrappedMongoTemplate;

    public List<JSONObject> getAllRefusingRecord() {
        return wrappedMongoTemplate.query(MISSING_INTAKE_COLLECTION, new JSONObject());
    }

    public void updateCheckedRecords(JSONArray records) {
        for (Object r : records) {
            JSONObject record = new JSONObject((Map) r);
            template.getCollection(MISSING_INTAKE_COLLECTION)
                    .update(
                            new BasicDBObject("code", record.getString("code"))
                                    .append("need", record.getString("need"))
                                    .append("date", record.getString("date")),
                            new BasicDBObject("$set", new BasicDBObject("checked", record.getBooleanValue("checked")))
                            , false, true);
        }
    }
}
