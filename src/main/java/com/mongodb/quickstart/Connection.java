package com.mongodb.quickstart;

import com.mongodb.ConnectionString;

//https://github.com/mongodb-developer/java-quick-start

import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.quickstart.models.Grade;
import com.mongodb.quickstart.models.Score;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.addToSet;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.mul;
import static com.mongodb.client.model.Updates.push;
import static com.mongodb.client.model.Updates.rename;
import static com.mongodb.client.model.Updates.set;
import static java.util.Arrays.asList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

class ItemOfArray {
	String index;
	String introduction;

	public static List<ItemOfArray> getArray(){
		List<ItemOfArray> list = new ArrayList<ItemOfArray>();
		
		for(int i=0;i<3; i++) {
			ItemOfArray item = new ItemOfArray();
			
			item.index = i+"";
			item.introduction = "intro gen: " + Connection.getTimestamp();
			list.add(item);
		}
		
		return list;
	}
}

//with string
class Recomm1 {
	 public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getObject_id() {
		return object_id;
	}

	public void setObject_id(String object_id) {
		this.object_id = object_id;
	}

	public String getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(String last_modified) {
		this.last_modified = last_modified;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}
	
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Recomm1{");
        sb.append("object_id=").append(object_id);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", last_modified=").append(last_modified);
        sb.append(", recommendation=").append(recommendation);
        sb.append('}');
        return sb.toString();
    }

    private ObjectId id;
    
    @BsonProperty(value = "object_id")
    private String object_id;

	 
	@BsonProperty(value = "last_modified")
	private String last_modified;
	 
	@BsonProperty(value = "timestamp")
	private String timestamp;
	 
	@BsonProperty(value = "recommendation")
	private String recommendation;

}

class Recomm2 {
	 public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(String last_modified) {
		this.last_modified = last_modified;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<ItemOfArray> getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(List<ItemOfArray> recommendation) {
		this.recommendation = recommendation;
	}

	private String id;
	 //@BsonProperty(value = "student_id")
	 //private Double studentId;
	 
	 @BsonProperty(value = "last_modified")
	 private String last_modified;
	 
	 @BsonProperty(value = "timestamp")
	 private String timestamp;
	 
	 @BsonProperty(value = "recommendation")
	 private List<ItemOfArray> recommendation;
}

public class Connection {
	
	private static final Random rand = new Random();
	
    private static Document generateNewGrade(double studentId, double classId) {
        List<Document> scores = asList(new Document("type", "exam").append("score", rand.nextDouble() * 100),
                                       new Document("type", "quiz").append("score", rand.nextDouble() * 100),
                                       new Document("type", "homework").append("score", rand.nextDouble() * 100),
                                       new Document("type", "homework").append("score", rand.nextDouble() * 100));
        return new Document("_id", new ObjectId()).append("student_id", studentId)
                                                  .append("class_id", classId)
                                                  .append("scores", scores);
    }
    private static Document generate1(String id) {

        return new Document("_id", id).append("last_modified", getTimestamp())
                                      .append("timestamp", getTimestamp())
                                      .append("recommentation", "json-string");
    }
    private static Document generate2(String id) {
    	
    	List<Document> recomms = new ArrayList<Document>();
    	recomms.add( new Document("type", "exam").append("score", rand.nextDouble() * 100));

        return new Document("_id", id).append("last_modified", getTimestamp())
                                      .append("timestamp", getTimestamp())
                                      .append("recommentation", recomms);
    }

    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        
        JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();

        
        //String connectionString = System.getProperty("mongodb.uri");
        //String conn1 = "mongodb+srv://USERNAME:PASSWORD@cluster0-abcde.mongodb.net/test?w=majority";
        
        String conn = "mongodb+srv://user:user@cluster0-ddiwx.mongodb.net/test?retryWrites=true&w=majority";
        System.out.println("mongodb: " + conn);
        
        ConnectionString connectionString = new ConnectionString(conn);
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                                                                .applyConnectionString(connectionString)
                                                                .codecRegistry(codecRegistry)
                                                                .build();
        
        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
        
        	//list od sbs	
        	//System.out.println("\nlist of db");
        	//List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
            //databases.forEach(db -> System.out.println(db.toJson()));
        
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("test");
            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("test");
            
