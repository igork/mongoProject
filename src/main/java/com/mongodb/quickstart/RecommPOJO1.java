package com.mongodb.quickstart;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.quickstart.models.Recomm1;
import com.mongodb.quickstart.models.Recommendation;
import com.mongodb.quickstart.models.Score;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static java.util.Collections.singletonList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class RecommPOJO1 {
	
    public static void main(String[] args) {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
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
            MongoDatabase db = mongoClient.getDatabase("test");

            //test settings
            String recommTest = "10003";
            boolean isAppend = true;
            boolean isClean = true;
            
            MongoCollection<Recomm1> Recomm1s = db.getCollection("test", Recomm1.class);
            
                  
            //find
            // find a list of documents and use a List object instead of an iterator
            
            List<Recomm1> recs = Recomm1s.find(eq("recomm_id", recommTest)).into(new ArrayList<>());
            System.out.println("Found " + recs.size() + " test records");
            /*
            for (Recomm1 rec : recs) {
                System.out.println(rec);
            }
            */
            
            //clean
            //System.out.println("Recomm1 deleted:\t" + Recomm1s.deleteMany(filterByRecomm1Id));
            if (isClean && recs.size()>=1) {
	            try {
	                // delete this Recomm1
	            	Document filterByRecomm1Id = new Document("recomm_id", recommTest);
	                System.out.println("Recomm1 deleted:\t" + Recomm1s.deleteMany(filterByRecomm1Id));
	            } finally {
	            	
	            }
            }
            
            ////////////
            
            // create a new Recomm1.
            /*
            Recomm1 newRecomm1 = new Recomm1().setRecommId(recommTest)
                                       .setTimestamp(new Date().toString())
                                        .setRecommendation(singletonList(new Score().setType("homework").setScore(50d)));
            Recomm1s.insertOne(newRecomm1);
            System.out.println("Recomm1 inserted.");
			*/

            // find this Recomm1.
            Recomm1 recomm1 = Recomm1s.find(eq("recomm_id", recommTest)).first();
          
            //upsert
            if (recomm1==null) {
            	
            	//create
            	List<Score> list = singletonList(new Score().setType("homework").setScore(50d));
                recomm1 = (Recomm1) new Recomm1().setRecommId(recommTest)
                        .setTimestamp(new Date().toString())   
                        .setRecommendation(list);
                Recomm1s.insertOne(recomm1);
                System.out.println("Recomm1 inserted.");
                
            } else {
            	
                // update this Recomm1: adding an exam Recomm1
                List<Score> newScores = isAppend? new ArrayList<>(recomm1.getRecommendation()):new ArrayList<>();
                newScores.add(new Score().setType("exam").setScore(42d));
                recomm1.setRecommendation(newScores);
                //Document filterByRecomm1Id = new Document("_id", recomm1.getId());
                Document filterByRecomm1Id = new Document("recomm_id", recommTest);
                FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
                Recomm1 updatedRecomm1 = Recomm1s.findOneAndReplace(filterByRecomm1Id, recomm1, returnDocAfterReplace);
                System.out.println("Recomm1 replaced:\t" + updatedRecomm1);
            	
            }
            System.out.println("\nRecomm1 found:\t\t" + recomm1);
            if (recomm1.getRecommendation() instanceof List) {
            	System.out.println("Recomm1 size:\t\t" + recomm1.getRecommendation().size());
            }
 
            // delete this Recomm1
            //System.out.println("Recomm1 deleted:\t" + Recomm1s.deleteMany(filterByRecomm1Id));
        }
    }
}
