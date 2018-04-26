package models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@XmlRootElement
@Entity("students")
public class Student {
    @Id
    @XmlTransient
  //  @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    private ObjectId id;
    private int index;
    private String firstname, lastname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "CET")
    private Date birthday;
    @Embedded
    private List<Grade> grades;

    @InjectLinks({
            @InjectLink(resource = StudentService.class, rel = "parent"),
            @InjectLink(value="students/${instance.index}", rel="self"),
            @InjectLink(value="students/${instance.index}/grades", rel="grades"),
    })
    @XmlElement(name="link")
    @XmlElementWrapper(name = "links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;


    public Student() {
        setIndex();
        grades = new ArrayList<Grade>();
    }

    public Student(String firstname, String lastname, Date birthday) {
        setIndex();
        grades = new ArrayList<Grade>();
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
    }

    public boolean addGrade(Grade grade) {
        int max = 0;
        for(Grade g : grades){
            if(g.getId()>max){
                max = g.getId();
            }
        }
        grade.setId(++max);
        grades.add(grade);
        return true;
    }

    public Grade findGradeById(int id) {
        for(Grade g : grades){
            if(g.getId() == id){
                return g;
            }
        }
        return null;
    }

    public void updateGrade(int id, Grade grade) throws Exception {
        Grade g = findGradeById(id);
        if (g == null) {
            throw new Exception("Grade with id=" + id + " for student with index=" + index + " don't exist");
        }
        g.setCourse(grade.getCourse());
        g.setDate(grade.getDate());
        g.setValue(grade.getValue());
    }

    public boolean deleteGrade(int id) {
        return grades.remove(findGradeById(id));
    }

    public int getIndex() {
        return index;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Date getBirthday() {
        return birthday;
    }

    @XmlTransient
    public List<Grade> getGrades() {
        return grades;
    }

    public void setIndex() {
        try {
            Query<AutoIncrement> query = MongoDB.getDatastore().createQuery(AutoIncrement.class).filter("colectionName","students");
            UpdateOperations<AutoIncrement> updateOperations = MongoDB.getDatastore().createUpdateOperations(AutoIncrement.class).inc("actual");
            AutoIncrement autoIncrement = MongoDB.getDatastore().findAndModify(query, updateOperations);
            if (autoIncrement == null) {
                autoIncrement = new AutoIncrement(1, "students");
                MongoDB.getDatastore().save(autoIncrement);
            }
            index = autoIncrement.getActual();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
