package models;

import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@Entity("courses")
@XmlRootElement
public class Course {
    @Id
    @XmlTransient
   // @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    private ObjectId id;
    private int uid;
    private String name,lecturer;

    @InjectLinks({
            @InjectLink(resource = CourseService.class, rel = "parent"),
            @InjectLink(value="/courses/${instance.uid}", rel="self"),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;

    public Course(){
        setUid();
    }

    public Course(String name, String lecturer) {
        setUid();
        this.name = name;
        this.lecturer = lecturer;
    }

    public int getUid() {
        return uid;
    }

    public void setUid() {
        try {
            Query<AutoIncrement> query = MongoDB.getDatastore().createQuery(AutoIncrement.class).filter("colectionName","courses");
            UpdateOperations<AutoIncrement> updateOperations = MongoDB.getDatastore().createUpdateOperations(AutoIncrement.class).inc("actual");
            AutoIncrement autoIncrement = MongoDB.getDatastore().findAndModify(query, updateOperations);
            if (autoIncrement == null) {
                autoIncrement = new AutoIncrement(1, "courses");
                MongoDB.getDatastore().save(autoIncrement);
            }
            uid = autoIncrement.getActual();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    @XmlTransient
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
