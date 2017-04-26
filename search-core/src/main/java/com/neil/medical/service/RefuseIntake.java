package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhu on 4/23/2017.
 */
@Service
public class RefuseIntake {

    public static final String MISSING_INTAKE_COLLECTION = "missing_intake";
    @Autowired
    private MongoTemplate template;

    public List<JSONObject> getAllRefusingRecord() {
        DBCursor cursor = template.getCollection(MISSING_INTAKE_COLLECTION)
                .find();
        List<JSONObject> records = new ArrayList<>();
        while (cursor.hasNext()) {
            records.add(new JSONObject((BasicDBObject) cursor.next()));
        }
        return records;
    }

    public void updateCheckedRecords(List<JSONObject> records) {
        for (JSONObject record : records) {
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
