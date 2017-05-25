package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhu on 5/2/2017.
 */
@Component
public class WrappedMongoTemplate {

    @Autowired
    private MongoTemplate template;


    public static List<JSONObject> getListFromCursor(DBCursor cursor) {
        List<JSONObject> resultList = new ArrayList<>();
        while (cursor.hasNext()) {
            resultList.add(new JSONObject(cursor.next().toMap()));
        }
        return resultList;
    }

    public List<JSONObject> query(String collection, JSONObject condition) {
        DBCollection targetingCollection = template.getCollection(collection);
        DBCursor cursor = targetingCollection.find(new BasicDBObject(condition));
        return getListFromCursor(cursor);
    }

    public void save(String collection, JSONObject data) {
        DBCollection targetingCollection = template.getCollection(collection);
        targetingCollection.save(new BasicDBObject(data));
    }


}
