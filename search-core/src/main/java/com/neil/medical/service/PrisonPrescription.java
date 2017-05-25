package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.neil.medical.pojo.PrisonMedicalInfo;
import com.neil.medical.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void recordChange(List<PrisonMedicalInfo> to, String code){
        String prisonCode = to.get(0).getCode();
        List<JSONObject> from = prisonMedical.getPrisonMedicalInfo(prisonCode);
        prisonMedical.insertInmateMedicalInfo(to);
        List<JSONObject> after = prisonMedical.getPrisonMedicalInfo(prisonCode);
        JSONObject record = new JSONObject().fluentPut("changer", code)
                .fluentPut("changee", prisonCode)
                .fluentPut("from", from).fluentPut("to", after)
                .fluentPut("date", TimeUtil.getCurrentDate())
                .fluentPut("timestamp", new Date().getTime());
        template.save(PRISON_PRESCRIPTION_CHANGE_HISTORY, record);

    }

    public List<JSONObject> getHistory(String code, String timespan) {
        JSONObject condition = new JSONObject();
        if(!code.equalsIgnoreCase("all")){
            condition.put("changee", code);
        }
        JSONObject timespanRange = TimeUtil.parseTimespan(timespan);
        condition.put("timestamp", new BasicDBObject(timespanRange));
        return template.query(PRISON_PRESCRIPTION_CHANGE_HISTORY, condition);
    }
}