            test1(gradesCollection);
            if (true)
            	return;

            String testId1 = "001";
            String testId2 = "002";
            String testId3 = "003";
            String[] tests = {testId1,testId2,testId3};
            
            System.out.println("\ntest for reading");
            // find one document with new Document
            for(String test : tests) {
            	Document record = gradesCollection.find(new Document("_id", test)).first();
            	if (record!=null) {
            		System.out.println("record _id: " + test + "\n" + record.toJson());
            	}
            }
                   
            /*
             * update 1
             */
            /*
            MongoCollection<Recomm1> coll1 = sampleTrainingDB.getCollection("test",Recomm1.class);
            Recomm1 recomm1 = coll1.find(eq("object_id", testId1)).first();
            if (recomm1==null) {
            	recomm1 = new Recomm1();
            	recomm1.setId(new ObjectId());
            	recomm1.setObject_id(testId1);
              	coll1.insertOne(recomm1);
            } 
            recomm1.setLast_modified(getTimestamp());
            recomm1.setTimestamp(getTimestamp());	
        	recomm1.setRecommendation("json-string");	
            
            System.out.println("Recomm1 :\t" + recomm1);
            */
            
            // update one document
            System.out.println("\ntest for updating _id: " + testId1);
            Bson filter = eq("_id", testId1);
            Bson updateOperation = set("comment", "You should learn MongoDB!");
            UpdateResult updateResult = gradesCollection.updateOne(filter, updateOperation);
            System.out.println("=> Updating the doc with " + filter + ". Adding comment.");
            System.out.println(gradesCollection.find(filter).first().toJson(prettyPrint));
            System.out.println(updateResult);
           
            // upsert
            filter = and(eq("_id", testId1), eq("class_id", 10d));
            updateOperation = push("comments", "You will learn a lot if you read the MongoDB blog!");
            UpdateOptions options = new UpdateOptions().upsert(true);
          
            //E11000 duplicate key error collection: 5e3e36f7014b768935c738b8_test.test index: _id_ dup key: { _id: "008" }
            updateResult = gradesCollection.updateOne(filter, updateOperation, options);
            ////////////////////
            System.out.println("\n=> Upsert document with " + filter.toString() + " because it doesn't exist yet.");
            System.out.println(updateResult);
            System.out.println(gradesCollection.find(filter).first().toJson(prettyPrint));

