package com.neil.fpdatabase.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.neil.fpdatabase.fingercore.FingerPrintCache;
import com.neil.fpdatabase.fingercore.persistent.MongoFingerPrintPersistent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhu on 4/21/2017.
 */
@Service
public class ExistingFingerPrints {

    @Autowired
    private MongoTemplate template;

    @Autowired
    private FingerPrintCache cache;

    public List<JSONObject> getRegisteredIdentities() {
        List<JSONObject> registeredIdentities = new ArrayList<>();
        DBCursor cursor = template.getCollection(MongoFingerPrintPersistent.COLLECTION_NAME).find();
        while (cursor.hasNext()) {
            DBObject identity = cursor.next();
            JSONObject idJSON = new JSONObject();
            idJSON.put("code", identity.get("code"));
            idJSON.put("identity", identity.get("identity"));
            registeredIdentities.add(idJSON);
        }
        return registeredIdentities;
    }

    public void deleteRegisteredIdentities(String code) {
        cache.deleteRegisteredFingerPrint(code);
        template.getCollection(MongoFingerPrintPersistent.COLLECTION_NAME)
                .remove(new BasicDBObject("code", code));
    }

}
