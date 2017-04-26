package com.neil.service;

import com.alibaba.fastjson.JSONObject;
import com.neil.medical.MedicalMatching;
import com.neil.medical.service.MedicalTakenExam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by nhu on 4/23/2017.
 */
@Test
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MedicalMatching.class)
@DirtiesContext
public class MedicalTakenExamTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MedicalTakenExam exam;

    @Test
    public void checkUntaken() {
        List<JSONObject> missing = exam.calculateTheMissingRecords(19);
        System.out.println(missing);
        exam.persistentMissingRecord(missing);
    }

    @Test
    public void checkReduce() {
        exam.reduceMedicalNumber(19);
    }
}
