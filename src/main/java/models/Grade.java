package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.DBRef;
import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@XmlRootElement
public class Grade {
    @Id
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    ObjectId id;
    private float value;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CET")
    private Date date;
    @Reference
    private Course course;
    private static int idCounter = 0;



    public Grade(){
        //setId();
    }

    public Grade(int id, float value, Date date, Course course) {
        //setId();
        this.value = value;
        this.date = date;
        this.course = course;
    }

    /*public int getId() {
        return id;
    }

    public void setId() {
        idCounter++;
        this.id = idCounter;
    }*/

    public float getValue() {
        return value;
    }

    public void setValue(float value) throws Exception {
        this.value = Math.round(value * 2) / 2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(int id) throws Exception {
        Course course = CourseService.findCoursetById(id);
        if (course != null) {
            this.course = course;
        } else {
            throw new Exception("Course with id=" + id + " don't exist");
        }
    }

    @XmlTransient
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
