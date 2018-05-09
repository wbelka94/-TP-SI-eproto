package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import components.MongoDB;
import org.mongodb.morphia.annotations.Reference;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class Grade {
    //@XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    private int id;
    private float value;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CET")
    private Date date;
    @Reference
    private Course course;

    public Grade(){
        //setId();
    }

    public Grade(int id, float value, Date date, Course course) {
        //setId();
        this.value = value;
        this.date = date;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    //@XmlTransient
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) throws Exception {
        //this.course = course;
        Course c = MongoDB.getDatastore().createQuery(Course.class).filter("uid",course.getUid()).get();
        if (c != null) {
            this.course = c;
        } else {
            throw new Exception("Course with id=" + course.getUid() + " don't exist");
        }
    }
}
