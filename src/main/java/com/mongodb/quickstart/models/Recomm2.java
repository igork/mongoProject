package com.mongodb.quickstart.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

public class Recomm2 extends Recommendation{

    private String recommendation;

    public Recomm2 setRecommendation(String scores) {
        this.recommendation = scores;
        return this;
    }
    
    public String getRecommendation(){
    	return this.recommendation;
    }

}
