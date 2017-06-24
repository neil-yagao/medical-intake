package com.neil.service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.neil.medical.MedicalMatching;
import com.neil.medical.pojo.PrisonMedicalInfo;
import com.neil.medical.service.PrisonPrescription;
import com.neil.medical.service.WrappedMongoTemplate;
import com.neil.medical.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.neil.medical.service.PrisonPrescription.PRISON_PRESCRIPTION_CHANGE_HISTORY;

/**
 * Created by nhu on 6/24/2017.
 */
@Test
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MedicalMatching.class)
@DirtiesContext
public class OneTimeExecutionTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private PrisonPrescription prescription;

    @Autowired
    private MongoTemplate raw;
    @Autowired
    private WrappedMongoTemplate template;

    public void prescriptionUpdate(){
        List<JSONObject> prescriptions = prescription.getHistory("all", "0:" + new Date().getTime());
        for(JSONObject history: prescriptions){
            List<JSONObject> updatedChanges = updateChangesField((List<BasicDBObject>) history.get("changes"));
            JSONObject record = new JSONObject().fluentPut("changer", history.getString("changer"))
                    .fluentPut("changee", history.getString("changee"))
                    .fluentPut("changes", updatedChanges)
                    .fluentPut("date", TimeUtil.getCurrentDate())
                    .fluentPut("timestamp", new Date().getTime());
            template.save(PRISON_PRESCRIPTION_CHANGE_HISTORY, record);
        }
    }

    private List<JSONObject> updateChangesField(List<BasicDBObject> raw){
        List<JSONObject> result = new ArrayList<>();
        for(BasicDBObject b: raw){
            JSONObject temp = new JSONObject(b);
            temp.put("amount", b.getDouble("number"));
            temp.remove("number");
            result.add(temp);
        }
        return result;
    }
}
