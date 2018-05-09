package components;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MongoDB {
    private static Datastore datastore;

    public MongoDB() {
        if(datastore == null){
            init();
        }
    }

    public Datastore init(){
        final Morphia morphia = new Morphia();
        morphia.mapPackage("models");
        datastore = morphia.createDatastore(new MongoClient("localhost" , 8004 ), "test");
        datastore.ensureIndexes();
        return datastore;
    }

    public static Datastore getDatastore() {
        if(datastore == null) {
            throw new NullPointerException();
        }
        return datastore;
    }
}
