package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.neil.medical.pojo.Medical;
import com.neil.medical.pojo.MedicalList;
import com.neil.medical.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * Created by nhu on 4/4/2017.
 */
@Service
public class MedicalInventory {

    @Autowired
    private MongoTemplate mongoTemplate;



    /**
     * System will add num if record existed
     * otherwise create new record
     */
    public void insertOrUpdateMedicalInfo(List<Medical> medicalInfo,String code) {
        DBCollection medicals = mongoTemplate.getCollection("medicals");
        for (Medical medical : medicalInfo) {
            medicals.update(new BasicDBObject("name", medical.getName()),
                    new BasicDBObject("$inc", new BasicDBObject("num", medical.getNum())), true, false);
        }
        insertInventoryRecord(medicalInfo, code);
    }

    private void insertInventoryRecord(List<Medical> medicalRecords,String code){
        DBCollection medicals = mongoTemplate.getCollection("medicals-records");
        for (Medical medical : medicalRecords) {
            DBObject record = new BasicDBObject();
            record.put("medical", medical.getName());
            record.put("amount", Math.abs(medical.getNum()));
            record.put("date", TimeUtil.getCurrentDate());
            record.put("timestamp", new Date().getTime());
            if(medical.getNum() < 0){
                record.put("operation", "outbound");
                record.put("prison", code);
            }else{
                record.put("operation", "inbound");
            }
            medicals.save(record);
        }
    }

    public MedicalList getMedicals() {
        DBCollection medicals = mongoTemplate.getCollection("medicals");
        DBCursor cursor = medicals.find();
        MedicalList list = new MedicalList();
        while (cursor.hasNext()) {
            DBObject data = cursor.next();
            Medical medical = new JSONObject(data.toMap()).toJavaObject(Medical.class);
            list.add(medical);
        }
        return list;
    }

    public List<JSONObject> getMedicalInboundRecord(String medical, String timespan){
        return getMedicalRecords(medical,timespan,"inbound");
    }

    public List<JSONObject> getMedicalOutboundRecord(String medical, String timespan){
        return getMedicalRecords(medical,timespan,"outbound");
    }

    private List<JSONObject> getMedicalRecords(String medical, String timespan, String operation){
        DBObject queryCondition = new BasicDBObject();
        if (!medical.equalsIgnoreCase("all")) {
            queryCondition.put("medical", medical);
        }
        JSONObject timespanRange = TimeUtil.parseTimespan(timespan);
        queryCondition.put("operation",operation);
        queryCondition.put("timestamp", new BasicDBObject(timespanRange));
        return WrappedMongoTemplate.getListFromCursor(mongoTemplate.getCollection("medicals-records").find(queryCondition));
    }


}
