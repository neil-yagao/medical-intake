package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DuplicateKeyException;
import com.mongodb.client.model.DBCollectionUpdateOptions;
import com.neil.medical.pojo.Medical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.neil.medical.service.PrisonIntakeRecord.INTAKE_RECORD_COLLECTION;
import static com.neil.medical.service.PrisonMedicalService.INMATE_REQUIRED_MEDICAL_COLLECTION;

/**
 * Created by nhu on 4/22/2017.
 */
@Component
@EnableScheduling
public class ScheduledMedicalTakenExam {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledMedicalTakenExam.class);

    @Autowired
    private MongoTemplate template;

    @Autowired
    private MedicalInfo medicalInfo;

    @Scheduled(cron = "0 0 9,13,19,23 * * *")
    public void examineIntakeRecord() {
        LOGGER.info("exam medical intake");
        Calendar calendar = new GregorianCalendar(Locale.CHINA);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        List<JSONObject> missing = calculateTheMissingRecords(currentHour);
        persistentMissingRecord(missing);
        reduceMedicalNumber(currentHour);
    }

    public void reduceMedicalNumber(int currentHour) {
        Map<String, List<Integer>> intakeRecording = findIntakedRecording();
        for (String prisonCode : intakeRecording.keySet()) {
            DBCursor cursor = template.getCollection(INMATE_REQUIRED_MEDICAL_COLLECTION)
                    .find(new BasicDBObject("code", prisonCode).append("time", new BasicDBObject("$in", getPredefineList(currentHour))));
            while (cursor.hasNext()) {
                BasicDBObject data = (BasicDBObject) cursor.next();
                Medical medical = new Medical(data.getString("medical"), (0 - data.getDouble("amount")));
                medicalInfo.insertOrUpdateMedicalInfo(Collections.singletonList(medical));
            }

        }
    }

    public void persistentMissingRecord(List<JSONObject> missing) {
        for (JSONObject js : missing) {
            try {
                template.getCollection(RefuseIntake.MISSING_INTAKE_COLLECTION).save(new BasicDBObject(js));
            } catch (DuplicateKeyException e) {
                LOGGER.warn("find duplicate record:" + js);
            }
        }
    }

    public List<JSONObject> calculateTheMissingRecords(int currentHour) {
        Map<String, List<Integer>> intakeRecording = findIntakedRecording();
        setIntakeRecordingChecked(intakeRecording);
        Map<String, Set<String>> medicalRecord = getNeedMedicalList(currentHour);
        return getMissingRecord(intakeRecording, medicalRecord);
    }

    //return code : need intaking time
    private Map<String, Set<String>> getNeedMedicalList(int currentHour) {
        List<String> timeSpan = getPredefineList(currentHour);
        Map<String, Set<String>> medicalRecord = new HashMap<>();
        DBCursor cursor = template.getCollection(INMATE_REQUIRED_MEDICAL_COLLECTION)
                .find(new BasicDBObject("time", new BasicDBObject("$in", timeSpan)));
        while (cursor.hasNext()) {
            JSONObject object = new JSONObject((BasicDBObject) cursor.next());
            String prisonCode = object.getString("code");
            String time = object.getString("time");
            if (medicalRecord.get(prisonCode) == null) {
                medicalRecord.put(prisonCode, new HashSet<>());
            }
            medicalRecord.get(prisonCode).add(time);
        }
        return medicalRecord;
    }

    //return code : intaking hours
    private Map<String, List<Integer>> findIntakedRecording() {
        Map<String, List<Integer>> intakeRecording = new HashMap<>();
        DBCursor cursor = template.getCollection(INTAKE_RECORD_COLLECTION)
                .find(new BasicDBObject("checked", false));
        while (cursor.hasNext()) {
            JSONObject object = new JSONObject((BasicDBObject) cursor.next());
            String prisonCode = object.getString("code");
            Calendar transferTime = new GregorianCalendar(Locale.CHINA);
            transferTime.setTimeInMillis(object.getLong("timestamp"));
            int intakeTime = transferTime.get(Calendar.HOUR_OF_DAY);
            if (intakeRecording.get(prisonCode) == null) {
                intakeRecording.put(prisonCode, new ArrayList<>());
            }
            intakeRecording.get(prisonCode).add(intakeTime);
        }
        return intakeRecording;
    }

    private void setIntakeRecordingChecked(Map<String, List<Integer>> intakeRecording) {
        for (String code : intakeRecording.keySet()) {
            template.getCollection(INTAKE_RECORD_COLLECTION).update(
                    new BasicDBObject("code", code).append("checked", false),
                    new BasicDBObject("$set", new BasicDBObject("checked", true)),
                    new DBCollectionUpdateOptions().multi(true)
            );
        }
    }

    /**
     * for each prison , compare each intaking hour is matching any need intaking time
     *
     * @param intakeRecording code : intaking hours
     * @param medicalRecord   code : need intaking time
     * @return
     */
    private List<JSONObject> getMissingRecord(Map<String, List<Integer>> intakeRecording, Map<String, Set<String>> medicalRecord) {
        List<JSONObject> missingRecord = new ArrayList<>();
        for (String prisonCode : medicalRecord.keySet()) {
            List<Integer> records = intakeRecording.get(prisonCode);
            Set<String> required = medicalRecord.get(prisonCode);
            for (String r : required) {
                boolean foundMatching = false;
                if (records != null) {
                    for (Integer rec : records) {
                        foundMatching = isMatching(r, rec);
                    }
                }
                if (!foundMatching) {
                    JSONObject record = new JSONObject().fluentPut("code", prisonCode);
                    record.put("need", r);
                    record.put("checked", false);
                    record.put("time", new Date().getTime());
                    LocalDateTime datetime = LocalDateTime.now();
                    record.put("date",
                            datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    missingRecord.add(record);
                }
            }
        }
        return missingRecord;
    }

    private boolean isMatching(String needTime, int intakeTime) {
        //now we only exam the time
        //ignore the medical matching for now
        if (needTime.equals("早餐后") || needTime.equals("早餐前")) {
            return intakeTime <= 9 && intakeTime >= 6;
        } else if (needTime.equals("午餐前") || needTime.equals("午餐后")) {
            return intakeTime >= 11 && intakeTime <= 13;
        } else if (needTime.equals("晚餐前") || needTime.equals("晚餐后")) {
            return intakeTime >= 17 && intakeTime <= 19;
        } else if (needTime.equals("临睡前")) {
            return intakeTime >= 20;
        }
        return false;
    }

    private List<String> getPredefineList(int currentHour) {
        List<String> predefineList = new ArrayList<>();
        predefineList.add(currentHour + "");
        if (currentHour == 9) {
            predefineList.add("早餐前");
            predefineList.add("早餐后");
        } else if (currentHour == 13) {
            predefineList.add("午餐前");
            predefineList.add("午餐后");
        } else if (currentHour == 19) {
            predefineList.add("晚餐前");
            predefineList.add("晚餐后");
        } else if (currentHour == 23) {
            predefineList.add("临睡前");
        }
        return predefineList;
    }
}
