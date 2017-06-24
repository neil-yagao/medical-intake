package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.neil.medical.pojo.Medical;
import com.neil.medical.util.TimeUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by nhu on 5/2/2017.
 */
@Service
public class PrisonIntakeRecord {
    public static final String INTAKE_RECORD_COLLECTION = "intake_records";

    private static Logger LOGGER = LoggerFactory.getLogger(PrisonIntakeRecord.class);

    @Autowired
    private WrappedMongoTemplate template;

    @Autowired
    private MongoTemplate rawTemplate;

    @Autowired
    private MedicalInventory medicalInventory;

    @Value("${file.location}")
    private String directoryToWatch;

    public void inmateConfirmMedicalIntake(String code, List<JSONObject> medicals) {
        template.save(INTAKE_RECORD_COLLECTION, new JSONObject().fluentPut("code", code)
                .fluentPut("timestamp", new Date().getTime())
                .fluentPut("date", TimeUtil.getCurrentDate())
                .fluentPut("time", TimeUtil.getCurrentDateFormat("HH"))
                .fluentPut("medicals", medicals)
                .fluentPut("checked", false));
        //reduce medical num
        List<Medical> usedMedical = new ArrayList<>();
        for (JSONObject data : medicals) {
            usedMedical.add(new Medical(data.getString("medical"), (0 - data.getDouble("amount"))));
        }
        medicalInventory.insertOrUpdateMedicalInfo(usedMedical, code);
    }

    public List<JSONObject> getPrisonIntakeRecord(String code, String timespan, String time) {
        DBObject queryCondition = new BasicDBObject();
        if (!code.equalsIgnoreCase("all")) {
            queryCondition.put("code", code);
        }
        if(!time.equalsIgnoreCase("all")){
            queryCondition.put("medicals.time", time);
        }
        JSONObject timespanRange = TimeUtil.parseTimespan(timespan);
        queryCondition.put("timestamp", new BasicDBObject(timespanRange));
        return template.getListFromCursor(rawTemplate.getCollection(INTAKE_RECORD_COLLECTION).find(queryCondition));
    }


    public JSONObject findVideo(String time) {

        JSONObject exactMinute = doFindVideo(time);
        if(exactMinute.isEmpty()){
            //find next minute video
            String[] splited = time.split("-");
            String minute = splited[splited.length - 1];
            String nextMinute = (Integer.parseInt(minute) + 1) + "";
            splited[splited.length - 1] = nextMinute;
            return doFindVideo(String.join("-",splited));
        }
        return exactMinute;

    }

    private JSONObject doFindVideo(String time) {
        try{
            boolean recursive = true;
            String directoryPath = directoryToWatch + "/video";

            Collection files = FileUtils.listFiles(new File(directoryPath), null, recursive);

            for (Iterator iterator = files.iterator(); iterator.hasNext();) {
                File file = (File) iterator.next();
                if (file.getName().startsWith(time))
                    return new JSONObject().fluentPut("video", file.getName());
            }


        }catch (Exception e){
            LOGGER.error("exception during find video:",e);
        }
        return new JSONObject();
    }
}