            // findOneAndUpdate
            filter = eq("_id", testId1);
            Bson update1 = inc("x", 10); // increment x by 10. As x doesn't exist yet, x=10.
            Bson update2 = rename("class_id", "new_class_id"); // rename variable "class_id" in "new_class_id".
            Bson update3 = mul("scores.0.score", 2); // multiply the first score in the array by 2.
            Bson update4 = addToSet("comments", "This comment is uniq"); // creating an array with a comment.
            Bson update5 = addToSet("comments", "This comment is uniq"); // using addToSet so no effect.
            Bson updates = combine(update1, update2, update3, update4, update5);
            // returns the old version of the document before the update.
            Document oldVersion = gradesCollection.findOneAndUpdate(filter, updates);
            System.out.println("\n=> FindOneAndUpdate operation. Printing the old version by default:");
            System.out.println(oldVersion.toJson(prettyPrint));

        
        
        }
        
        
        
    }
    public static String getTimestamp() {
    	return new Date().toString();
    }
    
    private static String getJson(Bson bson) {
    	if (bson!=null) {
    		BsonDocument bsonDocument = bson.toBsonDocument(BsonDocument.class, MongoClientSettings.getDefaultCodecRegistry());
    		return bsonDocument.toJson();
    	} else {
    		return "";
    	}
    }
    
    private static void test1(MongoCollection<Document> gradesCollection) {
    	
    	
        String testId = "007";
        //String testId2 = "008";
        JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();

        
        System.out.println("\ntest for reading _id: " + testId);
        // find one document with new Document
        Document record1 = gradesCollection.find(new Document("_id", testId)).first();
        System.out.println("record: " + record1.toJson());
        
  
        // find one document with Filters.eq()
        Document record2 = gradesCollection.find(eq("_id", testId)).first();
        System.out.println("record by eq: " + record2.toJson());

        // find a list of documents and iterate throw it using an iterator.
        FindIterable<Document> iterable = gradesCollection.find(gte("_id", testId));
        MongoCursor<Document> cursor = iterable.iterator();
        System.out.println("record list with a cursor: ");
        while (cursor.hasNext()) {
            System.out.println(cursor.next().toJson());
        }

        // find a list of documents and use a List object instead of an iterator
        List<Document> recordList = gradesCollection.find(gte("_id", testId)).into(new ArrayList<>());
        System.out.println("record list with an ArrayList:");
        for (Document doc : recordList) {
            System.out.println(doc.toJson());
        }

        // find a list of documents and print using a consumer
        System.out.println("record list using a Consumer:");
        Consumer<Document> printConsumer = document -> System.out.println(document.toJson());
        gradesCollection.find(gte("_id", testId)).forEach(printConsumer);

        // find a list of documents with sort, skip, limit and projection
        List<Document> docs = gradesCollection.find(and(eq("_id", testId), lte("class_id", 5)))
                                              .projection(fields(excludeId(), include("class_id", "_id")))
                                              .sort(descending("class_id"))
                                              .skip(2)
                                              .limit(2)
                                              .into(new ArrayList<>());

        System.out.println("record sorted, skipped, limited and projected: ");
        for (Document doc : docs) {
            System.out.println(doc.toJson());
        }
        
        /*
         * 
         */
       
        System.out.println("\ntest for updating _id: " + testId);
        // update one document
        Bson filter = eq("_id", testId);
        
        Bson updateOperation = set("comment", "You should learn MongoDB! " + getTimestamp());
        
        //updateOperaton = push();
        
        UpdateResult updateResult = gradesCollection.updateOne(filter, updateOperation);
        System.out.println("=> Updating the doc with " + filter.toString() + ". Adding comment."); //getJson(filter));
        System.out.println(gradesCollection.find(filter).first().toJson(prettyPrint));
        System.out.println(updateResult);

        // upsert
        filter = and(eq("_id", testId), eq("class_id", 10d));
        updateOperation = push("comments", "You will learn a lot if you read the MongoDB blog!");
        UpdateOptions options = new UpdateOptions().upsert(false);
        
        //E11000 duplicate key error collection: 5e3e36f7014b768935c738b8_test.test index: _id_ dup key: { _id: "008" }
        updateResult = gradesCollection.updateOne(filter, updateOperation, options);
        ////////////////////
        System.out.println("\n=> Upsert document with " + filter.toString() + " because it doesn't exist yet.");
        System.out.println(updateResult);
        if (gradesCollection.find(filter).first()!=null ) {
        	System.out.println(gradesCollection.find(filter).first().toJson(prettyPrint));
        }

        // update many documents
        filter = gte("_id", testId);
        updateResult = gradesCollection.updateMany(filter, updateOperation);
        System.out.println("\n=> Updating all the documents with " + filter.toString() + ".");
        System.out.println(updateResult);

        // findOneAndUpdate
        filter = eq("_id", testId);
        Bson update1 = inc("x", 10); // increment x by 10. As x doesn't exist yet, x=10.
        Bson update2 = rename("class_id", "new_class_id"); // rename variable "class_id" in "new_class_id".
        Bson update3 = mul("scores.0.score", 2); // multiply the first score in the array by 2.
        Bson update4 = addToSet("comments", "This comment is uniq"); // creating an array with a comment.
        Bson update5 = addToSet("comments", "This comment is uniq"); // using addToSet so no effect.
        Bson updates = combine(update1, update2, update3, update4, update5);
        // returns the old version of the document before the update.
        Document oldVersion = gradesCollection.findOneAndUpdate(filter, updates);
        System.out.println("\n=> FindOneAndUpdate operation. Printing the old version by default:");
        System.out.println(oldVersion.toJson(prettyPrint));

        // but I can also request the new version
        filter = eq("_id", testId);
        FindOneAndUpdateOptions optionAfter = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        Document newVersion = gradesCollection.findOneAndUpdate(filter, updates, optionAfter);
        System.out.println("\n=> FindOneAndUpdate operation. But we can also ask for the new version of the doc:");
        System.out.println(newVersion.toJson(prettyPrint));
    	
    }
}
