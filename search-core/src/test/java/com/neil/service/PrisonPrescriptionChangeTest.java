package com.neil.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.neil.medical.MedicalMatching;
import com.neil.medical.pojo.PrisonMedicalInfo;
import com.neil.medical.service.PrisonPrescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhu on 6/24/2017.
 */
@Test
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MedicalMatching.class)
@DirtiesContext
public class PrisonPrescriptionChangeTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private PrisonPrescription service;

    public void examChanges(){
        List<BasicDBObject> fromDB = JSONObject.parseArray("[{\"_id\":{\"counter\":8066045,\"date\":1496475679000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496475679000,\"timeSecond\":1496475679,\"timestamp\":1496475679},\"amount\":2.0,\"medical\":\"药物13\",\"code\":\"药物管理you3\",\"time\":\"午餐前\"}, {\"_id\":{\"counter\":8066039,\"date\":1496475679000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496475679000,\"timeSecond\":1496475679,\"timestamp\":1496475679},\"amount\":2.0,\"medical\":\"药物13\",\"code\":\"药物管理you3\",\"time\":\"早餐前\"}, {\"_id\":{\"counter\":8066051,\"date\":1496475679000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496475679000,\"timeSecond\":1496475679,\"timestamp\":1496475679},\"amount\":3.0,\"medical\":\"药物13\",\"code\":\"药物管理you3\",\"time\":\"晚餐前\"}, {\"_id\":{\"counter\":8066043,\"date\":1496475679000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496475679000,\"timeSecond\":1496475679,\"timestamp\":1496475679},\"amount\":3.0,\"medical\":\"药物23\",\"code\":\"药物管理you3\",\"time\":\"午餐前\"}, {\"_id\":{\"counter\":8066037,\"date\":1496475679000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496475679000,\"timeSecond\":1496475679,\"timestamp\":1496475679},\"amount\":3.0,\"medical\":\"药物23\",\"code\":\"药物管理you3\",\"time\":\"早餐前\"}, {\"_id\":{\"counter\":8066049,\"date\":1496475679000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496475679000,\"timeSecond\":1496475679,\"timestamp\":1496475679},\"amount\":3.0,\"medical\":\"药物23\",\"code\":\"药物管理you3\",\"time\":\"晚餐前\"}, {\"_id\":{\"counter\":8066041,\"date\":1496475679000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496475679000,\"timeSecond\":1496475679,\"timestamp\":1496475679},\"amount\":3.0,\"medical\":\"药物3\",\"code\":\"药物管理you3\",\"time\":\"午餐前\"}, {\"_id\":{\"counter\":8066035,\"date\":1496475679000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496475679000,\"timeSecond\":1496475679,\"timestamp\":1496475679},\"amount\":3.0,\"medical\":\"药物3\",\"code\":\"药物管理you3\",\"time\":\"早餐前\"}, {\"_id\":{\"counter\":8066047,\"date\":1496475679000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496475679000,\"timeSecond\":1496475679,\"timestamp\":1496475679},\"amount\":3.0,\"medical\":\"药物3\",\"code\":\"药物管理you3\",\"time\":\"晚餐前\"}]",BasicDBObject.class);
        List<BasicDBObject> toDB = JSONObject.parseArray("[{\"_id\":{\"counter\":8073611,\"date\":1496494988000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496494988000,\"timeSecond\":1496494988,\"timestamp\":1496494988},\"amount\":3.0,\"medical\":\"药物13\",\"code\":\"药物管理you3\",\"time\":\"午餐前\"}, {\"_id\":{\"counter\":8073605,\"date\":1496494988000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496494988000,\"timeSecond\":1496494988,\"timestamp\":1496494988},\"amount\":3.0,\"medical\":\"药物13\",\"code\":\"药物管理you3\",\"time\":\"早餐前\"}, {\"_id\":{\"counter\":8073617,\"date\":1496494988000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496494988000,\"timeSecond\":1496494988,\"timestamp\":1496494988},\"amount\":3.0,\"medical\":\"药物13\",\"code\":\"药物管理you3\",\"time\":\"晚餐前\"}, {\"_id\":{\"counter\":8073623,\"date\":1496494988000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496494988000,\"timeSecond\":1496494988,\"timestamp\":1496494988},\"amount\":2.0,\"medical\":\"药物15\",\"code\":\"药物管理you3\",\"time\":\"临睡前\"}, {\"_id\":{\"counter\":8073613,\"date\":1496494988000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496494988000,\"timeSecond\":1496494988,\"timestamp\":1496494988},\"amount\":3.0,\"medical\":\"药物23\",\"code\":\"药物管理you3\",\"time\":\"午餐前\"}, {\"_id\":{\"counter\":8073607,\"date\":1496494988000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496494988000,\"timeSecond\":1496494988,\"timestamp\":1496494988},\"amount\":3.0,\"medical\":\"药物23\",\"code\":\"药物管理you3\",\"time\":\"早餐前\"}, {\"_id\":{\"counter\":8073619,\"date\":1496494988000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496494988000,\"timeSecond\":1496494988,\"timestamp\":1496494988},\"amount\":3.0,\"medical\":\"药物23\",\"code\":\"药物管理you3\",\"time\":\"晚餐前\"}, {\"_id\":{\"counter\":8073615,\"date\":1496494988000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496494988000,\"timeSecond\":1496494988,\"timestamp\":1496494988},\"amount\":3.0,\"medical\":\"药物3\",\"code\":\"药物管理you3\",\"time\":\"午餐前\"}, {\"_id\":{\"counter\":8073609,\"date\":1496494988000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496494988000,\"timeSecond\":1496494988,\"timestamp\":1496494988},\"amount\":3.0,\"medical\":\"药物3\",\"code\":\"药物管理you3\",\"time\":\"早餐前\"}, {\"_id\":{\"counter\":8073621,\"date\":1496494988000,\"machineIdentifier\":4706424,\"processIdentifier\":28968,\"time\":1496494988000,\"timeSecond\":1496494988,\"timestamp\":1496494988},\"amount\":3.0,\"medical\":\"药物3\",\"code\":\"药物管理you3\",\"time\":\"晚餐前\"}]",BasicDBObject.class);
        List<PrisonMedicalInfo> from = translate(fromDB);
        List<PrisonMedicalInfo> to = translate(toDB);
        List<JSONObject> changes = service.diffPrescriptions(from, to);
        List<JSONObject> assertTo = JSON.parseArray("[{\"number\":1.0,\"medical\":\"药物13\",\"incOrDesc\":\"增加\",\"time\":\"午餐前\"}, {\"number\":1.0,\"medical\":\"药物13\",\"incOrDesc\":\"增加\",\"time\":\"早餐前\"}, {\"number\":2.0,\"medical\":\"药物15\",\"incOrDesc\":\"新增\",\"time\":\"临睡前\"}]", JSONObject.class);
        Assert.assertTrue(TestUtil.compareJSONList(changes,assertTo));
    }



    private List<PrisonMedicalInfo> translate(List<BasicDBObject> raw){
        List<PrisonMedicalInfo> result = new ArrayList<>();
        for(BasicDBObject b: raw){
            JSONObject temp = new JSONObject(b);
            result.add(temp.toJavaObject(PrisonMedicalInfo.class));
        }
        return result;
    }
}
