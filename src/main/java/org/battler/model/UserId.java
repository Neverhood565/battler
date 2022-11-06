package org.battler.model;


import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
public class UserId {

    private final String id;

    @BsonCreator
    public UserId(@BsonProperty("id") final String id) {
        this.id = id;
    }
}
