package com.mongodb.quickstart.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

public class Recomm1 extends Recommendation{

    protected List<Score> recommendation;

    public Recomm1 setRecommendation(List<Score> scores) {
        this.recommendation = scores;
        return this;
    }
    
    public List<Score> getRecommendation(){
    	return this.recommendation;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RecommOne{");
        sb.append("id=").append(id);
        sb.append(", recomm_id=").append(recommId);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", last_modified=").append(last_modified);
        sb.append(", recommendation=").append(recommendation);
        sb.append('}');
        return sb.toString();
    }
   
}
