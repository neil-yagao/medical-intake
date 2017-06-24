package com.neil.service;

import com.alibaba.fastjson.JSONObject;
import org.testng.Assert;

import java.util.List;

/**
 * Created by nhu on 6/24/2017.
 */
public class TestUtil {

    public static Boolean compareJOSNObject(JSONObject object, JSONObject to){
        Boolean result = (object.size() == to.size());
        for(String key: object.keySet()){
            if(object.get(key) == null && ( to.containsKey(key)&&to.get(key) == null )){
                continue;
            }else{
                if(object.get(key) instanceof String){
                    if(!object.getString(key).equalsIgnoreCase(to.getString(key))){
                        result = false;
                    }
                }else if(object.get(key) instanceof Number){
                    if(((Number)object.get(key)).doubleValue() != ((Number) to.get(key)).doubleValue())
                        result = false;
                }
            }
        }
        return result;
    }

    public  static Boolean compareJSONList(List<JSONObject> object, List<JSONObject> to){
        Assert.assertEquals(object.size(), to.size());
        int matching = 0;
        for(JSONObject o: object){
            for(JSONObject t: to){
                if(compareJOSNObject(o,t)){
                    matching++;
                    break;
                }
            }
        }
        return matching == object.size();
    }
}
