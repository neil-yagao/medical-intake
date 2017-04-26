/**
* since finger core need index, so create database via script
*/
conn = new Mongo();
db = conn.getDB("medical");

collections = db.getCollectionNames();
if (collections.indexOf("inmate-medical") >= 0) {
    print("found inmate-medical")
} else {
    db.createCollection("inmate-medical");
    inmateMedical = db.getCollection("inmate-medical");
    inmateMedical.createIndex({
        "code": 1,
        "medical": 1,
        "time": 1
    }, {
        "unique": true
    })
}

if (collections.indexOf("intake_records") >= 0) {
    print("found intake_records")
} else {
    db.createCollection("intake_records");
    intake_records = db.getCollection("intake_records");
    intake_records.createIndex({
        "code": 1,
        "timestamp": 1
    }, {
        "unique": true
    })
}

if (collections.indexOf("medicals") >= 0) {
    print("found medicals")
} else {
    db.createCollection("medicals");
    medicals = db.getCollection("medicals");
    medicals.createIndex({
        "name": 1
    }, {
        "unique": true
    })
}

if (collections.indexOf("missing_intake") >= 0) {
    print("found missing_intake")
} else {
    db.createCollection("missing_intake");
    missing_intake = db.getCollection("missing_intake");
    missing_intake.createIndex({
        "code": 1,
        "date": 1,
        "need": 1
    }, {
        "unique": true
    })
}
print("current collections:")
printjson(db.getCollectionNames());