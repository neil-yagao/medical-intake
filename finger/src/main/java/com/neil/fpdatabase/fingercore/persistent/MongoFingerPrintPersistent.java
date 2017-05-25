package com.neil.fpdatabase.fingercore.persistent;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.neil.fpdatabase.fingercore.FingerPrintCache;
import com.neil.fpdatabase.fingercore.PersistentFingerPrint;
import com.neil.fpdatabase.fingercore.fingerprint.CachedFingerPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by nhu on 4/20/2017.
 */
@Component
public class MongoFingerPrintPersistent implements PersistentFingerPrint {

    public static final String COLLECTION_NAME = "persistent";
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private FingerPrintCache fingerPrintCache;

    @Override
    public void persistentFingerPrint(CachedFingerPrint cachedFingerPrint) {
        DBObject fingerPrint = new BasicDBObject();
        fingerPrint.put("temp1", cachedFingerPrint.getImg(0).getRegBytes());
        fingerPrint.put("temp2", cachedFingerPrint.getImg(1).getRegBytes());
        fingerPrint.put("temp3", cachedFingerPrint.getImg(2).getRegBytes());
        fingerPrint.put("generated", cachedFingerPrint.getCombinedTemplate());
        fingerPrint.put("code", cachedFingerPrint.getIdentityCode());
        fingerPrint.put("identity", cachedFingerPrint.getIdentity());
        mongoTemplate.getCollection(COLLECTION_NAME).save(fingerPrint);
    }

    @Override
    public void loadPersistedFingerPrint() {
        DBCursor cursor = mongoTemplate.getCollection(COLLECTION_NAME).find();
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            CachedFingerPrint fingerPrint = new CachedFingerPrint();
            fingerPrint.setCombinedTemplate((byte[]) object.get("generated"));
            fingerPrint.setIdentity(object.get("identity").toString());
            fingerPrint.setPrisonCode(object.get("code").toString());
            fingerPrintCache.cacheFingerPrint(fingerPrint);
        }
    }
}
