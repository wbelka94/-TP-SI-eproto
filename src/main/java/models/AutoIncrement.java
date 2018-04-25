package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Query;

@Entity
public class AutoIncrement {
    @Id
    private ObjectId id;
    private Integer actual;

    public AutoIncrement(){

    }

    public AutoIncrement(Integer actual) {
        this.actual = actual;
    }

    public Integer getActual() {
        return actual;
    }

}
