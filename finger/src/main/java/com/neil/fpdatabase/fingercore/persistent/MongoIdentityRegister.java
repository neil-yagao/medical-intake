package com.neil.fpdatabase.fingercore.persistent;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Service;

/**
 * Created by nhu on 5/31/2017.
 */
@Service
public class MongoIdentityRegister {

    @Value("${register.database}")
    private String needRegisterTo;

    @Autowired
    private MongoDbFactory client;

    public void doRegister(String code, String identity, String headPic){
        if(needRegisterTo == null || needRegisterTo.isEmpty()){
            return;
        }
        String[] databases = needRegisterTo.split(",");
        DBObject identityObject = new BasicDBObject("code", code)
                .append("identity", identity)
                .append("headPic", headPic);
        for(String database : databases){
            DB db = client.getDb(database);
            db.getCollection("registered_identity").save(identityObject);
        }
    }
}
