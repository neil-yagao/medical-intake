package com.neil.medical.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nhu on 5/31/2017.
 */
@Service
public class Identities {

    @Autowired
    private WrappedMongoTemplate template;

    public List<JSONObject> getIdentities(String code){
        JSONObject queryCondition = new JSONObject().fluentPut("identity","prison");
        if(!code.equalsIgnoreCase("all")){
            queryCondition.put("code",code);
        }
        return template.query("registered_identity", queryCondition);
    }

}
