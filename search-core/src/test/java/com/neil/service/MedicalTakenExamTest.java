package com.neil.service;

import com.alibaba.fastjson.JSONObject;
import com.neil.medical.MedicalMatching;
import com.neil.medical.service.PrisonMedical;
import com.neil.medical.service.ScheduledMedicalTakenExam;
import com.neil.medical.service.WrappedMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nhu on 4/23/2017.
 */
@Test
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MedicalMatching.class)
@DirtiesContext
public class MedicalTakenExamTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ScheduledMedicalTakenExam exam;

    @Autowired
    private PrisonMedical prisonMedical;

    @Autowired
    private WrappedMongoTemplate template;

    @Test
    public void checkUntaken() {
        List<JSONObject> missing = exam.calculateTheMissingRecords(19);
        System.out.println(missing);
        exam.persistentMissingRecord(missing);
    }

}
