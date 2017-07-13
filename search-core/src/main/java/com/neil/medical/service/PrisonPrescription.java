package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.neil.medical.pojo.PrescriptionChangeItem;
import com.neil.medical.pojo.PrisonMedicalInfo;
import com.neil.medical.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nhu on 5/22/2017.
 * record change
 */
@Service
public class PrisonPrescription {

    public static final String PRISON_PRESCRIPTION_CHANGE_HISTORY = "prescription_history";

    @Autowired
    private PrisonMedical prisonMedical;

    @Autowired
    private WrappedMongoTemplate template;

    @Autowired
    private MongoTemplate rawTemplate;

    public void recordChange(List<PrisonMedicalInfo> to, String code) {
        String prisonCode = to.get(0).getCode();
        List<PrisonMedicalInfo> from = prisonMedical.getPrisonMedicalInfo(prisonCode);
        prisonMedical.insertInmateMedicalInfo(to);
        List<PrisonMedicalInfo> after = prisonMedical.getPrisonMedicalInfo(prisonCode);
        List<JSONObject> differ = diffPrescriptions(from, after);
        if(differ.isEmpty()){
            //identical or new creation do not insert.
            return;
        }
        JSONObject record = new JSONObject().fluentPut("changer", code)
                .fluentPut("changee", prisonCode)
                .fluentPut("changes", differ)
                .fluentPut("date", TimeUtil.getCurrentDate())
                .fluentPut("timestamp", new Date().getTime());
        template.save(PRISON_PRESCRIPTION_CHANGE_HISTORY, record);

    }

    public List<JSONObject> getHistory(String code, String timespan) {
        JSONObject condition = new JSONObject();
        if (!code.equalsIgnoreCase("all")) {
            condition.put("changee", code);
        }
        JSONObject timespanRange = TimeUtil.parseTimespan(timespan);
        condition.put("timestamp", new BasicDBObject(timespanRange));
        return template.query(PRISON_PRESCRIPTION_CHANGE_HISTORY, condition);
    }

    public List<JSONObject> diffPrescriptions(List<PrisonMedicalInfo> from, List<PrisonMedicalInfo> to) {
        if(from.isEmpty()){
            //new Creation
            return new ArrayList<>();
        }
        List<JSONObject> items = new ArrayList<>();
        for (PrisonMedicalInfo tInfo : to) {
            String medical = tInfo.getMedical();
            Boolean newMedical = true;
            PrescriptionChangeItem item = null;
            for (PrisonMedicalInfo fInfo : from) {
                if (fInfo.getMedical().equalsIgnoreCase(medical)
                        && fInfo.getTime().equalsIgnoreCase(tInfo.getTime())) {
                    String incOrDesc = "";
                    if (tInfo.getAmount().equals(fInfo.getAmount())) {
                        newMedical = false;
                        break;
                    } else if (tInfo.getAmount() > fInfo.getAmount()) {
                        incOrDesc = "增加";
                    } else {
                        incOrDesc = "减少";
                    }
                    newMedical = false;
                    item = new PrescriptionChangeItem(medical, incOrDesc, Math.abs(tInfo.getAmount() - fInfo.getAmount()));
                    item.setTime(fInfo.getTime());
                    break;
                }
            }
            if (newMedical) {
                item = new PrescriptionChangeItem(medical, "新增", tInfo.getAmount());
                item.setTime(tInfo.getTime());
            }
            if (item != null) {
                items.add((JSONObject) JSONObject.toJSON(item));
            }
        }
        return items;
    }

    public JSONObject confirmPrescriptionChanges(List<JSONObject> changes, String code) {
        for(JSONObject change:changes){
            Query condition = new Query();
            condition.addCriteria(
                    new Criteria("timestamp")
                            .is(change.getLong("timestamp"))
            );
            Update update = new Update();
            update.set("confirmer",code);
            update.set("confirm_timestamp", new Date().getTime());
            rawTemplate.updateMulti(condition,update,PRISON_PRESCRIPTION_CHANGE_HISTORY);
        }
        return new JSONObject();
    }
}
