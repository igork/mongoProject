package com.mongodb.quickstart.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

public class Recommendation {
	
    protected ObjectId id;
    
    @BsonProperty(value = "recomm_id")
	protected String recommId;
    
    @BsonProperty(value = "timestamp")
    protected String timestamp;
    
    @BsonProperty(value = "last_modified")
    protected String last_modified;
    
    //private List<Score> recommendation;
    protected Object recommendation;
    
    public Object getRecommendation() {
        return recommendation;
    }

    public Recommendation setRecommendation(Object scores) {
        this.recommendation = scores;
        return this;
    }
	

    public String getTimestamp() {
		return timestamp;
	}

	public Recommendation setTimestamp(String timestamp) {
		this.timestamp = timestamp;
        return this;
	}

	public String getLast_modified() {
		return last_modified;
	}

	public Recommendation setLast_modified(String last_modified) {
		this.last_modified = last_modified;
        return this;
	}

	
    public ObjectId getId() {
        return id;
    }

    public Recommendation setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public String getRecommId() {
        return recommId;
    }

    public Recommendation setRecommId(String recommId) {
        this.recommId = recommId;
        return this;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Recommendation recomm = (Recommendation) o;
        return Objects.equals(id, recomm.id) 
        		&& Objects.equals(recommId, recomm.recommId) 
        		&& Objects.equals(timestamp,recomm.timestamp) 
        		&& Objects.equals(last_modified,recomm.last_modified) 
        		&& Objects.equals(recommendation, recomm.recommendation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, recommId, timestamp, last_modified, recommendation);
    }
}
